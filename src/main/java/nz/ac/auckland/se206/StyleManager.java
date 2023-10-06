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

  public enum HoverColour {
    RED,
    GREEN,
    ORANGE,
  }

  private static StyleManager instance = new StyleManager();
  private static Map<Node, Tooltip> tooltipMap = new HashMap<>();
  private static List<Node> itemsList = new ArrayList<>();

  public static StyleManager getInstance() {
    return instance;
  }

  public static void reset() {
    tooltipMap.clear();
    itemsList.clear();
  }

  public static void setAlarm(boolean on) {
    if (on) {
      App.textToSpeech("Security risk found, triggering alarm");
    }

    // Create a set of items to include
    Set<String> includeIds =
        new HashSet<>(
            Arrays.asList(
                "guard",
                "drawerHolder",
                "drawer",
                "credentialsNote",
                "computer",
                "doorHolder",
                "guardeyes",
                "key1",
                "key3",
                "key4"));

    // Go through all items
    for (Node item : itemsList) {
      if (item == null) {
        continue; // Skip null items
      }
      String itemId = item.getId();
      System.out.println("Item ID: " + itemId);

      if (itemId != null) {
        // If the item is in the set, disable it
        if (includeIds.contains(itemId)) {
          item.setDisable(true);
          item.setStyle(null);
        }
        // If the item is the background, animate it
        if (itemId.endsWith("background")) {
          AnimationManager.toggleAlarmAnimation(item, on, 0.7);
        }
      }
    }
  }

  private StyleManager() {}

  public void setItemsMessage(String message, String... items) {
    // Go through each item
    for (String item : items) {
      Node node = getItem(item);
      Tooltip tooltip = tooltipMap.get(node);
      // if the tool tip is null then create a new one
      if (tooltip == null) {
        tooltip = new Tooltip();
        tooltipMap.put(node, tooltip);
      }
      // set the message and install the tooltip
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
    // Get the rgba value for the given colour
    String rgba = getRgbaForHoverColour(colour);
    // For each item add a hover glow effect
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
    // return the rgba value for the given colour
    switch (colour) {
      case RED:
        // Red hover colour
        return "rgba(255, 0, 0, 0.7)";
      case GREEN:
        // Green hover colour
        return "rgba(34, 255, 0, 0.7)";
      case ORANGE:
        // Orange hover colour
        return "rgba(255, 183, 0, 0.7)";
      default:
        return null;
    }
  }

  // adds Items into arraylist
  public void addItems(Node... items) {
    itemsList.addAll(List.of(items));
  }

  public void setClueHover(String item, boolean isOn) {
    for (Node node : itemsList) {
      if (node.getId().equals(item)) {
        AnimationManager.toggleHoverAnimation(node, isOn, 1);
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
