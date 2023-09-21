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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
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

  private static int currentIndex = 0;

  private ChatCompletionRequest chatCompletionRequest;
  private ChatMessage lastMsg;
  private boolean animationIsFinished = false;
  private Queue<ChatMessage> messageQueue = new LinkedList<>();
  private boolean isTyping = false;

  public void initialize() throws ApiProxyException {
    SceneManager.setController(Scenes.COMPUTER, this);
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);

    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.5).setTopP(0.9).setMaxTokens(256);

    ChatMessage msg = App.getStartMessage();
    System.out.println(msg);
    if (msg != null) {
      typeText(msg.getContent());
    }

    // Set the text color to green
    securityTextArea.setStyle("-fx-text-fill: green;");
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
          new ChatCompletionRequest().setN(1).setTemperature(0.8).setTopP(0.9).setMaxTokens(300);
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
      System.out.println("Starting Authentication...");
      ChatMessage response =
          runGpt(new ChatMessage("user", GptPromptEngineering.initiliseComputerAI()));

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

            if (message.trim().equals("1")) {
              lastMsg = getRiddle();

            } else {
              lastMsg = runGpt(msg);
            }

            if (message.trim().isEmpty()) {
              return null;
            }

            inputTextField.clear();

            typeText(msg.getContent());
            typeText(lastMsg.getContent());

            if (lastMsg.getRole().equals("assistant")
                && lastMsg.getContent().startsWith("Correct")) {
              msg = startAuthentication();
              appendChatMessage(msg);
            }
            if (lastMsg.getRole().equals("assistant")
                && lastMsg.getContent().startsWith("Authenticated")) {
              System.out.println("Authenticated");

              appendChatMessage(lastMsg);
            }
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

  //   handling mouse events on walkie talkie
  //   open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  @FXML
  public void displayText(ChatMessage message) {}

  @FXML
  public void typeText(String textToType) {
    currentIndex = 0;

    Platform.runLater(
        () -> {
          Timeline timeline =
              new Timeline(
                  new KeyFrame(
                      Duration.seconds(0.05),
                      event -> {
                        if (currentIndex <= textToType.length()) {
                          animationIsFinished = false;
                          securityTextArea.setText(
                              securityTextArea.getText() + textToType.charAt(currentIndex));
                          currentIndex++;
                        } else {
                          animationIsFinished = true;
                          System.out.println("textToType");
                          if (!messageQueue.isEmpty()) {
                            // If there are more messages in the queue, start typing the next one
                            typeNextMessage();
                          }
                        }
                      }));

          timeline.setCycleCount(textToType.length());
          timeline.setOnFinished(
              event -> {
                if (!messageQueue.isEmpty()) {
                  // If there are more messages in the queue, start typing the next one
                  typeNextMessage();
                } else {
                  isTyping = false; // No more messages to type
                }
              });

          if (!isTyping) {
            isTyping = true;
            timeline.play();
          }
        });
  }

  private void typeNextMessage() {
    if (!messageQueue.isEmpty()) {
      ChatMessage nextMessage = messageQueue.poll();
      typeText(nextMessage.getContent());
    }
  }

  private void appendChatMessage(ChatMessage msg) {
    securityTextArea.appendText(msg.getContent() + "\n\n");
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
