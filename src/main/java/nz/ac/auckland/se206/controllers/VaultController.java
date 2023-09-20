package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.AnimationManager;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.RandomnessGenerate;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.StyleManager.HoverColour;
import nz.ac.auckland.se206.WalkieTalkieManager;

public class VaultController extends Controller {

  @FXML private ImageView Lobby;
  @FXML private ImageView Security;
  @FXML private Rectangle whiteBackground;
  @FXML private Rectangle laserCuttingScene;
  @FXML private ImageView VaultDoor;
  @FXML private ImageView goldDoor;
  @FXML private ImageView silverDoor;
  @FXML private ImageView bronzeDoor;
  @FXML private ImageView vaultbackground;
  @FXML private ImageView bomblogo;
  @FXML private Label timerLabel;

  @FXML private Rectangle dialogueBox;
  @FXML private Label moneyValue;
  @FXML private Label difficultyValue;
  @FXML private HBox exitHolder;

  @FXML private HBox doorHolder;
  @FXML private VBox walkietalkie;
  @FXML private VBox walkietalkieText;
  @FXML private HBox bombHolder;
  @FXML private VBox bombPuzzle;
  @FXML private Button button;
  @FXML private Button checkBtn;
  @FXML private Label inputLbl;
  @FXML private Label statusLbl;
  @FXML private Label givencode;
  @FXML private HBox escapeDoor;
  @FXML private Pane slidePane;
  @FXML private Button lootBtn;

  @FXML private HBox switchHolder;
  @FXML private HBox walkietalkieHolder;

  private Canvas canvas;
  private GraphicsContext gc;
  private double prevX, prevY;
  private boolean cutting = false;
  @FXML private Rectangle AIAccess;

  private Boolean AIAccessGranted = false;
  StyleManager styleManager = StyleManager.getInstance();
  private StringBuilder labelText = new StringBuilder();

  public void initialize() {
    SceneManager.setController(Scenes.VAULT, this);
    super.setTimerLabel(timerLabel);
    styleManager.addItems(
        goldDoor,
        silverDoor,
        bronzeDoor,
        vaultbackground,
        doorHolder,
        exitHolder,
        bombHolder,
        bombPuzzle,
        walkietalkie,
        walkietalkieHolder,
        switchHolder,
        escapeDoor);
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
    givencode.setText("Code: " + RandomnessGenerate.getPasscode());
    styleManager.setItemsMessage("set bomb down", "exitHolder");
    styleManager.setItemsMessage("escape", "escapeDoor");
    styleManager.setItemsMessage("activate bomb", "bombHolder");
  }

  //   handling mouse events on walkie talkie
  //   open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  @FXML
  public void switchToLobby() {
    App.setUI(Scenes.LOBBY);
  }

  @FXML
  public void switchToSecurity() {
    App.setUI(Scenes.SECURITY);
  }

  @FXML
  void onBombPressed(MouseEvent event) {
    AnimationManager.slideDoorsAnimation(doorHolder);
    AnimationManager.slideDoorsAnimation(vaultbackground);
    AnimationManager.slideDoorsAnimation(slidePane);
    bomblogo.setVisible(false);
    styleManager.removeItemsMessage("bombHolder");
  }

  @FXML
  void onBombPlaced(MouseEvent event) {
    if (!GameState.isBombActivated) {
      bombPuzzle.setVisible(true);
    }
  }

  @FXML
  public void onSwitchToHacker() {
    SceneManager.setPreviousScene(Scenes.HACKERVAN, Scenes.VAULT);
    App.setUI(Scenes.HACKERVAN);
  }

  public void switchToEyeScanner() {
    if (GameState.isFirewallDisabled) {
      App.setUI(Scenes.EYESCANNER);
    }
  }

  public void onSwitchToChemicalMixing() {
    if (GameState.isFirewallDisabled) {
      App.setUI(Scenes.CHEMICALMIXING);
    }
  }

  public void grantAccess() {
    GameState.isFirewallDisabled = true;
    styleManager.setDisable(true, "computer");
  }

  @FXML
  void onLootCollected(ActionEvent event) {
    if (GameState.isFirewallDisabled && GameState.isAnyDoorOpen) {
      styleManager.setAlarm(true);
      GameState.isAlarmTripped = true;
      styleManager.setItemsState(HoverColour.GREEN, "electricityBox");
      styleManager.setItemsState(HoverColour.GREEN, "guardpocket");
      styleManager.setItemsMessage("Something seems odd?", "guardpocket");
      styleManager.setItemsMessage("Alarm Wires...?", "electricityBox");
      lootBtn.setDisable(true);
    }
  }

  @FXML
  public void laserCuttingScene() {
    if (GameState.isFirewallDisabled) {
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
      GameState.isBombActivated = true;

    } else {
      statusLbl.setText("Wrong Try Again");
      inputLbl.setText(null);
    }
    labelText.setLength(0);
  }

  public void onExitBomb() {
    bombPuzzle.setVisible(false);
    if (GameState.isBombActivated) {
      styleManager.setVisible(
          false, "walkietalkie", "switchHolder", "walkietalkieHolder", "bombHolder");
      AnimationManager.startBombAnimation(exitHolder);
      AnimationManager.delayAnimation(exitHolder, escapeDoor);
      exitHolder.setDisable(true);
    }
  }

  public void onEscape() {
    App.setUI(Scenes.GAMEFINISH);
  }

  @FXML
  public void showInfo(MouseEvent event) {
    String door = event.getSource().toString();

    if (GameState.isFirewallDisabled) {
      String style = "-fx-effect: dropshadow(gaussian, #00bf00, 5, 5, 0, 0);";
      String moneyText = "Money: $";
      String difficultyText = "Difficulty: ";

      if (door.contains("goldDoor")) {
        setDoorStyle(goldDoor, style);
        setInfoText(moneyText + "20,000,000", difficultyText + "★★★★★");
      } else if (door.contains("silverDoor")) {
        setDoorStyle(silverDoor, style);
        setInfoText(moneyText + "10,000,000", difficultyText + "★★★☆☆");
      } else if (door.contains("bronzeDoor")) {
        setDoorStyle(bronzeDoor, style);
        setInfoText(moneyText + "5,000,000", difficultyText + "★☆☆☆☆");
      }
    } else {
      String style = "-fx-effect: dropshadow(gaussian, #ff0000, 5, 5, 0, 0);";
      setDoorStyle(getDoorByEvent(event), style);
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

  private ImageView getDoorByEvent(MouseEvent event) {
    if (event.getSource().toString().contains("goldDoor")) {
      return goldDoor;
    } else if (event.getSource().toString().contains("silverDoor")) {
      return silverDoor;
    } else if (event.getSource().toString().contains("bronzeDoor")) {
      return bronzeDoor;
    }
    return null;
  }
}
