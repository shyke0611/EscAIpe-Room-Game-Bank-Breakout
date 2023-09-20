package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.RandomnessGenerate;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;

public class ChemicalMixingController extends Controller {

  @FXML private Slider slider;
  @FXML private Button stopButton;
  @FXML private Button retryButton;
  @FXML private Button continueBtn;
  @FXML private ImageView greenVile;
  @FXML private ImageView redVile;
  @FXML private ImageView blueVile;
  @FXML private ImageView yellowVile;

  @FXML private Label redParts;
  @FXML private Label blueParts;
  @FXML private Label yellowParts;
  @FXML private Label greenParts;
  @FXML private Label currentVile;
  @FXML private Label winLabel;

  @FXML private Rectangle firstPour;
  @FXML private Rectangle secondPour;
  @FXML private Rectangle thirdPour;
  @FXML private Rectangle fourthPour;
  @FXML private Rectangle whiteRectangle;

  private String randomYellow;
  private String randomRed;
  private String randomBlue;
  private String randomGreen;
  private String vileColour;

  private int pourCount;

  private Timeline sliderAnimation;
  private ScaleTransition scaleTransitionGreen;
  private ScaleTransition scaleTransitionYellow;
  private ScaleTransition scaleTransitionRed;
  private ScaleTransition scaleTransitionBlue;
  private boolean sliderMoving = false;

  StyleManager styleManager = StyleManager.getInstance();

