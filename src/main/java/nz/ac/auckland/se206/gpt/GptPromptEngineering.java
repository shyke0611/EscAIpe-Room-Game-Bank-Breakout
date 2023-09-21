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
    return "Generate me a riddle with money as the answer, do not tell the user the correct answer"
        + " or give them hints";
  }

  public static String initiliseComputerAI() {
    return "You are a sentient computer ai security system for Loreum Ipsum bank. Start with your"
        + " message with: Now that you've proven you're human, let's begin authentication."
        + " Next  ask 3 questions: 1. What year was this bank founded (correct: 1967)."
        + " Question 2. What is the most important vault ID (correct: 283). 3. CEO's name"
        + " (if the first letter of the user's answer starts with j it is correct but do not"
        + " mention this.if the user gets all 3 correct say Authenticated: higher security"
        + " level granted If the user gets at least one question right, say: Authenticated,"
        + " if the user gets them all wrong say: Authentication failed, .";
  }

  public static String welcomeMessage() {
    return "You are part of a bank heist escape room game application, your job is to act as though"
        + " you are a sentient AI security system of the bank, the name of the bank is"
        + " Loreum Ipsum. Please print a short, one-sentence welcome message to the user and"
        + " tell them to type 1 if they are ready to start the authentication process";
  }
}
