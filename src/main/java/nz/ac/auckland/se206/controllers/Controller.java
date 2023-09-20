package nz.ac.auckland.se206.controllers;

import javafx.scene.control.Label;

public abstract class Controller {
  private Label timerLabel;
  private int format;

  public void setTimerLabel(Label timerLabel, int format) {
    this.timerLabel = timerLabel;
    this.format = format;
  }

  public Label getTimerLabel() {
    return timerLabel;
  }

  public int getFormat() {
    return format;
  }
}
