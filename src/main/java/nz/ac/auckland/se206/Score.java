package nz.ac.auckland.se206;

import nz.ac.auckland.se206.difficulties.Difficulty.Difficulties;

public class Score {
  private Difficulties difficulty;
  private String timeChosen;
  private Integer timeTaken;
  private Integer moneyStolen;

  public Score(Difficulties difficulty, String timechosen, Integer timeTaken, Integer moneyStolen) {
    this.difficulty = difficulty;
    this.timeChosen = timechosen;
    this.timeTaken = timeTaken;
    this.moneyStolen = moneyStolen;
  }

  public Difficulties getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(Difficulties difficulty) {
    this.difficulty = difficulty;
  }

  public String getTimeChosen() {
    return timeChosen;
  }

  public void setTimeChosen(String timeChosen) {
    this.timeChosen = timeChosen;
  }

  public Integer getTimeTaken() {
    return timeTaken;
  }

  public void setTimeTaken(Integer timeTaken) {
    this.timeTaken = timeTaken;
  }

  public Integer getMoneyStolen() {
    return moneyStolen;
  }

  public void setMoneyStolen(Integer moneyStolen) {
    this.moneyStolen = moneyStolen;
  }
}
