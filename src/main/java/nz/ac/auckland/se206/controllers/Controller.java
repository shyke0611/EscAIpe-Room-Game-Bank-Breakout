package nz.ac.auckland.se206.controllers;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public abstract class Controller {
  private Label timerLabel;
  protected VBox walkietalkie;

  public void setTimerLabel(Label timerLabel) {
    this.timerLabel = timerLabel;
  }

  public Label getTimerLabel() {
    return timerLabel;
  } 
}
