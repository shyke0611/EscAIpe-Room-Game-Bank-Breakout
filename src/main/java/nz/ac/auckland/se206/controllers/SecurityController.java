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
import javafx.scene.layout.VBox;
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

  @FXML private ImageView Lobby;
  @FXML private Button LogOffBtn;
  @FXML private AnchorPane SecurityPane;
  @FXML private VBox SecurityRoomSwitch;
  @FXML private ImageView Vault;
  @FXML private VBox VaultRoomSwitch;
  @FXML private VBox lobbyRoomSwitch;
  @FXML private Button logInBtn;
  @FXML private HBox logInScreen;
  @FXML private HBox electricityBox;
  @FXML private Label loginMsgLbl;
  @FXML private PasswordField passwordField;
  @FXML private HBox computer;
  @FXML private TextField usernameField;
  @FXML private VBox walkietalkie;
  @FXML private VBox walkietalkieText;
  @FXML private ImageView securitybackground;
  @FXML private ImageView tempbackground;
  @FXML private TextArea securityTextArea;
  @FXML private TextField securityInputField;

  StyleManager styleManager = StyleManager.getInstance();
  WalkieTalkieManager walkieTalkieManager = WalkieTalkieManager.getInstance();
  HackerAiManager hackerAiManager = HackerAiManager.getInstance();

  public void initialize() {
    SceneManager.setController(Scenes.SECURITY, this);
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
    styleManager.addItems(computer, electricityBox, securitybackground);
    WalkieTalkieManager.addWalkieTalkie(null, walkietalkie);
    // styleManager.setItemsMessage("A computer...?", computer);
    // styleManager.setItemsMessage("it requires credentials?", logInBtn);
    // styleManager.setItemsMessage("no need to open this right now", electricityBox);
  }

  //   handling mouse events on walkie talkie
  //   open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  @FXML
  public void switchToLobby() {
    App.setUI(Scenes.LOBBY);
  }

  @FXML
  public void switchToVault() {
    if (GameState.isAlarmDisabled) {
      styleManager.getItem("bombHolder").setVisible(true);
      styleManager.setDisable(true, "bronzeDoor", "silverDoor", "goldDoor");
    }
    App.setUI(Scenes.VAULT);
  }

  public void switchToDots() {
    App.setUI(Scenes.CONNECTDOTS);
  }

  public void onSwitchToHacker() {
    SceneManager.setPreviousScene(Scenes.HACKERVAN, Scenes.VAULT);
    HackerVanController vanController =
        (HackerVanController) SceneManager.getController(Scenes.HACKERVAN);
    vanController.printChatHistory();
    vanController.loadQuickHints();
    App.setUI(Scenes.HACKERVAN);
  }

  @FXML
  void onWireCutting(MouseEvent event) {
    if (!GameState.isWiresCut && GameState.isAlarmTripped) {
      App.setUI(Scenes.WIRECUTTING);
    } else if (GameState.isWiresCut) {
      electricityBox.setDisable(true);
    }
  }

  // set visibility of log in screen off (log off computer)
  public void OnLogOff() {
    logInScreen.setVisible(false);
  }

  // check log in details before logging in
  public void onLogIn() {
    checkLogin();
  }

  // opening computer log in screen
  @FXML
  void onClickComputer(MouseEvent event) {
    // if already logged in, skip log in stage
    if (!GameState.isSecurityComputerLoggedIn) {
      logInScreen.setVisible(true);
    } else {
      logInScreen.setVisible(false);
      App.setUI(Scenes.COMPUTER);
      styleManager.removeItemsMessage("computer");
    }
  }

  // method that handles overall login mechanics
  private void checkLogin() {
    // get user input credentials
    String enteredUsername = usernameField.getText().toLowerCase();
    String enteredPassword = passwordField.getText();
    // get generated credentials
    String randomUsername = RandomnessGenerate.getUsername();
    String randomPassword = RandomnessGenerate.getPasscode();

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

  // check credentials
  private boolean areCredentialsValid(
      String enteredUsername,
      String enteredPassword,
      String randomUsername,
      String randomPassword) {
    return enteredUsername.equals(randomUsername) && enteredPassword.equals(randomPassword);
  }

  // check if credentials are empty
  private boolean areCredentialsEmpty() {
    return usernameField.getText().isEmpty() && passwordField.getText().isEmpty();
  }

  // mechanics for when login is successful
  private void handleSuccessfulLogin() {
    loginMsgLbl.setText("Success");
    GameState.isSecurityComputerLoggedIn = true;
    App.setUI(Scenes.COMPUTER);
    styleManager.setDisable(true, "credentialsBook");
    styleManager.setVisible(false, "credentialsNote");
  }

  // mechanics for empty credential input
  private void handleEmptyCredentials() {
    loginMsgLbl.setText("Enter your credentials");
  }

  // mechanics for when login fails
  private void handleFailedLogin() {
    loginMsgLbl.setText("Wrong username or password");
  }

  @FXML
  public void invokeHackerAI(KeyEvent event) throws ApiProxyException {

    if (event.getCode() == KeyCode.ENTER) {
      walkieTalkieManager.startAnimation();

      Task<Void> aiTask =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              // Perform AI-related operations here
              ChatMessage msg = new ChatMessage("user", securityInputField.getText());
              hackerAiManager.addChatHistory(msg.getContent());
              walkieTalkieManager.clearWalkieTalkie();

              ChatMessage responce = hackerAiManager.processInput(msg);
              hackerAiManager.addChatHistory(responce.getContent());

              // Move this code here to use the `responce` variable within the call method
              Platform.runLater(
                  () -> {
                    walkieTalkieManager.setWalkieTalkieText(responce);

                    securityInputField.clear();
                  });
              walkieTalkieManager.stopAnimation();
              return null;
            }
          };

      Thread aiThread = new Thread(aiTask);
      aiThread.setDaemon(true);
      aiThread.start();
    }
  }

  @FXML
  public void quickHint(ActionEvent event) {
    String hint = hackerAiManager.GetQuickHint();
    hackerAiManager.storeQuickHint();
    walkieTalkieManager.setWalkieTalkieText(new ChatMessage("user", hint));
  }
}
