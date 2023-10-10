package nz.ac.auckland.se206.controllers;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.AnimationManager;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

/** Controller class for the Main Menu scene. */
public class MainMenuController extends Controller {

  @FXML private VBox buttonsContainer;
  @FXML private Button quitBtn;
  @FXML private Button newGameBtn;
  @FXML private Button instructionsBtn;

  public void initialize() {
    SceneManager.setController(Scenes.MAIN_MENU, this);

    AnimationManager.fadeTransition(newGameBtn, 1);
    AnimationManager.fadeTransition(instructionsBtn, 1.5);
    AnimationManager.fadeTransition(quitBtn, 2);
  }

  /**
   * Handle the event when the "New Game" button is clicked. It navigates to the Difficulty Page
   * scene.
   *
   * @param event The ActionEvent triggered by clicking the "New Game" button.
   */
  @FXML
  private void onNewGameBtnClicked(ActionEvent event) {
    App.setUI(Scenes.DIFFICULTYPAGE);
  }

  /**
   * Handle the event when the "Quit" button is clicked. It exits the application.
   *
   * @param event The ActionEvent triggered by clicking the "Quit" button.
   */
  @FXML
  private void exitGame() {
    Platform.exit();
  }

  @FXML
  private void onInstructions() {
    App.setUI(Scenes.INSTRUCTIONS);
  }
}
