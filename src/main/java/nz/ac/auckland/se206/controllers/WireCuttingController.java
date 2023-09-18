package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.RandomnessGenerate;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;

public class WireCuttingController extends Controller {
  StyleManager styleManager = StyleManager.getInstance();
  private List<Rectangle> wiresCut; // To store the order of clicked wires

  @FXML private HBox wirecutter;

  @FXML private Rectangle bluewire;
  @FXML private Rectangle greenwire;
  @FXML private Rectangle redwire;
  @FXML private Rectangle yellowwire;

  @FXML private Label taskLbl;

  private boolean isWireCutterSelected = false;
  private double xOffset, yOffset;

  public void initialize() {
    SceneManager.setController(Scenes.WIRECUTTING, this);
    // styleManager.setItemsState(HoverColour.GREEN, State.CLICK, wirecutter);
    wiresCut = new ArrayList<>();
    RandomnessGenerate.addWires(bluewire, yellowwire, greenwire, redwire);
  }

  @FXML
  void onWireClicked(MouseEvent event) {
    if (isWireCutterSelected == true) {
      Rectangle clickedWire = (Rectangle) event.getSource();
      clickedWire.setVisible(false);
      wiresCut.add(clickedWire);

      checkWireCombination();
    }
  }

  @FXML
  void onWireCutterClicked(MouseEvent event) {
      isWireCutterSelected = true;
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
        Rectangle clickedWire = wiresCut.get(i);
        Rectangle expectedWire = RandomnessGenerate.getRandomWires().get(i);

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
