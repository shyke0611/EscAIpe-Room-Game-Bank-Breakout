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

  // FXML elements
  @FXML private ImageView lobbybackground;
  @FXML private VBox securityRoomSwitch;
  @FXML private Label timerLabel;
  @FXML private VBox vaultRoomSwitch;
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

  @FXML private Label numberOfHints;
  @FXML private Label titleLbl;
  @FXML private Label passwordLbl;
  @FXML private Label usernameLbl;
  @FXML private Label orderLabel;

  // Other fields
  private String randomUsername;
  private String randomPassword;
  private StyleManager styleManager = StyleManager.getInstance();
  private WalkieTalkieManager walkieTalkieManager = WalkieTalkieManager.getInstance();
  private HackerAiManager hackerAiManager = HackerAiManager.getInstance();
  private boolean isZzz1Visible = false;

  /** Initializes the LobbyController. */
  public void initialize() {
    // set method on initialisation
    SceneManager.setController(Scenes.LOBBY, this);
    WalkieTalkieManager.addWalkieTalkieImage(this, lobbyWalkieTalkie);
    WalkieTalkieManager.addWalkieTalkieHint(this, numberOfHints);
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
    WalkieTalkieManager.addQuickHintBtn(this, quickHintBtn);
    super.setTimerLabel(timerLabel, 1);
    // set random components
    RandomnessGenerate.generateRandomCredentials();
    randomUsername = RandomnessGenerate.getUsername();
    randomPassword = RandomnessGenerate.getPasscode();
    RandomnessGenerate.addKeyLocation(key1, key3, key4);
    RandomnessGenerate.generateRandomKeyLocation();

    // adding relevant nodes (items) into the stylemanager list
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
        securityRoomSwitch,
        vaultRoomSwitch,
        lobbyRoomSwitch,
        guardeyes);
    styleManager.setItemsMessage(
        "Guard is watching...", "key1", "key3", "key4", "guardpocket", "guardeyes");

    // setting style to items
    styleManager.setItemsMessage("It's locked...", "drawerHolder");
    styleManager.setItemsMessage("A note?", "credentialsBook");
    styleManager.setItemsMessage("Put him to sleep", "guard");
    styleManager.setClueHover("guard", true);
    setUpListener(key);
  }

  // Handling mouse events on walkie talkie
  // Opens and closes when walkie talkie is clicked
  @FXML
  private void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  @FXML
  private void onSwitchToHacker() {
    // setting relevant method for hacker scene
    HackerVanController vanController =
        (HackerVanController) SceneManager.getController(Scenes.HACKERVAN);
    // loading relevant information
    vanController.printChatHistory();
    vanController.loadQuickHints();
    App.setUI(Scenes.HACKERVAN);
  }

  // Closing credential notes
  @FXML
  private void onCloseNote() {
    credentialsNote.setVisible(false);
  }

  // Opening drawer to get credential notes
  @FXML
  private void onDrawerPressed(MouseEvent event) {
    // Opens only when key is found to the drawer
    if (GameState.isKeyFound) {
      // setting visibility
      styleManager.setVisible(false, "drawer");
      styleManager.setVisible(true, "openDrawer", "credentialsBook");
      styleManager.setDisable(true, "drawerHolder");
    }
  }

  @FXML
  private void onGuardPocket(MouseEvent event) {

    // executes when alarm is tripped
    if (GameState.isAlarmTripped) {
      // opening credentials note
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

      // setting wire order information to text
      orderLabel.setText(wireNames.toString());
      orderLabel.setVisible(true);
      titleLbl.setText("Wire Cutting Order");
      titleLbl.setPrefHeight(35);
      // setting style
      styleManager.getItem("wirecuttingorderLbl").setVisible(false);
      styleManager.setClueHover("guardpocket", false);
      GameState.isWireCredentialsFound = true;
    }
  }

  private String getWireLabel(Node wire) {
    String name = wire.getId();
    // checking wire id to set name
    if (name.startsWith("red")) {
      name = "red wire";
    } else if (name.startsWith("blue")) {
      name = "blue wire";
    } else if (name.startsWith("yellow")) {
      name = "yellow wire";
    } else {
      name = "green wire";
    }
    // returning the wire label
    return name;
  }

  @FXML
  private void onGuardEyes(MouseEvent event) {
    // execute only if eye scanner puzzle is pressed
    if (GameState.isEyeScannerEntered) {
      guardeyes.setDisable(true);
      // updating items
      styleManager.getItem("compareBtn").setDisable(false);
      styleManager.getItem("geteyesampleLbl").setVisible(false);
      ((EyeScannerController) SceneManager.getController(Scenes.EYESCANNER)).updateGuardEye();
    }
  }

  // Pressing book in drawer
  @FXML
  private void onCredentialsBookPressed(MouseEvent event) {
    credentialsNote.setVisible(true);
    // Set note text to the randomly generated credentials
    passwordLbl.setText("Password: " + randomPassword);
    usernameLbl.setText("Username: " + randomUsername);
    styleManager.removeItemsMessage("credentialsBook");
    // set glow effect
    if (!GameState.isSecurityRoomHoverPressed) {
      styleManager.setClueHover("securityRoomSwitch", true);
    }
    GameState.isSecurityRoomHoverPressed = true;
    // update game state
    GameState.isCredentialsFound = true;
    GameManager.completeObjective();
  }

  // Pressing any location of the keys
  // If key found it turns invisible (we can change mechanics later)
  @FXML
  private void onKeyLocationPressed(MouseEvent event) {
    // execute only when guard is distracted
    if (GameState.isGuardDistracted) {
      Node clickedKeyLocation = (HBox) event.getSource();
      styleManager.setItemsMessage("Already looked here...", clickedKeyLocation.getId().toString());
      // get the clicked key location and execute relevant methods
      if (clickedKeyLocation == RandomnessGenerate.getkeyLocation()) {
        GameState.isKeyLocationFound = true;
        AnimationManager.fadeTransition(key, 2);
        styleManager.setDisable(false, "key");
        styleManager.setDisable(true, "key1", "key3", "key4");
      }
    }
  }

  // Pressing the key
  @FXML
  private void onKeyPressed(MouseEvent event) {
    // updating game state
    GameState.isKeyFound = true;
    GameManager.completeObjective();
    walkieTalkieManager.enableQuickHintBtns();
    key.setVisible(false);
    // setting style
    styleManager.setItemsState(HoverColour.GREEN, "drawerHolder");
    styleManager.setItemsMessage("The key fits...", "drawerHolder");
  }

  @FXML
  private void onGuardPressed(MouseEvent event) {
    GameState.isGuardDistracted = true;
    // setting relevant animation for guard is sleeping
    sleepingAnimation();
    guard.setDisable(true);
    // setting style when guard is pressed
    styleManager.setClueHover("guard", false);
    styleManager.setItemsState(HoverColour.GREEN, "key1", "key3", "key4");
    styleManager.setItemsState(HoverColour.RED, "guardpocket", "guardeyes");
    styleManager.setItemsMessage("Something seems odd here...", "key1", "key3", "key4");
    styleManager.setItemsMessage("Seems dangerous for now", "guardpocket", "guardeyes");
  }

  private void toggleImageViews() {
    // method to use in sleeeping animation (toggles between two images)
    zzz1.setVisible(isZzz1Visible);
    zzz2.setVisible(!isZzz1Visible);
    isZzz1Visible = !isZzz1Visible;
  }

  private void sleepingAnimation() {
    // creating new timeline for sleeping animation
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> toggleImageViews()));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  @FXML
  private void onInvokeHacker(KeyEvent event) throws ApiProxyException {
    // Check if the Enter key is pressed and the Walkie-Talkie is open
    if (event.getCode() == KeyCode.ENTER && walkieTalkieManager.isWalkieTalkieOpen()) {
      // Start the typing animation
      walkieTalkieManager.startAnimation();
      // Create a background task for AI processing
      Task<Void> aiTask3 =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              // Perform AI-related operations here
              // Create a ChatMessage from the user's input
              ChatMessage msg = new ChatMessage("user", lobbyTextInput.getText());
              // Add the user's input to the chat history managed by the hackerAiManager
              hackerAiManager.addChatHistory("User: " + msg.getContent());
              walkieTalkieManager.clearWalkieTalkie();
              // Process the user's input with the hackerAiManager and get a response
              ChatMessage response = hackerAiManager.processInput(msg);
              hackerAiManager.addChatHistory("Cipher: " + response.getContent());
              // Use Platform.runLater to update the UI on the JavaFX Application Thread
              Platform.runLater(
                  () -> {
                    walkieTalkieManager.setWalkieTalkieText(response);
                    lobbyTextInput.clear();
                    walkieTalkieManager.stopAnimation();
                  });
              return null;
            }
          };
      // Create a new thread for the AI task and start it
      Thread aiThread3 = new Thread(aiTask3);
      aiThread3.setDaemon(true);
      aiThread3.start();
    }
  }

  @FXML
  private void onQuickHint(ActionEvent event) {
    // Get a quick hint from the hackerAiManager
    String hint = hackerAiManager.getQuickHint();
    hackerAiManager.storeQuickHint();
    // Set the Walkie-Talkie text to the hint
    walkieTalkieManager.setWalkieTalkieText(new ChatMessage("user", hint));
  }
}
