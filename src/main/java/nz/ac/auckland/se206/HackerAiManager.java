package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.GptPromptEngineering;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class HackerAiManager {

  private int hintLimit;
  private int hintCounter = 0;
  private static HackerAiManager instance = new HackerAiManager();

  // Map to store hints for different game stages
  private Map<String, String> hintMappings = new HashMap<>();
  private List<String> chatHistory = new ArrayList<>();
  private List<String> hintHistory = new ArrayList<>();

  private int hintNumber = 1; // Hint number for hint history
  private ChatMessage response;
  private ChatMessage tellAI;
  private String currentStage;
  private String hint;

  private ChatCompletionRequest chatCompletionRequest;
  private Difficulties currentDifficulty;

  public enum Difficulties {
    EASY,
    MEDIUM,
    HARD
  }

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
  }

  // Method to set hint limits for different game stages
  public void setHintLimit(int limit) {
    hintLimit = limit;
  }

  public void initialiseHackerAi(Difficulties difficulty) throws ApiProxyException {

    switch (difficulty) {
      case EASY:
        currentDifficulty = Difficulties.EASY;
        setHintLimit(1000);
        chatCompletionRequest =
            new ChatCompletionRequest().setN(1).setTemperature(0.8).setTopP(0.9).setMaxTokens(100);
        runGpt(new ChatMessage("user", GptPromptEngineering.initisialiseHackerAiEasy()));

        break;
      case MEDIUM:
        setHintLimit(5);
        currentDifficulty = Difficulties.MEDIUM;
        chatCompletionRequest =
            new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.8).setMaxTokens(100);
        runGpt(new ChatMessage("user", GptPromptEngineering.intisialiseHackerAiMeidium()));

        break;
      case HARD:
        setHintLimit(0);
        currentDifficulty = Difficulties.HARD;
        chatCompletionRequest =
            new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.8).setMaxTokens(100);
        runGpt(new ChatMessage("user", GptPromptEngineering.intisialiseHackerAiHard()));
        break;
    }
  }

  public static HackerAiManager getInstance() {
    return instance;
  }

  public void incrementHintCounter() {
    hintCounter++;
  }

  // Method to get a hint for the current game stage
  public String getHintForCurrentStage(String currentStage) {
    return hintMappings.getOrDefault(currentStage, "No hint available for this stage.");
  }

  public String GetQuickHint() {
    currentStage = GameManager.getObjectiveString();
    hint = getHintForCurrentStage(currentStage);
    hintCounter++;
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

  public void storeQuickHint() {

    String hint = GetQuickHint();

    // Check if the hint is unique
    if (!hintHistory.contains(hint)) {

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
    if (currentDifficulty == Difficulties.MEDIUM && hintCounter < hintLimit) {
      incrementHintCounter();
      currentStage = GameManager.getObjectiveString();
      hint = getHintForCurrentStage(currentStage);
      tellAI = new ChatMessage("user", "the hint for this stage is " + hint);

      runGpt(tellAI);
      response = runGpt(msg);

    } else if (currentDifficulty == Difficulties.HARD) {
      // Things to add, update ai to say what has happend during round
      tellAI = new ChatMessage("user", "You cannot give any hints no matter what");
      runGpt(tellAI);
      response = runGpt(msg);

    } else if (currentDifficulty == Difficulties.EASY) {
      currentStage = GameManager.getObjectiveString();
      hint = getHintForCurrentStage(currentStage);
      tellAI = new ChatMessage("user", "the current hint for stage is " + hint);
      runGpt(tellAI);
      response = runGpt(msg);

    } else {
      tellAI = new ChatMessage("user", "You have used all your hints");
      runGpt(tellAI);
      response = runGpt(msg);
    }

    if (msg.getContent().contains("hint")
        || msg.getContent().contains("Hint") && response.getContent().contains("hint")
        || response.getContent().contains("Hint")) {
      storeAiHint(response);
    }
    return response;
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
