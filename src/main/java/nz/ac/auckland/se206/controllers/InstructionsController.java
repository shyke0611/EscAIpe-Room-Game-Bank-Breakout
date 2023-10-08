package nz.ac.auckland.se206.controllers;

import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class InstructionsController extends Controller {

  public void initialize() {
    SceneManager.setController(Scenes.INSTRUCTIONS, this);
  }
}
