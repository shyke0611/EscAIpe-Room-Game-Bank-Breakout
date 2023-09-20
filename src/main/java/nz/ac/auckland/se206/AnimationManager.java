package nz.ac.auckland.se206;

import java.util.HashSet;
import java.util.Set;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.util.Duration;

public class AnimationManager {
  private static boolean isShadowOn = false;
  private static boolean isSlideAnimationPlayed = false;
  private static Set<Timeline> timelineList = new HashSet<>();
  private static Set<Node> nodesWithActiveAnimation = new HashSet<>();

  public enum Type {
    DropShadow,
    InnerShadow
  }

  public static void fadeTransition(Node node, int seconds) {
    FadeTransition fadeIn = new FadeTransition(Duration.seconds(seconds), node);
    fadeIn.setFromValue(0.0);
    fadeIn.setToValue(1.0);
    fadeIn.play();
  }

  public static void toggleAlarmAnimation(Node node, boolean isOn, double time, Type effect) {
    if (isOn && !nodesWithActiveAnimation.contains(node)) {
      startAlarmAnimation(node, time, effect);
      nodesWithActiveAnimation.add(node);
    } else if (!isOn && nodesWithActiveAnimation.contains(node)) {
      for (Timeline timeline : timelineList) {
        timeline.stop();
      }
      node.setStyle(null);
      // nodesWithActiveAnimation.remove(node);
    }
  }

  public static void startAlarmAnimation(Node node, double time, Type effect) {
    Duration duration = Duration.seconds(time);

    KeyFrame keyFrame =
        new KeyFrame(
            duration,
            event -> {
              if (isShadowOn) {
                node.setStyle(null);
              } else {
                if (effect == Type.InnerShadow) {
                  node.setStyle("-fx-effect: innershadow(gaussian, red, 200, 0, 0, 0)");
                 

                } else if (effect == Type.DropShadow) {
                  node.setStyle(
                      "-fx-effect: dropshadow(gaussian, rgba(34, 255, 0, 0.7), 10, 10, 0, 0)");
                }
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
}
