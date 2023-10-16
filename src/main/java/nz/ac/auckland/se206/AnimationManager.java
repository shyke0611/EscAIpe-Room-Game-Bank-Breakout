package nz.ac.auckland.se206;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class AnimationManager {

  public static void fadeTransition(Node node, double seconds,double from,double to) {
    FadeTransition fadeIn = new FadeTransition(Duration.seconds(seconds), node);
    fadeIn.setFromValue(from);
    fadeIn.setToValue(to);
    fadeIn.play();
  }

  public static void toggleAlarmAnimation(Node node, boolean isOn, double time) {
    Duration duration = Duration.seconds(time);

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

    if (isOn) {
      animation.play();
    } else {
      node.setEffect(null);
      animation.stop();
    }
  }

  public static void toggleHoverAnimation(Node node, boolean isOn, double time) {
    Duration duration = Duration.seconds(time);

    if (isOn) {
      // Create a DropShadow effect
      DropShadow dropShadow = new DropShadow();
      dropShadow.setColor(Color.rgb(34, 255, 0, 0.7));
      dropShadow.setBlurType(BlurType.GAUSSIAN);

      node.setEffect(dropShadow);

      // Create a Timeline animation
      Timeline animation =
          new Timeline(
              new KeyFrame(Duration.ZERO, new KeyValue(dropShadow.spreadProperty(), 0)),
              new KeyFrame(duration, new KeyValue(dropShadow.spreadProperty(), 1)));
      animation.setCycleCount(Timeline.INDEFINITE);
      animation.play();
    } else {
      // Remove the effect
      node.setEffect(null);
    }
  }

  public static void delayAnimation(Node node, Node node1) {
    // Delay the animation for 3 seconds
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

  public static void roomSwitchAnimation(Duration duration, Runnable... actions) {
    PauseTransition pause = new PauseTransition(duration);
    pause.setOnFinished(e -> {
        for (Runnable action : actions) {
            action.run();
        }
    });
    pause.play();
}

}
