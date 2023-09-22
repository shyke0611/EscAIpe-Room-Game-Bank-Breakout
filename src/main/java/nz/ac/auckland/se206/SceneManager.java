package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.controllers.Controller;

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
    EYESCANNER,
    CHEMICALMIXING,
    CONNECTDOTS,
    WIRECUTTING,
    LASERCUTTING,
    GAMEFINISH
  }

  private static Controller activeController;
  private static HashMap<Scenes, Controller> controllerMap = new HashMap<>();

  private static HashMap<Scenes, Parent> sceneMap = new HashMap<>();

  private static HashMap<Scenes, Scenes> previousSceneMap = new HashMap<>();

  // Method to set the previous scene for a given current scene
  public static void setPreviousScene(Scenes currentScene, Scenes previousScene) {
    previousSceneMap.put(currentScene, previousScene);
  }

  // Method to get the previous scene for a given current scene
  public static Scenes getPreviousScene(Scenes currentScenesPrev) {
    return previousSceneMap.get(currentScenesPrev);
  }

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

  public static void clearScenes() {
    sceneMap.clear();
    previousSceneMap.clear();
  }
}
