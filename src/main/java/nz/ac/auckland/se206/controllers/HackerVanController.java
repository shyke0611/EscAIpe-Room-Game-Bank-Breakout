package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class HackerVanController extends Controller {


  @FXML private Button goBackBtn;

  @FXML private TextArea hintTextArea;

  @FXML private TextArea historyTextArea;

  public void onGoBack() {
   App.setUI(SceneManager.getPreviousScene(Scenes.HACKERVAN));
  }
}
