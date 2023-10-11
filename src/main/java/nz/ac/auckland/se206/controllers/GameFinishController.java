package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.se206.AnimationManager;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.TimerControl;

public class GameFinishController extends Controller {
  @FXML private Button exitButton;
  @FXML private Button mainmenuBtn;
  @FXML private Label moneyLbl;
  @FXML private Label difficultyLbl;
  @FXML private Label timeLbl;
  @FXML private Label timeChosenLbl;
  @FXML private AnchorPane successPage;
  @FXML private AnchorPane failPage;

  public void initialize() {
    // initialize the controller for the current scene
    SceneManager.setController(Scenes.GAMEFINISH, this);
  }

  public void setDifficultyLabel(String difficulty) {
    difficultyLbl.setText(difficulty);
  }

  public void setStatLabels() {
    // set the labels for the statistics
    timeChosenLbl.setText(TimerControl.getInitialTime() + " Minutes");
    timeLbl.setText(TimerControl.getTimeTaken());
    moneyLbl.setText(GameManager.getMoneyGained());
  }

  public void setGameLostPage() {
    // set the game lost page
    successPage.setVisible(false);
    failPage.setVisible(true);
  }

  public void setGameWonPage() {
    // set the game won page
    failPage.setVisible(false);
    successPage.setVisible(true);
  }

  @FXML
  private void onExit(ActionEvent event) {
    // exit code here
    Platform.exit();
  }

  @FXML
  private void onSwitchToMainMenu(ActionEvent event) {
    App.setUI(Scenes.MAIN_MENU);
    mainmenuBtn.setDisable(true);
    // reset game code here
    GameManager.resetGame();
  }
}
