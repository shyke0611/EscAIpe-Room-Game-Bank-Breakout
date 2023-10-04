package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.StyleManager.HoverColour;
import nz.ac.auckland.se206.WalkieTalkieManager;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class ComputerController extends Controller {

  @FXML private Button goBackBtn;
  @FXML private TextField inputTextField;
  @FXML private Button quickHintBtn;
  @FXML private TextArea securityTextArea;
  @FXML private Button sendBtn;
  @FXML private Button viewHistoryBtn;
  @FXML private VBox walkietalkie;
  @FXML private VBox walkietalkieText;
  @FXML private Button dotGameBtn;
  @FXML private Label timerLabel;
  @FXML private Label processingLabel;
  @FXML private ImageView usbStick;

  private ChatCompletionRequest chatCompletionRequest;
  private ChatMessage lastMsg;
  private int dotCount = 0;
  private Queue<ChatMessage> messageQueue = new LinkedList<>();
  private WalkieTalkieManager walkieTalkieManager;
  private StyleManager styleManager = StyleManager.getInstance();

  private Timeline timeline;

  public void initialize() throws ApiProxyException {
    // initialising all the relevant methods
    SceneManager.setController(Scenes.COMPUTER, this);
    super.setTimerLabel(timerLabel, 1);
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
    walkieTalkieManager = WalkieTalkieManager.getInstance();
    styleManager.addItems(usbStick);
    // creating new timeline
    timeline = new Timeline(new KeyFrame(Duration.seconds(0.6), e -> updateLabel()));
    timeline.setCycleCount(Timeline.INDEFINITE);
    // setting chat message
    ChatMessage msg =
        new ChatMessage(
            "user",
            "Welcome to Lorem Ipsum, where security is our top priority, please type 'yes' to start"
                + " the authentication process");

    if (msg != null) {
      messageQueue.add(msg);
      appendChatMessage();
    }
  }

  // exit computer view back to security room
  @FXML
  private void onGoBack(ActionEvent event) {
    App.setUI(Scenes.SECURITY);
  }

  @FXML
  private ChatMessage getRiddle() {
    // setting new chat completion request
    try {
      chatCompletionRequest =
          new ChatCompletionRequest().setN(1).setTemperature(0.3).setTopP(1).setMaxTokens(256);
      // getting riddle for computer ai
      ChatMessage response =
          runGpt(new ChatMessage("assistant", GptPromptEngineering.getRiddleWithGivenWord()));

      return response;
    } catch (Exception e) {
      System.out.println("start dots");
      startConnectDots();
      // e.printStackTrace();
    }
    return null;
  }

  @FXML
  private ChatMessage startAuthentication() {
    try {
      // Add logging here to trace the flow and variable values
      chatCompletionRequest =
          new ChatCompletionRequest().setN(1).setTemperature(0.4).setTopP(1).setMaxTokens(256);
      System.out.println("Starting Authentication...");
      ChatMessage response =
          runGpt(new ChatMessage("user", GptPromptEngineering.initiliseComputer()));
      messageQueue.add(response);
      // Add more logging to check response and its properties
      System.out.println("Authentication Response: " + response);
      // return what computer ai says
      return response;
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
    return null;
  }

  @FXML
  private void onSend(ActionEvent event) throws ApiProxyException, IOException {
    // creating new task for the thread
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            securityTextArea.appendText("\n");
            // getting the message the user inputs
            String message = inputTextField.getText();
            ChatMessage msg = new ChatMessage("user", message);
            messageQueue.add(msg);
            inputTextField.clear();
            appendChatMessage();
            // setting relevant methods
            Platform.runLater(
                () -> {
                  timeline.play();
                });

            // if the message is yes, then start the authentication process
            if (message.trim().equalsIgnoreCase("yes")) {
              lastMsg = getRiddle();
              System.out.println("message recived");
              messageQueue.add(lastMsg);
              // else
            } else {
              lastMsg = runGpt(msg);
              messageQueue.add(lastMsg);
            }

            if (lastMsg == null) {
              startConnectDots();
              return null;
            }

            // if input is empty then return null
            if (message.trim().isEmpty()) {
              return null;
            }

            // if the message is correct then increment the number of messages correct
            if (lastMsg.getRole().equals("assistant") && lastMsg.getContent().startsWith("Correct")
                || lastMsg.getContent().startsWith("correct")) {
              startAuthentication();
            }
            // if the computer authenticates access
            if (lastMsg.getRole().equals("assistant")
                && lastMsg.getContent().contains("security")) {
              WalkieTalkieManager.toggleWalkieTalkie();
              walkieTalkieManager.setWalkieTalkieText(
                  // setting the walkie talkie text when firewall disabled
                  new ChatMessage(
                      "user", "FireWall Disabled, you can now see what is behind each vault door"));

              styleManager.getItem("goldDoor").setStyle("");
              styleManager.getItem("silverDoor").setStyle("");
              styleManager.getItem("bronzeDoor").setStyle("");
              styleManager.removeItemsMessage("goldDoor", "silverDoor", "bronzeDoor");
              styleManager.setItemsState(HoverColour.GREEN, "goldDoor", "silverDoor", "bronzeDoor");

              GameState.isFirewallDisabled = true;
              GameState.isSecondRiddleSolved = true;

            } else if (lastMsg.getRole().equals("assistant")
                && lastMsg.getContent().startsWith("Authenticated")) {
              System.out.println("Authenticated");
              // setting the walkie talkie text when firewall disabled
              WalkieTalkieManager.toggleWalkieTalkie();
              walkieTalkieManager.setWalkieTalkieText(
                  new ChatMessage(
                      "user", "FireWall Disabled, you can now see what is behind each vault door"));

              styleManager.getItem("goldDoor").setStyle("");
              styleManager.getItem("silverDoor").setStyle("");
              styleManager.getItem("bronzeDoor").setStyle("");
              styleManager.removeItemsMessage("goldDoor", "silverDoor", "bronzeDoor");
              styleManager.setItemsState(HoverColour.GREEN, "silverDoor", "bronzeDoor");
              GameState.isFirewallDisabled = true;
              GameManager.completeObjective();
            }

            // when authentication fails
            if (lastMsg.getRole().equals("assistant")
                && lastMsg.getContent().contains("Authentication failed")) {
              System.out.println("authetication failed");

              startConnectDots();
              // Logic to start connect dots mini game
            }
            // handle relevant methods
            Platform.runLater(
                () -> {
                  processingLabel.setText("");
                  timeline.stop();
                  appendChatMessage();
                });

            return null;
          }
        };

    // thread for the task
    Thread searchThreadDave = new Thread(task, "Search Thread Bob");
    searchThreadDave.start();
  }

  @FXML
  private void onSwitchToHacker() {
    // switch mechanic to hacker van
    SceneManager.setPreviousScene(Scenes.HACKERVAN, Scenes.COMPUTER);
    App.setUI(Scenes.HACKERVAN);
  }

  private void updateLabel() {
    // update label for the processing label
    dotCount = (dotCount % 3) + 1; // Cycle dots from 1 to 3
    String dots = ".".repeat(dotCount); // Generate dots
    processingLabel.setText("Processing" + dots);
  }

  // Handling mouse events on walkie talkie
  // Opens and closes when walkie talkie is clicked
  @FXML
  private void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  @FXML
  private void startConnectDots() {
    // setting the walkie talkie text on when they fail all three riddles
    WalkieTalkieManager.toggleWalkieTalkie();
    walkieTalkieManager.setWalkieTalkieText(
        new ChatMessage(
            "user", "Authentication failed, plug in the usbstick to bypass the firewall"));

    // set the second authentication method to visible
    usbStick.setVisible(true);
    styleManager.setClueHover("usbStick", true);
  }

  @FXML
  private void connectDots() {
    App.setUI(Scenes.CONNECTDOTS);
  }

  private void appendChatMessage() {
    // if the message queue is not empty then append the chat message
    if (!messageQueue.isEmpty()) {
      ChatMessage nextMessage = messageQueue.poll();
      String content = nextMessage.getContent();

      // Add typing effect (delay)
      int delay = 30; // Adjust this value to control the typing speed (in milliseconds)

      // create new task for the thread
      Task<Void> task =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              // Perform typing operations here
              for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);
                Platform.runLater(
                    () -> {
                      securityTextArea.appendText(String.valueOf(c));
                    });
                Thread.sleep(delay);
              }

              Platform.runLater(
                  () -> {
                    //
                    appendChatMessage(); // Continue with the next message in the queue
                    securityTextArea.appendText("\n");
                  });

              return null;
            }
          };

      // creating new thread for the task
      Thread typingThread = new Thread(task);
      typingThread.setDaemon(true);
      typingThread.start();
    }
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  public ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    // add message to the chat completion request
    chatCompletionRequest.addMessage(msg);
    try {
      // execute the chat completion request
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      // return the response chat message from ai
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }
}
