package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class EyeScannerController  extends Controller{
  
    @FXML private Label error;
    @FXML private VBox accessGranted;
    @FXML private VBox accessDenied;
    @FXML private Label mystery;

    @FXML private Circle guardEye;
    @FXML private Circle artificialEye;

    @FXML private Slider redSlider;
    @FXML private Slider greenSlider;
    @FXML private Slider blueSlider;

    @FXML private Label redValue;
    @FXML private Label greenValue;
    @FXML private Label blueValue;


  public void initialize() {
    SceneManager.setController(Scenes.EYESCANNER, this);
    accessGranted.setVisible(false);
    accessDenied.setVisible(false);
    
    redValue.textProperty().bind(redSlider.valueProperty().asString("%.0f"));
    greenValue.textProperty().bind(greenSlider.valueProperty().asString("%.0f"));
    blueValue.textProperty().bind(blueSlider.valueProperty().asString("%.0f"));

  }

  

  public void compareSample(){}

  
}


