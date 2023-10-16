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
import nz.ac.auckland.se206.GameManager.Difficulties;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.Score;
import nz.ac.auckland.se206.TimerControl;

/** Controller class for the Game Finish scene. */
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
  @FXML private TableColumn<Score, Number> timeTakenColumn;
  @FXML private TableColumn<Score, Number> moneyStolenColumn;

  /** Initialize the GameFinish Controller. Sets up the initial state of the scene. */
  public void initialize() {
    // initialize the controller for the current scene
    SceneManager.setController(Scenes.GAMEFINISH, this);

    // set the table columns
    difficultyColumn.setCellValueFactory(
        new PropertyValueFactory<Score, Difficulties>("difficulty"));
    timeChosenColumn.setCellValueFactory(new PropertyValueFactory<Score, String>("timeChosen"));
    timeTakenColumn.setCellValueFactory(new PropertyValueFactory<Score, Number>("timeTaken"));
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
                setText(GameManager.getDifficultyString(item));
              }
            }
          };
        });

    // Format all values in the time taken column to be formatted
    timeTakenColumn.setCellFactory(
        column -> {
          return new javafx.scene.control.TableCell<Score, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
              super.updateItem(item, empty);
              if (item == null || empty) {
                setText(null);
              } else {
                setText(TimerControl.formatTimeTaken(item.intValue()));
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

  /** Reset the sort order of the table columns. */
  public void resetSortOrder() {
    // reset the sort order of the table
    scoreTable.getSortOrder().clear();
    scoreTable.getSortOrder().add(difficultyColumn);
    scoreTable.getSortOrder().add(moneyStolenColumn);
    scoreTable.getSortOrder().add(timeChosenColumn);
    scoreTable.getSortOrder().add(timeTakenColumn);
  }

  /** Set the labels with game statistics and add a new score to the score table. */
  public void setStatLabels() {

    // Retrieve the statistics from the game
    Difficulties difficulty = GameManager.getDifficulty();
    String timeChosen = TimerControl.getInitialTime() + " Minutes";
    int timeTaken = TimerControl.getTimeTaken();
    int moneyCollected = GameManager.getMoneyGained();

    // set the labels for the statistics
    difficultyLbl.setText(GameManager.getDifficultyString(difficulty));
    timeChosenLbl.setText(timeChosen);
    timeLbl.setText(TimerControl.formatTimeTaken(timeTaken));
    moneyLbl.setText(GameManager.formatMoney(moneyCollected));

    // add the new score to the table and sort table
    Score newScore = new Score(difficulty, timeChosen, timeTaken, moneyCollected);
    scoreTable.getItems().add(newScore);
    resetSortOrder();
    scoreTable.sort();
    scoreTable.getSelectionModel().select(newScore);
  }

  /** Set the page to display the game as lost. */
  public void setGameLostPage() {
    // set the game lost page
    successPage.setVisible(false);
    failPage.setVisible(true);
  }

  /** Set the page to display the game as won. */
  public void setGameWonPage() {
    // set the game won page
    failPage.setVisible(false);
    successPage.setVisible(true);
  }

  /**
   * Handle the "Exit" button click event to exit the application.
   *
   * @param event The action event triggered by the "Exit" button click.
   */
  @FXML
  private void onExit(ActionEvent event) {
    // exit code here
    Platform.exit();
  }

  /**
   * Handle the "Main Menu" button click event to switch to the main menu scene.
   *
   * @param event The action event triggered by the "Main Menu" button click.
   */
  @FXML
  private void onSwitchToMainMenu(ActionEvent event) {
    mainmenuBtn.setDisable(true);
    // reset game code here
    GameManager.resetGame();
    App.setUi(Scenes.MAIN_MENU);
    mainmenuBtn.setDisable(false);
    resetSortOrder();
  }
}
