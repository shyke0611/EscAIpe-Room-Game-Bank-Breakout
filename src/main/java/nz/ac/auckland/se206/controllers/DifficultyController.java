package nz.ac.auckland.se206.controllers;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.AnimationManager;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.HackerAiManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.TimerControl;
import nz.ac.auckland.se206.difficulties.Difficulty.Difficulties;

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
  @FXML private RadioButton audiobtn;
  @FXML private Slider timerSlider;
  @FXML private AnchorPane itemContainer;
  @FXML private StackPane audioholder;
  @FXML private ImageView mutebtn;
  @FXML private ImageView unmutebtn;

  private boolean easyVboxClicked = false;
  private boolean mediumVboxClicked = false;
  private boolean hardVboxClicked = false;
  private Difficulties difficulty;
  private boolean difficultySelected = false;
  private HackerAiManager hackerAiManager = HackerAiManager.getInstance();
  private StyleManager styleManager = StyleManager.getInstance();

  /**
   * Initialize the Difficulty Selection controller. It sets the controller for the Difficulty
   * Selection scene.
   */
  public void initialize() {
    SceneManager.setController(Scenes.DIFFICULTYPAGE, this);
    styleManager.addHoverItems(itemContainer);
  }

  /**
   * Handle the mouse hovering event over each difficulty. When the mouse enters the difficulty
   * boxes, turn image colour on.
   *
   * @param event The MouseEvent triggered by hovering over a difficulty box.
   */
  @FXML
  private void onDifficultyEntered(MouseEvent event) {
    // setting visibility of each difficulty clicked
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
    // setting visibility of each difficulty clicked
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
    // handle if difficulty is not chosen
    if (!difficultySelected) {
      difficultySelected = true;
      AnimationManager.fadeTransition(playBtn, 1, 0.0, 1.0);
      AnimationManager.fadeTransition(audioholder, 1, 0.0, 1.0);
    }

    // handle if difficulty is easy
    if (event.getSource() == easyVbox) {
      handleDifficultySelection(easyVbox, easyAlarmImage, "Easy");
      easyVboxClicked = true;
      difficulty = Difficulties.EASY;

      // handle if difficulty is medium
    } else if (event.getSource() == mediumVbox) {
      handleDifficultySelection(mediumVbox, mediumAlarmImage, "Medium");
      mediumVboxClicked = true;
      difficulty = Difficulties.MEDIUM;

    } else {
      // handle if difficulty is hard
      handleDifficultySelection(hardVbox, hardAlarmImage, "Hard");
      hardVboxClicked = true;
      difficulty = Difficulties.HARD;
    }
  }

  public void initialiseHacker(Difficulties difficulties) {
    // set relevant methods to initialise hacker
    Task<Void> aiTask3 =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            hackerAiManager.initialiseHacker(difficulties);
            return null;
          }
        };

    // new thread for hacker
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

  // toggling tts button
  @FXML
  private void onSoundOn() {
    if (audiobtn.isSelected()) {
      App.setAudio(true);
      audiobtn.setText("Sound On");
      unmutebtn.setVisible(true);
      mutebtn.setVisible(false);
    } else {
      App.setAudio(false);
      audiobtn.setText("Sound Off");
      unmutebtn.setVisible(false);
      mutebtn.setVisible(true);
    }
  }

  /**
   * Handle the event when the "Start Heist" button is clicked.
   *
   * @param event The ActionEvent triggered by clicking the "Start Heist" button.
   */
  @FXML
  private void onStartHeist(ActionEvent event) {
    // setting relevant methods to start game
    int timerValue = (int) timerSlider.getValue();
    initialiseHacker(difficulty);
    GameManager.completeObjective();
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
    // set boolean states
    easyVboxClicked = false;
    mediumVboxClicked = false;
    hardVboxClicked = false;

    // setting css styling to each difficulty box
    easyVbox.getStyleClass().remove("clicked-container");
    mediumVbox.getStyleClass().remove("clicked-container");
    hardVbox.getStyleClass().remove("clicked-container");

    // setting visibility to each difficulty box
    easyAlarmImage.setVisible(false);
    mediumAlarmImage.setVisible(false);
    hardAlarmImage.setVisible(false);

    difficultyLabel.setText(label);
    vbox.getStyleClass().add("clicked-container");
    alarmImage.setVisible(true);
  }
}
