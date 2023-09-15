package nz.ac.auckland.se206;

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
}
