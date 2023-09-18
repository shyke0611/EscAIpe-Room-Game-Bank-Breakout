package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class StyleManager {

  private static StyleManager instance = new StyleManager();
  private Map<HBox, Tooltip> tooltipMap = new HashMap<>();
  private Map<HBox, HoverColour> hoverStyleMap = new HashMap<>();


  public enum HoverColour {
    RED,
    GREEN,
    ORANGE,
  }

  public enum State {
    HOVER,
    CLICK,
  }

  private StyleManager() {}

  public static StyleManager getInstance() {
    return instance;
  }

  // Set and apply tooltips for multiple items
  public void setItemsMessage(String message, HBox... items) {
    for (HBox item : items) {
      Tooltip tooltip = tooltipMap.get(item);
      if (tooltip == null) {
        tooltip = new Tooltip();
        tooltipMap.put(item, tooltip);
      }
      tooltip.setText(message);
      Tooltip.install(item, tooltip);
      tooltip.setShowDelay(Duration.seconds(0));
    }
  }

  // Remove tooltips for multiple items
  public void removeItemsMessage(HBox... items) {
    for (HBox item : items) {
      Tooltip tooltip = tooltipMap.get(item);
      if (tooltip != null) {
        Tooltip.uninstall(item, tooltip);
        tooltipMap.remove(item);
      }
    }
  }

  public void setItemsState(HoverColour colour,State state, HBox... items) {
    String rgba = getRgbaForHoverColour(colour);

    for (HBox item : items) {
      HoverColour hover = hoverStyleMap.get(item);
      if (hover == null) {
        hoverStyleMap.put(item, colour);
      }

      item.setOnMouseEntered(
          event ->
              item.setStyle(
                  "-fx-effect: dropshadow(gaussian, " + rgba + ", 5, 5, 0, 0); -fx-cursor: hand;"));
      if (state == State.HOVER) {
      item.setOnMouseExited(event -> item.setStyle(""));
      } else {
        item.setOnMousePressed(event ->
              item.setStyle(
                  "-fx-effect: dropshadow(gaussian, " + rgba + ", 5, 5, 0, 0); -fx-cursor: hand;"));
      }
    }
  }

  private String getRgbaForHoverColour(HoverColour colour) {
    switch (colour) {
        case RED:
            return "rgba(255, 0, 0, 0.7)";
        case GREEN:
            return "rgba(34, 255, 0, 0.7)";
        case ORANGE:
            return "rgba(255, 183, 0, 0.7)";
        default:
            return null; 
    }
}

  // Remove hover state
  public void removeItemsHoverState(HBox... items) {
    for (HBox item : items) {
      hoverStyleMap.remove(item);
      item.setOnMouseEntered(null);
      item.setOnMouseExited(null);
      item.setStyle("");
    }
  }

}
