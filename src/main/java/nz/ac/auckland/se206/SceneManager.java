package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.controllers.Controller;
import nz.ac.auckland.se206.controllers.LobbyController;
import nz.ac.auckland.se206.controllers.SecurityController;
import nz.ac.auckland.se206.controllers.VaultController;

public class SceneManager {

  // The different Scenes
  public enum Scenes {
    VAULT,
    LOBBY,
    SECURITY,
    MAIN_MENU,
    DIFFICULTYPAGE,
    COMPUTER,
    HACKERVAN,
  }

  private static Controller activeController;
  private static HashMap<Scenes, Controller> controllerMap = new HashMap<>();

  private static HashMap<Scenes, Parent> sceneMap = new HashMap<>();

  private static boolean walkieTalkieOpen = false;

  // Add a scene to the scene map
  public static void addUi(Scenes appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  // Get a scene from the scene map and set the active controller
  public static Parent getUiRoot(Scenes appUi) {
    activeController = controllerMap.get(appUi);
    return sceneMap.get(appUi);
  }

  // Set a scene in the scene map
  public static void setParent(Scenes appUi, Parent uiRoot) {
    sceneMap.replace(appUi, uiRoot);
  }

  // Set the active controller
  public static void setActiveController(Controller controller) {
    activeController = controller;
  }

  // Get the active controller
  public static Controller getActiveController() {
    return activeController;
  }

  // Add a controller to the controller map
  public static void addController(Scenes appUi, Controller controller) {
    controllerMap.put(appUi, controller);
  }

  // Get a controller from the controller map
  public static Controller getController(Scenes appUi) {
    return controllerMap.get(appUi);
  }

  // Set a controller in the controller map
  public static void setController(Scenes appUi, Controller controller) {
    controllerMap.replace(appUi, controller);
  }

  // Get the timer label from the active controller
  public static Label getTimerLabel() {
    return activeController.getTimerLabel();
  }

  // Get the walkie talkie open boolean
  public static boolean isWalkieTalkieOpen() {
    return walkieTalkieOpen;
  }

  // Toggle the walkie talkie open boolean
  public static void toggleWalkieTalkieOpen() {
    walkieTalkieOpen = !walkieTalkieOpen;
    ((LobbyController) controllerMap.get(Scenes.LOBBY)).synchWalkieTalkie(walkieTalkieOpen);
    ((SecurityController) controllerMap.get(Scenes.SECURITY)).synchWalkieTalkie(walkieTalkieOpen);
    ((VaultController) controllerMap.get(Scenes.VAULT)).synchWalkieTalkie(walkieTalkieOpen);
  }
}
