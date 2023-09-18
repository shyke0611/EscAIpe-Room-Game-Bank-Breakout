package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.RandomnessGenerate;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager.HoverColour;
import nz.ac.auckland.se206.StyleManager.State;
import nz.ac.auckland.se206.StyleManager;

public class WireCuttingController extends Controller {
  StyleManager styleManager = StyleManager.getInstance();
  private List<HBox> wiresCut; // To store the order of clicked wires

  @FXML private HBox wirecutter;

  @FXML private HBox bluewire;
  @FXML private HBox greenwire;
  @FXML private HBox redwire;
  @FXML private HBox yellowwire;

  @FXML private Label taskLbl;

  private boolean isWireCutterSelected = false;

  public void initialize() {
    SceneManager.setController(Scenes.WIRECUTTING, this);
    // styleManager.setItemsState(HoverColour.GREEN, State.CLICK, wirecutter);
    wiresCut = new ArrayList<>();
    RandomnessGenerate.addWires(bluewire, yellowwire, greenwire, redwire);
    styleManager.setItemsMessage("use the wirecutter", bluewire,yellowwire,greenwire,redwire);
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
      isWireCutterSelected = true;
      styleManager.removeItemsMessage(redwire,greenwire,bluewire,yellowwire);
      styleManager.setItemsState(HoverColour.GREEN,State.HOVER,redwire,greenwire,bluewire,yellowwire);
  }


  @FXML
  void onRetry() {
   wiresCut.clear();
   yellowwire.setVisible(true);
   redwire.setVisible(true);
   greenwire.setVisible(true);
   bluewire.setVisible(true);
   taskLbl.setText(null);
  }

  @FXML
  void switchToSecurity(MouseEvent event) {
    App.setUI(Scenes.SECURITY);
  }

  public void checkWireCombination() {
    if (wiresCut.size() == RandomnessGenerate.getRandomWires().size()) {
      for (int i = 0; i < wiresCut.size(); i++) {
        HBox clickedWire = wiresCut.get(i);
        HBox expectedWire = RandomnessGenerate.getRandomWires().get(i);

        if (clickedWire != expectedWire) {
          handleIncorrectCombination();
          break;
        }
        handleCorrectCombination();
      }
    }
  }

  private void handleCorrectCombination() {
    taskLbl.setText("Success");
  }

  private void handleIncorrectCombination() {
    taskLbl.setText("Fail");
  }
}
