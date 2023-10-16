package nz.ac.auckland.se206;

import nz.ac.auckland.se206.GameManager.Difficulties;

/** An object to keep the record score of each game. */
public class Score {
  private Difficulties difficulty;
  private String timeChosen;
  private Integer timeTaken;
  private Integer moneyStolen;

  /**
   * Constructor for a creating new Score.
   *
   * @param difficulty - The difficulty of the game
   * @param timechosen - The time chosen by the user
   * @param timeTaken - The time taken to complete the game
   * @param moneyStolen - The amount of money stolen
   */
  public Score(Difficulties difficulty, String timechosen, Integer timeTaken, Integer moneyStolen) {
    this.difficulty = difficulty;
    this.timeChosen = timechosen;
    this.timeTaken = timeTaken;
    this.moneyStolen = moneyStolen;
  }

  /**
   * Get the difficulty of the game.
   *
   * @return - The difficulty of the game
   */
  public Difficulties getDifficulty() {
    return difficulty;
  }

  /**
   * Set the difficulty of the game.
   *
   * @param difficulty - The difficulty of the game
   */
  public void setDifficulty(Difficulties difficulty) {
    this.difficulty = difficulty;
  }

  /**
   * Get the time chosen by the user.
   *
   * @return - The time chosen by the user
   */
  public String getTimeChosen() {
    return timeChosen;
  }

  /**
   * Set the time chosen by the user.
   *
   * @param timeChosen - The time chosen by the user
   */
  public void setTimeChosen(String timeChosen) {
    this.timeChosen = timeChosen;
  }

  /**
   * Get the time taken to complete the game.
   *
   * @return - The time taken to complete the game
   */
  public Integer getTimeTaken() {
    return timeTaken;
  }

  /**
   * Set the time taken to complete the game.
   *
   * @param timeTaken - The time taken to complete the game
   */
  public void setTimeTaken(Integer timeTaken) {
    this.timeTaken = timeTaken;
  }

  /**
   * Get the amount of money stolen.
   *
   * @return - The amount of money stolen
   */
  public Integer getMoneyStolen() {
    return moneyStolen;
  }

  /**
   * Set the amount of money stolen.
   *
   * @param moneyStolen - The amount of money stolen
   */
  public void setMoneyStolen(Integer moneyStolen) {
    this.moneyStolen = moneyStolen;
  }
}
