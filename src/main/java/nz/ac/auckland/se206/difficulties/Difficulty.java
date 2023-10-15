package nz.ac.auckland.se206.difficulties;

public abstract class Difficulty {

  // The different difficulties
  public enum Difficulties {
    HARD,
    MEDIUM,
    EASY
  }

  public static String toStringEnum(Difficulties difficulty) {
    switch (difficulty) {
      case EASY:
        return "Easy";
      case MEDIUM:
        return "Medium";
      case HARD:
        return "Hard";
      default:
        return null;
    }
  }

  protected int totalHints;
  protected int remainingHints;

  public Difficulty(int totalHints) {
    this.totalHints = totalHints;
    this.remainingHints = totalHints;
  }

  public int getTotalHints() {
    return totalHints;
  }

  public int getRemainingHints() {
    return remainingHints;
  }

  public void useHint() {
    remainingHints--;
  }

  public abstract String toString();
}
