package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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
import nz.ac.auckland.se206.TimerControl;
import nz.ac.auckland.se206.WalkieTalkieManager;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller class for the Vault scene. */
public class VaultController extends Controller {

  @FXML private Rectangle whiteBackground;
  @FXML private Rectangle laserCuttingScene;
  @FXML private ImageView vaultDoor;
  @FXML private ImageView realvaultbackground;
  @FXML private ImageView bomblogo;
  @FXML private TextArea vaultTextArea;
  @FXML private TextField vaultTextField;
  @FXML private Label timerLabel;
  @FXML private ImageView vaultNotification;

  @FXML private Rectangle dialogueBox;
  @FXML private Label moneyValue;
  @FXML private Text lootLbl;
  @FXML private Label difficultyValue;
  @FXML private HBox exitHolder;

  @FXML private HBox exitDoor;
  @FXML private HBox goldDoorHolder;
  @FXML private HBox silverDoorHolder;
  @FXML private HBox bronzeDoorHolder;
  @FXML private VBox vaultwalkietalkie;
  @FXML private VBox walkietalkieText;
  @FXML private HBox bombHolder;
  @FXML private HBox bomblayer;
  @FXML private VBox bombPuzzle;
  @FXML private VBox lobbyRoomSwitch;
  @FXML private VBox securityRoomSwitch;
  @FXML private VBox vaultRoomSwitch;
  @FXML private Button button;
  @FXML private Button checkBtn;
  @FXML private Button quickHintBtn;
  @FXML private VBox lootBtnHolder;
  @FXML private Text inputLbl;
  @FXML private Label statusLbl;
  @FXML private HBox escapeDoor;
  @FXML private Pane slidePane;
  @FXML private Button lootBtn;
  @FXML private Button redBtn;
  @FXML private StackPane timerClock;
  @FXML private StackPane bombpuzzlestackpane;
  @FXML private Label numberOfHints;
  @FXML private Label moneyCount;

  @FXML private HBox switchHolder;
  @FXML private HBox walkietalkieHolder;
  @FXML private ImageView vaultWalkieTalkie;
  @FXML private ImageView escapeDoorImage;

  @FXML private ImageView silverDoorImage;
  @FXML private ImageView goldDoorImage;
  @FXML private ImageView bronzeDoorImage;

  @FXML private ImageView silverMoneyImage;
  @FXML private ImageView goldMoneyImage;
  @FXML private ImageView bronzeMoneyImage;

  // initialising instances
  private StringBuilder labelText = new StringBuilder();
  private WalkieTalkieManager walkieTalkieManager = WalkieTalkieManager.getInstance();
  private HackerAiManager hackerAiManager = HackerAiManager.getInstance();

  /** Initialize the Vault controller. Sets up the initial state of the scene. */
  public void initialize() {
    // setting up walkietalkie and controller
    SceneManager.setController(Scenes.VAULT, this);
    WalkieTalkieManager.addWalkieTalkieImage(this, vaultWalkieTalkie);
    WalkieTalkieManager.addWalkieTalkieHint(this, numberOfHints);
    WalkieTalkieManager.addQuickHintBtn(this, quickHintBtn);
    WalkieTalkieManager.addWalkieTalkieTextArea(this, vaultTextArea);
    WalkieTalkieManager.addWalkieTalkieNotification(this, vaultNotification);

    super.setTimerLabel(timerLabel, 1);

    StyleManager.addHoverItems(
        bronzeDoorHolder,
        silverDoorHolder,
        goldDoorHolder,
        lootBtnHolder,
        lootLbl,
        realvaultbackground,
        bombHolder,
        switchHolder,
        vaultwalkietalkie,
        walkietalkieText,
        bomblogo,
        exitDoor,
        exitHolder,
        escapeDoorImage,
        silverDoorImage,
        goldDoorImage,
        bronzeDoorImage,
        escapeDoor,
        silverMoneyImage,
        goldMoneyImage,
        bronzeMoneyImage);

    // adding relevant items to the stylemanager list
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
    GameManager.addMoneyGainedLabel(this, moneyCount);

    // setting style for the items
    StyleManager.setItemsMessage("It's locked tight", "exitDoor");
    StyleManager.setItemsMessage("set bomb down", "exitHolder");
    StyleManager.setItemsMessage("A way out!", "escapeDoor");
    StyleManager.setItemsMessage("activate bomb", "bombHolder");
    StyleManager.setItemsMessage(
        "Need to disable firewall from blocking us",
        "bronzeDoorHolder",
        "silverDoorHolder",
        "goldDoorHolder");
  }

  /** Shows the money collected when loot is opened. */
  public void showMoneyCollected() {
    setInfoText("Money: " + GameManager.getMoneyToGain(), null);
  }

