package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import nz.ac.auckland.se206.difficulties.Difficulty.Difficulties;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class HackerAiManager {

  private int hintLimit;
  private int hintCounter;
  private static HackerAiManager instance = new HackerAiManager();
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
  private ChatMessage tellAiContext;

  private ChatCompletionRequest chatCompletionRequest;
  private Difficulties currentDifficulty;

  public HackerAiManager() {
    instance = this;
    // Initialize the hint mappings
    hintMappings.put(
        "Find Keys", "You must distract the guard to look in the places where the keys may be.");
    hintMappings.put(
        "Find Passcode", "The passcode is in the drawer on the wall, use the keys to unlock it");
    hintMappings.put(
        "Login", "Use the credentials on the note to login to the computer, in security.");
    hintMappings.put("Disable Firewall", "Convince the AI you are an employee to log in");
    hintMappings.put("Complete Minigame", "Complete the connect dots minigame to proceed");
    hintMappings.put(
        "Scan Eye",
        "You must scan the guards eye and use it to match your fake eye so you can get through the"
            + " door");
    hintMappings.put("Mix Chemicals", "Mix the chemicals in the correct order to melt the lock");
    hintMappings.put(
        "Cut Through Lasers", "You must use your laser gun to cut a hole and get through the door");
    hintMappings.put(
        "Disable Lasertrap",
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

  // Method to set hint limits for different game stages
  public void setHintLimit(int limit) {
    hintLimit = limit;
  }

  public void initialiseHackerAi(Difficulties difficulty) throws ApiProxyException {

    // Initialise the ai based on the difficulty
    switch (difficulty) {
      case EASY:
        currentDifficulty = Difficulties.EASY;
        setHintLimit(-1);
        hintCounter = -1;
        chatCompletionRequest =
            new ChatCompletionRequest().setN(1).setTemperature(0.3).setTopP(1).setMaxTokens(100);
        runGpt(new ChatMessage("user", GptPromptEngineering.initisialiseHackerAiEasy()));
        break;

        // Initialise the ai for the medium difficulty
      case MEDIUM:
        setHintLimit(5);
        hintCounter = 5;
        currentDifficulty = Difficulties.MEDIUM;
        chatCompletionRequest =
            new ChatCompletionRequest().setN(1).setTemperature(0.3).setTopP(1).setMaxTokens(100);
        runGpt(new ChatMessage("user", GptPromptEngineering.intisialiseHackerAiMeidium()));
        break;

        // Initialise the ai for the hard difficulty
      case HARD:
        setHintLimit(0);
        hintCounter = -1;
        currentDifficulty = Difficulties.HARD;
        chatCompletionRequest =
            new ChatCompletionRequest().setN(1).setTemperature(0.3).setTopP(1).setMaxTokens(100);
        runGpt(new ChatMessage("user", GptPromptEngineering.intisialiseHackerAiHard()));
        break;
    }
  }

  public static HackerAiManager getInstance() {
    return instance;
  }

  public void incrementHintCounter() {
    hintCounter--;
  }

  // Method to get a hint for the current game stage
  public String getHintForCurrentStage(String currentStage) {
    return hintMappings.getOrDefault(currentStage, "No hint available for this stage.");
  }

  public String GetQuickHint() {
    currentStage = GameManager.getObjectiveString();

    if (Difficulties.EASY == currentDifficulty) {
      // Update the visual hint counter and retrieve hint
      Platform.runLater(() -> walkieTalkieManager.setHintText("Unlimited"));
      hint = getHintForCurrentStage(currentStage);
    } else if (Difficulties.HARD == currentDifficulty) {
      // Update the visual hint counter and return no hint
      Platform.runLater(() -> walkieTalkieManager.setHintText("0"));
      hint = "You are not allow to have hints ";
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

    return hint;
  }

  public void storeAiHint(ChatMessage msg) {
    String hint = msg.getContent();
    // Check if the hint is unique
    if (!hintHistory.contains(hint)) {

      String formattedHint = hintNumber + ". " + hint;
      hintHistory.add(formattedHint);
      hintNumber++;
    }
  }

  public boolean stringExists(String existingHint) {
    for (String hint : hintHistory) {
      if (hint.equals(existingHint)) {
        return true;
      }
    }
    return false;
  }

  public void storeQuickHint() {

    String hint = GetQuickHint();
    incrementHintCounter();
    System.out.println(hint);
    System.out.println(hintCounter);

    // Check if the hint is unique
    if (!stringExists(hint)) {

      String formattedHint = hintNumber + ". " + hint;
      hintHistory.add(formattedHint);
      hintNumber++;
    }
  }

  public void addChatHistory(String chat) {
    chatHistory.add(chat);
  }

  public List<String> getChatHistory() {
    return chatHistory;
  }

  // Method gets gets the current stages hint and feeds that to the ai before feeding the ai the
  // users message
  public ChatMessage processInput(ChatMessage msg) throws ApiProxyException {

    // Replace 'msg' with the appropriate input
    if (currentDifficulty == Difficulties.MEDIUM && hintCounter > 0) {
      currentStage = GameManager.getObjectiveString();
      hint = getHintForCurrentStage(currentStage);
      tellAiContext = new ChatMessage("user", "Context:" + contextMappsing.get(currentStage));
      tellAiHint = new ChatMessage("user", "Hint:" + hint);
      ChatMessage gptCall =
          new ChatMessage("user", tellAiHint.getContent() + tellAiContext.getContent());
      runGpt(gptCall);

      response = runGpt(msg);

    } else if (currentDifficulty == Difficulties.HARD) {
      // Things to add, update ai to say what has happend during round
      tellAiContext = new ChatMessage("user", contextMappsing.get(currentStage));
      runGpt(tellAiContext);

      response = runGpt(msg);

    } else if (currentDifficulty == Difficulties.EASY) {
      currentStage = GameManager.getObjectiveString();
      hint = getHintForCurrentStage(currentStage);
      tellAiContext = new ChatMessage("user", "Context:" + contextMappsing.get(currentStage));
      tellAiHint = new ChatMessage("user", "the current hint for stage is " + hint);
      runGpt(tellAiHint);
      runGpt(tellAiContext);
      response = runGpt(msg);

    } else {
      tellAiHint = new ChatMessage("user", "You have used all your hints");
      tellAiContext = new ChatMessage("user", contextMappsing.get(currentStage));
      ChatMessage gptCall =
          new ChatMessage("user", tellAiHint.getContent() + tellAiContext.getContent());
      runGpt(gptCall);
    }

    if (userIsAiAskingForHelp(msg.getContent())) {
      storeAiHint(response);
      incrementHintCounter();
      if (hintCounter > 0) {
        Platform.runLater(() -> walkieTalkieManager.setHintText(Integer.toString(hintCounter)));
      }
    }
    return response;
  }

  public boolean userIsAiAskingForHelp(String msg) {
    // Convert the message to lowercase for case-insensitive matching
    String lowercaseMsg = msg.toLowerCase();

    // Define an array of keywords to check for
    String[] keywords = {
      "help", "hint", "assist", "support", "clue", "pointer", "aid", "guide", "give me a hand"
    };

    // Check if any of the keywords is present in the message
    for (String keyword : keywords) {
      if (lowercaseMsg.contains(keyword)) {
        return true; // User is asking for help
      }
    }

    return false; // No help-related keywords found in the message
  }

  public String getFormattedChatHistory() {
    // Initialize chat history text area
    StringBuilder chatHistoryText = new StringBuilder();
    for (String message : chatHistory) {
      chatHistoryText.append(message).append("\n");
    }

    return chatHistoryText.toString();
  }

  public String getFormattedHintHistory() {
    // Initialize chat history text area
    StringBuilder hintHistoryText = new StringBuilder();
    for (String message : hintHistory) {
      hintHistoryText.append(message).append("\n");
    }

    return hintHistoryText.toString();
  }

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
      // TODO handle exception appropriately
      e.printStackTrace();
      return null;
    }
  }
}