  public void initialize() {
    SceneManager.setController(Scenes.CHEMICALMIXING, this);

    // Setting up hover animations
    setupListeners();

    // Intialsing recipe and saving for later reference
    initializeRecipe();

    // Setting up scene
    stopButton.setDisable(true);
    retryButton.setDisable(true);
    currentVile.setText("Pick a vile");
    pourCount = 0;

    // Creating slider animation
    sliderAnimation =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1), new KeyValue(slider.valueProperty(), slider.getMax())));

    sliderAnimation.setAutoReverse(true); // Enable auto-reverse
    sliderAnimation.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
  }

  @FXML
  public void pourChemical(MouseEvent event) {

    ImageView image = (ImageView) event.getSource();

    // Setting the current vile label and saving current clicked colour
    if (image.getId().equals("yellowVile")) {
      currentVile.setText("Yellow Vile");
      vileColour = "yellow";
    } else if (image.getId().equals("redVile")) {
      currentVile.setText("Red Vile");
      vileColour = "red";
    } else if (image.getId().equals("blueVile")) {
      currentVile.setText("Blue Vile");
      vileColour = "blue";
    } else if (image.getId().equals("greenVile")) {
      currentVile.setText("Green Vile");
      vileColour = "green";
    }

    sliderAnimation.play();
    stopButton.setDisable(false);

    // Pour button was clicked
    stopButton.setOnAction(
        e -> {
          stopSlider();
          stopButton.setDisable(true);
          currentVile.setText("Pick a vile");

          int value = (int) Math.floor(slider.getValue());

          // Ugly code to determine if user was correct, please help refactor if you know a better
          // wayy
          if (vileColour == "yellow" && value == Integer.parseInt(randomYellow)) {
            value = 1;
            pourCount++;
            fillBeaker(value, pourCount);
            yellowVile.setDisable(true);
            yellowVile.setOpacity(0.5);
          } else if (vileColour == "red" && value == Integer.parseInt(randomRed)) {
            value = 2;
            pourCount++;
            fillBeaker(value, pourCount);
            redVile.setDisable(true);
            redVile.setOpacity(0.5);
          } else if (vileColour == "blue" && value == Integer.parseInt(randomBlue)) {
            value = 3;
            pourCount++;
            fillBeaker(value, pourCount);
            blueVile.setDisable(true);
            blueVile.setOpacity(0.5);
          } else if (vileColour == "green" && value == Integer.parseInt(randomGreen)) {
            value = 4;
            pourCount++;
            fillBeaker(value, pourCount);
            greenVile.setDisable(true);
            greenVile.setOpacity(0.5);
          } else {
            retryButton.setDisable(false);
            yellowVile.setDisable(true);
            redVile.setDisable(true);
            blueVile.setDisable(true);
            greenVile.setDisable(true);
            yellowVile.setOpacity(0.5);
            redVile.setOpacity(0.5);
            blueVile.setOpacity(0.5);
            greenVile.setOpacity(0.5);
          }
        });
  }

  @FXML
  public void fillBeaker(int value, int pourCount) {

    // More ugly code to determine which rectangle to fill and what colour
    Paint currentColour;
    if (value == 1) {
      currentColour = Paint.valueOf("#ffd45e");
    } else if (value == 2) {
      currentColour = Paint.valueOf("#f27377");
    } else if (value == 3) {
      currentColour = Paint.valueOf("#21b1ff");
    } else {
      currentColour = Paint.valueOf("#34f86f");
    }

    if (pourCount == 1) {
      firstPour.setVisible(true);
      firstPour.setFill(currentColour);
      whiteRectangle.setVisible(true);
    } else if (pourCount == 2) {
      secondPour.setVisible(true);
      secondPour.setFill(currentColour);
    } else if (pourCount == 3) {
      thirdPour.setVisible(true);
      thirdPour.setFill(currentColour);
    } else if (pourCount == 4) {
      fourthPour.setVisible(true);
      fourthPour.setFill(currentColour);
      checkWin();
      GameState.isChemicalMixingBypassed = true;
    }
  }

  @FXML
  public void checkWin() {
    retryButton.setVisible(false);
    stopButton.setVisible(false);

    continueBtn.setVisible(true);
    winLabel.setVisible(true);
  }

  // @FXML
  // public void setVault() {
  //   if (GameState.isChemicalMixingBypassed) {
  //     styleManager.getItem("goldDoor").setVisible(false);
  //     styleManager.getItem("lootBtnHolder").setVisible(true);
  //     GameState.isAnyDoorOpen = true;
  //   }
  //   App.setUI(Scenes.VAULT);
  // }

  @FXML
  public void retryButtonClicked() {
    // Reset all necessary variables and elements
    slider.setValue(0);
    currentVile.setText("");
    stopButton.setDisable(true);
    pourCount = 0;
    firstPour.setVisible(false); // Hide pour rectangles
    secondPour.setVisible(false);
    thirdPour.setVisible(false);
    fourthPour.setVisible(false);

    // Reset all the viles
    yellowVile.setDisable(false);
    yellowVile.setOpacity(1);
    redVile.setDisable(false);
    redVile.setOpacity(1);
    blueVile.setDisable(false);
    blueVile.setOpacity(1);
    greenVile.setDisable(false);
    greenVile.setOpacity(1);

    // Clear the vileColour
    vileColour = null;
    retryButton.setDisable(true);
    currentVile.setText("Pick a vile");

    // Resume the slider animation
    if (!sliderMoving) {
      sliderAnimation.play();
      sliderMoving = true;
    }
  }

  @FXML
  private void stopSlider() {
    if (sliderAnimation != null && sliderAnimation.getStatus() == Timeline.Status.RUNNING) {
      sliderAnimation.pause();
      sliderMoving = false;
    } else {
      sliderAnimation.play();
      sliderMoving = true;
    }
  }

  private ScaleTransition createScaleTransition(ImageView imageView) {
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), imageView);
    scaleTransition.setFromX(1.0); // Initial scale X
    scaleTransition.setFromY(1.0); // Initial scale Y
    scaleTransition.setToX(1.2); // Enlarged scale X
    scaleTransition.setToY(1.2); // Enlarged scale Y
    return scaleTransition;
  }

  private void playAnimationForward(ScaleTransition scaleTransition) {
    scaleTransition.setRate(1); // Play forward
    scaleTransition.play();
  }

  private void playAnimationReverse(ScaleTransition scaleTransition) {
    scaleTransition.setRate(-1); // Play in reverse
    scaleTransition.play();
  }

  private void setupListeners() {
    scaleTransitionGreen = createScaleTransition(greenVile);
    scaleTransitionYellow = createScaleTransition(yellowVile);
    scaleTransitionRed = createScaleTransition(redVile);
    scaleTransitionBlue = createScaleTransition(blueVile);

    // Add hover listeners to start and stop the animation
    greenVile.setOnMouseEntered(event -> playAnimationForward(scaleTransitionGreen));
    greenVile.setOnMouseExited(event -> playAnimationReverse(scaleTransitionGreen));
    redVile.setOnMouseEntered(event -> playAnimationForward(scaleTransitionRed));
    redVile.setOnMouseExited(event -> playAnimationReverse(scaleTransitionRed));
    yellowVile.setOnMouseEntered(event -> playAnimationForward(scaleTransitionYellow));
    yellowVile.setOnMouseExited(event -> playAnimationReverse(scaleTransitionYellow));
    blueVile.setOnMouseEntered(event -> playAnimationForward(scaleTransitionBlue));
    blueVile.setOnMouseExited(event -> playAnimationReverse(scaleTransitionBlue));
  }

  @FXML
  public void initializeRecipe() {
    RandomnessGenerate random = new RandomnessGenerate();
    randomYellow = random.getRandomChemialAmount();
    randomRed = random.getRandomChemialAmount();
    randomBlue = random.getRandomChemialAmount();
    randomGreen = random.getRandomChemialAmount();

    yellowParts.setText("Yellow: " + randomYellow);
    redParts.setText("Red: " + randomRed);
    blueParts.setText("Blue: " + randomBlue);
    greenParts.setText("Green: " + randomGreen);
  }
}
