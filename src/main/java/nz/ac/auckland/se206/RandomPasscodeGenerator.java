package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomPasscodeGenerator {

    // create Arraylist of passcodes and usernames
    private static List<String> passcodeList = new ArrayList<>();
    private static List<String> usernameList = new ArrayList<>();
    private static Random random = new Random();

    public RandomPasscodeGenerator() {
        // creating list of passcodes and usernames and storing them
        List<String> passcodes = Arrays.asList("206", "281", "282", "284", "274");
        List<String> usernames = Arrays.asList("username1", "username2", "username3", "username4");
        passcodeList.addAll(passcodes);
        usernameList.addAll(usernames);
    }

    public static String getPasscode() {
        int randomIndex = random.nextInt(passcodeList.size());
        return passcodeList.get(randomIndex);
    }

    public static String getUsername() {
        int randomIndex = random.nextInt(usernameList.size());
        return usernameList.get(randomIndex);
    }
    
}
