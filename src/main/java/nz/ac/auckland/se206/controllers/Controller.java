package nz.ac.auckland.se206.controllers;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.SceneManager;

public abstract class Controller {
  private Label timerLabel;


  public void setTimerLabel(Label timerLabel) {
    this.timerLabel = timerLabel;
  }

  public Label getTimerLabel() {
    return timerLabel;
  } 
}
