package nz.ac.auckland.se206;

import java.util.HashMap;

import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.controllers.Controller;

public class WalkieTalkieManager {

    //storing the walkietalkie textboxes
    private static HashMap<Controller, VBox> walkietalkieMap = new HashMap<>();
    private static boolean walkieTalkieOpen = false;

    public static void addWalkieTalkie(Controller controller, VBox walkietalkie) {
        walkietalkieMap.put(controller, walkietalkie);
    }

    public static void toggleWalkieTalkie() {
        walkieTalkieOpen = !walkieTalkieOpen;
        
        // Iterate through the map and update the visibility of all VBoxes
        for (VBox vBox : walkietalkieMap.values()) {
            vBox.setVisible(walkieTalkieOpen);
        }
    }
}
