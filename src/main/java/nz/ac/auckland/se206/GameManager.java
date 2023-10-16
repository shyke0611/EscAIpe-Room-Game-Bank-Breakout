package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.controllers.Controller;

/** Class to manage all the game related information. */
public class GameManager {

  // The different objectives
  public enum Objectives {
    START_GAME,
    GET_KEYS,
    FIND_PASSCODE,
    LOGIN,
    DISABLE_FIREWALL,
    COMPLETE_MINIGAME,
    SELECT_VAULT_DOOR,
    DOOR_OBJECTIVES,
    ALARM_TRIPPED,
    DISABLE_LASERTRAP,
    FIND_ESCAPE,
    ESCAPE,
    GAME_OVER,
    CHEMICAL_MIXING,
    LAZER_CUTTING,
    EYE_SCANNER,
  }

  // The different objectives for the doors
  public enum DoorObjectives {
    FIND_EXTRA_PASSCODE,
    CHEMICAL_MIXING,
    LAZER_CUTTING,
    EYE_SCANNER,
  }

  // The different doors
  public enum Doors {
    EASY,
    MEDIUM,
    HARD
  }

  // The different difficulties
  public enum Difficulties {
    HARD,
    MEDIUM,
    EASY
  }

  private static Difficulties difficulty;

  private static int questionsCorrect = 0;
  private static Doors selectedDoor;

  private static Objectives activeObjective = Objectives.START_GAME;
  private static DoorObjectives activeDoorObjective = null;
  private static WalkieTalkieManager walkieTalkieManager = WalkieTalkieManager.getInstance();
  private static HashMap<Controller, Label> moneyGainedLabels = new HashMap<>();

  private static int moneyGained = 0;
  private static int moneyToGain = 0;

  /**
   * Set variables for a new game.
   *
   * @param difficulty - The difficulty of the game
   * @param minutes - The amount of minutes the game will last
   */
  public static void createGame(Difficulties difficulty, int minutes) {
    // Create the difficulty
    switch (difficulty) {
      case EASY:
        GameManager.difficulty = difficulty;
        walkieTalkieManager.setHintText("Unlimited");
        break;

      case MEDIUM:
        GameManager.difficulty = difficulty;
        walkieTalkieManager.setHintText("5");

        break;

      case HARD:
        GameManager.difficulty = difficulty;
        walkieTalkieManager.setHintText("0");
        break;

      default:
        break;
    }

    TimerControl.setTimer(minutes);
  }

  /**
   * Add a label to the money gained labels.
   *
   * @param controller - The controller the label is in
   * @param label - The label to add
   */
  public static void addMoneyGainedLabel(Controller controller, Label label) {
    moneyGainedLabels.put(controller, label);
  }

