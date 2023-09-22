package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import javafx.event.ActionEvent;
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

/**
 * Controller class for the Wire Cutting scene.
 */
public class WireCuttingController extends Controller {

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
  private StyleManager styleManager = StyleManager.getInstance();
  private List<HBox> wiresCut; 

   /**
   * Initialize the Wire Cutting controller.
   * It sets up the initial state of the Wire Cutting scene.
   */
  public void initialize() {
    SceneManager.setController(Scenes.WIRECUTTING, this);
    super.setTimerLabel(timerLabel, 2);
    wiresCut = new ArrayList<>();
    styleManager.addItems(
        redwire, greenwire, bluewire, yellowwire, wirecuttingbackground, wirecuttingorderLbl);
    RandomnessGenerate.addWires(bluewire, yellowwire, greenwire, redwire);
    styleManager.setItemsMessage(
        "use the wirecutter", "bluewire", "yellowwire", "greenwire", "redwire");
    setUpListener(wirecutter);
  }

    /**
   * Handle the "Go Back" button click event.
   * Navigates back to the Security scene.
   *
   * @param event The ActionEvent triggered by clicking the "Go Back" button.
   */
  @FXML
  private void onGoBack(ActionEvent event) {
    App.setUI(Scenes.SECURITY);
    styleManager.setClueHover("electricityBox", false);
  }

   /**
   * Handle the wire click event.
   * If the wire cutter is selected, hides the clicked wire and checks the wire combination.
   *
   * @param event The MouseEvent triggered by clicking a wire.
   */
  @FXML
  private void onWireClicked(MouseEvent event) {
    if (isWireCutterSelected == true) {
      HBox clickedWire = (HBox) event.getSource();
      clickedWire.setVisible(false);
      wiresCut.add(clickedWire);
      checkWireCombination();
    }
  }

  /**
   * Handle the wire cutter click event.
   * Sets the wire cutter as selected and updates the wire colors accordingly.
   *
   * @param event The MouseEvent triggered by clicking the wire cutter.
   */
  @FXML
  private void onWireCutterClicked(MouseEvent event) {
    wirecutter.setVisible(false);
    wirecutterLbl.setVisible(false);
    isWireCutterSelected = true;
    styleManager.removeItemsMessage("redwire", "greenwire", "bluewire", "yellowwire");
    styleManager.setItemsState(HoverColour.GREEN, "redwire", "greenwire", "bluewire", "yellowwire");
  }

   /**
   * Handle the retry button click event.
   * Resets the wire cutting puzzle.
   *
   * @param event The ActionEvent triggered by clicking the retry button.
   */
  @FXML
  private void onRetry(ActionEvent event) {
    wiresCut.clear();
    styleManager.setVisible(true, "yellowwire", "redwire", "bluewire", "greenwire");
    taskLbl.setText(null);
  }

  /**
   * Check if the wires cut by the player match the expected wire combination.
   * If all wires are cut in the correct order, handleCorrectCombination is called.
   */
  private void checkWireCombination() {
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

  /**
   * Handle the correct wire combination.
   * Sets the success message, updates the game state, and disables related elements.
   */
  private void handleCorrectCombination() {
    taskLbl.setText("Success");
    taskLbl.setTextFill(Color.GREEN);
    GameState.isWiresCut = true;
    StyleManager.setAlarm(false);
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

  /**
   * Handle the incorrect wire combination.
   * Sets the fail message.
   */
  private void handleIncorrectCombination() {
    taskLbl.setText("Fail");
    taskLbl.setTextFill(Color.RED);
  }
}
