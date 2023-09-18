package nz.ac.auckland.se206.controllers;

import java.util.List;

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
import javafx.scene.paint.Color;
import javafx.util.Duration;
import nz.ac.auckland.se206.AnimationManager;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.RandomnessGenerate;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager.HoverColour;
import nz.ac.auckland.se206.StyleManager.State;
import nz.ac.auckland.se206.WalkieTalkieManager;

public class LobbyController extends Controller {

  @FXML private ImageView Security;
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
    styleManager.setItemsMessage("Guard is watching...",key1,key3,key4);
    styleManager.setItemsMessage("It's locked...",drawerHolder);
    styleManager.setItemsMessage("A note?",credentialsBook);
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

  @FXML
  void onGuardPocket(MouseEvent event) {
      List<HBox> wires = RandomnessGenerate.getRandomWires();
      StringBuilder wireNames = new StringBuilder(); 
  
      for (HBox wire : wires) {
          String name = wire.getId();
          wireNames.append(name).append(", "); 
      }
      if (wireNames.length() > 0) {
          wireNames.setLength(wireNames.length() - 2);
      }
  
      credentialsNote.setVisible(true);
      usernameLbl.setText(wireNames.toString());
      passwordLbl.setText(null);
      titleLbl.setText("Wire Cutting Order");
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
      styleManager.setItemsMessage("Already looked here...",clickedHBox);
      if (clickedHBox == RandomnessGenerate.getkeyLocation()) {
        GameState.isKeyLocationFound = true;
        AnimationManager.fadeTransition(key,2);
        key.setDisable(false);
        key1.setDisable(true);
        key3.setDisable(true);
        key4.setDisable(true);
      }
    }
  }

// pressing the key
  @FXML
  void onKeyPressed(MouseEvent event) {
      GameState.isKeyFound = true;
      key.setVisible(false);
      styleManager.setItemsState(HoverColour.GREEN,State.HOVER, drawerHolder);
      styleManager.setItemsMessage("The key fits...",drawerHolder);
  }


  @FXML
  void onGuardPressed(MouseEvent event) {
    GameState.isGuardDistracted = true;
    sleepingAnmiation();
    guard.setDisable(true);
    styleManager.setItemsState(HoverColour.GREEN,State.HOVER,key1,key3,key4);
    styleManager.setItemsMessage("Something seems odd here...",key1,key3,key4);
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
