package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.HackerAiManager;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.WalkieTalkieManager;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;

public class HackerVanController extends Controller {

  @FXML private Button goBackBtn;

  @FXML private TextArea hintTextArea;
  @FXML private Rectangle transparentRectangle;
  @FXML private TextField hackerVanInput;

  @FXML private TextArea historyTextArea;
  @FXML private Label timerLabel;

  private HackerAiManager hackerAiManager = HackerAiManager.getInstance();
  private WalkieTalkieManager walkieTalkieManage = WalkieTalkieManager.getInstance();

  private ChatMessage responce;

  public void initialize() {
    SceneManager.setController(Scenes.HACKERVAN, this);
    super.setTimerLabel(timerLabel, 3);
    printChatHistory();
    loadQuickHints();
  }

  @FXML
  private void onGoBack() {
    App.setUI(SceneManager.getPreviousScene(Scenes.HACKERVAN));
  }

  @FXML
  private void onInvokeHacker(KeyEvent event) throws ApiProxyException {

    if (event.getCode() == KeyCode.ENTER) {
      // fetch message from field and add it to chat history
      ChatMessage msg = new ChatMessage("user", hackerVanInput.getText());
      hackerAiManager.addChatHistory(msg.getContent());
      printChatHistory();

      Task<Void> aiTask3 =
          new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              responce = hackerAiManager.processInput(msg);

              Platform.runLater(
                  () -> {
                    hackerAiManager.addChatHistory(responce.getContent());
                    printChatHistory();
                    walkieTalkieManage.setWalkieTalkieText(responce);
                  });

              return null;
            }
          };

      // new thread for hacker
      Thread threadHackerVan = new Thread(aiTask3);
      threadHackerVan.setDaemon(true);
      threadHackerVan.start();

      hackerVanInput.clear();
    }
  }

  public void printChatHistory() {
    String formattedHistory = hackerAiManager.getFormattedChatHistory();
    historyTextArea.setText(formattedHistory);
  }

  public void loadQuickHints() {
    String formattedHints = hackerAiManager.getFormattedHintHistory();
    hintTextArea.setText(formattedHints);
  }
}
