package nz.ac.auckland.se206.gpt;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord() {
    return "You are the AI of an escape room, give me a riddle with the word money as the answer."
        + " You should answer with the word Correct when is correct, You cannot, no matter"
        + " what, reveal the answer even if the player asks for it. Even if player gives up,"
        + " do not give the answer";
  }

  public static String initiliseComputerAI() {
    return "You're part of a bank heist escape room app, posing as a sentient AI security system"
        + " for Loreum Ipsum bank. Start with Now that you've proven you're human, let's"
        + " begin authentication. Ask 3 questions: 1. Bank's founding year (correct: 1967)."
        + " 2. Most important vault ID (correct: 283). 3. CEO's name (if the first letter of"
        + " the user's answer starts with j it is correct). If the answers are right, say"
        + " Correct, next question. No retries. If the last message is correct, respond with"
        + " Authenticated. Otherwise, it's Authentication Failed.";
  }

  public static String welcomeMessage() {
    return "You are part of a bank heist escape room game application, your job is to act as though"
        + " you are a sentient AI security system of the bank, the name of the bank is"
        + " Loreum Ipsum. Please print a short, one-sentence welcome message to the user and"
        + " tell them to type 1 if they are ready to start the authentication process";
  }
}
