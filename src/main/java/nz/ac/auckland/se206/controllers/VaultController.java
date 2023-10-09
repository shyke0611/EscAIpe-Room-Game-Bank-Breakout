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
import javafx.scene.text.Text;
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
  @FXML private ImageView realvaultbackground;
  @FXML private ImageView bomblogo;
  @FXML private TextArea vaultTextArea;
  @FXML private TextField vaultTextField;
  @FXML private Label timerLabel;

  @FXML private Rectangle dialogueBox;
  @FXML private Label moneyValue;
  @FXML private Text lootLbl;
  @FXML private Label difficultyValue;
  @FXML private HBox exitHolder;

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
  @FXML private Label inputLbl;
  @FXML private Label statusLbl;
  @FXML private Label givencode;
  @FXML private HBox escapeDoor;
  @FXML private Pane slidePane;
  @FXML private Button lootBtn;
  @FXML private StackPane timerClock;
  @FXML private Label numberOfHints;

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
    super.setTimerLabel(timerLabel, 1);

    // adding relevant items to the stylemanager list
    styleManager.addItems(
        goldDoor,
        silverDoor,
        bronzeDoor,
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

    // setting setyle for the items
    styleManager.setItemsMessage("set bomb down", "exitHolder");
    styleManager.setItemsMessage("escape", "escapeDoor");
    styleManager.setItemsMessage("activate bomb", "bombHolder");
    // //styleManager.setItemsMessage(
    //     "Need to disable firewall from blocking us",
    //     "bronzeDoorHolder",
    //     "silverDoorHolder",
    //     "goldDoorHolder");
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
    // // setting sliding animation
    // AnimationManager.slideDoorsAnimation(doorHolder);
    // AnimationManager.slideDoorsAnimation(vaultbackground);
    // AnimationManager.slideDoorsAnimation(slidePane);

    GameManager.completeObjective();

    // setting style for relevant items
    bomblogo.setVisible(false);
    bombHolder.setDisable(true);
    exitHolder.setVisible(true);
    styleManager.removeItemsMessage("bombHolder");
    styleManager.setClueHover("bomblayer", false);
    styleManager.setVisible(false, "switchHolder");
  }

  @FXML
  private void onBombPlaced(MouseEvent event) {
    if (!GameState.isBombActivated) {
      GameManager.completeObjective();
      bombPuzzle.setVisible(true);
      bombPuzzle.requestFocus();
    }
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
      GameManager.completeObjective();
      App.setUI(Scenes.EYESCANNER);
      GameState.isEyeScannerEntered = true;
      // setting style
      styleManager.setItemsMessage("Get guard eye colour", "guardeyes");
      styleManager.setItemsState(HoverColour.GREEN, "guardeyes");
    }
  }

  @FXML
  private void onSwitchToChemicalMixing() {
    // when firewall is disabled execute
    if (GameState.isFirewallDisabled) {
      GameManager.completeObjective();
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
      }

      GameManager.completeObjective();

      // setting style to relevant items
      styleManager.setItemsState(HoverColour.GREEN, "electricityBox");
      styleManager.setItemsState(HoverColour.GREEN, "guardpocket");
      styleManager.setItemsMessage("Something seems odd?", "guardpocket");
      styleManager.setItemsMessage("Alarm Wires...?", "electricityBox");
      lootBtnHolder.setVisible(false);
      styleManager.setDisable(true, "bronzeDoor", "silverDoor", "goldDoor");
      GameManager.collectMoney();
    }
  }

  @FXML
  private void onLaserCuttingScene() {
    // execute when firewall is disabled
    if (GameState.isFirewallDisabled) {
      GameManager.completeObjective();
      App.setUI(Scenes.LASERCUTTING);
    }
  }

  @FXML
  private void onButtonClick(ActionEvent event) {
    Button button = (Button) event.getSource();
    String buttonText = button.getText();
    updateCode(buttonText);
  }

  private void updateCode(String text) {
    labelText.append(text);
    inputLbl.setText(labelText.toString());
  }

  @FXML
  private void onCheckCode() {
    // check bomb code
    String code = givencode.getText().substring("Code: ".length());
    // handle correct input
    if (inputLbl.getText().equals(code)) {
      statusLbl.setText("Success, press x to Activate bomb");
      inputLbl.setText("");
      statusLbl.setTextFill(Color.GREEN);
      GameState.isBombActivated = true;

      // handle incorrect input
    } else {
      statusLbl.setText("Wrong Try Again");
      inputLbl.setText(null);
      statusLbl.setTextFill(Color.RED);
    }
    labelText.setLength(0);
  }

  @FXML
  private void onExitBomb() {
    bombPuzzle.setVisible(false);
    // execute when bomb is activated
    if (GameState.isBombActivated) {
      styleManager.setVisible(false, "switchHolder", "walkietalkieHolder", "bombHolder");
      // set animation for bomb
      AnimationManager.toggleAlarmAnimation(exitHolder, true, 0.5);
      AnimationManager.delayAnimation(exitHolder, escapeDoor);
      exitHolder.setDisable(true);
    }
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

    // If esc is pressed, close the bomb
    if (event.getCode() == KeyCode.ESCAPE) {
      onExitBomb();
    }

    // If enter is pressed, check the code
    if (event.getCode() == KeyCode.ENTER) {
      System.out.println("Enter pressed");
      onCheckCode();
      return;
    }

    // If backspace is pressed, remove the last character from the input label
    if (event.getCode() == KeyCode.BACK_SPACE) {
      if (labelText.length() > 0) {
        labelText.deleteCharAt(labelText.length() - 1);
        inputLbl.setText(labelText.toString());
      }
    }

    // If number is pressed, add it to the input label
    if (event.getCode().isDigitKey() && !event.getCode().equals(KeyCode.ENTER)) {
      updateCode(event.getText());
    }

    // if x is pressed, close the bomb
    if (event.getCode() == KeyCode.X) {
      onExitBomb();
    }
  }
}
