package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
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

  private boolean isWalkieTalkieOpened = false;

  public void initialize() {
    SceneManager.setController(Scenes.SECURITY, this);
  }

  // handling mouse events on walkie talkie
  // open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    isWalkieTalkieOpened = !isWalkieTalkieOpened;
    walkietalkieText.setVisible(isWalkieTalkieOpened);
  }

  public void switchToLobby() {
    App.setUI(Scenes.LOBBY);
  }

  public void switchToVault() {
    App.setUI(Scenes.VAULT);
  }

  // set visibility og log in screen off (log off computer)
  @FXML
  public void OnLogOff(ActionEvent event) {
    logInScreen.setVisible(false);
  }

  // check log in details before logging in
  @FXML
  public void onLogIn(ActionEvent event) {
    checkLogin();
  }

  // opening computer log in screen
  @FXML
  void onClickComputer(MouseEvent event) {
    logInScreen.setVisible(true);
  }

  private void checkLogin() {
    // correct credentials
    if (usernameField.getText().toLowerCase().equals("random username")
        && passwordField.getText().equals("random password")) {
      loginMsgLbl.setText("Success");
      // switch view here on this line
      // empty
    } else if (usernameField.getText().isEmpty() && passwordField.getText().isEmpty()) {
      loginMsgLbl.setText("Enter your credentials");
    } else {
      // wrong credentials
      loginMsgLbl.setText("Wrong username or password");
    }
  }
}
