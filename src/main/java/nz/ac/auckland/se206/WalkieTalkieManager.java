package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.controllers.Controller;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class WalkieTalkieManager {

  private ChatCompletionRequest chatCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.8).setMaxTokens(100);

  // storing the walkietalkie textboxes
  private static HashMap<Controller, VBox> walkietalkieMap = new HashMap<>();
  private static boolean walkieTalkieOpen = false;
  private static WalkieTalkieManager instance = new WalkieTalkieManager();

  public static void addWalkieTalkie(Controller controller, VBox walkietalkie) {
    walkietalkieMap.put(controller, walkietalkie);
  }

  public static WalkieTalkieManager getInstance() {
    return instance;
  }

  public ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {

    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      // appendChatMessage(result.getChatMessage());
      System.out.println(result.getChatMessage().getContent());
      return result.getChatMessage();
    } catch (ApiProxyException e) {
      // TODO handle exception appropriately
      e.printStackTrace();
      return null;
    }
  }

  public static void clearWalkieTalkie() {
    for (VBox vBox : walkietalkieMap.values()) {
      // Iterate through the children of the VBox (assuming they are HBox containers)
      ObservableList<Node> children = vBox.getChildren();
      for (Node node : children) {
        if (node instanceof HBox) {
          HBox hBox = (HBox) node;

          // Now, iterate through the children of the HBox to find the TextArea
          ObservableList<Node> hboxChildren = hBox.getChildren();
          for (Node hboxChild : hboxChildren) {
            if (hboxChild instanceof TextField) {
              TextField textArea = (TextField) hboxChild;
              textArea.clear(); // Set the text to the desired message
            }
          }
        }
      }
    }
  }

  public static void setWalkieTalkieText(ChatMessage msg) {
    for (VBox vBox : walkietalkieMap.values()) {
      // Iterate through the children of the VBox (assuming they are HBox containers)
      ObservableList<Node> children = vBox.getChildren();
      for (Node node : children) {
        if (node instanceof HBox) {
          HBox hBox = (HBox) node;

          // Now, iterate through the children of the HBox to find the TextArea
          ObservableList<Node> hboxChildren = hBox.getChildren();
          for (Node hboxChild : hboxChildren) {
            if (hboxChild instanceof TextArea) {
              TextArea textArea = (TextArea) hboxChild;
              textArea.setText(msg.getContent()); // Set the text to the desired message
            }
          }
        }
      }
    }
  }

  public static void toggleWalkieTalkie() {
    walkieTalkieOpen = !walkieTalkieOpen;

    // Iterate through the map and update the visibility of all VBoxes
    for (VBox vBox : walkietalkieMap.values()) {
      vBox.setVisible(walkieTalkieOpen);
    }
  }
}
