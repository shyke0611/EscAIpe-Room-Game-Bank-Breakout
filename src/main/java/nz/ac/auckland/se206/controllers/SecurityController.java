package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameManager;
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

/** Controller class for the Security Controller scene. */
public class SecurityController extends Controller {

  // FXML elements
  @FXML private Label timerLabel;
  @FXML private Label numberOfHints;
  @FXML private HBox logOffBtn;
  @FXML private AnchorPane securityPane;
  @FXML private VBox securityRoomSwitch;
  @FXML private VBox vaultRoomSwitch;
  @FXML private VBox lobbyRoomSwitch;
  @FXML private HBox logInBtn;
  @FXML private Button quickHintBtn;
  @FXML private Pane logInScreen;
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
  @FXML private StackPane wallEmployeeImage;
  @FXML private StackPane hoverEmployee;
  @FXML private Label employeeName;
  @FXML private Label moneyCount;
  @FXML private ImageView securityNotification;
  @FXML private ImageView computerImage;

  // Managers and controllers
  private StyleManager styleManager = StyleManager.getInstance();
  private WalkieTalkieManager walkieTalkieManager = WalkieTalkieManager.getInstance();
  private HackerAiManager hackerAiManager = HackerAiManager.getInstance();

  /** Initialize the Security Controller. Sets up the initial state of the Security scene. */
  public void initialize() {
    // Set the controller for the current scene
    SceneManager.setController(Scenes.SECURITY, this);
    // Set the timer label
    super.setTimerLabel(timerLabel, 1);
    // Set the employee name
    employeeName.setText(RandomnessGenerate.getRandomEmployeeName());
    System.out.println(employeeName.getText());
    // Add walkie talkie elements to the scene
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
    WalkieTalkieManager.addWalkieTalkieImage(this, securityWalkieTalkie);
    WalkieTalkieManager.addWalkieTalkieHint(this, numberOfHints);
    WalkieTalkieManager.addQuickHintBtn(this, quickHintBtn);
    WalkieTalkieManager.addWalkieTalkieTextArea(this, securityTextArea);
    WalkieTalkieManager.addWalkieTalkieNotification(this, securityNotification);
    GameManager.addMoneyGainedLabel(this, moneyCount);
    // add styling
    styleManager.addHoverItems(
        computer, electricityBox, wallEmployeeImage, securitybackground, computerImage);
    StyleManager.setItemsMessage("A computer...?", "computer");
    StyleManager.setItemsMessage("no need to open this right now", "electricityBox");
    StyleManager.setItemsMessage("Employee of the month..?", "wallEmployeeImage");
  }

  /**
   * Handles the mouse click event on the Walkie-Talkie icon. Opens and closes the Walkie-Talkie.
   *
   * @param event The mouse click event.
   */
  @FXML
  private void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  /**
   * Handles the event when switching to the Hacker scene. Loads relevant information for the
   * HackerVan scene.
   */
  @FXML
  private void onSwitchToHacker() {
    // setting relevant method for hacker scene
    HackerVanController vanController =
        (HackerVanController) SceneManager.getController(Scenes.HACKERVAN);
    // loading relevant information
    vanController.printChatHistory();
    vanController.loadQuickHints();
    App.setUi(Scenes.HACKERVAN);
  }

  /**
   * Handles the wire cutting event. If the wire is not cut and the alarm is tripped, it opens the
   * Wire Cutting scene.
   *
   * @param event The mouse click event.
   */
  @FXML
  private void onWireCutting(MouseEvent event) {
    // if wire not cur and alarm is tripped
    if (!GameState.isWiresCut && GameState.isAlarmTripped) {
      StyleManager.setClueHover("electricityBox", false);
      StyleManager.removeItemsMessage("electricityBox");
      App.setUi(Scenes.WIRECUTTING);
      // when wire is cut
    } else if (GameState.isWiresCut) {
      electricityBox.setDisable(true);
    }
  }

  /**
   * Handles the log-off event. Sets the visibility of the log-in screen to off.
   *
   * @param event The mouse event.
   */
  @FXML
  private void onLogOff(MouseEvent event) {
    logInScreen.setVisible(false);
  }

  /**
   * Checks login credentials and handles the log-in event.
   *
   * @param event The mouse event.
   */
  @FXML
  private void onLogIn(MouseEvent event) {
    checkLogin();
  }

