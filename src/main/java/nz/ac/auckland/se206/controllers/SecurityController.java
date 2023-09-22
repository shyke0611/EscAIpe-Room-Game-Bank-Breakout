package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.HackerAiManager;
import nz.ac.auckland.se206.RandomnessGenerate;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.StyleManager.HoverColour;
import nz.ac.auckland.se206.WalkieTalkieManager;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class SecurityController extends Controller {

  // FXML elements
  @FXML private Label timerLabel;
  @FXML private Label numberOfHints;
  @FXML private Button logOffBtn;
  @FXML private AnchorPane securityPane;
  @FXML private VBox securityRoomSwitch;
  @FXML private VBox vaultRoomSwitch;
  @FXML private VBox lobbyRoomSwitch;
  @FXML private Button logInBtn;
  @FXML private HBox logInScreen;
  @FXML private HBox electricityBox;
  @FXML private Label loginMsgLbl;
  @FXML private PasswordField passwordField;
  @FXML private HBox computer;
  @FXML private TextField usernameField;
  @FXML private VBox securitywalkietalkie;
  @FXML private VBox walkietalkieText;
  @FXML private ImageView securitybackground;
  @FXML private ImageView tempbackground;
  @FXML private TextArea securityTextArea;
  @FXML private TextField securityInputField;
  @FXML private ImageView securityWalkieTalkie;

  // Managers and controllers
  private StyleManager styleManager = StyleManager.getInstance();
  private WalkieTalkieManager walkieTalkieManager = WalkieTalkieManager.getInstance();
  private HackerAiManager hackerAiManager = HackerAiManager.getInstance();

  public void initialize() {
    // Set the controller for the current scene
    SceneManager.setController(Scenes.SECURITY, this);

    // Set the timer label
    super.setTimerLabel(timerLabel, 1);

    // Add walkie talkie elements to the scene
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
    WalkieTalkieManager.addWalkieTalkieImage(this, securityWalkieTalkie);
    WalkieTalkieManager.addWalkieTalkieHint(this, numberOfHints);

    // Add style items and set messages
    styleManager.addItems(
        computer,
        electricityBox,
        securitybackground,
        vaultRoomSwitch,
        lobbyRoomSwitch,
        securityRoomSwitch);
    styleManager.setItemsMessage("A computer...?", "computer");
    styleManager.setItemsMessage("no need to open this right now", "electricityBox");
  }

  // Handling mouse events on walkie talkie - opens and closes when walkie talkie is clicked
  @FXML
  private void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  // Switch to Hacker scene when the button is clicked
  @FXML
  private void onSwitchToHacker() {
    // setting relevant method for hacker scene
    SceneManager.setPreviousScene(Scenes.HACKERVAN, Scenes.LOBBY);
    HackerVanController vanController =
        (HackerVanController) SceneManager.getController(Scenes.HACKERVAN);
    // loading relevant information
    vanController.printChatHistory();
    vanController.loadQuickHints();
    App.setUI(Scenes.HACKERVAN);
  }

  // Handling wire cutting when clicked
  @FXML
  private void onWireCutting(MouseEvent event) {
    // if wire not cur and alarm is tripped
    if (!GameState.isWiresCut && GameState.isAlarmTripped) {
      // when wire order are not found yet
      if (!GameState.isWireCredentialsFound) {
        App.textToSpeech("you need to find wire cutting order");
      }
      App.setUI(Scenes.WIRECUTTING);
      // when wire is cut
    } else if (GameState.isWiresCut) {
      electricityBox.setDisable(true);
    }
  }

  // Set visibility of the log in screen off (log off computer)
  @FXML
  private void onLogOff(ActionEvent event) {
    logInScreen.setVisible(false);
  }

  // Check log in details before logging in
  @FXML
  private void onLogIn(ActionEvent event) {
    checkLogin();
  }

  // Opening computer log in screen
  @FXML
  private void onClickComputer(MouseEvent event) {
    styleManager.setClueHover("computer", false);
    // If already logged in, skip log in stage
    if (!GameState.isSecurityComputerLoggedIn) {
      logInScreen.setVisible(true);
      // if connect the dot puzzle is reached
    } else if (GameState.isConnectDotreached) {
      App.setUI(Scenes.CONNECTDOTS);
    } else {
      logInScreen.setVisible(false);
      App.setUI(Scenes.COMPUTER);
      styleManager.removeItemsMessage("computer");
    }
  }

  // Method that handles overall login mechanics
  private void checkLogin() {
    // Get user input credentials
    String enteredUsername = usernameField.getText().toLowerCase();
    String enteredPassword = passwordField.getText();
    // Get generated credentials
    String randomUsername = RandomnessGenerate.getUsername();
    String randomPassword = RandomnessGenerate.getPasscode();

    // handling the credentials
    if (areCredentialsValid(enteredUsername, enteredPassword, randomUsername, randomPassword)) {
      handleSuccessfulLogin();
      logInScreen.setVisible(false);
      styleManager.setItemsState(HoverColour.GREEN, "computer");
    } else if (areCredentialsEmpty()) {
      handleEmptyCredentials();
    } else {
      handleFailedLogin();
    }
  }

  // Check credentials
  private boolean areCredentialsValid(
      String enteredUsername,
      String enteredPassword,
      String randomUsername,
      String randomPassword) {
    // return if they are correct
    return enteredUsername.equals(randomUsername) && enteredPassword.equals(randomPassword);
  }

  // Check if credentials are empty
  private boolean areCredentialsEmpty() {
    return usernameField.getText().isEmpty() && passwordField.getText().isEmpty();
  }

  // Mechanics for when login is successful
  private void handleSuccessfulLogin() {
    // sets relevant method for when credentials are correct
    loginMsgLbl.setText("Success");
    loginMsgLbl.setTextFill(Color.GREEN);
    GameState.isSecurityComputerLoggedIn = true;
    App.setUI(Scenes.COMPUTER);
    // setting style
    styleManager.setDisable(true, "credentialsBook");
    styleManager.setVisible(false, "credentialsNote");
  }

  // Mechanics for empty credential input
  private void handleEmptyCredentials() {
    loginMsgLbl.setText("Enter your credentials");
    loginMsgLbl.setTextFill(Color.ORANGE);
  }

  // Mechanics for when login fails
  private void handleFailedLogin() {
    loginMsgLbl.setText("Wrong username or password");
    loginMsgLbl.setTextFill(Color.RED);
  }

  // Invoke Hacker AI when the Enter key is pressed
  @FXML
  private void invokeHackerAI(KeyEvent event) throws ApiProxyException {

    if (event.getCode() == KeyCode.ENTER && walkieTalkieManager.isWalkieTalkieOpen()) {

      Task<Void> aiTask2 =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              walkieTalkieManager.startAnimation();
              // Perform AI-related operations here
              ChatMessage msg = new ChatMessage("user", securityInputField.getText());
              hackerAiManager.addChatHistory(msg.getContent());
              walkieTalkieManager.clearWalkieTalkie();

              ChatMessage response = hackerAiManager.processInput(msg);
              hackerAiManager.addChatHistory(response.getContent());

              // Move this code here to use the `response` variable within the call method
              Platform.runLater(
                  () -> {
                    walkieTalkieManager.setWalkieTalkieText(response);

                    securityInputField.clear();
                    walkieTalkieManager.stopAnimation();
                  });

              return null;
            }
          };

      Thread aiThread2 = new Thread(aiTask2);
      aiThread2.setDaemon(true);
      aiThread2.start();
    }
  }

  // Display a quick hint when the hint button is clicked
  @FXML
  private void onQuickHint(ActionEvent event) {
    String hint = hackerAiManager.GetQuickHint();
    hackerAiManager.storeQuickHint();
    walkieTalkieManager.setWalkieTalkieText(new ChatMessage("user", hint));
  }
}
