package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.GameManager.Objectives;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.RandomnessGenerate;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;

/** Controller class for the Chemical Mixing scene. */
public class ChemicalMixingController extends Controller {

  @FXML private Label timerLabel;
  @FXML private Slider slider;
  @FXML private StackPane pourBtn;
  @FXML private StackPane retryButton;
  @FXML private Button continueBtn;
  @FXML private ImageView greenVial;
  @FXML private ImageView redVial;
  @FXML private ImageView blueVial;
  @FXML private ImageView yellowVial;
  @FXML private ImageView largeVialBlue;
  @FXML private ImageView largeVialGreen;
  @FXML private ImageView largeVialRed;
  @FXML private ImageView largeVialYellow;
  @FXML private ImageView emptyVial;
  @FXML private ImageView redArrow;
  @FXML private StackPane largeVialPane;

  @FXML private Label labelOne;
  @FXML private Label labelTwo;
  @FXML private Label labelThree;
  @FXML private Label labelFour;
  @FXML private Label currentVial;
  @FXML private Label winLabel;
  @FXML private Label selectVialLabel;
  @FXML private Label moneyCount;

  @FXML private Region firstPour;
  @FXML private Rectangle secondPour;
  @FXML private Rectangle thirdPour;
  @FXML private Rectangle fourthPour;
  @FXML private Rectangle whiteRectangle;
  @FXML private ImageView background;

  private String randomYellow;
  private String randomRed;
  private String randomBlue;
  private String randomGreen;
  private String vialColour;

  private int pourCount;
  private Timeline sliderAnimation;

