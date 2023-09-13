package nz.ac.auckland.se206.difficulties;

public abstract class Difficulty {
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
}
