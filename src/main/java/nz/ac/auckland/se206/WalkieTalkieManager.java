package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

  // Static Fields
  private static HashMap<Controller, VBox> walkieTalkieMap = new HashMap<>();
  private static HashMap<Controller, TextArea> walkieTalkieTextAreas = new HashMap<>();
  private static HashMap<Controller, ImageView> walkieTalkieImageMap = new HashMap<>();
  private static HashMap<Controller, Label> walkieTalkieHints = new HashMap<>();
  private static HashMap<Controller, ImageView> walkieTalkieNotifications = new HashMap<>();
  private static boolean walkieTalkieOpen = false;
  private static WalkieTalkieManager instance = new WalkieTalkieManager();
  private static HashMap<Controller, Button> quickHintBtns = new HashMap<>();
  private static TranslateTransition translateTransition;

  public WalkieTalkieManager() {
    translateTransition = new TranslateTransition(Duration.millis(300));
    translateTransition.setByY(10); // Move 10 pixels vertically
    translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
    translateTransition.setAutoReverse(true);
  }

  // Static Methods
  public static void addWalkieTalkie(Controller controller, VBox walkietalkie) {
    walkieTalkieMap.put(controller, walkietalkie);
  }

  public static void addWalkieTalkieNotification(Controller controller, ImageView notification) {
    walkieTalkieNotifications.put(controller, notification);
  }

  public static void addWalkieTalkieTextArea(Controller controller, TextArea textArea) {
    walkieTalkieTextAreas.put(controller, textArea);
  }

  public static void addWalkieTalkieHint(Controller controller, Label hint) {
    walkieTalkieHints.put(controller, hint);
  }

  public static void addWalkieTalkieImage(Controller controller, ImageView walkietalkie) {
    walkieTalkieImageMap.put(controller, walkietalkie);
  }

  public static void addQuickHintBtn(Controller controller, Button btn) {
    quickHintBtns.put(controller, btn);
  }

  public static WalkieTalkieManager getInstance() {
    return instance;
  }

  public static void setWalkieTalkieNotifcationOn() {

    for (ImageView image : walkieTalkieNotifications.values()) {

      image.setVisible(true);
    }
  }

  public static void setWalkieTalkieOpen() {
    Controller activeController = SceneManager.getActiveController();
    walkieTalkieOpen = true;

    // Iterate through the map and update the visibility of all VBoxes
    for (VBox vertBox : walkieTalkieMap.values()) {
      vertBox.setVisible(true);
    }

    VBox activeWalkieTalkie = walkieTalkieMap.get(activeController);
    if (walkieTalkieOpen && activeWalkieTalkie != null) {
      ((HBox) activeWalkieTalkie.getChildren().get(1)).getChildren().get(0).requestFocus();
    }
  }

  public static void toggleWalkieTalkie() {
    walkieTalkieOpen = !walkieTalkieOpen;
    Controller activeController = SceneManager.getActiveController();

    for (ImageView image : walkieTalkieNotifications.values()) {
      image.setVisible(false);
    }

    // Iterate through the map and update the visibility of all VBoxes
    for (VBox vertBox : walkieTalkieMap.values()) {
      vertBox.setVisible(walkieTalkieOpen);
    }

    VBox activeWalkieTalkie = walkieTalkieMap.get(activeController);
    if (walkieTalkieOpen && activeWalkieTalkie != null) {
      ((HBox) activeWalkieTalkie.getChildren().get(1)).getChildren().get(0).requestFocus();
    }
  }

  public static void reset() {
    walkieTalkieOpen = false;
    walkieTalkieMap.clear();
    walkieTalkieImageMap.clear();
    walkieTalkieHints.clear();
  }

  // Instance Fields
  private Timeline timeline;
  private ChatCompletionRequest chatCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.8).setMaxTokens(100);
  private int dotCount = 1;

  // Instance Methods
  public boolean isWalkieTalkieOpen() {
    return walkieTalkieOpen;
  }

  public ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    // Add the message to the request
    chatCompletionRequest.addMessage(msg);
    try {
      // Execute the request and get the result
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());
      return result.getChatMessage();
      // If there is an error, print the stack trace and return null
    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void clearWalkieTalkie() {

    for (VBox vertBox : walkieTalkieMap.values()) {
      // Iterate through the children of the VBox (assuming they are HBox containers)
      ObservableList<Node> children = vertBox.getChildren();
      for (Node node : children) {
        if (node instanceof HBox) {
          HBox horizontalBox = (HBox) node;

          // Now, iterate through the children of the HBox to find the TextArea
          ObservableList<Node> hboxChildren = horizontalBox.getChildren();
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

    for (TextArea textArea : walkieTalkieTextAreas.values()) {
      textArea.setText(msg.getContent());
    }
  }

  public void setHintText(String hintCount) {
    for (Label label : walkieTalkieHints.values()) {
      label.setText(hintCount);
    }
  }

  public void startAnimation() {
    // creating new timeline for animation
    timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> updateTypingLabel()));

    Platform.runLater(
        () -> {
          // switching imageviews
          for (ImageView image : walkieTalkieImageMap.values()) {
            if (image.getId().endsWith("WalkieTalkie")) {
              image.setImage(new Image("images/hacker.png"));
            }
          }
        });

    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  private void updateTypingLabel() {
    // creating dots for animation
    StringBuilder dots = new StringBuilder();
    for (int i = 0; i < dotCount; i++) {
      dots.append(".");
    }
    // creating new message and setting it to walkietalkie
    ChatMessage msg = new ChatMessage("user", "Typing" + dots.toString());
    setWalkieTalkieText(msg);
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
      if (image.getId().contains("WalkieTalkie")) {
        image.setImage(new Image("images/walkietalkie.png"));
      }
    }
  }

  public void disableQuickHintBtns() {
    for (Button btn : quickHintBtns.values()) {
      btn.setDisable(true);
    }
  }

  public void enableQuickHintBtns() {
    for (Button btn : quickHintBtns.values()) {
      btn.setDisable(false);
    }
  }

  public Boolean isQuickHintBtnsVisible() {
    for (Button btn : quickHintBtns.values()) {
      if (btn.isDisable()) {
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  public static boolean getWalkieTalkieOpen() {
    return walkieTalkieOpen;
  }
}
