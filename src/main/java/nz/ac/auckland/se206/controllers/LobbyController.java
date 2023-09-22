package nz.ac.auckland.se206.controllers;

import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nz.ac.auckland.se206.AnimationManager;
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

public class LobbyController extends Controller {

  @FXML private ImageView Security;
  @FXML private ImageView lobbybackground;
  @FXML private VBox SecurityRoomSwitch;
  @FXML private Label timerLabel;
  @FXML private ImageView Vault;
  @FXML private VBox VaultRoomSwitch;
  @FXML private VBox lobbyRoomSwitch;
  @FXML private Button closeNoteBtn;
  @FXML private HBox credentialsNote;
  @FXML private HBox drawerHolder;
  @FXML private HBox guardeyes;
  @FXML private Button quickHintBtn;
  @FXML private Button viewHistoryBtn;
  @FXML private VBox lobbywalkietalkie;
  @FXML private VBox walkietalkieText;
  @FXML private TextField lobbyTextInput;
  @FXML private TextArea lobbyTextArea;
  @FXML private ImageView lobbyWalkieTalkie;
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
  @FXML private Label orderLabel;

  private String randomUsername;
  private String randomPassword;
  private StyleManager styleManager = StyleManager.getInstance();
  private WalkieTalkieManager walkieTalkieManager = WalkieTalkieManager.getInstance();
  private HackerAiManager hackerAiManager = HackerAiManager.getInstance();

  public void initialize() {
    SceneManager.setController(Scenes.LOBBY, this);
    WalkieTalkieManager.addWalkieTalkieImage(this, lobbyWalkieTalkie);
    super.setTimerLabel(timerLabel, 1);

    // obtain random credentials
    RandomnessGenerate.generateRandomCredentials();
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
        lobbybackground,
        drawerHolder,
        credentialsBook,
        credentialsNote,
        drawer,
        openDrawer,
        SecurityRoomSwitch,
        VaultRoomSwitch,
        lobbyRoomSwitch,
        guardeyes);
    styleManager.setItemsMessage(
        "Guard is watching...", "key1", "key3", "key4", "guardpocket", "guardeyes");

    styleManager.setItemsMessage("It's locked...", "drawerHolder");
    styleManager.setItemsMessage("A note?", "credentialsBook");
    styleManager.setItemsMessage("put him to sleep", "guard");
    styleManager.setClueHover("guard", true);
    // setupListeners(key1,key3,key4,guard,credentialsBook,drawerHolder,guardpocket);
    setupListeners(key);
  }

  //   handling mouse events on walkie talkie
  //   open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  // @FXML
  // public void switchToSecurity() {

  // }

  // @FXML
  // public void switchToVault() {
  //   App.setUI(Scenes.VAULT);
  // }

  @FXML
  public void onSwitchToHacker() {
    // SceneManager.setPreviousScene(Scenes.HACKERVAN, Scenes.VAULT);
    HackerVanController vanController =
        (HackerVanController) SceneManager.getController(Scenes.HACKERVAN);
    vanController.printChatHistory();
    vanController.loadQuickHints();
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
      List<HBox> wires = RandomnessGenerate.getRandomWires();
      StringBuilder wireNames = new StringBuilder();

      for (HBox wire : wires) {
        String name = getWireLabel(wire);
        wireNames.append(name).append(" \n");
      }
      if (wireNames.length() > 0) {
        wireNames.setLength(wireNames.length() - 1);
      }

      usernameLbl.setText(null);
      passwordLbl.setText(null);

      orderLabel.setText(wireNames.toString());

      orderLabel.setVisible(true);
      styleManager.getItem("wirecuttingorderLbl").setVisible(false);
      titleLbl.setText("Wire Cutting Order");
      styleManager.setClueHover("guardpocket", false);
      GameState.isWireCredentialsFound = true;
      titleLbl.setPrefHeight(35);
    }
  }

  public String getWireLabel(Node wire) {
    String name = wire.getId();
    if (name.startsWith("red")) {
      name = "red wire";
    } else if (name.startsWith("blue")) {
      name = "blue wire";
    } else if (name.startsWith("yellow")) {
      name = "yellow wire";
    } else {
      name = "green wire";
    }
    return name;
  }

  @FXML
  void onGuardEyes(MouseEvent event) {
    if (GameState.isEyeScannerEntered) {
      guardeyes.setDisable(true);
      styleManager.getItem("compareBtn").setDisable(false);
      styleManager.getItem("geteyesampleLbl").setVisible(false);
      ((EyeScannerController) SceneManager.getController(Scenes.EYESCANNER)).updateGuardEye();
    }
  }

  // pressing book in drawer
  @FXML
  void onCredentialsBookPressed(MouseEvent event) {
    credentialsNote.setVisible(true);
    // set note text to the randomly generated credentials
    passwordLbl.setText("Password: " + randomPassword);
    usernameLbl.setText("Username: " + randomUsername);
    styleManager.removeItemsMessage("credentialsBook");
    if (!GameState.isSecurityRoomHoverPressed) {
      styleManager.setClueHover("SecurityRoomSwitch", true);
    }
    GameState.isSecurityRoomHoverPressed = true;
    GameState.isCredentialsFound = true;
    // styleManager.removeItemsMessage("computer");
    GameManager.completeObjective();
  }

  // pressing any location of the keys
  // if key found it turns invisible (we can change mehanics later)
  @FXML
  void onkeyLocationPressed(MouseEvent event) {
    if (GameState.isGuardDistracted) {
      Node clickedHBox = (HBox) event.getSource();
      styleManager.setItemsMessage("Already looked here...", clickedHBox.getId().toString());
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
    GameManager.completeObjective();
    key.setVisible(false);
    styleManager.setItemsState(HoverColour.GREEN, "drawerHolder");
    styleManager.setItemsMessage("The key fits...", "drawerHolder");
  }

  @FXML
  void onGuardPressed(MouseEvent event) {
    GameState.isGuardDistracted = true;
    sleepingAnimation();
    guard.setDisable(true);
    styleManager.setClueHover("guard", false);
    styleManager.setItemsState(HoverColour.GREEN, "key1", "key3", "key4");
    styleManager.setItemsState(HoverColour.RED, "guardpocket", "guardeyes");
    styleManager.setItemsMessage("Something seems odd here...", "key1", "key3", "key4");
    styleManager.setItemsMessage("Seems dangerous for now", "guardpocket", "guardeyes");
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

  @FXML
  public void invokeHackerAI(KeyEvent event) throws ApiProxyException {

    if (event.getCode() == KeyCode.ENTER) {

      Task<Void> aiTask =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              walkieTalkieManager.startAnimation();
              // Perform AI-related operations here
              ChatMessage msg = new ChatMessage("user", lobbyTextInput.getText());
              hackerAiManager.addChatHistory(msg.getContent());
              walkieTalkieManager.clearWalkieTalkie();

              ChatMessage responce = hackerAiManager.processInput(msg);
              hackerAiManager.addChatHistory(responce.getContent());

              // Move this code here to use the `responce` variable within the call method

              Platform.runLater(
                  () -> {
                    walkieTalkieManager.setWalkieTalkieText(responce);

                    lobbyTextInput.clear();
                    walkieTalkieManager.stopAnimation();
                  });
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
