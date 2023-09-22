package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

/**
 * Controller class for the Main Menu scene.
 */
public class MainMenuController extends Controller {

  @FXML private Label titleLabel1;
  @FXML private Label titleLabel2;
  @FXML private VBox buttonsContainer;
  @FXML private Button quitBtn;
  @FXML private Button newGameBtn;
  @FXML private Button instructionsBtn;


  /**
   * Initialize the Main Menu controller.
   * It sets the controller for the Main Menu scene.
   */
  public void initialize() {
    SceneManager.setController(Scenes.MAIN_MENU, this);
  }

  /**
   * Handle the event when the "New Game" button is clicked.
   * It navigates to the Difficulty Page scene.
   *
   * @param event The ActionEvent triggered by clicking the "New Game" button.
   */
  @FXML
  private void onNewGameBtnClicked(ActionEvent event) {
    App.setUI(Scenes.DIFFICULTYPAGE);
  }

  /**
   * Handle the event when the "Quit" button is clicked.
   * It exits the application.
   *
   * @param event The ActionEvent triggered by clicking the "Quit" button.
   */
  @FXML
  private void exitGame(ActionEvent event) {
    Platform.exit();
  }
}
