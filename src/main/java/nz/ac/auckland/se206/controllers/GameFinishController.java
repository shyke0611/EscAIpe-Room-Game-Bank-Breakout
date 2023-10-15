package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.Score;
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

  @FXML private TableView<Score> scoreTable;
  @FXML private TableColumn<Score, String> difficultyColumn;
  @FXML private TableColumn<Score, String> timeChosenColumn;
  @FXML private TableColumn<Score, String> timeTakenColumn;
  @FXML private TableColumn<Score, String> moneyStolenColumn;

  public void initialize() {
    // initialize the controller for the current scene
    SceneManager.setController(Scenes.GAMEFINISH, this);

    difficultyColumn.setCellValueFactory(new PropertyValueFactory<Score, String>("difficulty"));
    timeChosenColumn.setCellValueFactory(new PropertyValueFactory<Score, String>("timeChosen"));
    timeTakenColumn.setCellValueFactory(new PropertyValueFactory<Score, String>("timeTaken"));
    moneyStolenColumn.setCellValueFactory(new PropertyValueFactory<Score, String>("moneyStolen"));
  }

  public void setStatLabels() {
    // set the labels for the statistics
    String difficulty = GameManager.getDifficulty().toString();
    String timeChosen = TimerControl.getInitialTime() + " Minutes";
    String timeTaken = TimerControl.getTimeTaken();
    String moneyCollected = GameManager.getMoneyGained();

    difficultyLbl.setText(difficulty);
    timeChosenLbl.setText(timeChosen);
    timeLbl.setText(timeTaken);
    moneyLbl.setText(moneyCollected);

    scoreTable.getItems().add(new Score(difficulty, timeChosen, timeTaken, moneyCollected));
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
    mainmenuBtn.setDisable(false);
  }
}
