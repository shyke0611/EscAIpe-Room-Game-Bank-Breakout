package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.Controller;
import nz.ac.auckland.se206.gpt.ChatMessage;
import nz.ac.auckland.se206.gpt.openai.ApiProxyException;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionRequest;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult;
import nz.ac.auckland.se206.gpt.openai.ChatCompletionResult.Choice;

public class WalkieTalkieManager {

  private ChatCompletionRequest chatCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.8).setMaxTokens(100);

  private int dotCount = 1;
  // storing the walkietalkie textboxes
  private static HashMap<Controller, VBox> walkietalkieMap = new HashMap<>();
  private static HashMap<Controller, ImageView> walkieTalkieImageMap = new HashMap<>();
  private static boolean walkieTalkieOpen = false;
  private static WalkieTalkieManager instance = new WalkieTalkieManager();
  private Timeline timeline;

  public static void addWalkieTalkie(Controller controller, VBox walkietalkie) {
    walkietalkieMap.put(controller, walkietalkie);
  }

  public static void addWalkieTalkieImage(Controller controller, ImageView walkietalkie) {
    walkieTalkieImageMap.put(controller, walkietalkie);
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

  public void clearWalkieTalkie() {
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

  public void setWalkieTalkieText(ChatMessage msg) {
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

  public void startAnimation() {
    timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> updateTypingLabel()));

    Platform.runLater(
        () -> {
          for (ImageView image : walkieTalkieImageMap.values()) {
            // Iterate through the children of the VBox (assuming they are HBox containers)

            // Check the ID of the ImageView
            if (image.getId().endsWith("WalkieTalkie")) {

              image.setImage(new Image("images/hacker.png"));
            }
          }
        });
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  private void updateTypingLabel() {
    // Update the typing label text with cycling dots
    StringBuilder dots = new StringBuilder();
    for (int i = 0; i < dotCount; i++) {
      dots.append(".");
    }

    ChatMessage msg = new ChatMessage("user", "Typing" + dots.toString());

    setWalkieTalkieText(msg);

    // Increase or reset dot count
    if (dotCount < 3) {
      dotCount++;
    } else {
      dotCount = 1;
    }
  }

  public void stopAnimation() {
    if (timeline != null) {
      timeline.stop();
    }
    for (ImageView image : walkieTalkieImageMap.values()) {
      // Iterate through the children of the VBox (assuming they are HBox containers)

      // Check the ID of the ImageView
      if (image.getId().contains("WalkieTalkie")) {
        image.setImage(new Image("images/walkietalkie.png"));
      }
    }
  }

  public static void reset() {
    walkieTalkieOpen = false;
    walkietalkieMap.clear();
    walkieTalkieImageMap.clear();
  }
}
