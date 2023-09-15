package nz.ac.auckland.se206.controllers;

import javafx.scene.control.Label;

public abstract class Controller {
  private Label timerLabel;

  public void setTimerLabel(Label timerLabel) {
    this.timerLabel = timerLabel;
  }

  public Label getTimerLabel() {
    return timerLabel;
  } 
}
