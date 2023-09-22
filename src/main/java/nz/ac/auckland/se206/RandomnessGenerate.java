package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javafx.scene.layout.HBox;

public class RandomnessGenerate {
  private static List<String> passcodeList = new ArrayList<>();
  private static List<String> usernameList = new ArrayList<>();
  private static List<HBox> keyLocationList = new ArrayList<>();
  private static List<String> partsAmount = new ArrayList<>();
  private static List<HBox> wiresList = new ArrayList<>();

  private static Random random = new Random();
  private static String randomUsername;
  private static String randomPassword;
  private static HBox randomKeyLocation;
  private static String randomChemialAmount;

  static {
    // Initialize the lists when the class is loaded and stores all the possible passwords and
    // usernames to choose from
    passcodeList = Arrays.asList("206", "281", "282", "284", "274", "234", "598", "101");
    usernameList =
        Arrays.asList(
            "FinanceShield3",
            "LoremIpsumBank",
            "SecureBanker2",
            "BankerDefender1",
            "AccountProtector",
            "CashFlowGuard");
    partsAmount = Arrays.asList("1", "2", "3", "4");
  }

  // calling all the generate random stuff into one method
  public static void generateRandomGameComponents() {
    generateRandomCredentials();
    // generateRandomKeyLocation();
  }

  // generating random login credentials for security computer log in
  public static void generateRandomCredentials() {
    int randomPasswordIndex = random.nextInt(passcodeList.size());
    int randomUsernameIndex = random.nextInt(usernameList.size());
    randomUsername = usernameList.get(randomUsernameIndex);
    randomPassword = passcodeList.get(randomPasswordIndex);
  }

  // generating random HBox (key location)
  public static void generateRandomKeyLocation() {
    int randomHBoxIndex = random.nextInt(keyLocationList.size());
    randomKeyLocation = keyLocationList.get(randomHBoxIndex);
  }

  // method to get random wires list
  public static List<HBox> getRandomWires() {
    return wiresList;
  }

  // Method to add HBox elements to the keyLocationList
  public static void addKeyLocation(HBox... hboxes) {
    keyLocationList.addAll(Arrays.asList(hboxes));
  }

  // Method to add wires to wireslist
  public static void addWires(HBox... wires) {
    List<HBox> shuffledWires = Arrays.asList(wires);
    Collections.shuffle(shuffledWires);
    wiresList.addAll(shuffledWires);
  }

  // getter for passcode
  public static String getPasscode() {
    return randomPassword;
  }

  // getter for username
  public static String getUsername() {
    return randomUsername;
  }

  // getter for key location
  public static HBox getkeyLocation() {
    return randomKeyLocation;
  }

  public static int getRandomColourValue() {
    return random.nextInt(255);
  }

  public String getRandomChemialAmount() {
    int randomChemialAmountIndex = random.nextInt(partsAmount.size());
    randomChemialAmount = partsAmount.get(randomChemialAmountIndex);
    return randomChemialAmount;
  }

  public static void reset() {
    keyLocationList.clear();
    wiresList.clear();
  }
}
