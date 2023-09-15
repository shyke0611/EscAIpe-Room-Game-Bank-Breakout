package nz.ac.auckland.se206;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomCredentialsGenerator {
    private static List<String> passcodeList;
    private static List<String> usernameList;
    private static Random random = new Random();
    private static String randomUsername;
    private static String randomPassword;

    static {
        // Initialize the lists when the class is loaded
        passcodeList = Arrays.asList("206", "281", "282", "284", "274");
        usernameList = Arrays.asList("username1", "username2", "username3", "username4");
    }

    public static void generateRandomCredentials() {
        int randomPasswordIndex = random.nextInt(passcodeList.size());
        int randomUsernameIndex = random.nextInt(usernameList.size());

        randomUsername = usernameList.get(randomUsernameIndex);
        randomPassword = passcodeList.get(randomPasswordIndex);
    }

    public static String getPasscode() {
        return randomPassword;
    }

    public static String getUsername() {
        return randomUsername;
    }
}
