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

public class VaultController extends Controller {

  @FXML private Rectangle whiteBackground;
  @FXML private Rectangle laserCuttingScene;
  @FXML private ImageView vaultDoor;
  @FXML private ImageView goldDoor;
  @FXML private ImageView silverDoor;
  @FXML private ImageView bronzeDoor;
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

  // initialising instances
  private StyleManager styleManager = StyleManager.getInstance();
  private StringBuilder labelText = new StringBuilder();
  private WalkieTalkieManager walkieTalkieManager = WalkieTalkieManager.getInstance();
  private HackerAiManager hackerAiManager = HackerAiManager.getInstance();

  public void initialize() {
    // setting up walkietalkie and controller
    SceneManager.setController(Scenes.VAULT, this);
    WalkieTalkieManager.addWalkieTalkieImage(this, vaultWalkieTalkie);
    WalkieTalkieManager.addWalkieTalkieHint(this, numberOfHints);
    WalkieTalkieManager.addQuickHintBtn(this, quickHintBtn);
    WalkieTalkieManager.addWalkieTalkieTextArea(this, vaultTextArea);
    WalkieTalkieManager.addWalkieTalkieNotification(this, vaultNotification);

    super.setTimerLabel(timerLabel, 1);

    styleManager.addHoverItems(
        bronzeDoorHolder,
        silverDoorHolder,
        goldDoorHolder,
        bronzeDoor,
        silverDoor,
        goldDoor,
        lootBtnHolder,
        lootLbl,
        realvaultbackground,
        bombHolder,
        switchHolder,
        vaultwalkietalkie,
        walkietalkieText,
        bomblogo,
        exitDoor,
        exitHolder);

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

  public void showMoneyCollected() {
    setInfoText("Money: " + GameManager.getMoneyToGain(), null);
  }

  //   handling mouse events on walkie talkie
  //   open and closes when walkie talkie is clicked
  @FXML
  private void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  @FXML
  private void onBombPressed(MouseEvent event) {
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

  // @FXML
  // private void onBombPlaced(MouseEvent event) {
  //     AnimationManager.toggleAlarmAnimation(exitHolder, true, 0.5);
  //     AnimationManager.delayAnimation(exitHolder, escapeDoor);
  //     exitHolder.setDisable(true);
  // }

  @FXML
  private void onExitBomb() {
    GameManager.setCurrentObjective(Objectives.GAME_OVER);
    exitHolder.setVisible(true);
    bombPuzzle.setVisible(false);
    AnimationManager.toggleAlarmAnimation(exitHolder, true, 0.5);
    AnimationManager.delayAnimation(exitHolder, escapeDoor);
    lootBtnHolder.setVisible(false);
    exitHolder.setDisable(true);
    bombHolder.setVisible(false);
    vaultwalkietalkie.setVisible(false);
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

  @FXML
  private void onSwitchToEyeScanner() {
    // when firewall is disabled execute
    if (GameState.isFirewallDisabled && GameState.isSecondRiddleSolved) {
      GameManager.setCurrentObjective(Objectives.EYE_SCANNER);
      App.setUI(Scenes.EYESCANNER);
      GameState.isEyeScannerEntered = true;
      // setting style
      StyleManager.setItemsMessage("Collect guard eye sample", "guardeyes");
      StyleManager.setItemsHoverColour(HoverColour.GREEN, "guardeyes");
      StyleManager.setClueHover("guardeyes", true);
    }
  }

  @FXML
  private void onSwitchToChemicalMixing() {
    // when firewall is disabled execute
    if (GameState.isFirewallDisabled) {
      GameManager.setCurrentObjective(Objectives.CHEMICAL_MIXING);
      App.setUI(Scenes.CHEMICALMIXING);
    }
  }

  @FXML
  private void onLootCollected(ActionEvent event) {
    // execute when firewall is disabled and any vault is opened

    if (GameState.isFirewallDisabled && GameState.isAnyDoorOpen) {

      if (!GameState.isAlarmTripped) {
        StyleManager.setAlarm(true);
        GameState.isAlarmTripped = true;
        WalkieTalkieManager.setWalkieTalkieOpen();
        walkieTalkieManager.setWalkieTalkieText(
            new ChatMessage("user", "Uh Oh! You better find a way to turn that off quick"));
      }
      GameManager.setCurrentObjective(Objectives.DISABLE_LASERTRAP);
      lootBtnHolder.setVisible(false);
      GameManager.collectMoney();
    }
  }

  @FXML
  private void onLaserCuttingScene() {
    // execute when firewall is disabled
    if (GameState.isFirewallDisabled) {
      GameManager.setCurrentObjective(Objectives.LAZER_CUTTING);
      App.setUI(Scenes.LASERCUTTING);
    }
  }

  @FXML
  private void onButtonClick(ActionEvent event) {
    Button button = (Button) event.getSource();
    String buttonText = button.getText();
    updateCode(buttonText);
    bombPuzzle.requestFocus();
  }

  private void updateCode(String text) {
    labelText.append(text);
    inputLbl.setText(labelText.toString());
  }

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

  @FXML
  private void onEscape() {
    TimerControl.cancelTimer();
    App.setUI(Scenes.GAMEFINISH);
    ((GameFinishController) SceneManager.getController(Scenes.GAMEFINISH)).setGameWonPage();
  }

  @FXML
  private void showInfo(MouseEvent event) {
    String door = event.getSource().toString();

    // execute when firewall is disabled
    if (GameState.isFirewallDisabled) {

      String moneyText = "Money: $";
      String difficultyText = "Difficulty: ";

      // setting style for relevant vaults
      if (door.contains("goldDoor") && !GameState.isFirstRiddleSolved) {
        setInfoText(moneyText + "20,000,000", difficultyText + "★★★★★");
      } else if (door.contains("silverDoor")) {
        setInfoText(moneyText + "10,000,000", difficultyText + "★★★☆☆");
      } else if (door.contains("bronzeDoor")) {
        setInfoText(moneyText + "5,000,000", difficultyText + "★☆☆☆☆");
      }
    } else {
      setInfoText("Money: ???????", "Difficulty: ???????");
    }
  }

  @FXML
  private void clearInfo(MouseEvent event) {
    // clearing info by setting visibility to false
    dialogueBox.setVisible(false);
    moneyValue.setText(null);
    difficultyValue.setText(null);
    // remove style
    setDoorStyle(goldDoor, "");
    setDoorStyle(bronzeDoor, "");
    setDoorStyle(silverDoor, "");
  }

  private void setDoorStyle(ImageView door, String style) {
    if (door != null) {
      door.setStyle(style);
    }
  }

  private void setInfoText(String moneyText, String difficultyText) {
    // setting visibility to true to set info
    dialogueBox.setVisible(true);
    moneyValue.setVisible(true);
    difficultyValue.setVisible(true);
    moneyValue.setText(moneyText);
    difficultyValue.setText(difficultyText);
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
              ChatMessage msg = new ChatMessage("user", vaultTextField.getText());

              hackerAiManager.addChatHistory("User: " + msg.getContent());
              walkieTalkieManager.clearWalkieTalkie();
              // Process the user's input with the hackerAiManager and get a response
              ChatMessage response = hackerAiManager.processInput(msg);
              hackerAiManager.addChatHistory("Cipher: " + response.getContent());
              // Use Platform.runLater to update the UI on the JavaFX Application Thread
              Platform.runLater(
                  () -> {
                    walkieTalkieManager.setWalkieTalkieText(response);
                    vaultTextField.clear();
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
