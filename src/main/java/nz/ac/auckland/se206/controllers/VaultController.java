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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
  @FXML private ImageView vaultbackground;
  @FXML private ImageView realvaultbackground;
  @FXML private ImageView bomblogo;
  @FXML private TextArea vaultTextArea;
  @FXML private TextField vaultTextField;
  @FXML private Label timerLabel;

  @FXML private Rectangle dialogueBox;
  @FXML private Label moneyValue;
  @FXML private Label lootLbl;
  @FXML private Label difficultyValue;
  @FXML private HBox exitHolder;

  @FXML private HBox doorHolder;
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
  @FXML private VBox lootBtnHolder;
  @FXML private Label inputLbl;
  @FXML private Label statusLbl;
  @FXML private Label givencode;
  @FXML private HBox escapeDoor;
  @FXML private Pane slidePane;
  @FXML private Button lootBtn;
  @FXML private StackPane timerClock;

  @FXML private HBox switchHolder;
  @FXML private HBox walkietalkieHolder;
  @FXML private ImageView vaultWalkieTalkie;

  @FXML private Rectangle AIAccess;

  private StyleManager styleManager = StyleManager.getInstance();
  private StringBuilder labelText = new StringBuilder();
  private WalkieTalkieManager walkieTalkieManager = WalkieTalkieManager.getInstance();
  private HackerAiManager hackerAiManager = HackerAiManager.getInstance();

  public void initialize() {
    SceneManager.setController(Scenes.VAULT, this);
    WalkieTalkieManager.addWalkieTalkieImage(this, vaultWalkieTalkie);
    super.setTimerLabel(timerLabel, 1);

    styleManager.addItems(
        goldDoor,
        silverDoor,
        bronzeDoor,
        vaultbackground,
        doorHolder,
        exitHolder,
        bombHolder,
        bombPuzzle,
        vaultwalkietalkie,
        walkietalkieHolder,
        switchHolder,
        escapeDoor,
        lootBtnHolder,
        lootLbl,
        bronzeDoorHolder,
        silverDoorHolder,
        goldDoorHolder,
        lobbyRoomSwitch,
        bomblayer,
        realvaultbackground,
        vaultRoomSwitch,
        securityRoomSwitch);
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
    givencode.setText("Code: " + RandomnessGenerate.getPasscode());

    styleManager.setItemsMessage("set bomb down", "exitHolder");
    styleManager.setItemsMessage("escape", "escapeDoor");
    styleManager.setItemsMessage("activate bomb", "bombHolder");
    styleManager.setItemsMessage(
        "Need to disable firewall from blocking us",
        "bronzeDoorHolder",
        "silverDoorHolder",
        "goldDoorHolder");
  }

  //   handling mouse events on walkie talkie
  //   open and closes when walkie talkie is clicked
  @FXML
  public void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  @FXML
 public void onBombPressed(MouseEvent event) {
    AnimationManager.slideDoorsAnimation(doorHolder);
    AnimationManager.slideDoorsAnimation(vaultbackground);
    AnimationManager.slideDoorsAnimation(slidePane);
    // timerClock.setTranslateX(350);
    bomblogo.setVisible(false);
    bombHolder.setDisable(true);
    styleManager.removeItemsMessage("bombHolder");
    styleManager.setClueHover("bomblayer", false);
    styleManager.setVisible(false, "switchHolder");
  }

  @FXML
  public void onBombPlaced(MouseEvent event) {
    if (!GameState.isBombActivated) {
      bombPuzzle.setVisible(true);
    }
  }

  @FXML
  public void onSwitchToHacker() {
    SceneManager.setPreviousScene(Scenes.HACKERVAN, Scenes.VAULT);
    HackerVanController vanController =
        (HackerVanController) SceneManager.getController(Scenes.HACKERVAN);
    vanController.printChatHistory();
    vanController.loadQuickHints();
    App.setUI(Scenes.HACKERVAN);
  }

  @FXML
  public void onSwitchToEyeScanner() {
    if (GameState.isFirewallDisabled /* && GameState.isSecondRiddleSolved*/) {
      App.setUI(Scenes.EYESCANNER);
      GameState.isEyeScannerEntered = true;
      styleManager.setItemsMessage("Get guard eye colour", "guardeyes");
      styleManager.setItemsState(HoverColour.GREEN, "guardeyes");
    }
  }

  @FXML
  public void onSwitchToChemicalMixing() {
    if (GameState.isFirewallDisabled /* && GameState.isThirdRiddleSolved*/) {
      App.setUI(Scenes.CHEMICALMIXING);
    }
  }

  @FXML
  public void onLootCollected(ActionEvent event) {
    if (GameState.isFirewallDisabled && GameState.isAnyDoorOpen) {
      App.textToSpeech("Alarm Triggered, Go and Disable it");
      StyleManager.setAlarm(true);
      GameState.isAlarmTripped = true;
      styleManager.setItemsState(HoverColour.GREEN, "electricityBox");
      styleManager.setItemsState(HoverColour.GREEN, "guardpocket");
      styleManager.setItemsMessage("Something seems odd?", "guardpocket");
      styleManager.setItemsMessage("Alarm Wires...?", "electricityBox");
      lootBtnHolder.setVisible(false);
      GameManager.collectMoney();
    }
  }

  @FXML
  public void onLaserCuttingScene() {
    if (GameState.isFirewallDisabled /*&& GameState.isFirstRiddleSolved*/) {
      App.setUI(Scenes.LASERCUTTING);
    }
  }

  @FXML
  public void onButtonClick(ActionEvent event) {
    Button button = (Button) event.getSource();
    String buttonText = button.getText();
    updateCode(buttonText);
  }

  private void updateCode(String text) {
    labelText.append(text);
    inputLbl.setText(labelText.toString());
  }

  @FXML
  public void onCheckCode(ActionEvent event) {
    String code = givencode.getText().substring("Code: ".length());
    if (inputLbl.getText().equals(code)) {
      statusLbl.setText("Success, press x to Activate bomb");
      inputLbl.setText("");
      statusLbl.setTextFill(Color.GREEN);
      GameState.isBombActivated = true;

    } else {
      statusLbl.setText("Wrong Try Again");
      inputLbl.setText(null);
      statusLbl.setTextFill(Color.RED);
    }
    labelText.setLength(0);
  }

  @FXML
  public void onExitBomb() {
    bombPuzzle.setVisible(false);
    if (GameState.isBombActivated) {
      styleManager.setVisible(false, "switchHolder", "walkietalkieHolder", "bombHolder");
      App.textToSpeech("Good job, 5,4,3,2,1");
      AnimationManager.toggleAlarmAnimation(exitHolder, true, 0.5);
      AnimationManager.delayAnimation(exitHolder, escapeDoor);
      exitHolder.setDisable(true);
    }
  }

  @FXML
  public void onEscape() {
    TimerControl.cancelTimer();
    App.setUI(Scenes.GAMEFINISH);
    ((GameFinishController) SceneManager.getController(Scenes.GAMEFINISH)).setGameWonPage();
  }

  @FXML
  public void showInfo(MouseEvent event) {
    String door = event.getSource().toString();

    if (GameState.isFirewallDisabled) {

      String moneyText = "Money: $";
      String difficultyText = "Difficulty: ";

      if (door.contains("goldDoor")) {
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
  public void clearInfo(MouseEvent event) {
    dialogueBox.setVisible(false);
    moneyValue.setText(null);
    difficultyValue.setText(null);
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
    dialogueBox.setVisible(true);
    moneyValue.setVisible(true);
    difficultyValue.setVisible(true);
    moneyValue.setText(moneyText);
    difficultyValue.setText(difficultyText);
  }

  public void showMoneyCollected() {
    setInfoText("Money: " + GameManager.getMoneyToGain(), null);
  }

  @FXML
  public void invokeHackerAI(KeyEvent event) throws ApiProxyException {

    if (event.getCode() == KeyCode.ENTER) {
      walkieTalkieManager.startAnimation();

      Task<Void> aiTask3 =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              // Perform AI-related operations here
              ChatMessage msg = new ChatMessage("user", vaultTextField.getText());
              hackerAiManager.addChatHistory(msg.getContent());
              walkieTalkieManager.clearWalkieTalkie();

              ChatMessage responce = hackerAiManager.processInput(msg);
              hackerAiManager.addChatHistory(responce.getContent());

              // Move this code here to use the `responce` variable within the call method

              Platform.runLater(
                  () -> {
                    walkieTalkieManager.setWalkieTalkieText(responce);

                    vaultTextField.clear();
                    walkieTalkieManager.stopAnimation();
                  });
              return null;
            }
          };

      Thread aiThread3 = new Thread(aiTask3);
      aiThread3.setDaemon(true);
      aiThread3.start();
    }
  }

  @FXML
  public void quickHint(ActionEvent event) {
    String hint = hackerAiManager.GetQuickHint();
    hackerAiManager.storeQuickHint();
    walkieTalkieManager.setWalkieTalkieText(new ChatMessage("user", hint));
  }
}
