package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.RandomnessGenerate;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.WalkieTalkieManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager.HoverColour;

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
  @FXML private Label loginMsgLbl;
  @FXML private PasswordField passwordField;
  @FXML private HBox computer;
  @FXML private TextField usernameField;
  @FXML private VBox walkietalkie;
  @FXML private VBox walkietalkieText;

  StyleManager styleManager = StyleManager.getInstance();

  public void initialize() {
    SceneManager.setController(Scenes.SECURITY, this);
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
    styleManager.setItemsMessage("A computer...?",computer);
  }

  //   handling mouse events on walkie talkie
  //   open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  public void switchToLobby() {
    App.setUI(Scenes.LOBBY);
  }

  public void switchToVault() {
    App.setUI(Scenes.VAULT);
  }

  public void onSwitchToHacker() {
    SceneManager.setPreviousScene(Scenes.HACKERVAN, Scenes.SECURITY);
    App.setUI(Scenes.HACKERVAN);
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
      styleManager.removeItemsMessage(computer);
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
      styleManager.setItemsHoverState(HoverColour.GREEN, computer);
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
  }


  // mechanics for empty credential input
  private void handleEmptyCredentials() {
    loginMsgLbl.setText("Enter your credentials");
  }

  // mechanics for when login fails
  private void handleFailedLogin() {
    loginMsgLbl.setText("Wrong username or password");
  }
}
