package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.scene.layout.HBox;

public class RandomnessGenerate {
  private static List<String> passcodeList = new ArrayList<>();
  private static List<String> usernameList = new ArrayList<>();
  private static List<HBox> keyLocationList = new ArrayList<>();

  private static Random random = new Random();
  private static String randomUsername;
  private static String randomPassword;
  private static HBox randomKeyLocation;

  static {
    // Initialize the lists when the class is loaded and stores all the possible passwords and
    // usernames to choose from
    passcodeList = Arrays.asList("206", "281", "282", "284", "274");
    usernameList = Arrays.asList("username1", "username2", "username3", "username4");
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

  // Method to add HBox elements to the keyLocationList
  public static void addKeyLocation(HBox... hboxes) {
    keyLocationList.addAll(Arrays.asList(hboxes));
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
}
