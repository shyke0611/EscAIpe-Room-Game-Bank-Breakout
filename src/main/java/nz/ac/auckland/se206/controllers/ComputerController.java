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
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;
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

  private static int currentIndex = 0;

  private ChatCompletionRequest chatCompletionRequest;
  private ChatMessage lastMsg;
  private int numberOfMessagesCorrect = 0;
  private int dotCount = 0;
  private boolean animationIsFinished = false;
  private Queue<ChatMessage> messageQueue = new LinkedList<>();
  private boolean isTyping = false;
  private StringBuilder textBuffer;
  private WalkieTalkieManager walkieTalkieManager;
  private StyleManager styleManager = StyleManager.getInstance();

  private Timeline timeline;

  public void initialize() throws ApiProxyException {
    SceneManager.setController(Scenes.COMPUTER, this);
    super.setTimerLabel(timerLabel, 1);
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
    walkieTalkieManager = WalkieTalkieManager.getInstance();
    styleManager.addItems(usbStick);

    textBuffer = new StringBuilder();

    timeline = new Timeline(new KeyFrame(Duration.seconds(0.6), e -> updateLabel()));
    timeline.setCycleCount(Timeline.INDEFINITE);

    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.3).setTopP(1).setMaxTokens(256);
    ChatMessage msg = runGpt(new ChatMessage("user", GptPromptEngineering.welcomeMessage()));

    if (msg != null) {
      messageQueue.add(msg);
      appendChatMessage();
    }
  }

  // exit computer view back to security room
  @FXML
  void onGoBack(ActionEvent event) {
    App.setUI(Scenes.SECURITY);
  }

  @FXML
  public ChatMessage getRiddle() {
    try {
      chatCompletionRequest =
          new ChatCompletionRequest().setN(1).setTemperature(0.3).setTopP(1).setMaxTokens(256);

      ChatMessage response =
          runGpt(new ChatMessage("assistant", GptPromptEngineering.getRiddleWithGivenWord()));

      return response;
    } catch (ApiProxyException e) {
      // TODO handle exception appropriately
      e.printStackTrace();
    }
    return null;
  }

  @FXML
  public ChatMessage startAuthentication() {
    try {
      // Add logging here to trace the flow and variable values
      chatCompletionRequest =
          new ChatCompletionRequest().setN(1).setTemperature(0.4).setTopP(1).setMaxTokens(256);
      System.out.println("Starting Authentication...");
      ChatMessage response =
          runGpt(new ChatMessage("user", GptPromptEngineering.initiliseComputerAI()));
      messageQueue.add(response);
      // Add more logging to check response and its properties
      System.out.println("Authentication Response: " + response);

      return response;

    } catch (ApiProxyException e) {
      // TODO handle exception appropriately
      e.printStackTrace();
    }
    return null;
  }

  @FXML
  public void onSend(ActionEvent event) throws ApiProxyException, IOException {

    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            securityTextArea.appendText("\n");

            String message = inputTextField.getText();
            ChatMessage msg = new ChatMessage("user", message);
            messageQueue.add(msg);
            inputTextField.clear();
            appendChatMessage();

            Platform.runLater(
                () -> {
                  timeline.play();
                });

            if (message.trim().equalsIgnoreCase("yes")) {
              lastMsg = getRiddle();
              System.out.println("message recived");
              messageQueue.add(lastMsg);

            } else {
              lastMsg = runGpt(msg);
              messageQueue.add(lastMsg);
            }

            if (message.trim().isEmpty()) {
              return null;
            }

            // appendChatMessage(msg);

            if (lastMsg.getRole().equals("assistant") && lastMsg.getContent().startsWith("Correct")
                || lastMsg.getContent().startsWith("correct")) {
              msg = startAuthentication();
            }
            if (lastMsg.getRole().equals("assistant")
                && lastMsg.getContent().startsWith("Authenticated")
                && lastMsg.getContent().contains("security")) {
              walkieTalkieManager.toggleWalkieTalkie();
              walkieTalkieManager.setWalkieTalkieText(
                  new ChatMessage(
                      "user", "FireWall Disabled, you can now see what is behind each vault door"));
              // Higer security level granted
              // set access to hard door

            }

            if (lastMsg.getRole().equals("assistant")
                && lastMsg.getContent().startsWith("Authenticated")) {
              System.out.println("Authenticated");
              walkieTalkieManager.toggleWalkieTalkie();
              walkieTalkieManager.setWalkieTalkieText(
                  new ChatMessage(
                      "user", "FireWall Disabled, you can now see what is behind each vault door"));

              // Set vault visble/firewall disabled
            }

            if (lastMsg.getRole().equals("assistant")
                && lastMsg.getContent().contains("Authentication failed")) {
              numberOfMessagesCorrect = 0;
              System.out.println("authetication failed");

              startConnectDots();
              // Logic to start connect dots mini game
            }

            Platform.runLater(
                () -> {
                  processingLabel.setText("");
                  timeline.stop();
                  appendChatMessage();
                });

            return null;
          }
        };

    Thread searchThreadDave = new Thread(task, "Search Thread Bob");
    searchThreadDave.start();
  }

  public void onSwitchToHacker() {
    SceneManager.setPreviousScene(Scenes.HACKERVAN, Scenes.COMPUTER);
    App.setUI(Scenes.HACKERVAN);
  }

  private void updateLabel() {
    dotCount = (dotCount % 3) + 1; // Cycle dots from 1 to 3
    String dots = ".".repeat(dotCount); // Generate dots
    processingLabel.setText("Processing" + dots);
  }

  public void switchToDots() {
    GameState.isConnectDotreached = true;
    App.setUI(Scenes.CONNECTDOTS);
  }

  //   handling mouse events on walkie talkie
  //   open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  @FXML
  private void startConnectDots() {
    walkieTalkieManager.toggleWalkieTalkie();
    walkieTalkieManager.setWalkieTalkieText(
        new ChatMessage(
            "user", "Authentication failed, plug in the usbstick to bypass the firewall"));

    usbStick.setVisible(true);
    styleManager.setClueHover("usbStick", true);
  }

  @FXML
  private void connectDots() {
    App.setUI(Scenes.CONNECTDOTS);
  }

  // @FXML
  // public void typeText(String textToType) {
  //   currentIndex = 0;

  //   timeline =
  //       new Timeline(
  //           new KeyFrame(
  //               Duration.seconds(0.05),
  //               event -> {
  //                 if (currentIndex < textToType.length()) {
  //                   securityTextArea.appendText(String.valueOf(textToType.charAt(currentIndex)));
  //                   currentIndex++;
  //                 } else {
  //                   // Animation is finished
  //                   animationIsFinished = true;
  //                   if (!messageQueue.isEmpty()) {
  //                     // If there are more messages in the queue, start typing the next one
  //                     typeNextMessage();
  //                   }
  //                 }
  //               }));

  //   // Set the cycle count to 1
  //   timeline.setCycleCount(textToType.length() + 1);

  //   timeline.setOnFinished(
  //       event -> {
  //         if (!messageQueue.isEmpty()) {
  //           // If there are more messages in the queue, start typing the next one
  //           typeNextMessage();
  //         } else {
  //           isTyping = false; // No more messages to type
  //         }
  //       });

  //   if (!isTyping) {
  //     isTyping = true;
  //     timeline.play();
  //   }
  // }

  // private void typeNextMessage() {
  //   if (!messageQueue.isEmpty()) {
  //     ChatMessage nextMessage = messageQueue.poll();
  //     typeText(nextMessage.getContent());
  //   }
  // }

  // securityTextArea.appendText(msg.getContent() + "\n\n");private void
  // appendChatMessage(ChatMessage msg) {

  // String content = messageQueue.poll().getContent();
  // int length = content.length();
  private void appendChatMessage() {
    if (!messageQueue.isEmpty()) {
      ChatMessage nextMessage = messageQueue.poll();
      String content = nextMessage.getContent();

      int delay = 30; // Adjust this value to control the typing speed (in milliseconds)

      Task<Void> task =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
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
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());

      return result.getChatMessage();
    } catch (ApiProxyException e) {
      // TODO handle exception appropriately
      e.printStackTrace();
      return null;
    }
  }
}
