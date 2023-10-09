package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javafx.scene.layout.HBox;

public class RandomnessGenerate {
  private static List<String> passcodeList;
  private static List<String> usernameList;
  private static List<String> CeoNames;
  private static List<String> employeeNames;
  private static List<String> foundingDatesList;
  private static List<HBox> keyLocationList = new ArrayList<>();
  private static List<String> partsAmount;
  private static List<HBox> wiresList = new ArrayList<>();

  private static Random random = new Random();
  private static String randomUsername;
  private static String randomPassword;
  private static HBox randomKeyLocation;
  private static String randomChemialAmount;
  private static String ceoName;
  private static String employeeName;
  private static String foundingDate;

  static {
    // Initialize the lists when the class is loaded and stores all the possible passwords and
    // usernames to choose from
    passcodeList = Arrays.asList("2062", "2314", "2556", "2144", "2246", "2341", "1142", "1012");
    usernameList =
        Arrays.asList("username1", "username2", "username3", "username4", "username5", "username6");
    partsAmount = Arrays.asList("1", "2", "3", "4");
    CeoNames = Arrays.asList("John", "Jack", "James", "Bob", "Will", "Jerry");
    employeeNames = Arrays.asList("Jessica", "Jenny", "Jasmine", "Jade", "Jenna", "Jill");
    foundingDatesList = Arrays.asList("1967", "1862", "1813", "1932", "1902", "1888", "1756");
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
    int randomKeyLocationIndex = random.nextInt(keyLocationList.size());
    randomKeyLocation = keyLocationList.get(randomKeyLocationIndex);
  }

  // method to get random wires list
  public static List<HBox> getRandomWires() {
    return wiresList;
  }

  public static String getRandomCeoName() {
    int randomCeoNameIndex = random.nextInt(CeoNames.size());
    ceoName = CeoNames.get(randomCeoNameIndex);

    return ceoName;
  }

  public static String getCurrentCeoName() {
    return ceoName;
  }

  public static String getRandomFoundingDate() {
    int randomFoundingDateIndex = random.nextInt(foundingDatesList.size());
    foundingDate = foundingDatesList.get(randomFoundingDateIndex);

    return foundingDate;
  }

  public static String getCurrentDate() {
    return foundingDate;
  }

  public static String getRandomEmployeeName() {
    int randomEmployeeNameIndex = random.nextInt(employeeNames.size());
    employeeName = employeeNames.get(randomEmployeeNameIndex);

    return employeeName;
  }

  public static String getCurrentEmployeeName() {
    return employeeName;
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

  public static String getRandomChemicalAmount() {
    int randomChemialAmountIndex = random.nextInt(partsAmount.size());
    randomChemialAmount = partsAmount.get(randomChemialAmountIndex);
    return randomChemialAmount;
  }

  public static void reset() {
    keyLocationList.clear();
    wiresList.clear();
  }
}
