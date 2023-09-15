package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class ComputerController extends Controller {

  @FXML
  private Button goBackBtn;
  @FXML
  private TextField inputTextField;
  @FXML
  private Button quickHintBtn;
  @FXML
  private TextArea securityTextArea;
  @FXML
  private Button sendBtn;
  @FXML
  private Button viewHistoryBtn;
  @FXML
  private VBox walkietalkie;
  @FXML
  private VBox walkietalkieText;

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

  public void onSwitchToHacker() {
    SceneManager.setPreviousScene(Scenes.HACKERVAN, Scenes.COMPUTER);
    App.setUI(Scenes.HACKERVAN);
  }

  //   handling mouse events on walkie talkie
  //   open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    SceneManager.toggleWalkieTalkieOpen();
  }

  public void synchWalkieTalkie(boolean isOpen) {
    walkietalkieText.setVisible(isOpen);
  }
}
