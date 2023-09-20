package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class StyleManager {

  private static StyleManager instance = new StyleManager();
  private Map<Node, Tooltip> tooltipMap = new HashMap<>();
  private List<Node> itemsList = new ArrayList<>();

  public enum HoverColour {
    RED,
    GREEN,
    ORANGE,
  }

  private StyleManager() {}

  public static StyleManager getInstance() {
    return instance;
  }

  // Set and apply tooltips for multiple items
  public void setItemsMessage(String message, String... items) {
    for (String item : items) {
      Node node = getItem(item);
      Tooltip tooltip = tooltipMap.get(node);
      if (tooltip == null) {
        tooltip = new Tooltip();
        tooltipMap.put(node, tooltip);
      }
      tooltip.setText(message);
      Tooltip.install(node, tooltip);
      tooltip.setShowDelay(Duration.seconds(0));
    }
  }

  // Remove tooltips for multiple items
  public void removeItemsMessage(String... items) {
    for (String item : items) {
      Node node = getItem(item);
      Tooltip tooltip = tooltipMap.get(node);
      if (tooltip != null) {
        Tooltip.uninstall(node, tooltip);
        tooltipMap.remove(node);
      }
    }
  }

  public void setItemsState(HoverColour colour, String... items) {
    String rgba = getRgbaForHoverColour(colour);
    for (String item : items) {
      Node node = getItem(item);
      node.setOnMouseEntered(
          event ->
              node.setStyle(
                  "-fx-effect: dropshadow(gaussian, " + rgba + ", 5, 5, 0, 0); -fx-cursor: hand;"));
      node.setOnMouseExited(event -> node.setStyle(""));
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

  // adds Items into arraylist
  public void addItems(Node... items) {
    itemsList.addAll(List.of(items));
  }

  public void setAlarm(boolean on) {
    Set<String> excludeIDs =
        new HashSet<>(
            Arrays.asList(
                "electricityBox",
                "guardpocket",
                "redwire",
                "greenwire",
                "bluewire",
                "yellowwire",
                "walkietalkie",
                "switchHolder",
                "walkietalkieHolder","bombHolder","exitHolder","bombPuzzle","escapeDoor"));
    for (Node item : itemsList) {
      if (item == null) {
        continue; // Skip null items
      }
      String itemId = item.getId();
      System.out.println("Item ID: " + itemId);

      if (itemId != null) {
        if (!excludeIDs.contains(itemId)) {
          item.setDisable(true);
        }

        if (itemId.endsWith("background")) {
          AnimationManager.toggleAlarmAnimation(item, on);
        }
      }
    }
  }

  public void setDisable(boolean value, String... items) {
    for (String item : items) {
      getItem(item).setDisable(value);
    }
  }

  public void setVisible(boolean value, String... items) {
    for (String item : items) {
      getItem(item).setVisible(value);
    }
  }

  public Node getItem(String node) {
    for (Node item : itemsList) {
      if (item.getId().toString().equals(node)) {
        return item;
      }
    }
    return null;
  }

}
