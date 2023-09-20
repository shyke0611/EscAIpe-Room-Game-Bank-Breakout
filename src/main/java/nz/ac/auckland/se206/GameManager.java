package nz.ac.auckland.se206;

import nz.ac.auckland.se206.difficulties.*;
import nz.ac.auckland.se206.difficulties.Difficulty.Difficulties;

public class GameManager {

  // The different objectives
  public enum Objectives {
    START_GAME,
    GET_KEYS,
    FIND_PASSCODE,
    LOGIN,
    DISABLE_FIREWALL,
    EXTRA_SECURITY_LAYER,
    SELECT_VAULT_DOOR,
    DOOR_OBJECTIVES,
    ALARM_TRIPPED,
    DISABLE_LASERTRAP,
    FIND_ESCAPE,
    ESCAPE,
    GAME_OVER,
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

  private static Difficulty difficulty;

  private static int questionsCorrect = 0;
  private static Doors selectedDoor;

  private static Objectives activeObjective = Objectives.START_GAME;
  private static DoorObjectives activeDoorObjective = null;

  public static void createGame(Difficulties difficulty, int minutes) {
    resetGame();

    // Create the difficulty
    switch (difficulty) {
      case EASY:
        GameManager.difficulty = new EasyDifficulty();
        break;

      case MEDIUM:
        GameManager.difficulty = new MediumDifficulty();
        break;

      case HARD:
        GameManager.difficulty = new HardDifficulty();
        break;

      default:
        break;
    }

    TimerControl.setTimer(minutes);
  }

  public static void resetGame() {
    questionsCorrect = 0;
    selectedDoor = null;
    activeObjective = Objectives.START_GAME;
    activeDoorObjective = null;
  }

  public static int getQuestionsCorrect() {
    return questionsCorrect;
  }

  public static void setQuestionsCorrect(int questionsCorrect) {
    GameManager.questionsCorrect = questionsCorrect;
  }

  public static Doors getSelectedDoor() {
    return selectedDoor;
  }

  public static void setSelectedDoor(Doors selectedDoor) {
    GameManager.selectedDoor = selectedDoor;
  }

  public static Difficulty getDifficulty() {
    return difficulty;
  }

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
          activeObjective = Objectives.EXTRA_SECURITY_LAYER;
        }

        break;

      case EXTRA_SECURITY_LAYER:
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

  public static String getObjectiveString() {
    switch (activeObjective) {
      case START_GAME:
        return "Start Game";
      case GET_KEYS:
        return "Find Keys";

      case FIND_PASSCODE:
        return "Find Passcode";

      case LOGIN:
        return "Login";

      case DISABLE_FIREWALL:
        return "Disable Firewall";

      case EXTRA_SECURITY_LAYER:
        return "complete Extra Security Layer";

      case SELECT_VAULT_DOOR:
        return "Select Vault Door";

      case DOOR_OBJECTIVES:
        if (activeDoorObjective == DoorObjectives.FIND_EXTRA_PASSCODE) {
          return "Find Extra Passcode";
        } else if (activeDoorObjective == DoorObjectives.CHEMICAL_MIXING) {
          return "Mix Chemicals";
        } else if (activeDoorObjective == DoorObjectives.LAZER_CUTTING) {
          return "Cut Through Lasers";
        } else if (activeDoorObjective == DoorObjectives.EYE_SCANNER) {
          return "Scan Eye";
        }

      case ALARM_TRIPPED:
        return "Alarm Tripped";

      case DISABLE_LASERTRAP:
        return "Disable Laser Trap";

      case FIND_ESCAPE:
        return "Find Escape";

      case ESCAPE:
        return "Escape";

      case GAME_OVER:
        return null;

      default:
        return null;
    }
  }
}
