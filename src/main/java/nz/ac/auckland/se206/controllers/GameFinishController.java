package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class GameFinishController extends Controller {
  @FXML private Button exitButton;
  @FXML private TextArea leaderboard;
  @FXML private Button mainmenuBtn;
  @FXML private Label moneyLbl;
  @FXML private Label outcomeLbl;
  @FXML private Label timeLbl;

  @FXML
  void onExit(ActionEvent event) {
    // exit code here
    Platform.exit();
  }

  @FXML
  void onSwitchToMainMenu(ActionEvent event) {
    App.setUI(Scenes.MAIN_MENU);
    // reset game code here
    GameManager.resetGame();
  }

  public void initialize() {
    SceneManager.setController(Scenes.GAMEFINISH, this);
  }
}
