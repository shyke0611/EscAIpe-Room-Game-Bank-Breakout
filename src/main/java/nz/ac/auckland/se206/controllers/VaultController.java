package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;
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

  @FXML private Rectangle dialogueBox;
  @FXML private Label moneyValue;
  @FXML private Label difficultyValue;

  @FXML private HBox doorHolder;

  @FXML private VBox walkietalkie;
  @FXML private VBox walkietalkieText;

  private Canvas canvas;
  private GraphicsContext gc;
  private double prevX, prevY;
  private boolean cutting = false;
  @FXML private Rectangle AIAccess;

  private Boolean AIAccessGranted = false;
  StyleManager styleManager = StyleManager.getInstance();

  public void initialize() {
    SceneManager.setController(Scenes.VAULT, this);
    styleManager.addItems(goldDoor, silverDoor, bronzeDoor, vaultbackground);
    WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
  }

  //   handling mouse events on walkie talkie
  //   open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    WalkieTalkieManager.toggleWalkieTalkie();
  }

  public void switchToLobby() {
    App.setUI(Scenes.LOBBY);
  }

  public void switchToSecurity() {
    App.setUI(Scenes.SECURITY);
  }

  public void onSwitchToHacker() {
    SceneManager.setPreviousScene(Scenes.HACKERVAN, Scenes.VAULT);
    App.setUI(Scenes.HACKERVAN);
  }

  public void switchToEyeScanner() {
    App.setUI(Scenes.EYESCANNER);
    styleManager.setAlarm(true);
    GameState.isAlarmTripped = true;
  }

  public void onSwitchToChemicalMixing() {
    App.setUI(Scenes.CHEMICALMIXING);
  }

  public void grantAccess() {
    AIAccessGranted = true;
  }

  @FXML
  public void showInfo(MouseEvent event) {
    String door = event.getSource().toString();

    if (AIAccessGranted) {
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
  public void laserCuttingScene() {

    App.setUI(Scenes.LASERCUTTING);
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
