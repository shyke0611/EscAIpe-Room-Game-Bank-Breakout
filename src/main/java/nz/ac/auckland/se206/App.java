package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.controllers.ComputerController;
import nz.ac.auckland.se206.controllers.GameFinishController;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  private static Scene scene;
  private static ChatMessage message;
  private static App instance;
  private static boolean isAudioOn = false;

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

  public static void setUI(Scenes newUi) {
    // Update stats if going to end screen
    if (newUi == Scenes.GAMEFINISH) {
      ((GameFinishController) SceneManager.getController(newUi)).setStatLabels();
    }

    // Update last room variable when going to a room
    if (newUi == Scenes.LOBBY || newUi == Scenes.SECURITY || newUi == Scenes.VAULT) {
      SceneManager.setLastRoom(newUi);
    }

    scene.setRoot(SceneManager.getUiRoot(newUi));
    SceneManager.setActiveController(SceneManager.getController(newUi));

    // Adjust focus upon entering computer
    if (newUi == Scenes.COMPUTER) {
      ((ComputerController) SceneManager.getController(newUi)).setFocus();
    }
  }

  public static ChatMessage getStartMessage() {
    return message;
  }

  public static void textToSpeech(String string) {

    if (!isAudioOn) {
      return;
    }

    // Create a text-to-speech task
    Task<Void> speechTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            // Perform text-to-speech operations here
            TextToSpeech textToSpeech = new TextToSpeech();
            textToSpeech.speak(string);
            return null;
          }
        };

    // Run the task in a background thread
    Thread speechThread = new Thread(speechTask);
    speechThread.setDaemon(true);
    speechThread.start();
  }

  public static void reloadScenes() throws IOException {
    instance.loadReloadScenes();
  }

  public static void setAudio(boolean audio) {
    isAudioOn = audio;
  }

  private ChatCompletionRequest chatCompletionRequest;

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   * @throws ApiProxyException
   */
  @Override
  public void start(final Stage stage) throws IOException, ApiProxyException {
    instance = this;
    // initialise the randomiser for all random components
    RandomnessGenerate.generateRandomCredentials();

    // Initialise controllers hashmap to SceneManager
    SceneManager.addController(SceneManager.Scenes.VAULT, null);
    SceneManager.addController(SceneManager.Scenes.LOBBY, null);
    SceneManager.addController(SceneManager.Scenes.SECURITY, null);
    SceneManager.addController(SceneManager.Scenes.WIRECUTTING, null);
    SceneManager.addController(SceneManager.Scenes.MAIN_MENU, null);
    SceneManager.addController(SceneManager.Scenes.DIFFICULTYPAGE, null);
    SceneManager.addController(SceneManager.Scenes.COMPUTER, null);
    SceneManager.addController(SceneManager.Scenes.HACKERVAN, null);
    SceneManager.addController(SceneManager.Scenes.EYESCANNER, null);
    SceneManager.addController(SceneManager.Scenes.CHEMICALMIXING, null);
    SceneManager.addController(SceneManager.Scenes.GAMEFINISH, null);
    SceneManager.addController(SceneManager.Scenes.CONNECTDOTS, null);
    SceneManager.addController(SceneManager.Scenes.WIRECUTTING, null);
    SceneManager.addController(SceneManager.Scenes.LASERCUTTING, null);
    SceneManager.addController(SceneManager.Scenes.INSTRUCTIONS, null);

    // Initialise Scenes hashmap to SceneManager
    SceneManager.addUi(SceneManager.Scenes.MAIN_MENU, null);
    SceneManager.addUi(SceneManager.Scenes.DIFFICULTYPAGE, null);
    SceneManager.addUi(SceneManager.Scenes.INSTRUCTIONS, null);
    SceneManager.addUi(SceneManager.Scenes.GAMEFINISH, null);
    SceneManager.addUi(SceneManager.Scenes.VAULT, null);
    SceneManager.addUi(SceneManager.Scenes.SECURITY, null);
    SceneManager.addUi(SceneManager.Scenes.LOBBY, null);
    SceneManager.addUi(SceneManager.Scenes.WIRECUTTING, null);
    SceneManager.addUi(SceneManager.Scenes.COMPUTER, null);
    SceneManager.addUi(SceneManager.Scenes.HACKERVAN, null);
    SceneManager.addUi(SceneManager.Scenes.EYESCANNER, null);
    SceneManager.addUi(SceneManager.Scenes.CHEMICALMIXING, null);
    SceneManager.addUi(SceneManager.Scenes.CONNECTDOTS, null);
    SceneManager.addUi(SceneManager.Scenes.LASERCUTTING, null);

    loadAllScenes();

    // DONT DELETE, ensures object starts on find keys which is important for ai to work
    // GameManager.completeObjective();

    Parent root = SceneManager.getUiRoot(Scenes.MAIN_MENU);

    scene = new Scene(root, 1000, 700);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
  }

  public ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      // TODO handle exception appropriately
      e.printStackTrace();
      return null;
    }
  }

  private void loadAllScenes() throws IOException {
    // Add scenes that don't need reloading to SceneManager
    SceneManager.changeUi(SceneManager.Scenes.INSTRUCTIONS, loadFxml("instructions"));
    SceneManager.changeUi(SceneManager.Scenes.GAMEFINISH, loadFxml("gamefinish"));
    loadReloadScenes();
  }

  private void loadReloadScenes() throws IOException {
    // Add main menu scenes to SceneManager
    SceneManager.changeUi(SceneManager.Scenes.MAIN_MENU, loadFxml("mainmenu"));
    SceneManager.changeUi(SceneManager.Scenes.DIFFICULTYPAGE, loadFxml("difficultypage"));
    // Add main room scenes to SceneManager
    SceneManager.changeUi(SceneManager.Scenes.VAULT, loadFxml("vault"));
    SceneManager.changeUi(SceneManager.Scenes.SECURITY, loadFxml("securityroom"));
    SceneManager.changeUi(SceneManager.Scenes.LOBBY, loadFxml("lobby"));
    // // Add scenes within game to SceneManager
    SceneManager.changeUi(SceneManager.Scenes.WIRECUTTING, loadFxml("wirecutting"));
    SceneManager.changeUi(SceneManager.Scenes.COMPUTER, loadFxml("computer"));
    SceneManager.changeUi(SceneManager.Scenes.HACKERVAN, loadFxml("hackervan"));
    SceneManager.changeUi(SceneManager.Scenes.EYESCANNER, loadFxml("eyescanner"));
    SceneManager.changeUi(SceneManager.Scenes.CHEMICALMIXING, loadFxml("chemicalmixing"));
    SceneManager.changeUi(SceneManager.Scenes.CONNECTDOTS, loadFxml("connectdots"));
    SceneManager.changeUi(SceneManager.Scenes.LASERCUTTING, loadFxml("laserCutting"));
  }
}
