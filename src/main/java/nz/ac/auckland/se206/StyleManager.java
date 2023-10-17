package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Lighting;
import javafx.util.Duration;
import nz.ac.auckland.se206.gpt.ChatMessage;

/** Class to manage the styling of all elements. */
public class StyleManager {

  /** The different hover glow colours in the game. */
  public enum HoverColour {
    RED,
    GREEN,
    ORANGE,
  }

  private static StyleManager instance = new StyleManager();
  private static Map<Node, Tooltip> tooltipMap = new HashMap<>();
  private static List<Node> hoverItemsList = new ArrayList<>();

  /**
   * Get the instance of the StyleManager.
   *
   * @return - The instance of the StyleManager
   */
  public static StyleManager getInstance() {
    return instance;
  }

  /**
   * Get the node with the given id.
   *
   * @param node - The id of the node to get
   * @return - The node with the given id
   */
  public static Node getHoverItem(String node) {
    for (Node item : hoverItemsList) {
      if (item.getId().toString().equals(node)) {
        return item;
      }
    }
    return null;
  }

  /**
   * Gets the corresponding CSS class for a given HoverColour enum value.
   *
   * @param colour The HoverColour enum value for which to determine the CSS class.
   * @return The CSS class name associated with the provided HoverColour, or an empty string for
   *     unknown or default cases.
   */
  private static String getColourClassForHoverColour(HoverColour colour) {
    switch (colour) {
      // for red hover colour
      case RED:
        return "red";
      // for green hover colour
      case GREEN:
        return "green";
      // for orange hover colour
      case ORANGE:
        return "orange";
      // Handle default or unknown cases as needed
      default:
        return "";
    }
  }

  /**
   * Set the hover colour for multiple items.
   *
   * @param colour - The colour to set the hover effect to
   * @param items - The item ids for the items to set the hover effect to
   */
  public static void setItemsHoverColour(HoverColour colour, String... items) {
    String colourClass = getColourClassForHoverColour(colour);
    // For each item add a hover glow effect
    for (String item : items) {
      Node node = getHoverItem(item);
      // setting new style class
      node.getStyleClass().clear();
      node.getStyleClass().add(colourClass);
    }
  }

  /**
   * Set the disable value for multiple items.
   *
   * @param value - The value to set the disable property to
   * @param items - The item ids for the items to set the disable property to
   */
  public static void setDisable(boolean value, String... items) {
    for (String item : items) {
      getHoverItem(item).setDisable(value);
    }
  }

  /**
   * Set the visible value for multiple items.
   *
   * @param value - The value to set the visible property to
   * @param items - The item ids for the items to set the visible property to
   */
  public static void setVisible(boolean value, String... items) {
    for (String item : items) {
      getHoverItem(item).setVisible(value);
    }
  }

  /**
   * Set the tooltip message for multiple items.
   *
   * @param message - The message to set
   * @param items - The item ids for the items to set the message to
   */
  public static void setItemsMessage(String message, String... items) {
    // Go through each item
    for (String item : items) {
      Node node = getHoverItem(item);
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

  /**
   * Remove tooltip messages for multiple items.
   *
   * @param items - The item ids for the items to remove the tooltips from
   */
  public static void removeItemsMessage(String... items) {
    for (String item : items) {
      Node node = getHoverItem(item);
      Tooltip tooltip = tooltipMap.get(node);
      if (tooltip != null) {
        Tooltip.uninstall(node, tooltip);
        tooltipMap.remove(node);
      }
    }
  }

  /**
   * Toggle the hover animation for an item.
   *
   * @param item - The item to set the hover animation for
   * @param isOn - Whether the hover animation should be on or off
   */
  public static void setClueHover(String item, boolean isOn) {
    for (Node node : hoverItemsList) {
      if (node.getId().equals(item)) {
        AnimationManager.toggleHoverAnimation(node, isOn, 1);
      }
    }
  }

  /**
   * Toggle the alarm for the game.
   *
   * @param on - Whether the alarm should be on or off
   */
  public static void setAlarm(boolean on) {
    if (on) {
      App.textToSpeech("Alarm Triggered");
      setAlarmStyle();
      Platform.runLater(
          () -> {
            WalkieTalkieManager.setWalkieTalkieOpen();
            WalkieTalkieManager walkieTalkieManager = WalkieTalkieManager.getInstance();
            walkieTalkieManager.setWalkieTalkieText(
                new ChatMessage("user", "Uh Oh! You better find a way to turn that off quick"));
          });
    }
    // Create a set of items to include
    Set<String> disableIds = createdisableIdSet();

    // Go through all items
    for (Node item : hoverItemsList) {
      if (item == null) {
        continue; // Skip null items
      }
      // Get the item id
      String itemId = item.getId();

      if (itemId != null) {
        // If the item is in the set, disable it
        if (disableIds.contains(itemId)) {
          item.setDisable(true);
          setClueHover(itemId.toString(), false);
        }
        // If the item is in the set, disable it
        if (itemId.endsWith("Image")) {
          item.setStyle(null);
          applyLightingEffect(item, on);
        }
        // If the item is the background, animate it
        if (itemId.endsWith("background")) {
          AnimationManager.toggleAlarmAnimation(item, on, 0.7);
        }
      }
    }
  }

  /** Set the styling for everything needed when the alarm goes off. */
  private static void setAlarmStyle() {
    setClueHover("guardpocket", true);
    setItemsMessage("something is inside", "guardpocket");
    setClueHover("electricityBox", true);
    setItemsMessage("wire cutting..?", "electricityBox");
    setItemsHoverColour(HoverColour.GREEN, "guardpocket", "electricityBox");
  }

  /**
   * Create a set of items to disable when alarm is triggered.
   *
   * @return - The set of items to disable when alarm is triggered
   */
  private static Set<String> createdisableIdSet() {
    // Create a set of items to disable when alarm is triggered
    return new HashSet<>(
        Arrays.asList(
            "guard",
            "drawerHolder",
            "credentialsNote",
            "computer",
            "doorHolder",
            "guardeyes",
            "key1",
            "key3",
            "key4",
            "ceoPainting",
            "wallEmployeeImage",
            "silverDoorHolder",
            "bronzeDoorHolder",
            "goldDoorHolder",
            "exitDoor"));
  }

  /**
   * Apply or remove a lighting effect to the given item.
   *
   * @param item - The item to apply the lighting effect to
   * @param on - Whether the lighting effect should be on or off
   */
  private static void applyLightingEffect(Node item, Boolean on) {
    // Create a lighting effect when on
    if (on) {
      Lighting lighting = new Lighting();
      lighting.setDiffuseConstant(0.4);
      lighting.setSpecularConstant(0.1);
      lighting.setSpecularExponent(3.0);
      item.setEffect(lighting);
    } else {
      // Remove the effect when off
      item.setEffect(null);
    }
  }

  /** Reset the lists relating to one round. */
  public static void reset() {
    tooltipMap.clear();
    hoverItemsList.clear();
  }

  private StyleManager() {}

  /**
   * Add items to the hover items list.
   *
   * @param items - The items to add
   */
  public void addHoverItems(Node... items) {
    hoverItemsList.addAll(List.of(items));
  }
}
