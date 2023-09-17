package nz.ac.auckland.se206;


import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationManager {

    // shoing animation of key moving up
  public static void fadeTransition(Node node, int seconds) {
    if (!GameState.isKeyFound) {
        // Disable mouse click events on the node
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(seconds), node);
        fadeIn.setFromValue(0.0); // Start from fully transparent
        fadeIn.setToValue(1.0);   // Fade in to fully opaque
        // Play the FadeTransition
        fadeIn.play();
    }
}
}
