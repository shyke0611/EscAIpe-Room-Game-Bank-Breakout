package nz.ac.auckland.se206;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.effect.InnerShadow;
import javafx.util.Duration;

public class AnimationManager {
  private static boolean isShadowOn = false;


  public static void fadeTransition(Node node, int seconds) {
    if (!GameState.isKeyFound) {
      FadeTransition fadeIn = new FadeTransition(Duration.seconds(seconds), node);
      fadeIn.setFromValue(0.0);
      fadeIn.setToValue(1.0);
      fadeIn.play();
    }
  }

  public static void toggleAlarmAnimation(Node node) {
    Duration duration = Duration.seconds(0.5);

    KeyFrame keyFrame =
        new KeyFrame(
            duration,
            event -> {
              if (isShadowOn) {
                node.setStyle(null); 
              } else {
                node.setStyle(
                  "-fx-effect: innershadow(gaussian, red, 200, 0, 0, 0)");
              }
              isShadowOn = !isShadowOn; 
            });

    Timeline timeline = new Timeline(keyFrame);
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }
}
