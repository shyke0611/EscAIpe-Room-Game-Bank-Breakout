package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationManager {
  private static boolean isShadowOn = false;
  private static boolean isSlideAnimationPlayed = false;
  private static List<Timeline> timelineList = new ArrayList<>();

  public static void fadeTransition(Node node, int seconds) {
    FadeTransition fadeIn = new FadeTransition(Duration.seconds(seconds), node);
    fadeIn.setFromValue(0.0);
    fadeIn.setToValue(1.0);
    fadeIn.play();
  }

  public static void toggleAlarmAnimation(Node node, boolean isOn) {
    if (isOn) {
      startAlarmAnimation(node);
    } else {
      for (Timeline timeline : timelineList) {
        timeline.stop();
        node.setStyle(null);
      }
    }
  }

  public static void startAlarmAnimation(Node node) {
    Duration duration = Duration.seconds(0.5);

    KeyFrame keyFrame =
        new KeyFrame(
            duration,
            event -> {
              if (isShadowOn) {
                node.setStyle(null);
              } else {
                node.setStyle("-fx-effect: innershadow(gaussian, red, 200, 0, 0, 0)");
              }
              isShadowOn = !isShadowOn;
            });

    Timeline alarmTimeline = new Timeline(keyFrame);
    alarmTimeline.setCycleCount(Timeline.INDEFINITE);
    alarmTimeline.play();

    timelineList.add(alarmTimeline);
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

  public static void startBombAnimation(Node node) {
    Duration duration = Duration.seconds(0.2);

    KeyFrame keyFrame =
        new KeyFrame(
            duration,
            event -> {
              if (isShadowOn) {
                node.setStyle(null);
              } else {
                node.setStyle("-fx-effect: innershadow(gaussian, red, 200, 0, 0, 0)");
              }
              isShadowOn = !isShadowOn;
            });

    Timeline alarmTimeline = new Timeline(keyFrame);
    alarmTimeline.setCycleCount(Timeline.INDEFINITE);
    alarmTimeline.play();
  }

  public static void delayAnimation(Node node, Node node1) {
    Duration delay = Duration.seconds(3);
    Timeline timeline = new Timeline(new KeyFrame(delay, event -> {
      node1.setVisible(true);
      node.setVisible(false);
      
  }));

    timeline.play();
  }
}
