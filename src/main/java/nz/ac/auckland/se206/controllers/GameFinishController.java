package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class GameFinishController extends Controller {
  @FXML private Button exitButton;
  @FXML private TextArea leaderboard;
  @FXML private Button mainmenuBtn;
  @FXML private Label moneyLbl;
  @FXML private Label difficultyLbl;
  @FXML private Label timeLbl;
  @FXML private AnchorPane successPage;
  @FXML private AnchorPane failPage;

  public void setDifficultyLabel(String difficulty) {
    difficultyLbl.setText(difficulty);
  }

  public void setTimeTakenLabel(String time) {
    timeLbl.setText(time + " Minutes");
  }

  // public void setMoneyStolenLabel() {
  //   moneyLbl.setText("$" + String.valueOf(moneyStolen));
  // }

  public void setGameWonPage() {
    failPage.setVisible(false);
  }

  public void setGameLostPage() {
    successPage.setVisible(false);
  }

  @FXML
  void onExit(ActionEvent event) {
    // exit code here
    Platform.exit();
  }

  @FXML
  void onSwitchToMainMenu(ActionEvent event) {
    App.setUI(Scenes.MAIN_MENU);
    // reset game code here
  }

  public void initialize() {
    SceneManager.setController(Scenes.GAMEFINISH, this);
  }
}