  /**
   * Opens the computer screen and handles login events.
   *
   * @param event The mouse event.
   */
  @FXML
  private void onClickComputer(MouseEvent event) {
    // If not already logged in, go to log in screen
    if (!GameState.isSecurityComputerLoggedIn) {
      logInScreen.setVisible(true);
      // if connect the dot puzzle is reached
    } else if (GameState.isConnectDotreached) {
      App.setUi(Scenes.CONNECTDOTS);
    } else {
      // go straight to computer scene
      logInScreen.setVisible(false);
      GameManager.setCurrentObjective(GameManager.Objectives.DISABLE_FIREWALL);
      App.setUi(Scenes.COMPUTER);
      StyleManager.removeItemsMessage("computer");
    }
    StyleManager.setClueHover("computer", false);
  }

  /** Validates user login credentials and handles the login process. */
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
    } else if (areCredentialsEmpty()) {
      loginMsgLbl.setText("Enter your credentials");
    } else {
      loginMsgLbl.setText("Wrong username or password");
    }
  }

  /**
   * Checks if the entered credentials are valid.
   *
   * @param enteredUsername The entered username.
   * @param enteredPassword The entered password.
   * @param randomUsername The generated random username.
   * @param randomPassword The generated random password.
   * @return True if the entered credentials are valid, false otherwise.
   */
  private boolean areCredentialsValid(
      String enteredUsername,
      String enteredPassword,
      String randomUsername,
      String randomPassword) {
    // return if they are correct
    return enteredUsername.equals(randomUsername) && enteredPassword.equals(randomPassword);
  }

  /**
   * Checks if the login credentials are empty.
   *
   * @return True if both username and password fields are empty, false otherwise.
   */
  private boolean areCredentialsEmpty() {
    return usernameField.getText().isEmpty() && passwordField.getText().isEmpty();
  }

  /** Handles actions when login is successful. */
  private void handleSuccessfulLogin() {
    // sets relevant method for when credentials are correct
    loginMsgLbl.setText("Success");
    GameState.isSecurityComputerLoggedIn = true;
    App.setUi(Scenes.COMPUTER);
    // setting style
    StyleManager.setItemsHoverColour(HoverColour.GREEN, "computer");
    StyleManager.removeItemsMessage("computer");
    StyleManager.setDisable(true, "credentialsBook");
    StyleManager.setVisible(false, "credentialsNote");
  }

  /**
   * Handles the event when the Enter key is pressed in the Walkie-Talkie input field and the
   * Walkie-Talkie is open. Initiates AI processing and communication.
   *
   * @param event The KeyEvent.
   * @throws ApiProxyException If there is an issue with the API proxy.
   */
  @FXML
  private void onInvokeHacker(KeyEvent event) throws ApiProxyException {
    // Check if the Enter key is pressed and the Walkie-Talkie is open
    if (event.getCode() == KeyCode.ENTER && walkieTalkieManager.isWalkieTalkieOpen()) {
      // Start the typing animation
      walkieTalkieManager.startAnimation();
      // Create a background task for AI processing
      Task<Void> aiTask3 =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              // Perform AI-related operations here
              // Create a ChatMessage from the user's input
              ChatMessage msg = new ChatMessage("user", securityInputField.getText());
              // Add the user's input to the chat history managed by the hackerAiManager
              hackerAiManager.addChatHistory("User: " + msg.getContent());
              walkieTalkieManager.clearWalkieTalkie();
              // Process the user's input with the hackerAiManager and get a response
              ChatMessage response = hackerAiManager.processInput(msg);
              hackerAiManager.addChatHistory("Cipher: " + response.getContent());
              // Use Platform.runLater to update the UI on the JavaFX Application Thread
              Platform.runLater(
                  () -> {
                    walkieTalkieManager.setWalkieTalkieText(response);
                    securityInputField.clear();
                    walkieTalkieManager.stopAnimation();
                  });
              return null;
            }
          };
      // Create a new thread for the AI task and start it
      Thread aiThread3 = new Thread(aiTask3);
      aiThread3.setDaemon(true);
      aiThread3.start();
    }
  }

  /**
   * Handles the event when the Quick Hint button is pressed to request a quick hint from the AI.
   * Sets the Walkie-Talkie text to the hint.
   *
   * @param event The action event.
   */
  @FXML
  private void onQuickHint(ActionEvent event) {
    // Get a quick hint from the hackerAiManager
    String hint = hackerAiManager.getQuickHint();
    hackerAiManager.storeQuickHint();
    // Set the Walkie-Talkie text to the hint
    walkieTalkieManager.setWalkieTalkieText(new ChatMessage("user", hint));
  }

  @FXML
  public void onEnterPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      checkLogin();
    }
  }

  @FXML
  private void onHoverEmployee(MouseEvent event) {
    hoverEmployee.setVisible(true);
  }

  @FXML
  private void onHoverEmployeeExit(MouseEvent event) {
    hoverEmployee.setVisible(false);
  }
}
