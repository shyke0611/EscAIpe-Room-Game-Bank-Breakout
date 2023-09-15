package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.RandomCredentialsGenerator;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class LobbyController extends Controller {

  @FXML private ImageView Security;
  @FXML private VBox SecurityRoomSwitch;
  @FXML private ImageView Vault;
  @FXML private VBox VaultRoomSwitch;
  @FXML private Button closeNoteBtn;
  @FXML private VBox credentialsNote;
  @FXML private HBox drawer;
  @FXML private VBox lobbyRoomSwitch;
  @FXML private Label passwordLbl;
  @FXML private Button quickHintBtn;
  @FXML private Label usernameLbl;
  @FXML private Button viewHistoryBtn;
  @FXML private VBox walkietalkie;
  @FXML private VBox walkietalkieText;

  private String randomUsername;
  private String randomPassword;

  public void initialize() {
    SceneManager.setController(Scenes.LOBBY, this);
    // Use credentialsManager to obtain random credentials
    randomUsername = RandomCredentialsGenerator.getUsername();
    randomPassword = RandomCredentialsGenerator.getPasscode();
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

  public void switchToSecurity() {
    App.setUI(Scenes.SECURITY);
  }

  public void switchToVault() {
    App.setUI(Scenes.VAULT);
  }

  public void onSwitchToHacker() {
    SceneManager.setPreviousScene(Scenes.HACKERVAN, Scenes.LOBBY);
    App.setUI(Scenes.HACKERVAN);
  }

  // closing credential notes
  @FXML
  void onCloseNote() {
    credentialsNote.setVisible(false);
  }

  // opening drawer to get credential notes
  @FXML
  void onDrawerClicked(MouseEvent event) {
    credentialsNote.setVisible(true);
    passwordLbl.setText(randomPassword);
    usernameLbl.setText(randomUsername);
  }
}
