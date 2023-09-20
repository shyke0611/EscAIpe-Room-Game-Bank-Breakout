package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
  @FXML private ImageView wirecuttingbackground;

  private boolean isWireCutterSelected = false;
  StyleManager styleManager = StyleManager.getInstance();

  public void initialize() {
    SceneManager.setController(Scenes.WIRECUTTING, this);
    wiresCut = new ArrayList<>();
    styleManager.addItems(redwire,greenwire,bluewire,yellowwire,wirecuttingbackground);
    RandomnessGenerate.addWires(bluewire, yellowwire, greenwire, redwire);
    styleManager.setItemsMessage("use the wirecutter", "bluewire", "yellowwire", "greenwire", "redwire");
  }

  @FXML 
  void onGoBack() {
    App.setUI(Scenes.SECURITY);
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
    isWireCutterSelected = true;
    styleManager.removeItemsMessage("redwire", "greenwire", "bluewire", "yellowwire");
    styleManager.setItemsState(
        HoverColour.GREEN, "redwire", "greenwire", "bluewire", "yellowwire");
  }

  @FXML
  void onRetry() {
    wiresCut.clear();
    styleManager.setVisible(true, "yellowwire","redwire","bluewire","greenwire");
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
    GameState.isWiresCut = true;
    styleManager.setAlarm(false);
    GameState.isAlarmDisabled = true;
    styleManager.setDisable(true, "electricityBox");
    styleManager.setDisable(true, "guardpocket");
    styleManager.setVisible(false, "credentialsNote");
    retryBtn.setDisable(true);
  }

  private void handleIncorrectCombination() {
    taskLbl.setText("Fail");
  }
}
