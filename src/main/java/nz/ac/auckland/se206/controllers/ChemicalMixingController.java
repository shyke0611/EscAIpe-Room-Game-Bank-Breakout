package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.RandomnessGenerate;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;

/** Controller class for the Chemical Mixing scene. */
public class ChemicalMixingController extends Controller {

  @FXML private Label timerLabel;
  @FXML private Slider slider;
  @FXML private Button pourBtn;
  @FXML private Button retryButton;
  @FXML private Button continueBtn;
  @FXML private ImageView greenVial;
  @FXML private ImageView redVial;
  @FXML private ImageView blueVial;
  @FXML private ImageView yellowVial;
  @FXML private ImageView largeVialBlue;
  @FXML private ImageView largeVialGreen;
  @FXML private ImageView largeVialRed;
  @FXML private ImageView largeVialYellow;

  @FXML private Label labelOne;
  @FXML private Label labelTwo;
  @FXML private Label labelThree;
  @FXML private Label labelFour;
  @FXML private Label currentVial;
  @FXML private Label winLabel;

  @FXML private Rectangle firstPour;
  @FXML private Rectangle secondPour;
  @FXML private Rectangle thirdPour;
  @FXML private Rectangle fourthPour;
  @FXML private Rectangle whiteRectangle;
  @FXML private Region ChemicalLiquid;

  private String randomYellow;
  private String randomRed;
  private String randomBlue;
  private String randomGreen;
  private String vileColour;

  private int pourCount;

  private StyleManager styleManager = StyleManager.getInstance();

  private Timeline sliderAnimation;
  private boolean sliderMoving = false;

