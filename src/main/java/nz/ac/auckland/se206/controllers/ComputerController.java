package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class ComputerController extends Controller {

  @FXML private Button goBackBtn;

  @FXML private TextField inputTextField;

  @FXML private TextArea securityTextArea;

  @FXML private Button sendBtn;

  public void initialize() {
    SceneManager.setController(Scenes.COMPUTER,this);
  }

  // exit computer view back to security room
  @FXML
  void onGoBack(ActionEvent event) {
    App.setUI(Scenes.SECURITY);
  }

  @FXML
  void onSend(ActionEvent event) {}
}
