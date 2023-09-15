package nz.ac.auckland.se206;

import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.controllers.SecurityController;
import nz.ac.auckland.se206.controllers.VaultController.Doors;

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

  private static Objectives activeObjective = Objectives.START_GAME;

  public static void completeObjective() {
    int questionsCorrect;
    Doors selectedDoor;

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

      // if the user gets no questions right, they will have to do the extra security layer
      case DISABLE_FIREWALL:
        questionsCorrect = ((SecurityController) SceneManager.getController(Scenes.SECURITY)).getQuestionsCorrect();

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
}
