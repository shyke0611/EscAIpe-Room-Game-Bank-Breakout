package nz.ac.auckland.se206.controllers;

import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nz.ac.auckland.se206.AnimationManager;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.RandomnessGenerate;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.StyleManager.HoverColour;
import nz.ac.auckland.se206.WalkieTalkieManager;

public class LobbyController extends Controller {

  @FXML private ImageView Security;
  @FXML private ImageView lobbybackground;
  @FXML private VBox SecurityRoomSwitch;
  @FXML private ImageView Vault;
  @FXML private VBox VaultRoomSwitch;
  @FXML private Button closeNoteBtn;
  @FXML private HBox credentialsNote;
  @FXML private HBox drawerHolder;
  @FXML private VBox lobbyRoomSwitch;
  @FXML private Button quickHintBtn;
  @FXML private Button viewHistoryBtn;
  @FXML private VBox walkietalkie;
  @FXML private VBox walkietalkieText;
  // key locations:
  @FXML private HBox key1;
  @FXML private HBox key3;
  @FXML private HBox key4;
  @FXML private HBox guardpocket;
  @FXML private HBox key;
  @FXML private HBox guard;
  @FXML private ImageView zzz1;
  @FXML private ImageView zzz2;
  @FXML private ImageView drawer;
  @FXML private ImageView openDrawer;
  @FXML private HBox credentialsBook;

  @FXML private Label titleLbl;
  @FXML private Label passwordLbl;
  @FXML private Label usernameLbl;

  private String randomUsername;
  private String randomPassword;
  StyleManager styleManager = StyleManager.getInstance();

  public void initialize() {
    SceneManager.setController(Scenes.LOBBY, this);
    // obtain random credentials
    randomUsername = RandomnessGenerate.getUsername();
    randomPassword = RandomnessGenerate.getPasscode();
    // add the hboxs into arraylist and generate random
    RandomnessGenerate.addKeyLocation(key1, key3, key4);
    RandomnessGenerate.generateRandomKeyLocation();
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
    styleManager.addItems(
      key,
        key1,
        key3,
        key4,
        credentialsBook,
        credentialsNote,
        guard,
        guardpocket,
        drawerHolder,
        lobbybackground, drawerHolder,credentialsBook,credentialsNote,drawer,openDrawer);
    styleManager.setItemsMessage("Guard is watching...", key1, key3, key4, guardpocket);
    styleManager.setItemsMessage("It's locked...", drawerHolder);
    styleManager.setItemsMessage("A note?", credentialsBook);
  }

  //   handling mouse events on walkie talkie
  //   open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  @FXML
  public void switchToSecurity() {
    App.setUI(Scenes.SECURITY);
  }

  @FXML
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
      styleManager.setVisible(false, "drawer");
      styleManager.setVisible(true, "openDrawer", "credentialsBook");
      styleManager.setDisable(true, "drawerHolder");
    }
  }

  @FXML
  void onGuardPocket(MouseEvent event) {
    if (GameState.isAlarmTripped) {
      credentialsNote.setVisible(true);
      credentialsNote.setDisable(false);
      styleManager.setItemsMessage("Wire cutting..?", guardpocket);
      List<HBox> wires = RandomnessGenerate.getRandomWires();
      StringBuilder wireNames = new StringBuilder();

      for (HBox wire : wires) {
        String name = wire.getId();
        wireNames.append(name).append(", ");
      }
      if (wireNames.length() > 0) {
        wireNames.setLength(wireNames.length() - 2);
      }

      usernameLbl.setText(wireNames.toString());
      passwordLbl.setText(null);
      titleLbl.setText("Wire Cutting Order");
    }
    styleManager.setItemsMessage("Already looked here", guardpocket);
  }

  // pressing book in drawer
  @FXML
  void onCredentialsBookPressed(MouseEvent event) {
    credentialsNote.setVisible(true);
    // set note text to the randomly generated credentials
    passwordLbl.setText("Password: " + randomPassword);
    usernameLbl.setText("Username: " + randomUsername);
    titleLbl.setText("Security Room Computer Log In");
    styleManager.removeItemsMessage(credentialsBook);
  }

  // pressing any location of the keys
  // if key found it turns invisible (we can change mehanics later)
  @FXML
  void onkeyLocationPressed(MouseEvent event) {
    if (GameState.isGuardDistracted) {
      HBox clickedHBox = (HBox) event.getSource();
      styleManager.setItemsMessage("Already looked here...", clickedHBox);
      if (clickedHBox == RandomnessGenerate.getkeyLocation()) {
        GameState.isKeyLocationFound = true;
        AnimationManager.fadeTransition(key, 2);
        styleManager.setDisable(false, "key");
        styleManager.setDisable(true, "key1", "key3", "key4");
      }
    }
  }

  // pressing the key
  @FXML
  void onKeyPressed(MouseEvent event) {
    GameState.isKeyFound = true;
    key.setVisible(false);
    styleManager.setItemsState(HoverColour.GREEN, "drawerHolder");
    styleManager.setItemsMessage("The key fits...", drawerHolder);
  }

  @FXML
  void onGuardPressed(MouseEvent event) {
    GameState.isGuardDistracted = true;
    sleepingAnimation();
    guard.setDisable(true);
    styleManager.setItemsState(HoverColour.GREEN, "key1", "key3", "key4");
    styleManager.setItemsMessage("Something seems odd here...", key1, key3, key4);
    styleManager.setItemsMessage("Something seems odd here", guardpocket);
  }

  boolean isZzz1Visible = false;

  public void toggleImageViews() {
    zzz1.setVisible(isZzz1Visible);
    zzz2.setVisible(!isZzz1Visible);
    isZzz1Visible = !isZzz1Visible;
  }

  public void sleepingAnimation() {
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> toggleImageViews()));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

}
