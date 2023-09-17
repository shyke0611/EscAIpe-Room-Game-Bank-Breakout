package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class StyleManager {

//   private static HashMap<HBox, Tooltip> tooltipMap = new HashMap<>();
private static Tooltip tooltip;

  // changing hover colour of items depending on gamestate
  public static void setItemsHoverState(int r, int g, int b, HBox... items) {
    String rgbaColor = "rgba(" + r + "," + g + "," + b + ",0.7)";
    for (HBox item : items) {
      item.setOnMouseEntered(
          event -> {
            item.setStyle(
                "-fx-effect: dropshadow(gaussian, "
                    + rgbaColor
                    + ", 5, 5, 0, 0); -fx-cursor: hand;");
          });
      item.setOnMouseExited(
          event -> {
            item.setStyle("");
          });
    }
  }

  // setting message for items
  public static void setItemsMessage(String message, HBox... items) {
   
    for (HBox item : items) {
     tooltip = new Tooltip(message);
        tooltip.setShowDelay(Duration.seconds(0));
        Tooltip.install(item, tooltip);

        // item.setOnMouseClicked(
        //     event -> {
        //       tooltip.show(
        //           item, ((MouseEvent) event).getScreenX(), ((MouseEvent) event).getScreenY());
        //     });
        // item.setOnMouseExited(
        //     event -> {
        //       tooltip.hide();
        //     });
      
    }
  }

//   public static void initialiseItemsMessage(String message, HBox... items) {
//     for (HBox item : items) {
//       Tooltip tooltip = new Tooltip(message);
//       tooltip.setShowDelay(Duration.seconds(0));
//       tooltip.hideDelayProperty();
//       tooltip.consumeAutoHidingEventsProperty();
//       Tooltip.install(item, tooltip);
//       tooltipMap.put(item, tooltip);
//     }
//   }

  public static void removeItemsMessage(HBox... items) {
    for (HBox item : items) {
      
            Tooltip.uninstall(item, tooltip);
            
        
    }
}

}
