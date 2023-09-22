package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.HackerAiManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.TimerControl;
import nz.ac.auckland.se206.difficulties.Difficulty.Difficulties;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

/** Controller class for the Difficulty Selection scene. */
public class DifficultyController extends Controller {

  @FXML private VBox buttonsContainer;
  @FXML private ImageView easyAlarmImage;
  @FXML private ImageView hardAlarmImage;
  @FXML private VBox easyVbox;
  @FXML private VBox hardVbox;
  @FXML private ImageView mediumAlarmImage;
  @FXML private VBox mediumVbox;
  @FXML private Label titleLabel1;
  @FXML private Label titleLabel2;
  @FXML private Label difficultyLabel;
  @FXML private Label timerLabel;
  @FXML private Button startBtn;
  @FXML private Button playBtn;
  @FXML private Slider timerSlider;

  private boolean easyVboxClicked = false;
  private boolean mediumVboxClicked = false;
  private boolean hardVboxClicked = false;
  private Difficulties difficulty;
  private boolean difficultySelected = false;
  private HackerAiManager hackerAiManager = HackerAiManager.getInstance();
  
  /**
   * Initialize the Difficulty Selection controller. It sets the controller for the Difficulty
   * Selection scene.
   */
  public void initialize() {
    SceneManager.setController(Scenes.DIFFICULTYPAGE, this);
  }

  /**
   * Handle the mouse hovering event over each difficulty. When the mouse enters the difficulty
   * boxes, turn image colour on.
   *
   * @param event The MouseEvent triggered by hovering over a difficulty box.
   */
  @FXML
  private void onDifficultyEntered(MouseEvent event) {
    if (event.getSource() == easyVbox) {
      easyAlarmImage.setVisible(true);
    } else if (event.getSource() == mediumVbox) {
      mediumAlarmImage.setVisible(true);
    } else {
      hardAlarmImage.setVisible(true);
    }
  }

  /**
   * Handle the mouse exiting event from each difficulty. When the mouse exits the difficulty boxes,
   * turn image colour off.
   *
   * @param event The MouseEvent triggered by exiting a difficulty box.
   */
  @FXML
  private void onDifficultyExited(MouseEvent event) {
    if (event.getSource() == easyVbox && !easyVboxClicked) {
      easyAlarmImage.setVisible(false);
    } else if (event.getSource() == mediumVbox && !mediumVboxClicked) {
      mediumAlarmImage.setVisible(false);
    } else if (event.getSource() == hardVbox && !hardVboxClicked) {
      hardAlarmImage.setVisible(false);
    }
  }

  /**
   * Handle the mouse click events over each difficulty.
   *
   * @param event The MouseEvent triggered by clicking a difficulty box.
   */
  @FXML
  private void onDifficultyClicked(MouseEvent event) {
    if (!difficultySelected) {
      difficultySelected = true;
      playBtn.setDisable(false);
    }

    if (event.getSource() == easyVbox) {
      handleDifficultySelection(easyVbox, easyAlarmImage, "Easy");
      easyVboxClicked = true;
      difficulty = Difficulties.EASY;
      initialiseHacker(difficulty);

      GameManager.completeObjective();
    } else if (event.getSource() == mediumVbox) {
      handleDifficultySelection(mediumVbox, mediumAlarmImage, "Medium");
      mediumVboxClicked = true;
      difficulty = Difficulties.MEDIUM;
      initialiseHacker(difficulty);
    } else {
      handleDifficultySelection(hardVbox, hardAlarmImage, "Hard");
      hardVboxClicked = true;
      difficulty = Difficulties.HARD;
      initialiseHacker(difficulty);
    }
  }

  public void initialiseHacker(Difficulties difficulties) {
    Task<Void> aiTask3 =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            hackerAiManager.initialiseHackerAi(difficulties);
            return null;
          }
        };

    Thread aiThread3 = new Thread(aiTask3);
    aiThread3.setDaemon(true);
    aiThread3.start();
  }

   /**
   * Handle the slider value change event. When the slider changes values to set the timer, the
   * timer value is shown in the text label.
   *
   * @param event The MouseEvent triggered by changing the slider value.
   */
  @FXML
  private void onSliderChanged(MouseEvent event) {
    int timerValue = (int) timerSlider.getValue();
    timerLabel.setText(Integer.toString(timerValue) + " Minutes");
  }

  /**
   * Handle the event when the "Start Heist" button is clicked.
   *
   * @param event The ActionEvent triggered by clicking the "Start Heist" button.
   */
  @FXML
  private void onStartHeist(ActionEvent event) {
    int timerValue = (int) timerSlider.getValue();
    GameManager.createGame(difficulty, timerValue);
    TimerControl.runTimer();
    App.setUI(Scenes.LOBBY);
    ((GameFinishController) SceneManager.getController(Scenes.GAMEFINISH))
        .setDifficultyLabel(difficulty.toString());
  }

  /**
   * Handle the selection of a difficulty level. Resets the clicked state of difficulty boxes,
   * updates the displayed difficulty label, and makes the selected difficulty box visually distinct
   * by adding a CSS class.
   *
   * @param vbox The VBox representing the selected difficulty box.
   * @param alarmImage The ImageView representing the alarm image for the selected difficulty.
   * @param label The label text for the selected difficulty.
   */
  private void handleDifficultySelection(VBox vbox, ImageView alarmImage, String label) {
    easyVboxClicked = false;
    mediumVboxClicked = false;
    hardVboxClicked = false;

    easyVbox.getStyleClass().remove("clicked-container");
    mediumVbox.getStyleClass().remove("clicked-container");
    hardVbox.getStyleClass().remove("clicked-container");

    easyAlarmImage.setVisible(false);
    mediumAlarmImage.setVisible(false);
    hardAlarmImage.setVisible(false);

    difficultyLabel.setText(label);
    vbox.getStyleClass().add("clicked-container");
    alarmImage.setVisible(true);
  }
}
