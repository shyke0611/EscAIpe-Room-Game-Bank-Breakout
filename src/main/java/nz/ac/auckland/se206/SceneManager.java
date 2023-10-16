package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.controllers.Controller;

/** Class to manage all the scenes and controllers. */
public class SceneManager {

  // The different Scenes
  public enum Scenes {
    VAULT,
    LOBBY,
    SECURITY,
    MAIN_MENU,
    INSTRUCTIONS,
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
  private static Scenes lastRoom;

  /**
   * Add a scene to the scene map.
   *
   * @param appUi - The scene to add
   * @param uiRoot - The loaded scene
   */
  public static void addUi(Scenes appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  /**
   * Replace a scene in the scene map.
   *
   * @param appUi - The scene to change
   * @param uiRoot - The loaded scene
   */
  public static void changeUi(Scenes appUi, Parent uiRoot) {
    sceneMap.replace(appUi, uiRoot);
  }

  /**
   * Get a scene from the scene map and set the active controller.
   *
   * @param appUi - The scene to get
   * @return - The loaded scene
   */
  public static Parent getUiRoot(Scenes appUi) {
    activeController = controllerMap.get(appUi);
    return sceneMap.get(appUi);
  }

  /**
   * Set a scene in the scene map.
   *
   * @param appUi - The scene to set
   * @param uiRoot - The loaded scene
   */
  public static void setParent(Scenes appUi, Parent uiRoot) {
    sceneMap.replace(appUi, uiRoot);
  }

  /**
   * Set the currently active controller.
   *
   * @param controller - The controller to set as active
   */
  public static void setActiveController(Controller controller) {
    activeController = controller;
  }

  /**
   * Get the currently active controller.
   *
   * @return - The active controller
   */
  public static Controller getActiveController() {
    return activeController;
  }

  /**
   * Add a controller to the controller map.
   *
   * @param appUi - The scene of the controller
   * @param controller - The controller to add
   */
  public static void addController(Scenes appUi, Controller controller) {
    controllerMap.put(appUi, controller);
  }

  /**
   * Get a controller from the controller map.
   *
   * @param appUi - The scene of the controller
   * @return - The controller
   */
  public static Controller getController(Scenes appUi) {
    return controllerMap.get(appUi);
  }

  /**
   * Replace a controller in the controller map.
   *
   * @param appUi - The scene of the controller
   * @param controller - The new controller
   */
  public static void setController(Scenes appUi, Controller controller) {
    controllerMap.replace(appUi, controller);
  }

  /**
   * Get the timer label from the active controller.
   *
   * @return - The timer label
   */
  public static Label getTimerLabel() {
    return activeController.getTimerLabel();
  }

  /**
   * Set the last room the player was in.
   *
   * @param appUi - The last room (Lobby, Security or Vault) the player was in
   */
  public static void setLastRoom(Scenes appUi) {
    lastRoom = appUi;
  }

  /**
   * Get the last room the player was in.
   *
   * @return - The last room (Lobby, Security or Vault) the player was in
   */
  public static Scenes getLastRoom() {
    return lastRoom;
  }
}
