package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.Scenes;
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
  private ChatCompletionRequest chatCompletionRequest;
  private static ChatMessage message;
  private static App instance;

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
    if (newUi == Scenes.GAMEFINISH) {
      ((GameFinishController) SceneManager.getController(newUi)).setStatLabels();
    }

    scene.setRoot(SceneManager.getUiRoot(newUi));
    SceneManager.setActiveController(SceneManager.getController(newUi));
  }

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

    HackerAiManager hackerAiManager = HackerAiManager.getInstance();
    hackerAiManager.initialiseHackerAi(Difficulties.EASY);
    GameManager.completeObjective();

    chatCompletionRequest =
        new ChatCompletionRequest().setN(1).setTemperature(0.5).setTopP(0.9).setMaxTokens(100);
    try {
      message = runGpt(new ChatMessage("user", GptPromptEngineering.welcomeMessage()));
    } catch (ApiProxyException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

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

    loadAllScenes();

    // DONT DELETE, ensures object starts on find keys which is important for ai to work
    GameManager.completeObjective();

    Parent root = SceneManager.getUiRoot(Scenes.MAIN_MENU);

    scene = new Scene(root, 1000, 700);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
  }

  public static ChatMessage getStartMessage() {
    return message;
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

  public static void textToSpeech(String string) {
    Task<Void> speechTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            TextToSpeech textToSpeech = new TextToSpeech();
            textToSpeech.speak(string);
            return null;
          }
        };

    Thread speechThread = new Thread(speechTask);
    speechThread.setDaemon(true);
    speechThread.start();
  }

  private void loadAllScenes() throws IOException {
    // Add scenes to SceneManager
    SceneManager.addUi(SceneManager.Scenes.MAIN_MENU, loadFxml("mainmenu"));
    SceneManager.addUi(SceneManager.Scenes.DIFFICULTYPAGE, loadFxml("difficultypage"));
    SceneManager.addUi(SceneManager.Scenes.GAMEFINISH, loadFxml("gamefinish"));
    loadGameScenes();
  }

  private void loadGameScenes() throws IOException {
    SceneManager.addUi(SceneManager.Scenes.VAULT, loadFxml("vault"));
    SceneManager.addUi(SceneManager.Scenes.LOBBY, loadFxml("lobby"));
    SceneManager.addUi(SceneManager.Scenes.SECURITY, loadFxml("securityroom"));
    SceneManager.addUi(SceneManager.Scenes.WIRECUTTING, loadFxml("wirecutting"));
    SceneManager.addUi(SceneManager.Scenes.COMPUTER, loadFxml("computer"));
    SceneManager.addUi(SceneManager.Scenes.HACKERVAN, loadFxml("hackervan"));
    SceneManager.addUi(SceneManager.Scenes.EYESCANNER, loadFxml("eyescanner"));
    SceneManager.addUi(SceneManager.Scenes.CHEMICALMIXING, loadFxml("chemicalmixing"));
    SceneManager.addUi(SceneManager.Scenes.CONNECTDOTS, loadFxml("connectdots"));
    SceneManager.addUi(SceneManager.Scenes.LASERCUTTING, loadFxml("laserCutting"));
  }

  public static void reloadScenes() throws IOException {
    instance.loadAllScenes();
  }
}
