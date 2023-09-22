package nz.ac.auckland.se206;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class AnimationManager {
  private static boolean isSlideAnimationPlayed = false;
  private ScaleTransition scaleTransitionGun;

  public static void fadeTransition(Node node, int seconds) {
    FadeTransition fadeIn = new FadeTransition(Duration.seconds(seconds), node);
    fadeIn.setFromValue(0.0);
    fadeIn.setToValue(1.0);
    fadeIn.play();
  }

  public static void toggleAlarmAnimation(Node node, boolean isOn, double time) {
    Duration duration = Duration.seconds(time);

    if (isOn) {
      InnerShadow innerShadow = new InnerShadow();
      innerShadow.setColor(Color.RED);

      // Create a Lighting effect
      Lighting lighting = new Lighting();
      lighting.setDiffuseConstant(0.4);
      lighting.setSpecularConstant(0.1);
      lighting.setSpecularExponent(3.0);

      innerShadow.setInput(lighting);
      node.setEffect(innerShadow);

      Timeline animation =
          new Timeline(
              new KeyFrame(Duration.ZERO, new KeyValue(innerShadow.radiusProperty(), 0)),
              new KeyFrame(duration, new KeyValue(innerShadow.radiusProperty(), 200)));
      animation.setCycleCount(Timeline.INDEFINITE);
      animation.play();
    } else {
      node.setEffect(null);
    }
  }

  public static void toggleHoverAnimation(Node node, boolean isOn, double time) {
    Duration duration = Duration.seconds(time);

    if (isOn) {
      DropShadow dropShadow = new DropShadow();
      dropShadow.setColor(Color.rgb(34, 255, 0, 0.7));
      dropShadow.setBlurType(BlurType.GAUSSIAN);

      node.setEffect(dropShadow);

      Timeline animation =
          new Timeline(
              new KeyFrame(Duration.ZERO, new KeyValue(dropShadow.spreadProperty(), 0)),
              new KeyFrame(duration, new KeyValue(dropShadow.spreadProperty(), 2)));
      animation.setCycleCount(Timeline.INDEFINITE);
      animation.play();
    } else {
      node.setEffect(null);
    }
  }

  public static void slideDoorsAnimation(Node node) {
    if (!isSlideAnimationPlayed) {
      TranslateTransition transition = new TranslateTransition();
      transition.setDuration(Duration.seconds(2));
      transition.setNode(node);
      transition.setByX(-1052);
      transition.play();

      transition.setOnFinished(
          event -> {
            isSlideAnimationPlayed = true; // Animation has finished
          });
      transition.play();
    }
  }

  public static void delayAnimation(Node node, Node node1) {
    Duration delay = Duration.seconds(3);
    Timeline timeline =
        new Timeline(
            new KeyFrame(
                delay,
                event -> {
                  node1.setVisible(true);
                  node.setVisible(false);
                }));

    timeline.play();
  }

  public static ScaleTransition createScaleTransition(Node node) {
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), node);
    scaleTransition.setFromX(1.0); // Initial scale X
    scaleTransition.setFromY(1.0); // Initial scale Y
    scaleTransition.setToX(1.1); // Enlarged scale X
    scaleTransition.setToY(1.1); // Enlarged scale Y
    return scaleTransition;
  }

  public static void playAnimationForward(ScaleTransition scaleTransition) {
    scaleTransition.setRate(1); // Play forward
    scaleTransition.play();
  }

  public static void playAnimationReverse(ScaleTransition scaleTransition) {
    scaleTransition.setRate(-1); // Play in reverse
    scaleTransition.play();
  }

  public static void reset() {
    isSlideAnimationPlayed = false;
  }
}