  /**
   * Handles the mouse click event on the Walkie-Talkie icon. Opens and closes the Walkie-Talkie.
   *
   * @param event The mouse click event.
   */
  @FXML
  private void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  /**
   * Handles the mouse click event when the bomb is pressed. Sets the current objective to GAME_OVER
   * and shows the bomb puzzle.
   *
   * @param event The mouse click event.
   */
  @FXML
  private void onBombPressed(MouseEvent event) {
    // set current onjective
    GameManager.setCurrentObjective(Objectives.GAME_OVER);
    // setting style for relevant items
    bombPuzzle.setVisible(true);
    AnimationManager.fadeTransition(bombpuzzlestackpane, 1, 0.0, 1.0);
    bombPuzzle.requestFocus();
    exitDoor.setDisable(true);
    lootBtnHolder.setVisible(true);
    lootBtn.setVisible(false);
    lootLbl.setText("Code: " + RandomnessGenerate.getPasscode());
  }

  /**
   * Handles the event when exiting the bomb puzzle. Sets the current objective to GAME_OVER and
   * toggles animations for relevant items.
   */
  @FXML
  private void onExitBomb() {
    // set current objective
    GameManager.setCurrentObjective(Objectives.GAME_OVER);
    // toggling animations for relevant items
    AnimationManager.toggleAlarmAnimation(exitHolder, true, 0.5);
    AnimationManager.delayAnimation(exitHolder, escapeDoor);
    // setting styles for relevant items
    exitHolder.setVisible(true);
    bombPuzzle.setVisible(false);
    lootBtnHolder.setVisible(false);
    exitHolder.setDisable(true);
    bombHolder.setVisible(false);
    vaultwalkietalkie.setVisible(false);
  }

  /**
   * Handles the event when switching to the HackerVan scene. Loads relevant information for the
   * HackerVan scene.
   */
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

  /**
   * Handles the event when switching to the Eye Scanner scene. Executes when the firewall is
   * disabled.
   */
  @FXML
  private void onSwitchToEyeScanner() {
    // when firewall is disabled execute
    if (GameState.isFirewallDisabled && GameState.isSecondRiddleSolved) {
      GameManager.setCurrentObjective(Objectives.EYE_SCANNER);
      App.setUi(Scenes.EYESCANNER);
      GameState.isEyeScannerEntered = true;
      // setting style
      StyleManager.setItemsMessage("Collect guard eye sample", "guardeyes");
      StyleManager.setItemsHoverColour(HoverColour.GREEN, "guardeyes");
      StyleManager.setClueHover("guardeyes", true);
    }
  }

  /**
   * Handles the event when switching to the Chemical Mixing scene. Executes when the firewall is
   * disabled.
   */
  @FXML
  private void onSwitchToChemicalMixing() {
    // when firewall is disabled execute
    if (GameState.isFirewallDisabled) {
      GameManager.setCurrentObjective(Objectives.CHEMICAL_MIXING);
      App.setUi(Scenes.CHEMICALMIXING);
    }
  }

  /**
   * Handles the event when loot is collected. Executes when the firewall is disabled and any vault
   * is opened.
   *
   * @param event The action event.
   */
  @FXML
  private void onLootCollected(ActionEvent event) {
    // execute when firewall is disabled and any vault is opened
    if (GameState.isFirewallDisabled && GameState.isAnyDoorOpen) {
      // handle alarm if not tripped already
      if (!GameState.isAlarmTripped) {
        StyleManager.setAlarm(true);
        GameState.isAlarmTripped = true;
      }
      StyleManager.setVisible(false, "goldMoneyImage", "silverMoneyImage", "bronzeMoneyImage");
      GameManager.setCurrentObjective(Objectives.DISABLE_LASERTRAP);
      lootBtnHolder.setVisible(false);
      GameManager.collectMoney();
    }
  }

  /**
   * Handles the event when entering the Laser Cutting scene. Executes when the firewall is
   * disabled.
   */
  @FXML
  private void onLaserCuttingScene() {
    // execute when firewall is disabled
    if (GameState.isFirewallDisabled) {
      GameManager.setCurrentObjective(Objectives.LAZER_CUTTING);
      App.setUi(Scenes.LASERCUTTING);
    }
  }

  /**
   * Handles the button click events and updates the bomb code input label.
   *
   * @param event The action event.
   */
  @FXML
  private void onButtonClick(ActionEvent event) {
    Button button = (Button) event.getSource();
    String buttonText = button.getText();
    updateCode(buttonText);
    bombPuzzle.requestFocus();
  }

  /**
   * Updates the bomb code input label with the text of the clicked button.
   *
   * @param text The text of the clicked button.
   */
  private void updateCode(String text) {
    labelText.append(text);
    inputLbl.setText(labelText.toString());
  }

