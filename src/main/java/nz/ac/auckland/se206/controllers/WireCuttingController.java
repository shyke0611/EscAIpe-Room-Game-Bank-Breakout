package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.RandomnessGenerate;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.StyleManager.HoverColour;

public class WireCuttingController extends Controller {
  private List<HBox> wiresCut; // To store the order of clicked wires

  @FXML private HBox wirecutter;

  @FXML private HBox bluewire;
  @FXML private HBox greenwire;
  @FXML private HBox redwire;
  @FXML private HBox yellowwire;
  @FXML private Button retryBtn;

  @FXML private Label taskLbl;
  @FXML private Label timerLabel;
  @FXML private Label wirecuttingorderLbl;
  @FXML private Label wirecutterLbl;
  @FXML private ImageView wirecuttingbackground;

  private boolean isWireCutterSelected = false;
  StyleManager styleManager = StyleManager.getInstance();

  public void initialize() {
    SceneManager.setController(Scenes.WIRECUTTING, this);
    super.setTimerLabel(timerLabel, 2);
    wiresCut = new ArrayList<>();
    styleManager.addItems(redwire, greenwire, bluewire, yellowwire, wirecuttingbackground,wirecuttingorderLbl);
    RandomnessGenerate.addWires(bluewire, yellowwire, greenwire, redwire);
    styleManager.setItemsMessage(
        "use the wirecutter", "bluewire", "yellowwire", "greenwire", "redwire");
    setupListeners(wirecutter);
  }

  @FXML
  void onGoBack() {
    App.setUI(Scenes.SECURITY);
    styleManager.setClueHover("electricityBox",false);
  }

  @FXML
  void onWireClicked(MouseEvent event) {
    if (isWireCutterSelected == true) {
      HBox clickedWire = (HBox) event.getSource();
      clickedWire.setVisible(false);
      wiresCut.add(clickedWire);
      checkWireCombination();
    }
  }

  @FXML
  void onWireCutterClicked(MouseEvent event) {
    wirecutter.setVisible(false);
    wirecutterLbl.setVisible(false);
    isWireCutterSelected = true;
    styleManager.removeItemsMessage("redwire", "greenwire", "bluewire", "yellowwire");
    styleManager.setItemsState(HoverColour.GREEN, "redwire", "greenwire", "bluewire", "yellowwire");
  }

  @FXML
  void onRetry() {
    wiresCut.clear();
    styleManager.setVisible(true, "yellowwire", "redwire", "bluewire", "greenwire");
    taskLbl.setText(null);
  }

  public void checkWireCombination() {
    if (wiresCut.size() == RandomnessGenerate.getRandomWires().size()) {
      boolean allWiresCorrect = true;

      for (int i = 0; i < wiresCut.size(); i++) {
        HBox clickedWire = wiresCut.get(i);
        HBox expectedWire = RandomnessGenerate.getRandomWires().get(i);

        if (!clickedWire.getId().equals(expectedWire.getId())) {

          allWiresCorrect = false;
          break;
        }
      }

      if (allWiresCorrect) {
        handleCorrectCombination();

      } else {
        handleIncorrectCombination();
      }
    }
  }

  private void handleCorrectCombination() {
    taskLbl.setText("Success");
    taskLbl.setTextFill(Color.GREEN);
    GameState.isWiresCut = true;
    styleManager.setAlarm(false);
    GameState.isAlarmDisabled = true;
    styleManager.setDisable(true, "electricityBox");
    styleManager.setDisable(true, "guardpocket");
    styleManager.setVisible(false, "credentialsNote");
    retryBtn.setDisable(true);
    styleManager.setClueHover("bomblayer", true);
    styleManager.setClueHover("lobbyRoomSwitch", false);
    styleManager.getItem("realvaultbackground").setVisible(false);
    styleManager.setClueHover("electricityBox", false);
    App.textToSpeech("Alarm Disabled");
  }

  private void handleIncorrectCombination() {
    taskLbl.setText("Fail");
    taskLbl.setTextFill(Color.RED);
  }
}
