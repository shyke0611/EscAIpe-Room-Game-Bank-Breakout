package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.RandomnessGenerate;
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
  @FXML private HBox key1;
  @FXML private HBox key2;
  @FXML private HBox key3;

  private String randomUsername;
  private String randomPassword;

  public void initialize() {
    SceneManager.setController(Scenes.LOBBY, this);
    // obtain random credentials
    randomUsername = RandomnessGenerate.getUsername();
    randomPassword = RandomnessGenerate.getPasscode();
    // add the hboxs into arraylist and generate random
    RandomnessGenerate.addKeyLocation(key1, key2, key3);
    RandomnessGenerate.generateRandomKeyLocation();
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
    // opens only when key is found to the drawer
    if (GameState.isKeyFound == true) {
    credentialsNote.setVisible(true);
    // set note text to the randomly generated credentials
    passwordLbl.setText(randomPassword);
    usernameLbl.setText(randomUsername);
    }
  }

  // pressing any location of the keys
  // if key found it turns invisible (we can change mehanics later)
  @FXML
  void onkeyPressed(MouseEvent event) {
    HBox clickedHBox = (HBox) event.getSource();
    if (clickedHBox == RandomnessGenerate.getkeyLocation()) {
      clickedHBox.setVisible(false);
      // set key found to true
      GameState.isKeyFound = true;
    }
  }
}