  /** Initialize the Chemical Mixing Controller. Sets up the initial state of the scene. */
  public void initialize() {
    SceneManager.setController(Scenes.CHEMICALMIXING, this);
    super.setTimerLabel(timerLabel, 1);
    GameManager.addMoneyGainedLabel(this, moneyCount);

    // Setting up hover animations
    setUpHover();
    StyleManager.addHoverItems(background);

    // Intialsing recipe and saving for later reference
    initializeRecipe();

    pourBtn.setDisable(true);
    pourBtn.setOpacity(0.5);

    retryButton.setDisable(true);
    retryButton.setOpacity(0.5);

    pourCount = 0;

    sliderAnimation =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1), new KeyValue(slider.valueProperty(), slider.getMax())));

    sliderAnimation.setAutoReverse(true); // Enable auto-reverse
    sliderAnimation.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
  }

  /** Set up hover animations for the chemical vial images. */
  private void setUpHover() {
    // Setting up hover animations
    StyleManager.addHoverItems(yellowVial, redVial, blueVial, greenVial);
    StyleManager.setItemsHoverColour(
        StyleManager.HoverColour.GREEN, "yellowVial", "redVial", "blueVial", "greenVial");
  }

  /**
   * Handle the event when a chemical vial is clicked.
   *
   * @param event The MouseEvent triggered by clicking a vial.
   */
  @FXML
  public void onVialClicked(MouseEvent event) {
    // setting visibility and disbabiulity to nodes
    ImageView image = (ImageView) event.getSource();
    String id = image.getId();
    sliderAnimation.play();
    pourBtn.setDisable(false);
    pourBtn.setOpacity(1);
    emptyVial.setVisible(false);

    redArrow.setVisible(false);
    retryButton.setOpacity(1);
    selectVialLabel.setVisible(false);

    // setting style for the slider
    Node track = slider.lookup(".thumb");
    track.setStyle("-fx-background-color: white");

    // setting the style for each vial
    switch (id) {
      case "yellowVial":
        // setting the style for yellow vial
        vialColour = "yellow";
        largeVialYellow.setVisible(true);
        largeVialBlue.setVisible(false);
        largeVialRed.setVisible(false);
        largeVialGreen.setVisible(false);
        setSliderGraident(Integer.parseInt(randomYellow));
        break;
      case "redVial":
        // setting the style for red vial
        vialColour = "red";
        largeVialRed.setVisible(true);
        largeVialBlue.setVisible(false);
        largeVialYellow.setVisible(false);
        largeVialGreen.setVisible(false);
        setSliderGraident(Integer.parseInt(randomRed));
        break;
      case "blueVial":
        // setting the style for blue vial
        vialColour = "blue";
        largeVialBlue.setVisible(true);
        largeVialGreen.setVisible(false);
        largeVialRed.setVisible(false);
        largeVialYellow.setVisible(false);
        setSliderGraident(Integer.parseInt(randomBlue));
        break;
      case "greenVial":
        // setting the style for green vial
        vialColour = "green";
        largeVialGreen.setVisible(true);
        largeVialBlue.setVisible(false);
        largeVialRed.setVisible(false);
        largeVialYellow.setVisible(false);
        setSliderGraident(Integer.parseInt(randomGreen));
        break;
    }
  }

  /**
   * Handles the event when a chemical vile is poured into the beaker.
   *
   * @param event The MouseEvent triggered by pouring a vile.
   */
  @FXML
  private void onPourChemical(MouseEvent event) {

    selectVialLabel.setVisible(true);
    // Pour button was clicked
    if (vialColour == null) {

      redArrow.setVisible(true);
      return;
    }

    onStopSlider();
    pourBtn.setDisable(true);
    pourBtn.setOpacity(0.5);

    int value = (int) Math.floor(slider.getValue());
    // filling beaker for yellow
    if (vialColour.equals("yellow") && value == Integer.parseInt(randomYellow)) {
      value = 1;
      pourCount++;
      fillBeaker(value, pourCount);
      yellowVial.setVisible(false);
      emptyVial.setVisible(true);

      // filling beaker for red
    } else if (vialColour.equals("red") && value == Integer.parseInt(randomRed)) {
      value = 2;
      pourCount++;
      fillBeaker(value, pourCount);
      redVial.setVisible(false);
      emptyVial.setVisible(true);
      // filling beaker for blue
    } else if (vialColour.equals("blue") && value == Integer.parseInt(randomBlue)) {
      value = 3;
      pourCount++;
      fillBeaker(value, pourCount);
      blueVial.setVisible(false);
      emptyVial.setVisible(true);
      // filling beaker for #15ed20
    } else if (vialColour.equals("green") && value == Integer.parseInt(randomGreen)) {
      value = 4;
      pourCount++;
      fillBeaker(value, pourCount);
      greenVial.setVisible(false);
      emptyVial.setVisible(true);
    } else {
      // setting properties to the viles and buttons
      selectVialLabel.setVisible(false);
      emptyVial.setVisible(false);
      retryButton.setDisable(false);
      yellowVial.setDisable(true);
      redVial.setDisable(true);
      blueVial.setDisable(true);
      greenVial.setDisable(true);
    }
  }

  /**
   * Fills the beaker with the poured chemical and checks if the combination is correct.
   *
   * @param value The value representing the poured chemical.
   * @param pourCount The number of times chemicals have been poured.
   */
  private void fillBeaker(int value, int pourCount) {

    // More ugly code to determine which rectangle to fill and what colour

    String currentColour;
    // setting paint colour ro the rectangles in beaker
    if (value == 1) {
      // yellow
      currentColour =
          "radial-gradient(focus-angle 0deg, focus-distance 50%, center 50% 50%, radius 100%, "
              + " yellow, derive(yellow, 50%), derive(yellow, 150%))";
    } else if (value == 2) {
      // red
      currentColour =
          "radial-gradient(focus-angle 0deg, focus-distance 50%, center 50% 50%, radius 100%, "
              + " red, derive(red, 50%), derive(red, 150%));";
    } else if (value == 3) {
      // blue
      currentColour =
          "radial-gradient(focus-angle 0deg, focus-distance 50%, center 50% 50%, radius 100%, blue,"
              + " derive(blue, 50%), derive(blue, 150%))";
    } else {
      // green
      currentColour =
          "radial-gradient(focus-angle 0deg, focus-distance 50%, center 50% 50%, radius 100%,"
              + "    green, derive(green, 50%), derive(green, 150%))";
    }

    // setting paint colour for the first vile selected
    if (pourCount == 1) {
      firstPour.setVisible(true);
      firstPour.setStyle("-fx-background-color: " + currentColour);
      // setting paint colour for the second vile selected
    } else if (pourCount == 2) {
      secondPour.setVisible(true);
      secondPour.setStyle("-fx-fill: " + currentColour);
      // setting paint colour for the third vile selected
    } else if (pourCount == 3) {
      thirdPour.setVisible(true);
      thirdPour.setStyle("-fx-fill: " + currentColour);
      // setting paint colour for the last vile selected
    } else if (pourCount == 4) {
      fourthPour.setVisible(true);
      fourthPour.setStyle("-fx-fill: " + currentColour);
      checkWin();
      GameState.isChemicalMixingBypassed = true;
    }
  }

  /**
   * Checks if the player has successfully completed the chemical mixing challenge. If successful,
   * it reveals the "Continue" button and win message, and increases the money to gain.
   */
  private void checkWin() {
    // set visibility of nodes
    largeVialPane.setVisible(false);
    emptyVial.setVisible(false);
    largeVialBlue.setVisible(false);
    largeVialGreen.setVisible(false);
    largeVialRed.setVisible(false);
    largeVialYellow.setVisible(false);
    retryButton.setVisible(false);
    pourBtn.setVisible(false);
    continueBtn.setVisible(true);
    winLabel.setVisible(true);
    // setting game states
    GameManager.setCurrentObjective(Objectives.SELECT_VAULT_DOOR);
    GameManager.increaseMoneyToGain(5000000);
    GameManager.setMoneyGained();
  }

  /**
   * Handles the event when the "Retry" button is clicked to reset the challenge.
   *
   * @param event The ActionEvent triggered by clicking the "Retry" button.
   */
  @FXML
  private void onRetryButtonClicked(MouseEvent event) {
    // Reset all necessary variables and elements
    sliderAnimation.pause();
    emptyVial.setVisible(false);
    selectVialLabel.setVisible(true);
    pourBtn.setDisable(true);
    pourBtn.setOpacity(0.5);
    largeVialBlue.setVisible(false);
    largeVialGreen.setVisible(false);
    largeVialRed.setVisible(false);
    largeVialYellow.setVisible(false);
    pourCount = 0;
    firstPour.setVisible(false); // Hide pour rectangles
    secondPour.setVisible(false);
    thirdPour.setVisible(false);
    fourthPour.setVisible(false);
    // Reset all the viles
    yellowVial.setVisible(true);
    redVial.setVisible(true);
    blueVial.setVisible(true);
    greenVial.setVisible(true);
    // reset disability
    yellowVial.setDisable(false);
    redVial.setDisable(false);
    blueVial.setDisable(false);
    greenVial.setDisable(false);
    // Clear the vialColour
    vialColour = null;
    retryButton.setDisable(true);
  }

  /** stops or resumes the slider animation when the "Stop" button is pressed. */
  @FXML
  private void onStopSlider() {
    // stopping slider animation
    if (sliderAnimation != null && sliderAnimation.getStatus() == Timeline.Status.RUNNING) {
      sliderAnimation.pause();
    } else {
      sliderAnimation.play();
    }
  }

  /**
   * Set the gradient color for the slider based on the chemical's part number.
   *
   * @param partNumber The part number representing the chemical.
   */
  @FXML
  public void setSliderGraident(int partNumber) {
    // setting the style for the slider
    Node track = slider.lookup(".track");
    String style = "-fx-background-color: linear-gradient(to right, ";

    // setting the style for the slider
    switch (partNumber) {
      case 1:
        // if part number is 1
        style += "#15ed20 0%, #15ed20 20%, #f44c4c9d 33%, #f44c4c9d 100%)";
        break;
      case 2:
        // if part number is 2
        style +=
            "#f44c4c9d 0%, #f44c4c9d 20%, #15ed20 30%, #15ed20 45%, #f44c4c9d 57%, #f44c4c9d 100%)";
        break;
      case 3:
        // if part number is 3
        style +=
            "#f44c4c9d 0%, #f44c4c9d 40%, #15ed20 52%, #15ed20 70%, #f44c4c9d 83%, #f44c4c9d 100%)";
        break;
      case 4:
        // if part number is 4
        style += "#f44c4c9d 0%, #f44c4c9d 67%, #15ed20 83%, #15ed20 100%)";
        break;
      default:
        // for default case
        style += "#f44c4c9d 0%, #f44c4c9d 100%)";
        break;
    }

    track.setStyle(style);
  }

  /**
   * Initializes the chemical recipe by generating random amounts of chemicals. Displays the initial
   * amounts on the labels.
   */
  private void initializeRecipe() {
    // initialising the random amounts of chemicals
    randomYellow = RandomnessGenerate.getRandomChemicalAmount();
    randomRed = RandomnessGenerate.getRandomChemicalAmount();
    randomBlue = RandomnessGenerate.getRandomChemicalAmount();
    randomGreen = RandomnessGenerate.getRandomChemicalAmount();
    // setting the text for the viles
    labelOne.setText("Yellow: " + randomYellow);
    labelTwo.setText("Red: " + randomRed);
    labelThree.setText("Green: " + randomGreen);
    labelFour.setText("Blue: " + randomBlue);
  }
}
