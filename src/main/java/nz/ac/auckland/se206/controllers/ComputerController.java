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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.GameManager.Objectives;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.RandomnessGenerate;
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
  @FXML private HBox usbStick;
  @FXML private TextArea computerTextArea;

  private ChatCompletionRequest chatCompletionRequest;
  private ChatMessage lastMsg;
  private int dotCount = 0;
  private Queue<ChatMessage> messageQueue = new LinkedList<>();
  private WalkieTalkieManager walkieTalkieManager;
  private StyleManager styleManager = StyleManager.getInstance();

  private Timeline timeline;
  private Boolean riddleStarted;
  private Boolean authenticationStarted;

  private Boolean onQuestionThree = false;
  private Boolean onQuestionTwo = false;
  private Boolean onQuestionOne = false;
  private Boolean questionsComplete = false;
  private int questionsCorrect = 0;

  public void initialize() throws ApiProxyException {
    // initialising all the relevant methods
    SceneManager.setController(Scenes.COMPUTER, this);
    super.setTimerLabel(timerLabel, 1);
    walkieTalkieManager = WalkieTalkieManager.getInstance();
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
    WalkieTalkieManager.addWalkieTalkieTextArea(this, computerTextArea);
    styleManager.addHoverItems(usbStick);
    // styleManager.addItems(usbStick);
    // creating new timeline
    timeline = new Timeline(new KeyFrame(Duration.seconds(0.6), e -> updateLabel()));
    timeline.setCycleCount(Timeline.INDEFINITE);

    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.4).setTopP(1).setMaxTokens(256);
    riddleStarted = false;
    authenticationStarted = false;
    // setting chat message
    ChatMessage msg =
        new ChatMessage(
            "user",
            "Welcome to Lorem Ipsum, where security is our top priority, please type 'yes' to start"
                + " the authentication process");
    lastMsg = getRiddle();

    if (msg != null) {
      messageQueue.add(msg);
      appendChatMessage();
    }
  }

  public void setFocus() {
    inputTextField.requestFocus();
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

      ChatMessage response =
          runGpt(new ChatMessage("assistant", GptPromptEngineering.getRiddleWithGivenWord()));

      return response;
    } catch (Exception e) {
      startConnectDots();
      e.printStackTrace();
    }
    return null;
  }

  @FXML
  private void startAuthentication() {
    try {
      // Add logging here to trace the flow and variable values
      System.out.println("Starting Authentication...");
      String ceoName = RandomnessGenerate.getCurrentCeoName().toLowerCase();
      String employeeName = RandomnessGenerate.getCurrentEmployeeName().toLowerCase();
      String date = RandomnessGenerate.getCurrentDate().toLowerCase();

      ChatMessage response =
          runGpt(
              new ChatMessage(
                  "user", GptPromptEngineering.initiliseComputer(ceoName, employeeName, date)));

      messageQueue.add(response);
      appendChatMessage();
      authenticationStarted = true;

      // return what computer ai says

    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onSend(ActionEvent event) throws ApiProxyException, IOException {
    if (sendBtn.isDisable()) {
      return;
    }
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            securityTextArea.appendText("\n");
            sendBtn.setDisable(true);
            // getting the message the user inputs
            String message = inputTextField.getText();
            inputTextField.clear();

            Platform.runLater(
                () -> {
                  timeline.play();
                });

            messageQueue.add(new ChatMessage("user", message));

            if (authenticationStarted) {
              performAuthentication(message);
            }

            if (riddleStarted) {
              lastMsg = runGpt(new ChatMessage("user", message));
              if (checkRiddle(lastMsg.getContent())) {
                messageQueue.add(new ChatMessage("assistant", "Correct"));
                messageQueue.add(
                    new ChatMessage(
                        "assistant",
                        "Now that you have proven you are human we can begin the authentication"
                            + " procedure"));
                authenticationStarted = true;
                onQuestionOne = true;
                messageQueue.add(new ChatMessage("assistant", "What year was this bank founded?"));

              } else if (!authenticationStarted) {
                messageQueue.add(new ChatMessage("user", "Incorrect, try again"));
              }
            }
            if (message.trim().equalsIgnoreCase("yes")) {
              riddleStarted = true;
              messageQueue.add(lastMsg);

              if (!(lastMsg == null)) {
                App.textToSpeech("Welcome, Starting Captcha test");
              }

              if (lastMsg == null) {
                startConnectDots();
              }
            } else if (!riddleStarted) {
              ChatMessage msg =
                  new ChatMessage("user", "Please type yes to start the authentication process");
              messageQueue.add(msg);
              sendBtn.setDisable(false);
              return null;
              // else
            }

            appendChatMessage();

            return null;
          }
        };

    // thread for the task
    Thread searchThreadDave = new Thread(task, "Search Thread Bob");
    searchThreadDave.start();
  }

  @FXML
  public void performAuthentication(String msg) {

    if (onQuestionThree) {
      if (msg.equalsIgnoreCase(RandomnessGenerate.getCurrentCeoName())) {
        messageQueue.add(new ChatMessage("assistant", "Correct"));
        questionsCorrect++;
      } else {
        messageQueue.add(new ChatMessage("assistant", "Incorrect"));
      }
      questionsComplete = true;
      authenticationResult();

    } else if (onQuestionTwo) {

      if (msg.equalsIgnoreCase(RandomnessGenerate.getCurrentEmployeeName())) {
        messageQueue.add(new ChatMessage("assistant", "Correct"));
        questionsCorrect++;
      } else {
        messageQueue.add(new ChatMessage("assistant", "Incorrect"));
      }
      onQuestionThree = true;
      messageQueue.add(new ChatMessage("assistant", "What is the ceos first name?"));

    } else if (onQuestionOne) {
      if (msg.equalsIgnoreCase(RandomnessGenerate.getCurrentDate())) {
        messageQueue.add(new ChatMessage("assistant", "Correct"));
        questionsCorrect++;
      } else {
        messageQueue.add(new ChatMessage("assistant", "Incorrect"));
      }
      onQuestionTwo = true;
      messageQueue.add(new ChatMessage("assistant", "Who won employee of the month last month?"));
    }
  }

  private void authenticationResult() {

    if (questionsComplete) {
      if (questionsCorrect == 3) {
        messageQueue.add(
            new ChatMessage("assistant", "Authentication complete, Level 3 vault access granted"));
        GameState.isFirewallDisabled = true;
        GameState.isSecondRiddleSolved = true;
        App.textToSpeech("Security Disabled, Level 3 Vault Access Granted");

        Platform.runLater(
            () -> {
              WalkieTalkieManager.setWalkieTalkieNotifcationOn();
              walkieTalkieManager.setWalkieTalkieText(
                  new ChatMessage("assistant", "Nice work! Now you have access to all 3 vaults"));
            });

        GameManager.setCurrentObjective(Objectives.SELECT_VAULT_DOOR);
        setLevelThreeStyle();

      } else if (questionsCorrect >= 1) {
        messageQueue.add(
            new ChatMessage("assistant", "Authentication Complete, Level 2 Vault Access Granted"));
        App.textToSpeech("Security Disabled, Level 2 Vault Access Granted");
        GameState.isFirewallDisabled = true;
        GameState.isFirstRiddleSolved = true;
        GameManager.setCurrentObjective(Objectives.SELECT_VAULT_DOOR);
        Platform.runLater(
            () -> {
              WalkieTalkieManager.setWalkieTalkieNotifcationOn();
              walkieTalkieManager.setWalkieTalkieText(
                  new ChatMessage(
                      "assistant",
                      "Nice job! 2 out of the 3 vault security systems have now been disabled"));
            });

        setLevelTwoStyle();

      } else if (questionsCorrect == 0) {
        messageQueue.add(
            new ChatMessage("assistant", "Authentication failed, no vault access granted"));
        Platform.runLater(
            () -> {
              WalkieTalkieManager.setWalkieTalkieNotifcationOn();
              walkieTalkieManager.setWalkieTalkieText(
                  new ChatMessage(
                      "assistant",
                      "Unfortuantly you failed authentication, but dont worry there is a plan B,"
                          + " plug in the USB i gave you"));
            });

        startConnectDots();
      }
    }
  }

  private Boolean checkRiddle(String message) {
    if (lastMsg.getRole().equals("assistant") && lastMsg.getContent().startsWith("Correct")
        || lastMsg.getContent().startsWith("correct")) {
      return true;
    } else {

      return false;
    }
  }

  @FXML
  private void onSwitchToHacker() {
    // switch to hacker van
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
    if (!WalkieTalkieManager.getWalkieTalkieOpen()) {
      inputTextField.requestFocus();
    }
  }

  @FXML
  private void startConnectDots() {

    // set the second authentication method to visible
    usbStick.setVisible(true);
    StyleManager.setClueHover("usbStick", true);
    GameState.isFirstRiddleSolved = true;
    StyleManager.setClueHover("computer", false);
    setConnectdotsStyle();
  }

  private void setConnectdotsStyle() {
    setVaultStyleLevelTwo();
    StyleManager.setDisable(true, "ceoPainting", "wallEmployee");
  }

  private void setLevelTwoStyle() {
    // set disability
    StyleManager.setDisable(true, "computer", "ceoPainting", "wallEmployee");
    StyleManager.setClueHover("computer", false);
    // setting vault style
    setVaultStyleLevelTwo();
  }

  private void setVaultStyleLevelTwo() {
    StyleManager.setItemsHoverColour(HoverColour.GREEN, "silverDoorHolder", "bronzeDoorHolder");
    StyleManager.setItemsMessage("Access granted", "bronzeDoorHolder", "silverDoorHolder");
    StyleManager.setItemsMessage("No access", "goldDoorHolder");
  }

  private void setLevelThreeStyle() {
    // disabling items
    StyleManager.setDisable(true, "computer", "ceoPainting", "wallEmployee");
    StyleManager.setClueHover("computer", false);
    // setting vault style
    StyleManager.setItemsHoverColour(
        HoverColour.GREEN, "silverDoorHolder", "bronzeDoorHolder", "goldDoorHolder");
    StyleManager.setItemsMessage(
        "Access granted", "bronzeDoorHolder", "silverDoorHolder", "goldDoorHolder");
  }

  @FXML
  private void connectDots() {
    GameState.isConnectDotreached = true;
    App.setUI(Scenes.CONNECTDOTS);
  }

  private void appendChatMessage() {
    // if the message queue is not empty then append the chat message

    if (!messageQueue.isEmpty()) {
      ChatMessage nextMessage = messageQueue.poll();
      String content = nextMessage.getContent();

      // Add typing effect (delay)
      int delay = 20; // Adjust this value to control the typing speed (in milliseconds)

      // create new task for thexs thread
      Task<Void> task =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              Platform.runLater(
                  () -> {
                    processingLabel.setText("");
                    timeline.stop();
                  });

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
                    sendBtn.setDisable(false);
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

  @FXML
  public void onEnterPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      try {
        onSend(new ActionEvent());
      } catch (ApiProxyException | IOException e) {
        System.out.println("Failed to send");
      }
    }
  }
}
