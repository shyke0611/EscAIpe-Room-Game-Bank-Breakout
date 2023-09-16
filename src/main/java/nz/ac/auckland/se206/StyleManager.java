package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.layout.HBox;

public class StyleManager {

    public static void setItemsState(int r, int g, int b, HBox... items) {
        if (GameState.isGuardDistracted) {
            String rgbaColor = "rgba(" + r + "," + g + "," + b + ",0.7)";
            for (HBox item : items) {
                item.setOnMouseEntered(event -> {
                    item.setStyle("-fx-effect: dropshadow(gaussian, " + rgbaColor + ", 5, 5, 0, 0); -fx-cursor: hand;");
                });
                item.setOnMouseExited(event -> {
                    item.setStyle("");
                });
            }
        }
    }
    




    
}
