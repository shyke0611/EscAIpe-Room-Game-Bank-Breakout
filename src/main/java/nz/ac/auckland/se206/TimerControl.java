package nz.ac.auckland.se206;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.controllers.GameFinishController;

public class TimerControl {

  private static int initialCount = 10;
  private static int count;
  private static int i = 0;

  private static Timer timer;
  private static TimerTask task;

  public static String getTime(int format) {
    return formatTime(count, format);
  }

  public static String formatTime(int time, int format) {
    int minutes = time / 60;
    String minutesSuffix = minutes == 1 ? " Minute " : " Minutes ";

    switch (format) {
      case 1:
        // Short format e.g. 0:00
        return "" + minutes + ":" + getSecondsString(time);
      case 2:
        // Medium format e.g. 0 Minutes and 00 Seconds
        return "" + minutes + minutesSuffix + "and " + getSecondsString(time) + " Seconds";
      case 3:
        // Long format e.g. Time Remaining: 0 Minutes and 00 Seconds
        return "Time Remaining: "
            + minutes
            + minutesSuffix
            + " and "
            + getSecondsString(time)
            + " Seconds";
      case 4:
        // Medium format adjusted e.g. 0 Minutes 0 Seconds
        return "" + minutes + minutesSuffix + (time % 60) + " Seconds";
      default:
        return "" + minutes + ":" + getSecondsString(time);
    }
  }

  private static String getSecondsString(int time) {
    int seconds = time % 60;
    String secondString = seconds < 10 ? "0" + seconds : "" + seconds;
    return secondString;
  }

  public static int getCount() {
    return count;
  }

  // method to set timer count
  public static void setTimer(int minutes) {
    // set initial count(seconds) based on minutes
    switch (minutes) {
      case 2:
        initialCount = 32;
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
    i = 0;
    count = initialCount;
  }

  private static void createTask() {
    // Create new timer task
    task =
        new TimerTask() {
          public void run() {
            i++;

            // if time is not up, update timer label
            if (count > 0) {
              if (i % 2 == 0) {
                count--;
                if (count == 30 && !GameState.isAlarmTripped) {
                  GameState.isAlarmTripped = true;
                  GameManager.completeObjective();
                  StyleManager.setAlarm(true);
                }
              }
              int format = SceneManager.getActiveController().getFormat();

              Platform.runLater(
                  () -> {
                    SceneManager.getTimerLabel().setText(getTime(format));
                  });
              // if time is up, reset game

            } else {
              ((GameFinishController) SceneManager.getController(Scenes.GAMEFINISH))
                  .setGameLostPage();
              GameManager.loseMoney();
              App.setUI(Scenes.GAMEFINISH);
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
    timer.scheduleAtFixedRate(task, 0, 500);
  }

  // method to cancel timer
  public static void cancelTimer() {
    task.cancel();
    timer.cancel();
    timer.purge();
  }

  public static String getTimeTaken() {
    int timeTaken = initialCount - count;
    if (timeTaken == initialCount) {
      return "Failed to Escape";
    }
    String time = formatTime(timeTaken, 4);
    return time;
  }

  public static String getInitialTime() {
    return "" + (initialCount / 60);
  }
}
