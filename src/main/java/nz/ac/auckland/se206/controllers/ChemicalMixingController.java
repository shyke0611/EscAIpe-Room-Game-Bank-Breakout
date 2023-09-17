package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class ChemicalMixingController extends Controller {

  @FXML private Slider slider;

  @FXML private Button stopButton;

  private Timeline sliderTimeline;
  private Timeline sliderAnimation;
  private boolean sliderMoving = false;

  public void initialize() {
    SceneManager.setController(Scenes.CHEMICALMIXING, this);

    sliderAnimation =
        new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(slider.valueProperty(), 0)),
            new KeyFrame(
                Duration.seconds(3), new KeyValue(slider.valueProperty(), slider.getMax())));

    sliderAnimation.setAutoReverse(true); // Enable auto-reverse
    sliderAnimation.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely

    // Start the animation
    sliderAnimation.play();

    // Create a timeline for automatically moving the slider
    // sliderTimeline = new Timeline(new KeyFrame(Duration.millis(100), event -> moveSliderUp()));
    // sliderTimeline.setCycleCount(Timeline.INDEFINITE);
    // sliderTimeline.setAutoReverse(true);
    // sliderTimeline.play();
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
    stopButton.setText(sliderMoving ? "Stop" : "Resume");
  }
}