  /**
   * Initializes the Chemical Mixing controller and sets up the initial state of the scene. It also
   * sets up hover animations for vile images and initializes the chemical recipe.
   */
  public void initialize() {
    SceneManager.setController(Scenes.CHEMICALMIXING, this);
    super.setTimerLabel(timerLabel, 3);

    // Setting up hover animations
    setUpHover();

    // Intialsing recipe and saving for later reference
    initializeRecipe();

    pourBtn.setDisable(true);
    retryButton.setDisable(true);

    pourCount = 0;

    sliderAnimation =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1), new KeyValue(slider.valueProperty(), slider.getMax())));

    sliderAnimation.setAutoReverse(true); // Enable auto-reverse
    sliderAnimation.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
  }

  private void setUpHover() {
    // Setting up hover animations
    styleManager.addHoverItems(yellowVial, redVial, blueVial, greenVial);
    StyleManager.setItemsHoverColour(
        StyleManager.HoverColour.GREEN, "yellowVial", "redVial", "blueVial", "greenVial");
  }

  @FXML
  public void onVialClicked(MouseEvent event) {
    ImageView image = (ImageView) event.getSource();
    String id = image.getId();

    switch (id) {
      case "yellowVial":
        largeVialYellow.setVisible(true);
        largeVialBlue.setVisible(false);
        largeVialRed.setVisible(false);
        largeVialGreen.setVisible(false);
        setSliderGraident(Integer.parseInt(randomYellow));
        break;
      case "redVial":
        largeVialRed.setVisible(true);
        largeVialBlue.setVisible(false);
        largeVialYellow.setVisible(false);
        largeVialGreen.setVisible(false);
        setSliderGraident(Integer.parseInt(randomRed));
        break;
      case "blueVial":
        largeVialBlue.setVisible(true);
        largeVialGreen.setVisible(false);
        largeVialRed.setVisible(false);
        largeVialYellow.setVisible(false);
        setSliderGraident(Integer.parseInt(randomBlue));
        break;
      case "greenVial":
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

    ImageView image = (ImageView) event.getSource();
    image.setStyle("-fx-cursor: hand;");

    // Resume the slider animation
    if (!sliderMoving) {
      sliderAnimation.play();
      sliderMoving = true;
    }

    // Setting the current vile label and saving current clicked colour
    // this is for yellow vile
    if (image.getId().equals("yellowVial")) {
      currentVial.setText("Yellow Vial");
      vileColour = "yellow";
      // this is for red vile
    } else if (image.getId().equals("redVial")) {
      currentVial.setText("Red Vial");
      vileColour = "red";
      // this is for blue vile
    } else if (image.getId().equals("blueVial")) {
      currentVial.setText("Blue Vial");
      vileColour = "blue";
      // this is for #15ed20 vile
    } else if (image.getId().equals("greenVial")) {
      currentVial.setText("Green Vial");
      vileColour = "#15ed20";
    }

    sliderAnimation.play();
    pourBtn.setDisable(false);

    // Pour button was clicked
    pourBtn.setOnAction(
        e -> {
          onStopSlider();
          pourBtn.setDisable(true);
          currentVial.setText("Pick a vial");

          int value = (int) Math.floor(slider.getValue());

          // Ugly code to determine if user was correct, please help refactor if you know a better
          // wayy
          // filling beaker for yellow
          if (vileColour == "yellow" && value == Integer.parseInt(randomYellow)) {
            value = 1;
            pourCount++;
            fillBeaker(value, pourCount);
            yellowVial.setDisable(true);
            yellowVial.setOpacity(0.5);
            // filling beaker for red
          } else if (vileColour == "red" && value == Integer.parseInt(randomRed)) {
            value = 2;
            pourCount++;
            fillBeaker(value, pourCount);
            redVial.setDisable(true);
            redVial.setOpacity(0.5);
            // filling beaker for blue
          } else if (vileColour == "blue" && value == Integer.parseInt(randomBlue)) {
            value = 3;
            pourCount++;
            fillBeaker(value, pourCount);
            blueVial.setDisable(true);
            blueVial.setOpacity(0.5);
            // filling beaker for #15ed20
          } else if (vileColour == "#15ed20" && value == Integer.parseInt(randomGreen)) {
            value = 4;
            pourCount++;
            fillBeaker(value, pourCount);
            greenVial.setDisable(true);
            greenVial.setOpacity(0.5);
          } else {
            // setting properties to the viles and buttons
            retryButton.setDisable(false);
            yellowVial.setDisable(true);
            redVial.setDisable(true);
            blueVial.setDisable(true);
            greenVial.setDisable(true);
            yellowVial.setOpacity(0.5);
            redVial.setOpacity(0.5);
            blueVial.setOpacity(0.5);
            greenVial.setOpacity(0.5);
          }
        });
  }

  /**
   * Fills the beaker with the poured chemical and checks if the combination is correct.
   *
   * @param value The value representing the poured chemical.
   * @param pourCount The number of times chemicals have been poured.
   */
  private void fillBeaker(int value, int pourCount) {

    // More ugly code to determine which rectangle to fill and what colour

    Paint currentColour;
    // setting paint colour ro the rectangles in beaker
    if (value == 1) {
      currentColour = Paint.valueOf("#ffd45e");
    } else if (value == 2) {
      currentColour = Paint.valueOf("#f27377");
    } else if (value == 3) {
      currentColour = Paint.valueOf("#21b1ff");
    } else {
      currentColour = Paint.valueOf("#34f86f");
    }

    // setting paint colour for the first vile selected
    if (pourCount == 1) {
      firstPour.setVisible(true);
      firstPour.setFill(currentColour);
      whiteRectangle.setVisible(true);
      // setting paint colour for the second vile selected
    } else if (pourCount == 2) {
      secondPour.setVisible(true);
      secondPour.setFill(currentColour);
      // setting paint colour for the third vile selected
    } else if (pourCount == 3) {
      thirdPour.setVisible(true);
      thirdPour.setFill(currentColour);
      // setting paint colour for the last vile selected
    } else if (pourCount == 4) {
      fourthPour.setVisible(true);
      fourthPour.setFill(currentColour);
      checkWin();
      GameState.isChemicalMixingBypassed = true;
    }
  }

  @FXML
  public void Pour(ActionEvent event) {
    ChemicalLiquid.setPrefHeight(ChemicalLiquid.getPrefHeight() + 50);
  }

  /**
   * Checks if the player has successfully completed the chemical mixing challenge. If successful,
   * it reveals the "Continue" button and win message, and increases the money to gain.
   */
  private void checkWin() {
    // set visibility
    retryButton.setVisible(false);
    pourBtn.setVisible(false);
    continueBtn.setVisible(true);
    winLabel.setVisible(true);
    GameManager.completeObjective();
    // $5 Million
    GameManager.increaseMoneyToGain(5000000);
  }

  /**
   * Handles the event when the "Retry" button is clicked to reset the challenge.
   *
   * @param event The ActionEvent triggered by clicking the "Retry" button.
   */
  @FXML
  private void onRetryButtonClicked(ActionEvent event) {
    // Reset all necessary variables and elements
    currentVial.setText("");
    pourBtn.setDisable(true);
    pourCount = 0;
    firstPour.setVisible(false); // Hide pour rectangles
    secondPour.setVisible(false);
    thirdPour.setVisible(false);
    fourthPour.setVisible(false);
    // Reset all the viles
    yellowVial.setDisable(false);
    yellowVial.setOpacity(1);
    redVial.setDisable(false);
    redVial.setOpacity(1);
    blueVial.setDisable(false);
    blueVial.setOpacity(1);
    greenVial.setDisable(false);
    greenVial.setOpacity(1);
    // Clear the vileColour
    vileColour = null;
    retryButton.setDisable(true);
    currentVial.setText("Pick a vial");
  }

  /** stops or resumes the slider animation when the "Stop" button is pressed. */
  @FXML
  private void onStopSlider() {
    // stopping slider animation
    if (sliderAnimation != null && sliderAnimation.getStatus() == Timeline.Status.RUNNING) {
      sliderAnimation.pause();
      sliderMoving = false;
    } else {
      sliderAnimation.play();
      sliderMoving = true;
    }
  }

  @FXML
  public void setSliderGraident(int partNumber) {
    Node track = slider.lookup(".track");
    String style = "-fx-background-color: linear-gradient(to right, ";

    switch (partNumber) {
      case 1:
        style += "#15ed20 0%, #15ed20 20%, red 33%, red 100%)";
        break;
      case 2:
        style += "red 0%, red 20%, #15ed20 30%, #15ed20 45%, red 57%, red 100%)";
        break;
      case 3:
        style += "red 0%, red 40%, #15ed20 52%, #15ed20 70%, red 83%, red 100%)";
        break;
      case 4:
        style += "red 0%, red 67%, #15ed20 83%, #15ed20 100%)";
        break;
      default:
        style += "red 0%, red 100%)";
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
    labelThree.setText("Blue: " + randomBlue);
    labelFour.setText("Green: " + randomGreen);
  }
}
