package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.GameManager.Objectives;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.RandomnessGenerate;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;

/** Controller class for the Eye Scanner scene. */
public class EyeScannerController extends Controller {

  @FXML private Label error;
  @FXML private Label geteyesampleLbl;
  @FXML private VBox accessGranted;
  @FXML private VBox accessDenied;
  @FXML private Label mystery;

  @FXML private Circle guardEye;
  @FXML private Circle artificialEye;

  @FXML private Slider redSlider;
  @FXML private Slider greenSlider;
  @FXML private Slider blueSlider;

  @FXML private Label redValue;
  @FXML private Label greenValue;
  @FXML private Label blueValue;
  @FXML private Label moneyCount;

  @FXML private VBox walkietalkieText;
  @FXML private Label timerLabel;

  @FXML private Button compareBtn;

  @FXML private ImageView background;

  private int red;
  private int green;
  private int blue;

  private int guardRed;
  private int guardGreen;
  private int guardBlue;

  /** Initialize the Eye Scanner Controller. Sets up the initial state of the scene. */
  public void initialize() {

    // set relevant method on initialisation
    StyleManager.addHoverItems(compareBtn, background);
    SceneManager.setController(Scenes.EYESCANNER, this);
    GameManager.addMoneyGainedLabel(null, moneyCount);
    super.setTimerLabel(timerLabel, 1);
    accessGranted.setVisible(false);
    accessDenied.setVisible(false);

    // Bind the label and the slider
    redValue.textProperty().bind(redSlider.valueProperty().asString("%.0f"));
    greenValue.textProperty().bind(greenSlider.valueProperty().asString("%.0f"));
    blueValue.textProperty().bind(blueSlider.valueProperty().asString("%.0f"));

    // Adjust the colour of the artificial eye and adjust slider colour based on the sliders

    // Red Slider
    redSlider
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              red = newValue.intValue();
              artificialEye.setFill(Paint.valueOf("rgb(" + red + "," + green + "," + blue + ")"));
              // From Stack Overflow:
              // https://stackoverflow.com/questions/51343759/how-to-change-fill-color-of-slider-in-javafx
              double percentage = 100.0 * newValue.doubleValue() / redSlider.getMax();
              String style =
                  String.format(
                      "-track-color: linear-gradient(to right, "
                          + "red, "
                          + "red, "
                          + "-default-track-color %1$.1f%%, "
                          + "-default-track-color 100%%);",
                      percentage);
              redSlider.setStyle(style);
            });

    // Green Slider
    greenSlider
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              green = newValue.intValue();
              artificialEye.setFill(Paint.valueOf("rgb(" + red + "," + green + "," + blue + ")"));
              // From Stack Overflow:
              // https://stackoverflow.com/questions/51343759/how-to-change-fill-color-of-slider-in-javafx
              double percentage = 100.0 * newValue.doubleValue() / redSlider.getMax();
              String style =
                  String.format(
                      "-track-color: linear-gradient(to right, "
                          + "green, "
                          + "green, "
                          + "-default-track-color %1$.1f%%, "
                          + "-default-track-color 100%%);",
                      percentage);
              greenSlider.setStyle(style);
            });

    // Blue Slider
    blueSlider
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              blue = newValue.intValue();
              artificialEye.setFill(Paint.valueOf("rgb(" + red + "," + green + "," + blue + ")"));
              // From Stack Overflow:
              // https://stackoverflow.com/questions/51343759/how-to-change-fill-color-of-slider-in-javafx
              double percentage = 100.0 * newValue.doubleValue() / redSlider.getMax();
              String style =
                  String.format(
                      "-track-color: linear-gradient(to right, "
                          + "blue, "
                          + "blue, "
                          + "-default-track-color %1$.1f%%, "
                          + "-default-track-color 100%%);",
                      percentage);
              blueSlider.setStyle(style);
            });

    // Sync Background walkie talkie
    // WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
  }

  /** Update the guard's eye color with a random color. */
  public void updateGuardEye() {
    // getting random eye colour combination
    guardRed = RandomnessGenerate.getRandomColourValue();
    guardGreen = RandomnessGenerate.getRandomColourValue();
    guardBlue = RandomnessGenerate.getRandomColourValue();
    // setting visibility
    mystery.setVisible(false);
    error.setVisible(false);
    guardEye.setFill(Paint.valueOf("rgb(" + guardRed + "," + guardGreen + "," + guardBlue + ")"));
    System.out.println("Guard eye colour: " + guardRed + " " + guardGreen + " " + guardBlue);
  }

  /**
   * Compares the sample eye color with the guard's eye color and updates the access status.
   *
   * @param event The ActionEvent triggered by the "Compare" button.
   */
  @FXML
  private void onCompareSample() {
    // comparing eye colour with guard eye colour
    if (isColourMatch(red, guardRed)
        && isColourMatch(green, guardGreen)
        && isColourMatch(blue, guardBlue)) {
      // access granted if match found
      accessGranted.setVisible(true);
      accessDenied.setVisible(false);
      GameState.isEyeScannerBypassed = true;
      compareBtn.setDisable(true);
      // $20 Million
      GameManager.setCurrentObjective(Objectives.SELECT_VAULT_DOOR);
      GameManager.increaseMoneyToGain(20000000);
      GameManager.setMoneyGained();
    } else {
      accessGranted.setVisible(false);
      accessDenied.setVisible(true);
    }
  }

  /** Boolean to return if colour of eye colour matches with margin error. */
  private boolean isColourMatch(int colour, int guardColour) {
    if (colour >= guardColour - 35 && colour <= guardColour + 35) {
      return true;
    }
    ;
    return false;
  }
}
