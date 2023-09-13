package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class MainMenuController extends Controller {

  @FXML private Label titleLabel1;
  @FXML private Label titleLabel2;
  @FXML private VBox buttonsContainer;
  @FXML private Button quitBtn;
  @FXML private Button newGameBtn;
  @FXML private Button instructionsBtn;

  public void initialize() {
    SceneManager.setController(Scenes.MAIN_MENU, this);
  }

  public void onNewGameBtnClicked() {
    App.setUI(Scenes.DIFFICULTYPAGE);
  }
}
