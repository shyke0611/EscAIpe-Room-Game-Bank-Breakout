package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.Scenes;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  private static Scene scene;

  public static void main(final String[] args) {
    launch();
  }

  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  public static void setUI(Scenes newUI) {
    scene.setRoot(SceneManager.getUiRoot(newUI));
    SceneManager.setActiveController(SceneManager.getController(newUI));
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {

    // Initialise controllers hashmap to SceneManager
    SceneManager.addController(SceneManager.Scenes.VAULT, null);
    SceneManager.addController(SceneManager.Scenes.LOBBY, null);
    SceneManager.addController(SceneManager.Scenes.SECURITY, null);
    SceneManager.addController(SceneManager.Scenes.MAIN_MENU, null);
    SceneManager.addController(SceneManager.Scenes.DIFFICULTYPAGE, null);

    // Add scenes to SceneManager
    SceneManager.addUi(SceneManager.Scenes.VAULT, loadFxml("vault"));
    SceneManager.addUi(SceneManager.Scenes.LOBBY, loadFxml("lobby"));
    SceneManager.addUi(SceneManager.Scenes.SECURITY, loadFxml("securityroom"));
    SceneManager.addUi(SceneManager.Scenes.MAIN_MENU, loadFxml("mainmenu"));
    SceneManager.addUi(SceneManager.Scenes.DIFFICULTYPAGE, loadFxml("difficultypage"));

    Parent root = SceneManager.getUiRoot(Scenes.MAIN_MENU);

    scene = new Scene(root, 1000, 700);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
  }
}
