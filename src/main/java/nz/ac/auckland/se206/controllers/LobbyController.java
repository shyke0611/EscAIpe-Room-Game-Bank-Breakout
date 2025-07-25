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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nz.ac.auckland.se206.AnimationManager;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.GameManager.Objectives;
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

/** Controller class for the Lobby scene. */
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
  @FXML private Label moneyCount;
  @FXML private ImageView lobbyNotification;

  @FXML private HBox key1;
  @FXML private HBox key3;
  @FXML private HBox key4;
  @FXML private HBox guardpocket;
  @FXML private HBox key;
  @FXML private HBox guard;
  @FXML private ImageView zzz1;
  @FXML private ImageView zzz2;
  @FXML private ImageView drawerImage;
  @FXML private ImageView openDrawerImage;
  @FXML private ImageView largePlaque;
  @FXML private ImageView enlargedPainting;
  @FXML private ImageView ceoPainting;
  @FXML private HBox credentialsBook;
  @FXML private StackPane plaqueandlabel;

  @FXML private Label numberOfHints;
  @FXML private Label titleLbl;
  @FXML private Label passwordLbl;
  @FXML private Label usernameLbl;
  @FXML private Label orderLabel;
  @FXML private Label johnIpsumLbl;
  @FXML private Label foundingDate;

  @FXML private ImageView key1Image;
  @FXML private ImageView key3Image;
  @FXML private ImageView key4Image;
  @FXML private ImageView guardImage;
  @FXML private ImageView lobbypaintingImage;

  // Other fields
  private String randomUsername;
  private String randomPassword;
  private WalkieTalkieManager walkieTalkieManager = WalkieTalkieManager.getInstance();
  private HackerAiManager hackerAiManager = HackerAiManager.getInstance();
  private boolean isZzz1Visible = false;

  /** Initialize the Lobby Controller. Sets up the initial state of the scene. */
  public void initialize() {
    // set method on initialisation
    SceneManager.setController(Scenes.LOBBY, this);
    // setting walkie talkie components
    WalkieTalkieManager.addWalkieTalkieImage(this, lobbyWalkieTalkie);
    WalkieTalkieManager.addWalkieTalkieHint(this, numberOfHints);
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
    WalkieTalkieManager.addQuickHintBtn(this, quickHintBtn);
    WalkieTalkieManager.addWalkieTalkieTextArea(this, lobbyTextArea);
    WalkieTalkieManager.addWalkieTalkieNotification(this, lobbyNotification);
    GameManager.addMoneyGainedLabel(this, moneyCount);
    super.setTimerLabel(timerLabel, 1);
    // set random components
    RandomnessGenerate.generateRandomCredentials();
    randomUsername = RandomnessGenerate.getUsername();
    randomPassword = RandomnessGenerate.getPasscode();
    RandomnessGenerate.addKeyLocation(key1, key3, key4);
    RandomnessGenerate.generateRandomKeyLocation();
    StyleManager.addHoverItems(
        key1,
        key3,
        key4,
        guard,
        guardpocket,
        guardeyes,
        drawerHolder,
        credentialsBook,
        credentialsNote,
        lobbypaintingImage,
        lobbybackground,
        ceoPainting,
        key1Image,
        key3Image,
        key4Image,
        guardImage,
        openDrawerImage,
        drawerImage);

    // setting style to items
    StyleManager.setItemsMessage(
        "Guard is watching...", "key1", "key3", "key4", "guardpocket", "guardeyes");
    StyleManager.setItemsMessage("It's locked...", "drawerHolder");
    StyleManager.setItemsMessage("A note?", "credentialsBook");
    StyleManager.setItemsMessage("Put him to sleep", "guard");
    StyleManager.setItemsMessage("CEO of the bank...?", "ceoPainting");
    StyleManager.setClueHover("guard", true);
    setUpListener(key);
    // more random components
    String plaqueName = RandomnessGenerate.getRandomCeoName();
    System.out.println(plaqueName);
    johnIpsumLbl.setText(plaqueName + " Ipsum");

    String date = RandomnessGenerate.getRandomFoundingDate();
    System.out.println(date);
    foundingDate.setText("SINCE " + date);

    WalkieTalkieManager.setWalkieTalkieNotifcationOn();
    walkieTalkieManager.setWalkieTalkieText(
        new ChatMessage(
            "user",
            "Nice work, you made it inside! First things first, we need to find the login"
                + " credentials hidden in the lobby."));
  }

  /**
   * Handles the event when the Walkie-Talkie is clicked, toggling its open/closed state.
   *
   * @param event The MouseEvent triggered by clicking the Walkie-Talkie.
   */
  @FXML
  private void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  /** Switches to the Hacker scene when the corresponding button is clicked. */
  @FXML
  private void onSwitchToHacker() {
    // setting relevant method for hacker scene
    HackerVanController vanController =
        (HackerVanController) SceneManager.getController(Scenes.HACKERVAN);
    // loading relevant information
    vanController.printChatHistory();
    vanController.loadQuickHints();
    App.setUi(Scenes.HACKERVAN);
  }

  /** Closes the credential notes by setting visibility. */
  @FXML
  private void onCloseNote() {
    credentialsNote.setVisible(false);
  }

  /**
   * Opens the drawer to get credential notes, but only when the key is found.
   *
   * @param event The MouseEvent triggered by clicking the drawer.
   */
  @FXML
  private void onDrawerPressed(MouseEvent event) {
    // Opens only when key is found to the drawer
    if (GameState.isKeyFound) {
      // setting visibility
      drawerImage.setVisible(false);
      openDrawerImage.setVisible(true);
      credentialsBook.setVisible(true);
      drawerHolder.setDisable(true);
    }
  }

  /**
   * Handles the event when the Guard's pocket is pressed, revealing the wire cutting order and
   * credentials note.
   *
   * @param event The MouseEvent triggered by clicking the Guard's pocket.
   */
  @FXML
  private void onGuardPocket(MouseEvent event) {
    // executes when alarm is tripped
    if (GameState.isAlarmTripped) {
      StyleManager.removeItemsMessage("guardpocket");
      StyleManager.setClueHover("guardpocket", false);
      guardpocket.setVisible(false);
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

  /**
   * Handles the event when the Guard's eyes are pressed during the eye scanner puzzle.
   *
   * @param event The MouseEvent triggered by clicking the Guard's eyes.
   */
  @FXML
  private void onGuardEyes(MouseEvent event) {
    // execute only if eye scanner puzzle is pressed
    if (GameState.isEyeScannerEntered) {
      StyleManager.setClueHover("guardeyes", false);
      guardeyes.setVisible(false);
      guardeyes.setDisable(true);
      // updating items
      StyleManager.getHoverItem("compareBtn").setDisable(false);
      // styleManager.getItem("geteyesampleLbl").setVisible(false);
      ((EyeScannerController) SceneManager.getController(Scenes.EYESCANNER)).updateGuardEye();
    }
  }

  /**
   * Handles the event when the book in the drawer is pressed, displaying the credentials note.
   *
   * @param event The MouseEvent triggered by clicking the book in the drawer.
   */
  @FXML
  private void onCredentialsBookPressed(MouseEvent event) {
    credentialsNote.setVisible(true);
    // Set note text to the randomly generated credentials
    passwordLbl.setText("Password: " + randomPassword);
    usernameLbl.setText("Username: " + randomUsername);
    StyleManager.removeItemsMessage("credentialsBook");
    WalkieTalkieManager.setWalkieTalkieNotifcationOn();
    walkieTalkieManager.setWalkieTalkieText(
        new ChatMessage(
            "user",
            "Great you found the log in credentials! Now log in and try to convince the AI you are"
                + " an employee"));

    GameState.isCredentialsFound = true;
    GameManager.setCurrentObjective(Objectives.DISABLE_FIREWALL);
    StyleManager.setClueHover("computer", true);
    StyleManager.setItemsHoverColour(HoverColour.GREEN, "computer");
  }

  /**
   * Handles the event when any key location is pressed, making the key visible if it's found.
   *
   * @param event The MouseEvent triggered by clicking a key location.
   */
  @FXML
  private void onKeyLocationPressed(MouseEvent event) {
    // execute only when guard is distracted
    if (GameState.isGuardDistracted) {
      Node clickedKeyLocation = (HBox) event.getSource();
      StyleManager.setItemsMessage("Already looked here...", clickedKeyLocation.getId().toString());
      // get the clicked key location and execute relevant methods
      if (clickedKeyLocation == RandomnessGenerate.getkeyLocation()) {
        // update game and item states
        GameState.isKeyLocationFound = true;
        AnimationManager.fadeTransition(key, 2, 0.0, 1.0);
        StyleManager.setDisable(true, "key1", "key3", "key4");
        key.setDisable(false);
      }
    }
  }

  /**
   * Handles the event when the key is pressed, marking the key as found and enabling quick hint
   * buttons.
   *
   * @param event The MouseEvent triggered by clicking the key.
   */
  @FXML
  private void onKeyPressed(MouseEvent event) {
    // updating game state
    GameState.isKeyFound = true;
    GameManager.setCurrentObjective(Objectives.FIND_PASSCODE);
    walkieTalkieManager.enableQuickHintBtns();
    key.setVisible(false);
    // setting style
    StyleManager.setItemsHoverColour(HoverColour.GREEN, "drawerHolder");
    StyleManager.setItemsMessage("The key fits...", "drawerHolder");
  }

  /**
   * Handles the event when the Guard is pressed, making him distracted and initiating the sleeping
   * animation.
   *
   * @param event The MouseEvent triggered by clicking the Guard.
   */
  @FXML
  private void onGuardPressed(MouseEvent event) {

    GameState.isGuardDistracted = true;
    // setting relevant animation for guard is sleeping
    sleepingAnimation();
    guard.setDisable(true);
    // setting style when guard is pressed
    StyleManager.setClueHover("guard", false);
    StyleManager.setItemsHoverColour(HoverColour.GREEN, "key1", "key3", "key4");
    StyleManager.setItemsHoverColour(HoverColour.ORANGE, "guardpocket", "guardeyes");
    StyleManager.setItemsMessage("Something seems odd here...", "key1", "key3", "key4");
    StyleManager.setItemsMessage("Seems dangerous for now", "guardpocket", "guardeyes");
  }

  /** Toggles between two sleeping animation images for the Guard. */
  private void toggleImageViews() {
    // method to use in sleeeping animation (toggles between two images)
    zzz1.setVisible(isZzz1Visible);
    zzz2.setVisible(!isZzz1Visible);
    isZzz1Visible = !isZzz1Visible;
  }

  /** Initiates a sleeping animation for the Guard, simulating him sleeping. */
  private void sleepingAnimation() {
    // creating new timeline for sleeping animation
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> toggleImageViews()));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  /**
   * Handles the Enter key event for invoking the hacker AI when the Walkie-Talkie is open.
   *
   * @param event The KeyEvent triggered by pressing the Enter key.
   * @throws ApiProxyException If there's an issue with the AI proxy.
   */
  @FXML
  private void onInvokeHacker(KeyEvent event) throws ApiProxyException {
    // Check if the Enter key is pressed and the Walkie-Talkie is open
    if (event.getCode() == KeyCode.ENTER && walkieTalkieManager.isWalkieTalkieOpen()) {
      // Start the typing animation
      walkieTalkieManager.startAnimation();
      ChatMessage msg = new ChatMessage("user", lobbyTextInput.getText());
      lobbyTextInput.clear();
      lobbyTextInput.setDisable(true);
      // Create a background task for AI processing
      Task<Void> aiTask3 =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              // Perform AI-related operations here
              // Create a ChatMessage from the user's input

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
                    lobbyTextInput.setDisable(false);
                    walkieTalkieManager.stopAnimation();
                    lobbyTextInput.requestFocus();
                    if (!walkieTalkieManager.isWalkieTalkieOpen()) {
                      WalkieTalkieManager.setWalkieTalkieNotifcationOn();
                    }
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

  /**
   * Retrieves and displays a quick hint from the hacker AI manager.
   *
   * @param event The ActionEvent triggered by clicking the quick hint button.
   */
  @FXML
  private void onQuickHint(ActionEvent event) {
    // Get a quick hint from the hackerAiManager
    String hint = hackerAiManager.getQuickHint();
    hackerAiManager.storeQuickHint();
    // Set the Walkie-Talkie text to the hint
    walkieTalkieManager.setWalkieTalkieText(new ChatMessage("user", hint));
  }

  /** Enlarges the painting and displays additional information when the painting is clicked. */
  @FXML
  private void enlargePainting() {
    enlargedPainting.setVisible(true);
    plaqueandlabel.setVisible(true);
    largePlaque.setVisible(true);
    johnIpsumLbl.setVisible(true);
  }

  /** Hides the enlarged painting and additional information when the painting is clicked again. */
  @FXML
  private void hidePainting() {
    enlargedPainting.setVisible(false);
    plaqueandlabel.setVisible(false);
    largePlaque.setVisible(false);
    johnIpsumLbl.setVisible(false);
  }
}
