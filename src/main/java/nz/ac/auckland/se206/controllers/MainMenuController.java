package nz.ac.auckland.se206.controllers;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nz.ac.auckland.se206.AnimationManager;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;

/** Controller class for the Main Menu scene. */
public class MainMenuController extends Controller {

  @FXML private VBox buttonsContainer;
  @FXML private Button quitBtn;
  @FXML private Button newGameBtn;
  @FXML private Button instructionsBtn;
  @FXML private ImageView title;

  public void initialize() {
    SceneManager.setController(Scenes.MAIN_MENU, this);
  }

  /**
   * Handle the event when the "New Game" button is clicked. It navigates to the Difficulty Page
   * scene.
   *
   * @param event The ActionEvent triggered by clicking the "New Game" button.
   */
  @FXML
  private void onNewGameBtnClicked(ActionEvent event) {
    // animation settings for smooth play
    PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
          App.setUI(Scenes.DIFFICULTYPAGE); // Change scene
          AnimationManager.fadeTransition(StyleManager.getHoverItem("itemContainer"), 2,0,1);
      });
      pause.play();
    buttonsContainer.setDisable(true);
    AnimationManager.fadeTransition(title, 2, 1.0, 0.0);
    AnimationManager.fadeTransition(buttonsContainer, 2, 1.0, 0.0);
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

  public void reset() {
    buttonsContainer.setDisable(false);
    title.setOpacity(1.0);
    buttonsContainer.setOpacity(1.0);
  }
}
