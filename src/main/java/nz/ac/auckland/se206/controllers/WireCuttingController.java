package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.GameManager.Objectives;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.RandomnessGenerate;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.WalkieTalkieManager;
import nz.ac.auckland.se206.gpt.ChatMessage;

/** Controller class for the Wire Cutting scene. */
public class WireCuttingController extends Controller {

  @FXML private HBox bluewire;
  @FXML private HBox greenwire;
  @FXML private HBox redwire;
  @FXML private HBox yellowwire;
  @FXML private HBox retryBtn;
  @FXML private Label moneyCount;

  @FXML private Label taskLbl;
  @FXML private Label timerLabel;
  @FXML private Label wirecutterLbl;
  @FXML private ImageView wirecuttingbackground;

  private StyleManager styleManager = StyleManager.getInstance();
  private List<HBox> wiresCut;
  private WalkieTalkieManager walkieTalkieManager = WalkieTalkieManager.getInstance();

  /** Initialize the Wire Cutting controller. It sets up the initial state of the scene. */
  public void initialize() {
    SceneManager.setController(Scenes.WIRECUTTING, this);
    GameManager.addMoneyGainedLabel(this, moneyCount);
    // setting timer
    super.setTimerLabel(timerLabel, 1);
    wiresCut = new ArrayList<>();
    // generating relevant methods on initialise
    styleManager.addHoverItems(wirecuttingbackground, bluewire, redwire, yellowwire, greenwire);
    RandomnessGenerate.addWires(bluewire, yellowwire, greenwire, redwire);
  }

  /**
   * Handle the "Go Back" button click event. Navigates back to the Security scene.
   *
   * @param event The ActionEvent triggered by clicking the "Go Back" button.
   */
  @FXML
  private void onGoBack(MouseEvent event) {
    App.setUi(Scenes.SECURITY);
  }

  /**
   * Handle the wire click event. If the wire cutter is selected, hides the clicked wire and checks
   * the wire combination.
   *
   * @param event The MouseEvent triggered by clicking a wire.
   */
  @FXML
  private void onWireClicked(MouseEvent event) {
    HBox clickedWire = (HBox) event.getSource();
    clickedWire.setVisible(false);
    wiresCut.add(clickedWire);
    checkWireCombination();
  }

  /**
   * Handle the retry button click event. Resets the wire cutting puzzle.
   *
   * @param event The ActionEvent triggered by clicking the retry button.
   */
  @FXML
  private void onRetry(MouseEvent event) {
    wiresCut.clear();
    StyleManager.setVisible(true, "yellowwire", "redwire", "bluewire", "greenwire");
    taskLbl.setText(null);
  }

  /**
   * Check if the wires cut by the player match the expected wire combination. If all wires are cut
   * in the correct order, handleCorrectCombination is called.
   */
  private void checkWireCombination() {
    if (wiresCut.size() == RandomnessGenerate.getRandomWires().size()) {
      boolean allWiresCorrect = true;

      // iterate through to get wire clicked
      for (int i = 0; i < wiresCut.size(); i++) {
        HBox clickedWire = wiresCut.get(i);
        HBox expectedWire = RandomnessGenerate.getRandomWires().get(i);

        if (!clickedWire.getId().equals(expectedWire.getId())) {

          allWiresCorrect = false;
          break;
        }
      }

      // handle combinations
      if (allWiresCorrect) {
        handleCorrectCombination();

      } else {
        handleIncorrectCombination();
      }
    }
  }

  /**
   * Handle the correct wire combination. Sets the success message, updates the game state, and
   * disables related elements.
   */
  private void handleCorrectCombination() {
    // handling relevant methods
    taskLbl.setText("Success");
    taskLbl.setTextFill(Color.GREEN);
    GameState.isWiresCut = true;
    StyleManager.setAlarm(false);
    GameState.isAlarmDisabled = true;
    // setting relevant styles
    StyleManager.setDisable(true, "electricityBox");
    StyleManager.setDisable(true, "guardpocket");
    StyleManager.setVisible(false, "credentialsNote");
    retryBtn.setDisable(true);
    GameManager.setCurrentObjective(Objectives.FIND_ESCAPE);
    App.textToSpeech("Alarm Disabled");
    WalkieTalkieManager.setWalkieTalkieNotifcationOn();
    walkieTalkieManager.setWalkieTalkieText(
        new ChatMessage("user", "That was fast! Now get back to the vault and escape quickly!"));
  }

  /** Handle the incorrect wire combination. Sets the fail message. */
  private void handleIncorrectCombination() {
    taskLbl.setText("Fail");
    taskLbl.setTextFill(Color.RED);
  }
}
