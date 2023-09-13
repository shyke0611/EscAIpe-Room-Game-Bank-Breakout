package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class DifficultyController {
  @FXML private VBox buttonsContainer;
  @FXML private Label difficultyLabel;
  @FXML private ImageView easyAlarmImage;
  @FXML private VBox easyVbox;
  @FXML private ImageView hardAlarmImage;
  @FXML private VBox hardVbox;
  @FXML private ImageView mediumAlarmImage;
  @FXML private VBox mediumVbox;
  @FXML private Button playBtn;
  @FXML private Label timerLabel;
  @FXML private Slider timerSlider;


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
    if (event.getSource() == easyVbox) easyAlarmImage.setVisible(false);
    else if (event.getSource() == mediumVbox) {
      mediumAlarmImage.setVisible(false);
    } else if (event.getSource() == hardVbox) {
      hardAlarmImage.setVisible(false);
    }
  }

}
