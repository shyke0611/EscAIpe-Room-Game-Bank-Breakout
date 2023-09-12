package nz.ac.auckland.se206;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

public class TimerControl {

  private static int initialCount = 0;
  private static int count;

  private static Timer timer;
  private static TimerTask task;

  public static String getTime() {
    int minutes = count / 60;
    String time = "" + minutes + ":" + getSecondsString();
    return time;
  }

  private static String getSecondsString() {
    int seconds = count % 60;
    String secondString = seconds < 10 ? "0" + seconds : "" + seconds;
    return secondString;
  }

  public static int getCount() {
    return count;
  }

  // method to set timer count
  public static void setCount(int minutes) {
    switch (minutes) {
      case 2:
        initialCount = 120;
        break;
      case 4:
        initialCount = 240;
        break;
      case 6:
        initialCount = 360;
        break;
      default:
        initialCount = minutes * 60;
        break;
    }
  }

  public static void resetCount() {
    count = initialCount;
  }

  private static void createTask() {
    // Create new timer task
    task =
        new TimerTask() {
          public void run() {

            // if time is not up, update timer label
            if (count > 0) {
              count--;
              Platform.runLater(
                  () -> {
                    SceneManager.getTimerLabel().setText(getTime());
                  });
              // if time is up, reset game
            } else {
              cancelTimer();
            }
          }
        };
  }

  // method to create timer thread and run timer
  public static void runTimer() {

    // kill previous timer if it exists
    if (timer != null && task != null) {
      cancelTimer();
    }

    resetCount();
    timer = new Timer("Timer", true);
    createTask();
    timer.scheduleAtFixedRate(task, 0, 1000);
  }

  // method to cancel timer
  public static void cancelTimer() {
    task.cancel();
    timer.cancel();
    timer.purge();
  }
}
