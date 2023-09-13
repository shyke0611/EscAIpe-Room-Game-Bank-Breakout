package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.Scenes;

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

  // Handling mouse hovering event over each difficulty

  // when mouse enters the difficulty boxes turn image visibility on
  @FXML
  void onDifficultyEntered(MouseEvent event) {
    if (event.getSource() == easyVbox) easyAlarmImage.setVisible(true);
    else if (event.getSource() == mediumVbox) {
      mediumAlarmImage.setVisible(true);
    } else {
      hardAlarmImage.setVisible(true);
    }
  }

  // when mouse exits the difficulty boxes turn image visibility off
  @FXML
  void onDifficultyExited(MouseEvent event) {
    if (event.getSource() == easyVbox && !easyVboxClicked) easyAlarmImage.setVisible(false);
    else if (event.getSource() == mediumVbox && !mediumVboxClicked) {
      mediumAlarmImage.setVisible(false);
    } else if (event.getSource() == hardVbox && !hardVboxClicked) {
      hardAlarmImage.setVisible(false);
    }
  }

  // Handling mouse click events over each difficulty

  @FXML
  void onDifficultyClicked(MouseEvent event) {
    easyVboxClicked = false;
    mediumVboxClicked = false;
    hardVboxClicked = false;

    // Remove the style class (css) from all VBox elements
    easyVbox.getStyleClass().remove("clicked-container");
    mediumVbox.getStyleClass().remove("clicked-container");
    hardVbox.getStyleClass().remove("clicked-container");

    easyAlarmImage.setVisible(false);
    mediumAlarmImage.setVisible(false);
    hardAlarmImage.setVisible(false);

    // Add the style class (css) to the clicked VBox (difficulty)
    if (event.getSource() == easyVbox) {
      difficultyLabel.setText("Easy");
      easyVbox.getStyleClass().add("clicked-container");
      easyAlarmImage.setVisible(true);
      easyVboxClicked = true;
    } else if (event.getSource() == mediumVbox) {
      difficultyLabel.setText("Medium");
      mediumVbox.getStyleClass().add("clicked-container");
      mediumAlarmImage.setVisible(true);
      mediumVboxClicked = true;
    } else {
      difficultyLabel.setText("Hard");
      hardVbox.getStyleClass().add("clicked-container");
      hardAlarmImage.setVisible(true);
      hardVboxClicked = true;
    }
  }

  // when slider changes values to set timer, timer value is shown in the text label
  @FXML
  void onSliderChanged(MouseEvent event) {
    int timerValue = (int) timerSlider.getValue();
    timerLabel.setText(Integer.toString(timerValue) + " Minutes");
  }

  @FXML
  public void startHeist(ActionEvent event) throws IOException {
    App.setUI(Scenes.LOBBY);
  }
}