  /** Handles the event when the "Check" button is pressed to check the bomb code. */
  @FXML
  private void onCheckCode() {
    // check bomb code
    String code = lootLbl.getText().substring("Code: ".length());

    if (inputLbl.getText() == null) {
      return;
    }

    // handle correct input
    if (inputLbl.getText().equals(code)) {
      statusLbl.setText("Success, Activate Bomb");
      inputLbl.setText("");
      redBtn.setDisable(false);
      checkBtn.setDisable(true);
      inputLbl.setVisible(false);

      // handle incorrect input
    } else {
      statusLbl.setText("Wrong Try Again");
      inputLbl.setText(null);
    }
    labelText.setLength(0);
  }

  /** Handles the event when escaping from the Vault scene. */
  @FXML
  private void onEscape() {
    TimerControl.cancelTimer();
    App.setUi(Scenes.GAMEFINISH);
    ((GameFinishController) SceneManager.getController(Scenes.GAMEFINISH)).setGameWonPage();
  }

  /**
   * Shows information about the vault (money and difficulty) when the mouse hovers over a vault
   * door.
   *
   * @param event The mouse event.
   */
  @FXML
  private void showInfo(MouseEvent event) {
    String door = event.getSource().toString();

    // execute when firewall is disabled
    if (GameState.isFirewallDisabled) {

      String moneyText = "Money: $";
      String difficultyText = "Difficulty: ";

      // setting style for relevant vaults
      if (door.contains("goldDoorImage") && !GameState.isFirstRiddleSolved) {
        setInfoText(moneyText + "20,000,000", difficultyText + "★★★★★");
      } else if (door.contains("silverDoorImage")) {
        setInfoText(moneyText + "10,000,000", difficultyText + "★★★☆☆");
      } else if (door.contains("bronzeDoorImage")) {
        setInfoText(moneyText + "5,000,000", difficultyText + "★☆☆☆☆");
      }
    } else {
      setInfoText("Money: ???????", "Difficulty: ???????");
    }
  }

  /**
   * Clears the information display when the mouse is no longer hovering over a vault door.
   *
   * @param event The mouse event.
   */
  @FXML
  private void clearInfo(MouseEvent event) {
    // clearing info by setting visibility to false
    dialogueBox.setVisible(false);
    moneyValue.setText(null);
    difficultyValue.setText(null);
    // remove style
    setDoorStyle(goldDoorImage, "");
    setDoorStyle(bronzeDoorImage, "");
    setDoorStyle(silverDoorImage, "");
  }

  /**
   * Sets the style of a vault door with the given style.
   *
   * @param door The ImageView of the vault door.
   * @param style The style to be set.
   */
  private void setDoorStyle(ImageView door, String style) {
    if (door != null) {
      door.setStyle(style);
    }
  }

  /**
   * Sets the information text for the vault doors (money and difficulty).
   *
   * @param moneyText The money information.
   * @param difficultyText The difficulty information.
   */
  private void setInfoText(String moneyText, String difficultyText) {
    // setting visibility to true to set info
    dialogueBox.setVisible(true);
    moneyValue.setVisible(true);
    difficultyValue.setVisible(true);
    moneyValue.setText(moneyText);
    difficultyValue.setText(difficultyText);
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
      ChatMessage msg = new ChatMessage("user", vaultTextField.getText());
      vaultTextField.clear();
      vaultTextField.setDisable(true);
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
                    vaultTextField.setDisable(false);
                    walkieTalkieManager.stopAnimation();
                    vaultTextField.requestFocus();
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
   * Handles the event when the Quick Hint button is pressed to request a quick hint from the AI.
   * Sets the Walkie-Talkie text to the hint.
   *
   * @param event The action event.
   */
  @FXML
  private void onQuickHint(ActionEvent event) {
    // Get a quick hint from the hackerAiManager
    String hint = hackerAiManager.getQuickHint();
    hackerAiManager.storeQuickHint();
    // Set the Walkie-Talkie text to the hint
    walkieTalkieManager.setWalkieTalkieText(new ChatMessage("user", hint));
  }

  /**
   * Handles key events when typing the bomb code.
   *
   * @param event The KeyEvent.
   */
  @FXML
  private void onBombTyped(KeyEvent event) {
    KeyCode code = event.getCode();

    // If esc is pressed, close the bomb
    if (code == KeyCode.ESCAPE) {
      if (!redBtn.isDisable()) {
        onExitBomb();
      }
      return;
    }

    // If enter is pressed, check the code
    if (code == KeyCode.ENTER) {
      onCheckCode();
      return;
    }

    // If backspace is pressed, remove the last character from the input label
    if (code == KeyCode.BACK_SPACE) {
      if (labelText.length() > 0) {
        labelText.deleteCharAt(labelText.length() - 1);
        inputLbl.setText(labelText.toString());
      }
      return;
    }

    // If number is pressed, add it to the input label
    if (code.isDigitKey() && !event.getCode().equals(KeyCode.ENTER)) {
      updateCode(event.getText());
    }
  }
}