  /** Reset the game and everthing related to that round. */
  public static void resetGame() {
    // Reset the game and all variables/classes
    resetGameManager();
    RandomnessGenerate.reset();
    StyleManager.reset();
    WalkieTalkieManager.reset();
    GameState.resetGameState();
    // ((MainMenuController) SceneManager.getController(Scenes.MAIN_MENU)).reset();
    new HackerAiManager();
    // Reload the scenes so they are reset
    try {
      App.reloadScenes();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** Reset the game manager variable. */
  public static void resetGameManager() {
    questionsCorrect = 0;
    selectedDoor = null;
    activeObjective = Objectives.START_GAME;
    activeDoorObjective = null;
    moneyGained = 0;
    moneyToGain = 0;
  }

  /**
   * Get the number of questions correct.
   *
   * @return - The number of questions correct
   */
  public static int getQuestionsCorrect() {
    return questionsCorrect;
  }

  /**
   * Set the number of questions correct.
   *
   * @param questionsCorrect - The number of questions correct
   */
  public static void setQuestionsCorrect(int questionsCorrect) {
    GameManager.questionsCorrect = questionsCorrect;
  }

  /**
   * Get the door that is selected.
   *
   * @return - The selected door
   */
  public static Doors getSelectedDoor() {
    return selectedDoor;
  }

  /**
   * Set the door that is selected.
   *
   * @param selectedDoor - The selected door
   */
  public static void setSelectedDoor(Doors selectedDoor) {
    GameManager.selectedDoor = selectedDoor;
  }

  /**
   * Get the current difficulty of the game.
   *
   * @return - The current difficulty
   */
  public static Difficulties getDifficulty() {
    return difficulty;
  }

  /**
   * Get the difficulty as a string.
   *
   * @param difficulty - The difficulty to get as a string
   * @return - The difficulty as a string
   */
  public static String getDifficultyString(Difficulties difficulty) {
    // Return the difficulty as a string
    switch (difficulty) {
      case EASY:
        return "Easy";
      case MEDIUM:
        return "Medium";
      case HARD:
        return "Hard";
      default:
        return null;
    }
  }

  /**
   * Set the current objective of the game.
   *
   * @param objective - The objective to set
   */
  public static void setCurrentObjective(Objectives objective) {
    walkieTalkieManager.enableQuickHintBtns();
    activeObjective = objective;
  }

  /** Progress to the next objective depending on what the current objective is. */
  public static void completeObjective() {

    switch (activeObjective) {
      case START_GAME:
        activeObjective = Objectives.GET_KEYS;
        break;

      case GET_KEYS:
        activeObjective = Objectives.FIND_PASSCODE;
        break;

      case FIND_PASSCODE:
        activeObjective = Objectives.LOGIN;
        break;

      case LOGIN:
        activeObjective = Objectives.DISABLE_FIREWALL;
        break;

      case DISABLE_FIREWALL:

        // no questions right, they will have to do the extra security layer
        if (questionsCorrect != 0) {
          activeObjective = Objectives.SELECT_VAULT_DOOR;
        } else {
          activeObjective = Objectives.COMPLETE_MINIGAME;
        }

        break;
      case COMPLETE_MINIGAME:
        activeObjective = Objectives.SELECT_VAULT_DOOR;
        break;

      case SELECT_VAULT_DOOR:
        activeObjective = Objectives.DOOR_OBJECTIVES;
        break;

      case DOOR_OBJECTIVES:
        // If they are already going through the extra security layer
        if (activeDoorObjective == DoorObjectives.FIND_EXTRA_PASSCODE) {
          if (selectedDoor == Doors.EASY) {
            activeDoorObjective = DoorObjectives.LAZER_CUTTING;
          } else if (selectedDoor == Doors.MEDIUM) {
            activeDoorObjective = DoorObjectives.CHEMICAL_MIXING;
          } else if (selectedDoor == Doors.HARD) {
            activeDoorObjective = DoorObjectives.EYE_SCANNER;
          }
          break;
        }

        // If they complete the door
        if (activeDoorObjective != null) {
          activeObjective = Objectives.ALARM_TRIPPED;
          break;
        }

        // If they are not going through the extra security layer
        if (selectedDoor == Doors.HARD) {
          if (questionsCorrect == 2 && activeDoorObjective == null) {
            activeDoorObjective = DoorObjectives.FIND_EXTRA_PASSCODE;
          } else {
            activeDoorObjective = DoorObjectives.EYE_SCANNER;
          }

        } else if (selectedDoor == Doors.MEDIUM) {
          activeDoorObjective = DoorObjectives.CHEMICAL_MIXING;

        } else if (selectedDoor == Doors.EASY) {
          activeDoorObjective = DoorObjectives.LAZER_CUTTING;
        }

        break;

      case ALARM_TRIPPED:
        activeObjective = Objectives.DISABLE_LASERTRAP;
        break;

      case DISABLE_LASERTRAP:
        activeObjective = Objectives.FIND_ESCAPE;
        break;

      case FIND_ESCAPE:
        activeObjective = Objectives.ESCAPE;
        break;

      case ESCAPE:
        activeObjective = Objectives.GAME_OVER;
        break;

      case GAME_OVER:
        break;

      default:
        break;
    }
  }

  /**
   * Get the current objective of the game.
   *
   * @return - The current objective
   */
  public static Objectives getCurrentObjective() {
    return activeObjective;
  }

  /**
   * Get the current objective as a string.
   *
   * @return - A String of the current objective
   */
  public static String getObjectiveString() {
    // Return the objective as a string
    switch (activeObjective) {
      case START_GAME:
        return "Start Game";
      case GET_KEYS:
        return "Find Keys";
      case FIND_PASSCODE:
        return "Find Passcode";
      case DISABLE_FIREWALL:
        return "Disable Firewall";

        // Door objectives and escape objectives
      case COMPLETE_MINIGAME:
        return "Complete Minigame";
      case SELECT_VAULT_DOOR:
        return "Select Vault Door";
      case CHEMICAL_MIXING:
        return "Mix Chemicals";
      case LAZER_CUTTING:
        return "Cut Through Lasers";
      case EYE_SCANNER:
        return "Scan Eye";
      case DISABLE_LASERTRAP:
        return "Disable Laser Trap";
      case FIND_ESCAPE:
        return "Find Escape";

        // If game is over or objective not in list return null
      case GAME_OVER:
        return null;
      default:
        return null;
    }
  }

  /**
   * Increase the money to gain.
   *
   * @param amount - The amount to increase the money to gain by
   */
  public static void increaseMoneyToGain(int amount) {
    moneyToGain += amount;
  }

  /** Collect all money to gain and reset money to gain. */
  public static void collectMoney() {
    moneyGained = moneyToGain;
    moneyToGain = 0;
  }

  /** Lose all of the money gained. */
  public static void loseMoney() {
    moneyGained = 0;
  }

  /**
   * Get the money gained value.
   *
   * @return - The money gained
   */
  public static int getMoneyGained() {
    return moneyGained;
  }

  /**
   * Get the money to gain.
   *
   * @return - The money to gain formatted as a String
   */
  public static String getMoneyToGain() {
    return formatMoney(moneyToGain);
  }

  /** Set the labels for the money Gained so far. */
  public static void setMoneyGained() {
    for (Label label : moneyGainedLabels.values()) {
      label.setText(moneyToGain / 1000000 + "M");
    }
  }

  /**
   * Format money as a string.
   *
   * @param money - The money to format
   * @return - The money formatted as a string
   */
  public static String formatMoney(int money) {
    if (money == 0) {
      return "$0";
    }

    int millions = money / 1000000;
    return "$" + millions + ",000,000";
  }
}
