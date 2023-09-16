package nz.ac.auckland.se206.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.RandomnessGenerate;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.WalkieTalkieManager;

public class LobbyController extends Controller {

  @FXML private ImageView Security;
  @FXML private VBox SecurityRoomSwitch;
  @FXML private ImageView Vault;
  @FXML private VBox VaultRoomSwitch;
  @FXML private Button closeNoteBtn;
  @FXML private VBox credentialsNote;
  @FXML private HBox drawerHolder;
  @FXML private VBox lobbyRoomSwitch;
  @FXML private Label passwordLbl;
  @FXML private Button quickHintBtn;
  @FXML private Label usernameLbl;
  @FXML private Button viewHistoryBtn;
  @FXML private VBox walkietalkie;
  @FXML private VBox walkietalkieText;
  // key locations:
  @FXML private HBox key1;
  @FXML private HBox key2;
  @FXML private HBox key3;
  @FXML private HBox key4;
  @FXML private ImageView key;
  @FXML private HBox guard;
  @FXML private ImageView zzz1;
  @FXML private ImageView zzz2;
  @FXML private ImageView drawer;
  @FXML private ImageView openDrawer;
  @FXML private HBox credentialsBook;

  private String randomUsername;
  private String randomPassword;

  public void initialize() {
    SceneManager.setController(Scenes.LOBBY, this);
    // obtain random credentials
    randomUsername = RandomnessGenerate.getUsername();
    randomPassword = RandomnessGenerate.getPasscode();
    // add the hboxs into arraylist and generate random
    RandomnessGenerate.addKeyLocation(key1, key2, key3, key4);
    RandomnessGenerate.generateRandomKeyLocation();
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
  }

  //   handling mouse events on walkie talkie
  //   open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
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
  void onDrawerPressed(MouseEvent event) {
    // opens only when key is found to the drawer
    if (GameState.isKeyFound) {
      drawer.setVisible(false);
      openDrawer.setVisible(true);
      credentialsBook.setVisible(true);
      drawerHolder.setDisable(true);
    }
  }

  // pressing book in drawer
  @FXML
  void onCredentialsBookPressed(MouseEvent event) {
    credentialsNote.setVisible(true);
    // set note text to the randomly generated credentials
    passwordLbl.setText("Password: " + randomPassword);
    usernameLbl.setText("Username: " + randomUsername);
  }

  // pressing any location of the keys
  // if key found it turns invisible (we can change mehanics later)
  @FXML
  void onkeyLocationPressed(MouseEvent event) {
    if (GameState.isGuardDistracted) {
      HBox clickedHBox = (HBox) event.getSource();
      if (clickedHBox == RandomnessGenerate.getkeyLocation()) {
        GameState.isKeyLocationFound = true;
        showKeyAnimation(key);
        disableKeyLocations();
      }
    }
  }

  // shoing animation of key moving up
  public void showKeyAnimation(Node node) {
    if (!GameState.isKeyFound) {
        // Disable mouse click events on the node
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), key);
        fadeIn.setFromValue(0.0); // Start from fully transparent
        fadeIn.setToValue(1.0);   // Fade in to fully opaque
        // Play the FadeTransition
        fadeIn.play();
    }
}


// pressing the key
  @FXML
  void onKeyPressed(MouseEvent event) {
    if (GameState.isKeyLocationFound) {
      GameState.isKeyFound = true;
      key.setVisible(false);
    }
  }

  // diabling key locations
  private void disableKeyLocations() {
    key1.setDisable(true);
    key2.setDisable(true);
    key3.setDisable(true);
    key4.setDisable(true);
  }


  @FXML
  void onGuardPressed(MouseEvent event) {
    GameState.isGuardDistracted = true;
    sleepingAnmiation();
    guard.setDisable(true);
    StyleManager.setItemsState(34,255,0,key1,key2,key3,key4);
  }

  private boolean isZzz1Visible = false;

  public void toggleImageViews() {
    if (isZzz1Visible) {
      zzz1.setVisible(false);
      zzz2.setVisible(true);
      isZzz1Visible = false;
    } else {
      zzz1.setVisible(true);
      zzz2.setVisible(false);
      isZzz1Visible = true;
    }
  }

  public void sleepingAnmiation() {
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> toggleImageViews()));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

}
