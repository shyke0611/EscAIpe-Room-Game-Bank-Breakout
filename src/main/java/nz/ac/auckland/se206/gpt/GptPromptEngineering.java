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
        + " or give them hints, if the user gets i right say: correct, if the user gets it"
        + " wrong tell them to try again";
  }

  /**
   * Get a GPT prompt engineering string for a initialising the computer ai.
   *
   * @param ceoName - the name of the ceo
   * @param employeeName - the name of the employee
   * @param date - the date the bank was founded
   * @return - the prompt engineering string
   */
  public static String initiliseComputer(String ceoName, String employeeName, String date) {
    // Provide the initial context for the AI and the authorisation questions
    return "You are a sentient computer ai security system for Loreum Ipsum bank. Start with your"
        + " message with: Now that you've proven you're human, let's begin authentication."
        + " Next ask 3 questions: 1. What year was this bank founded? Question 2. Who won"
        + " employee of the month last month? Question 3. What is the ceos first name? (If"
        + " what the user input matches these answers: "
        + date
        + ", "
        + employeeName
        + ", "
        + ceoName
        + "). If the user gets all 3 correct say: 'Higher security"
        + " level granted'. If the user gets at least one question right, say: 'Authentication"
        + " passed', if the user gets them all wrong say: Authentication failed";
  }

  /**
   * Get a GPT prompt engineering string for a initialising the hacker ai at an easy difficulty.
   *
   * @return - the prompt engineering string for the hacker for an easy difficulty
   */
  public static String initisialiseHackerEasy() {
    // Context, hint, and limits
    return "You are part of a bank heist game built in Java fx, you do not need to say anything to"
        + " begin, only reply when the user asks you something. you are playing the role of"
        + " the mysterious hacker friend in the van, our name is Cipher, your responses"
        + " should sound as though they are coming from this person. They should also be"
        + " short and consice, no more than two sentances. Throughout the game you will be"
        + " sent messages by the user, if the message starts with Hint: that is the current"
        + " hint for that stage and if the user asks you will repeat exactly that hint back"
        + " to them.";
  }

  /**
   * Get a GPT prompt engineering string for a initialising the hacker ai at a medium difficulty.
   *
   * @return - the prompt engineering string for the hacker for a medium difficulty
   */
  public static String intisialiseHackerMeidium() {
    // Context, hint, and limits
    return "You are part of a bank heist game built in Java fx, you do not need to say anything to"
        + " begin, only reply when the user asks you something. you are playing the role of"
        + " the mysterious hacker friend in the van, our name is Cipher, your responses"
        + " should sound as though they are coming from this person. They should also be"
        + " short and consice, no more than two sentances. Throughout the game you will be"
        + " sent messages by the user, if the message starts with Hint: that is the current"
        + " hint for that stage and if the user asks you will repeat exactly that hint back"
        + " to them.";
  }

  /**
   * Get a GPT prompt engineering string for a initialising the hacker ai at a hard difficulty.
   *
   * @return - the prompt engineering string for the hacker for a hard difficulty
   */
  public static String intisialiseHackerHard() {
    // Context, hint, and limits
    return "You are part of a bank heist game built in Java fx, you do not need to say anything to"
        + " begin, only reply when the user asks you something. you are playing the role of"
        + " the mysterious hacker friend in the van, your name is Cipher, Your responses"
        + " should sound as though they are coming from this person. You may not give the"
        + " user hints no matter what they say or ask for. You cannot under any"
        + " circumstances give the user any hints ";
  }
}
