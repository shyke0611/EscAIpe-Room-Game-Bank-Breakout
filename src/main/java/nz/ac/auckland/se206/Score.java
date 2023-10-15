package nz.ac.auckland.se206;

public class Score {
  private String difficulty;
  private String timeChosen;
  private String timeTaken;
  private String moneyStolen;

  public Score(String difficulty, String timechosen, String timeTaken, String moneyStolen) {
    this.difficulty = difficulty;
    this.timeChosen = timechosen;
    this.timeTaken = timeTaken;
    this.moneyStolen = moneyStolen;
  }

  public String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public String getTimeChosen() {
    return timeChosen;
  }

  public void setTimeChosen(String timeChosen) {
    this.timeChosen = timeChosen;
  }

  public String getTimeTaken() {
    return timeTaken;
  }

  public void setTimeTaken(String timeTaken) {
    this.timeTaken = timeTaken;
  }

  public String getMoneyStolen() {
    return moneyStolen;
  }

  public void setMoneyStolen(String moneyStolen) {
    this.moneyStolen = moneyStolen;
  }
}
