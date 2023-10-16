package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import nz.ac.auckland.se206.GameManager.Difficulties;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class HackerAiManager {

  private static HackerAiManager instance = new HackerAiManager();

  public static HackerAiManager getInstance() {
    return instance;
  }

  private int hintLimit = 0;
  private int hintCounter;
  private WalkieTalkieManager walkieTalkieManager = WalkieTalkieManager.getInstance();

  // Map to store hints for different game stages
  private Map<String, String> hintMappings = new HashMap<>();
  private Map<String, String> contextMappsing = new HashMap<>();
  private List<String> chatHistory = new ArrayList<>();
  private List<String> hintHistory = new ArrayList<>();

  private int hintNumber = 1; // Hint number for hint history
  private ChatMessage response;
  private ChatMessage tellAiHint;
  private String currentStage;
  private String hint;
  private boolean userNeedsHelp;

  private ChatCompletionRequest chatCompletionRequest;
  private Difficulties currentDifficulty;
  private ChatMessage gptCall;

  /** Constructor for HackerAiManager - also initialises the mappings */
  public HackerAiManager() {
    instance = this;
    // Initialize the hint mappings
    hintMappings.put("Start game", "Starting game");
    hintMappings.put(
        "Find Keys", "You must distract the guard to look in the places where the keys may be.");
    hintMappings.put(
        "Find Passcode", "The passcode is in the drawer on the wall, use the keys to unlock it");
    hintMappings.put(
        "Login", "Use the credentials on the note to login to the computer, in security.");
    hintMappings.put(
        "Disable Firewall",
        "Log into the computer and convince the AI you are an employee to log in");
    hintMappings.put("Complete Minigame", "Complete the connect dots minigame to proceed");
    hintMappings.put(
        "Select Vault Door", "Go to the vault and choose a door youd like to break into");
    hintMappings.put(
        "Scan Eye",
        "You must scan the guards eye and use it to match your fake eye so you can get through the"
            + " door");
    hintMappings.put("Mix Chemicals", "Mix the chemicals in the correct order to melt the lock");
    hintMappings.put(
        "Cut Through Lasers", "You must use your laser gun to cut a hole and get through the door");
    hintMappings.put(
        "Disable Laser Trap",
        "You must find the correct order of wires to cut in security, they are hidden in the guards"
            + " pocket");
    hintMappings.put("Find Escape", "Place the bomb in the vault and arm it to escape");

    // Initialize the context mappings
    contextMappsing.put("Find Keys", "You have not done anything yet");
    contextMappsing.put(
        "Find Passcode",
        "The player put the guard to sleep in order to check places to find the keys");
    contextMappsing.put(
        "Login", "The player used the keys to unlock the drawer and found the passcode");
    contextMappsing.put(
        "Disable Firewall", "The player used the passcode to login to the computer");
    contextMappsing.put(
        "Complete Minigame",
        "The player failed the authentication test but used the bypass to gain access to the vault"
            + " information");
    contextMappsing.put(
        "Scan Eye",
        "The player scanned the colour of the guards eye in order to get into one of the vaults");
    contextMappsing.put("Mix Chemicals", "The player mixed the chemicals to melt the lock");
    contextMappsing.put(
        "Cut Through Lasers",
        "The player used the laser gun to cut a hole in the door and get through");
    contextMappsing.put(
        "Disable Lasertrap", "The player disabled the lasertrap in security but cutting wires");
    contextMappsing.put("Find Escape", "The player placed the bomb in the vault and armed it");
  }

  /**
   * Set the hint limit.
   *
   * @param limit - The hint limit.
   */
  public void setHintLimit(int limit) {
    hintLimit = limit;
  }

  /**
   * Initialise the ai based on the difficulty.
   *
   * @param difficulty - The difficulty to initialise the ai for.
   * @throws ApiProxyException - If the ai fails to connect.
   */
  public void initialiseHacker(Difficulties difficulty) throws ApiProxyException {

    // Initialise the ai based on the difficulty
    switch (difficulty) {
      case EASY:
        System.out.println("easy");
        currentDifficulty = Difficulties.EASY;
        setHintLimit(-1);
        hintCounter = -1;
        chatCompletionRequest =
            new ChatCompletionRequest().setN(1).setTemperature(0.4).setTopP(1).setMaxTokens(1000);
        runGpt(new ChatMessage("user", GptPromptEngineering.initisialiseHackerEasy()));
        break;

        // Initialise the ai for the medium difficulty
      case MEDIUM:
        System.out.println("medium");
        setHintLimit(5);
        hintCounter = 5;
        currentDifficulty = Difficulties.MEDIUM;
        chatCompletionRequest =
            new ChatCompletionRequest().setN(1).setTemperature(0.4).setTopP(1).setMaxTokens(1000);
        runGpt(new ChatMessage("user", GptPromptEngineering.intisialiseHackerMeidium()));
        break;

        // Initialise the ai for the hard difficulty
      case HARD:
        System.out.println("hard");
        walkieTalkieManager.disableQuickHintBtns();
        setHintLimit(0);
        hintCounter = -1;
        currentDifficulty = Difficulties.HARD;
        chatCompletionRequest =
            new ChatCompletionRequest().setN(1).setTemperature(0.4).setTopP(1).setMaxTokens(1000);
        runGpt(new ChatMessage("user", GptPromptEngineering.intisialiseHackerHard()));
        break;
    }
  }

  /** Decrease the hint counter */
  public void decrementHintCounter() {
    hintCounter--;
  }

  /**
   * Get the hint limit.
   *
   * @return - The hint limit.
   */
  public int getHintLimit() {
    return hintLimit;
  }

  /**
   * Get the hint for the current game stage.
   *
   * @param currentStage - The current game stage.
   * @return - The corresponding hint for the current game stage.
   */
  public String getHintForCurrentStage(String currentStage) {
    return hintMappings.getOrDefault(currentStage, "No hint available for this stage.");
  }

  /**
   * Get a quick hint through button for the current game stage if allowed.
   *
   * @return - The quick hint for the current game stage or the text when not allowed.
   */
  public String getQuickHint() {
    currentStage = GameManager.getObjectiveString();

    if (Difficulties.EASY == currentDifficulty) {
      // Update the visual hint counter and retrieve hint
      Platform.runLater(() -> walkieTalkieManager.setHintText("Unlimited"));

      hint = getHintForCurrentStage(currentStage);

    } else if (Difficulties.HARD == currentDifficulty) {
      // Update the visual hint counter and return no hint
      Platform.runLater(() -> walkieTalkieManager.setHintText("0"));
      hint = "You are not allow to have hints";
      walkieTalkieManager.setWalkieTalkieText(
          new ChatMessage("user", "You are not allow to have hints"));

    } else if (Difficulties.MEDIUM == currentDifficulty && hintCounter <= 0) {
      // Update the visual hint counter and return all hints used

      Platform.runLater(
          () ->
              walkieTalkieManager.setWalkieTalkieText(
                  new ChatMessage(
                      "user",
                      "You have used all your hints, you can still ask for help but you will not"
                          + " get a hint")));

    } else {
      // Update the visual hint counter and retrieve hint
      Platform.runLater(() -> walkieTalkieManager.setHintText(Integer.toString(hintCounter)));
      hint = getHintForCurrentStage(currentStage);
    }
    walkieTalkieManager.disableQuickHintBtns();
    return hint;
  }

  /**
   * Store the ai hint in the hint history.
   *
   * @param msg - The ai hint to store.
   */
  public void storeAiHint(ChatMessage msg) {
    String hint = msg.getContent();
    // Check if the hint is unique
    if (!hintHistory.contains(hint)) {

      hintHistory.add(hintNumber + ". " + hint);
      hintNumber++;
    }
  }

  /**
   * Check if the hint already exists in the hint history.
   *
   * @param existingHint - The hint to check.
   * @return - True if the hint exists in the hint history, false otherwise.
   */
  public boolean stringExists(String existingHint) {
    for (String hint : hintHistory) {
      if (hint.equals(existingHint)) {
        return true;
      }
    }
    return false;
  }

  /** Store the quick hint in the hint history */
  public void storeQuickHint() {

    String hint = getQuickHint();
    decrementHintCounter();
    System.out.println(hint);
    System.out.println(hintCounter);

    // Check if the hint is unique
    if (!stringExists(hint)) {

      String formattedHint = hintNumber + ". " + hint;
      hintHistory.add(formattedHint);
      hintNumber++;
    }
  }

  /**
   * Add a chat to the chat history.
   *
   * @param chat - The chat to add.
   */
  public void addChatHistory(String chat) {
    chatHistory.add(chat);
  }

  /**
   * Get the chat history.
   *
   * @return - The chat history.
   */
  public List<String> getChatHistory() {
    return chatHistory;
  }

  //
  /**
   * Method gets the current stages hint and feeds that to the ai before feeding the ai the users
   * message
   *
   * @param msg - The message to process
   * @return - The response from the ai
   * @throws ApiProxyException - If the ai fails to connect
   */
  public ChatMessage processInput(ChatMessage msg) throws ApiProxyException {
    currentStage = GameManager.getObjectiveString();
    System.out.println(currentStage);
    userNeedsHelp = false;

    // Check if the hint limit has been reached for medium difficulty
    if (hintCounter <= 0 && currentDifficulty == Difficulties.MEDIUM) {
      tellAiHint =
          new ChatMessage(
              "user",
              "The player has used all of their hints, do not give out anymore hints whatsoever,"
                  + " all you can say is: 'Sorry you do not have anymore hints");
      gptCall = new ChatMessage("user", tellAiHint.getContent());
      runGpt(gptCall);
      return response = runGpt(msg);
    } else if (walkieTalkieManager.isQuickHintBtnsVisible()) {
      gptCall =
          new ChatMessage(
              "user",
              "The player is now out of hints for the current stage, until you recieve a new hint"
                  + " you cannot give out any you can, all you can say is: 'I have already given"
                  + " you a hint for this stage', you can still give out hints for other stages,"
                  + " you will be told when you can give out more and what the hint will be. Once"
                  + " that happens you can tell them the hint");
      System.out.println("user has already used hint");
      runGpt(gptCall);
      response = runGpt(msg);
      return response;
    } else if (userIsAiAskingForHelp(msg.getContent())) {
      System.out.println("user is asking for help");
      userNeedsHelp = true;
      decrementHintCounter();
    }

    // Check if the user is asking for help in easy mode
    if (currentDifficulty == Difficulties.EASY) {
      // System.out.println("typing");
      if (userNeedsHelp) {
        walkieTalkieManager.disableQuickHintBtns();
        currentStage = GameManager.getObjectiveString();
        hint = getHintForCurrentStage(currentStage);
        gptCall =
            new ChatMessage(
                "user",
                "You are now able to give hints again, ingore any previous command to not give out"
                    + " hints, here is the next one, say the hint and only the hint: Hint:"
                    + hint);
        System.out.println(hint);
        response = runGpt(gptCall);

        // response = runGpt(msg);
        storeAiHint(response);
      } else {
        response = runGpt(msg);
      }
    } else if (currentDifficulty == Difficulties.MEDIUM
        && hintCounter > 0
        && !walkieTalkieManager.isQuickHintBtnsVisible()
        && userNeedsHelp) {
      System.out.println("medium ai is typing");
      walkieTalkieManager.disableQuickHintBtns();

      hint = getHintForCurrentStage(currentStage);

      gptCall =
          new ChatMessage(
              "user",
              "You are now able to give hints again, ingore any previous command to not give out"
                  + " hints, here is the next one, say the hint and only the hint: Hint:"
                  + hint);

      response = runGpt(gptCall);
      // response = runGpt(msg);
      storeAiHint(response);
      Platform.runLater(() -> walkieTalkieManager.setHintText(Integer.toString(hintCounter)));

    } else {
      response = runGpt(msg);
    }

    if (currentDifficulty == Difficulties.HARD) {
      // Things to add, update ai to say what has happend during round
      response = runGpt(msg);
    }

    return response;
  }

  /**
   * Check if the user is asking for help.
   *
   * @param msg - The message to check.
   * @return - True if the user is asking for help, false otherwise.
   */
  public boolean userIsAiAskingForHelp(String msg) {
    // Convert the message to lowercase for case-insensitive matching
    String lowercaseMsg = msg.toLowerCase();

    // Define an array of keywords to check for
    String[] keywords = {
      "help",
      "hint",
      "assist",
      "support",
      "clue",
      "pointer",
      "aid",
      "guide",
      "give me a hand",
      "how do i do that",
      "what next"
    };

    // Check if any of the keywords is present in the message
    for (String keyword : keywords) {
      if (lowercaseMsg.contains(keyword)) {
        return true; // User is asking for help
      }
    }

    return false; // No help-related keywords found in the message
  }

  /**
   * Get the chat history after formatting it.
   *
   * @return - The formatted chat history.
   */
  public String getFormattedChatHistory() {
    // Initialize chat history text area
    StringBuilder chatHistoryText = new StringBuilder();
    for (String message : chatHistory) {
      chatHistoryText.append(message).append("\n");
    }

    return chatHistoryText.toString();
  }

  /**
   * Get the hint history after formatting it.
   *
   * @return - The formatted hint history.
   */
  public String getFormattedHintHistory() {
    // Initialize chat history text area
    StringBuilder hintHistoryText = new StringBuilder();
    for (String message : hintHistory) {
      hintHistoryText.append(message).append("\n");
    }

    return hintHistoryText.toString();
  }

  /**
   * Send a message to the ai and return the response.
   *
   * @param msg - The message to send to the ai.
   * @return - The response from the ai.
   * @throws ApiProxyException - If the request to the ai fails.
   */
  public ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {

    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      // appendChatMessage(result.getChatMessage());
      System.out.println(result.getChatMessage().getContent());
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }
}
