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
import nz.ac.auckland.se206.SceneManager.Scenes;

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
  @FXML private HBox temporaryComputer;
  @FXML private TextField usernameField;
  @FXML private VBox walkietalkie;
  @FXML private VBox walkietalkieText;

  public void initialize() {
    SceneManager.setController(Scenes.SECURITY, this);
  }

  //   handling mouse events on walkie talkie
  //   open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    SceneManager.toggleWalkieTalkieOpen();
  }

  public void synchWalkieTalkie(boolean isOpen) {
    walkietalkieText.setVisible(isOpen);
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
  @FXML
  public void OnLogOff() {
    logInScreen.setVisible(false);
  }

  // check log in details before logging in
  @FXML
  public void onLogIn() {
    checkLogin();
  }

  // opening computer log in screen
  @FXML
  void onClickComputer(MouseEvent event) {
    // if already logged in, skip log in stage
    if (GameState.isSecurityComputerLoggedIn == false) {
      logInScreen.setVisible(true);
    } else {
      logInScreen.setVisible(false);
      App.setUI(Scenes.COMPUTER);
    }
  }

  // method that checks log in credentials
  private void checkLogin() {
    // get user input credentials
    String enteredUsername = usernameField.getText().toLowerCase();
    String enteredPassword = passwordField.getText();

    // get generated credentials
    String randomUsername = RandomnessGenerate.getUsername();
    String randomPassword = RandomnessGenerate.getPasscode();

    // for testing purposes
    System.out.println(randomUsername);
    System.out.println(randomPassword);

    // correct credentials
    if (enteredUsername.equals(randomUsername) && enteredPassword.equals(randomPassword)) {
      loginMsgLbl.setText("Success");
      GameState.isSecurityComputerLoggedIn = true;
      logInScreen.setVisible(false);
      App.setUI(Scenes.COMPUTER);
      // empty input
    } else if (usernameField.getText().isEmpty() && passwordField.getText().isEmpty()) {
      loginMsgLbl.setText("Enter your credentials");
    } else {
      // wrong credentials
      loginMsgLbl.setText("Wrong username or password");
    }
  }

}
