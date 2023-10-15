package nz.ac.auckland.se206.controllers;

import java.text.DecimalFormat;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.Score;
import nz.ac.auckland.se206.TimerControl;
import nz.ac.auckland.se206.difficulties.Difficulty;
import nz.ac.auckland.se206.difficulties.Difficulty.Difficulties;

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
  @FXML private TableColumn<Score, Difficulties> difficultyColumn;
  @FXML private TableColumn<Score, String> timeChosenColumn;
  @FXML private TableColumn<Score, String> timeTakenColumn;
  @FXML private TableColumn<Score, Number> moneyStolenColumn;

  public void initialize() {
    // initialize the controller for the current scene
    SceneManager.setController(Scenes.GAMEFINISH, this);

    difficultyColumn.setCellValueFactory(
        new PropertyValueFactory<Score, Difficulties>("difficulty"));
    timeChosenColumn.setCellValueFactory(new PropertyValueFactory<Score, String>("timeChosen"));
    timeTakenColumn.setCellValueFactory(new PropertyValueFactory<Score, String>("timeTaken"));
    moneyStolenColumn.setCellValueFactory(new PropertyValueFactory<Score, Number>("moneyStolen"));

    // Format all values in the difficulty column to be formatted as a string
    difficultyColumn.setCellFactory(
        column -> {
          return new javafx.scene.control.TableCell<Score, Difficulties>() {
            @Override
            protected void updateItem(Difficulties item, boolean empty) {
              super.updateItem(item, empty);
              if (item == null || empty) {
                setText(null);
              } else {
                setText(Difficulty.toStringEnum(item));
              }
            }
          };
        });

    // Format all values in the moneyStolenColumn to be formatted as currency using the formatter
    DecimalFormat formatter = new DecimalFormat("$#,###");
    moneyStolenColumn.setCellFactory(
        column -> {
          return new javafx.scene.control.TableCell<Score, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
              super.updateItem(item, empty);
              if (item == null || empty) {
                setText(null);
              } else {
                setText(formatter.format(item));
              }
            }
          };
        });

    // Disable left click on table
    scoreTable.setRowFactory(
        tv -> {
          TableRow<Score> row = new TableRow<>();
          row.addEventFilter(
              MouseEvent.MOUSE_PRESSED,
              e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                  e.consume();
                }
              });
          return row;
        });
  }

  public void resetSortOrder() {
    scoreTable.getSortOrder().clear();
    scoreTable.getSortOrder().add(difficultyColumn);
    scoreTable.getSortOrder().add(moneyStolenColumn);
    scoreTable.getSortOrder().add(timeChosenColumn);
    scoreTable.getSortOrder().add(timeTakenColumn);
  }

  public void setStatLabels() {
    resetSortOrder();

    // set the labels for the statistics
    Difficulties difficulty = GameManager.getDifficulty();
    String timeChosen = TimerControl.getInitialTime() + " Minutes";
    String timeTaken = TimerControl.getTimeTaken();
    int moneyCollected = GameManager.getMoneyGained();

    difficultyLbl.setText(Difficulty.toStringEnum(difficulty));
    timeChosenLbl.setText(timeChosen);
    timeLbl.setText(timeTaken);
    moneyLbl.setText(GameManager.formatMoney(moneyCollected));

    Score newScore = new Score(difficulty, timeChosen, timeTaken, moneyCollected);
    scoreTable.getItems().add(newScore);
    scoreTable.sort();
    scoreTable.getSelectionModel().select(newScore);
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
    mainmenuBtn.setDisable(true);
    // reset game code here
    GameManager.resetGame();
    App.setUI(Scenes.MAIN_MENU);
    mainmenuBtn.setDisable(false);
    resetSortOrder();
  }
}
