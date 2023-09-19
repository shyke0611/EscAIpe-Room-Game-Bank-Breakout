package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.StyleManager;

public abstract class Controller {
  private Label timerLabel;

  public void setTimerLabel(Label timerLabel) {
    this.timerLabel = timerLabel;
  }

  public Label getTimerLabel() {
    return timerLabel;
  }
}
