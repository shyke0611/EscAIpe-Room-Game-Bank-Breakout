package nz.ac.auckland.se206;

/** Represents the state of the game. */
public class GameState {

  /** Indicates whether the riddle has been resolved. */
  public static boolean isRiddleResolved = false;

  /** Indicates whether the key has been found. */
  public static boolean isKeyFound = false;

  public static boolean isSecurityComputerLoggedIn = false;
  public static boolean isGuardDistracted = false;
  public static boolean isKeyLocationFound = false;
  public static boolean isWiresCut = false;
  public static boolean isAlarmTripped = false;
  public static boolean isAlarmDisabled = false;
  public static boolean isFirewallDisabled = false;
  public static boolean isFirstRiddleSolved = false;
  public static boolean isSecondRiddleSolved = false;
  public static boolean isThirdRiddleSolved = false;
  public static boolean isSecondUserAuthenticated = false;
  public static boolean isBombActivated = false;
  public static boolean isEyeScannerBypassed = false;
  public static boolean isChemicalMixingBypassed = false;
  public static boolean isLaserCuttingBypassed = false;
  public static boolean isAnyDoorOpen = false;
  public static boolean isConnectDotreached = false;
  public static boolean isCredentialsFound = false;
  public static boolean isWireCredentialsFound = false;

  // tracking record of if the green hover is pressed
  public static boolean isComputerHoverPressed = false;
  public static boolean isSecurityRoomHoverPressed = false;
  public static boolean isSecurityRoomHoverPressed2 = false;
  public static boolean isGuardPocketHoverPressed = false;
  public static boolean isLobbyRoomHoverPressed = false;
  public static boolean isElectricityHoverPressed = false;

  public static void resetGameState() {
    isRiddleResolved = false;
    isKeyFound = false;
    isSecurityComputerLoggedIn = false;
    isGuardDistracted = false;
    isKeyLocationFound = false;
    isWiresCut = false;
    isAlarmTripped = false;
    isAlarmDisabled = false;
    isFirewallDisabled = false;
    isFirstRiddleSolved = false;
    isSecondRiddleSolved = false;
    isThirdRiddleSolved = false;
    isSecondUserAuthenticated = false;
    isBombActivated = false;
    isEyeScannerBypassed = false;
    isChemicalMixingBypassed = false;
    isLaserCuttingBypassed = false;
    isAnyDoorOpen = false;
    isConnectDotreached = false;
    isCredentialsFound = false;
    isWireCredentialsFound = false;
    isComputerHoverPressed = false;
    isSecurityRoomHoverPressed = false;
    isSecurityRoomHoverPressed2 = false;
    isGuardPocketHoverPressed = false;
    isLobbyRoomHoverPressed = false;
    isElectricityHoverPressed = false;
  }
}
