package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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

/** Class to manage all walkie talkies. */
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

  // Static Methods

  /**
   * add a walkie talkie to the map.
   *
   * @param controller - The controller associated
   * @param walkietalkie - The walkie talkie scene object to add
   */
  public static void addWalkieTalkie(Controller controller, VBox walkietalkie) {
    walkieTalkieMap.put(controller, walkietalkie);
  }

  /**
   * Add a walkie talkie notification to the map.
   *
   * @param controller - The controller associated
   * @param notification - The notification scene object to add
   */
  public static void addWalkieTalkieNotification(Controller controller, ImageView notification) {
    walkieTalkieNotifications.put(controller, notification);
  }

  /**
   * Add a walkie talkie text area to the map.
   *
   * @param controller - The controller associated
   * @param textArea - The text area scene object to add
   */
  public static void addWalkieTalkieTextArea(Controller controller, TextArea textArea) {
    walkieTalkieTextAreas.put(controller, textArea);
  }

  /**
   * Add a walkie talkie hint to the map.
   *
   * @param controller - The controller associated
   * @param hint - The hint scene object to add
   */
  public static void addWalkieTalkieHint(Controller controller, Label hint) {
    walkieTalkieHints.put(controller, hint);
  }

  /**
   * Add a walkie talkie image to the map.
   *
   * @param controller - The controller associated
   * @param walkietalkie - The walkie talkie image scene object to add
   */
  public static void addWalkieTalkieImage(Controller controller, ImageView walkietalkie) {
    walkieTalkieImageMap.put(controller, walkietalkie);
  }

  /**
   * Add a quick hint button to the map.
   *
   * @param controller - The controller associated
   * @param btn - The quick hint button scene object to add
   */
  public static void addQuickHintBtn(Controller controller, Button btn) {
    quickHintBtns.put(controller, btn);
  }

  /**
   * Get the instance of the WalkieTalkieManager.
   *
   * @return - The instance of the WalkieTalkieManager
   */
  public static WalkieTalkieManager getInstance() {
    return instance;
  }

  /** Toggle the Walkie Talkie Notification on. */
  public static void setWalkieTalkieNotifcationOn() {

    for (ImageView image : walkieTalkieNotifications.values()) {
      if (image != null) {
        image.setVisible(true);
      }
    }
  }

  /** Toggle the walkie talkie to be open. */
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

  /** Toggle the walkie talkie to be closed and the notification to disappear. */
  public static void toggleWalkieTalkie() {
    walkieTalkieOpen = !walkieTalkieOpen;
    Controller activeController = SceneManager.getActiveController();

    // Toggle the visibility of the notification
    for (ImageView image : walkieTalkieNotifications.values()) {
      if (image != null) {
        image.setVisible(false);
      }
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

  /** Reset the lists of scene objects. */
  public static void reset() {
    walkieTalkieOpen = false;
    walkieTalkieMap.clear();
    walkieTalkieImageMap.clear();
    walkieTalkieHints.clear();
  }

  /**
   * Get the walkie talkie open status.
   *
   * @return - The walkie talkie open status
   */
  public static boolean getWalkieTalkieOpen() {
    return walkieTalkieOpen;
  }

  // Instance Fields
  private Timeline timeline;
  private ChatCompletionRequest chatCompletionRequest =
      new ChatCompletionRequest().setN(1).setTemperature(0.7).setTopP(0.8).setMaxTokens(100);
  private int dotCount = 1;

  // Instance Methods

  /**
   * Get the walkie talkie open status.
   *
   * @return - The walkie talkie open status
   */
  public boolean isWalkieTalkieOpen() {
    return walkieTalkieOpen;
  }

  /**
   * Send a message to the GPT chatbot.
   *
   * @param msg - The message to send to the chatbot
   * @return - The response from the chatbot
   * @throws ApiProxyException - If there is an error connecting with the API
   */
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

  /** Clear the text of all walkie-talkies. */
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

  /**
   * Set the text of all walkie-talkies.
   *
   * @param msg - The message to set the text to
   */
  public void setWalkieTalkieText(ChatMessage msg) {

    for (TextArea textArea : walkieTalkieTextAreas.values()) {
      textArea.setText(msg.getContent());
    }
  }

  /**
   * Set the hint remaining text of all walkie-talkies.
   *
   * @param hintCount - The hint count to set the text to
   */
  public void setHintText(String hintCount) {
    for (Label label : walkieTalkieHints.values()) {
      label.setText(hintCount);
    }
  }

  /** Start an image animation while waiting for response. */
  public void startAnimation() {
    // creating new timeline for animation
    timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> updateTypingLabel()));

    Platform.runLater(
        () -> {
          // switching imageviews
          for (ImageView image : walkieTalkieImageMap.values()) {
            if (image.getId().toLowerCase().endsWith("walkietalkie")) {
              image.setImage(new Image("images/hacker.png"));
            }
          }
        });

    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  /** Start a dot animation while waiting for response. */
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

  /** Stop the current image animation. */
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

  /** Disable the quick hint button. */
  public void disableQuickHintBtns() {
    for (Button btn : quickHintBtns.values()) {
      btn.setDisable(true);
    }
  }

  /** Enable the quick hint button. */
  public void enableQuickHintBtns() {
    for (Button btn : quickHintBtns.values()) {
      btn.setDisable(false);
    }
  }

  /**
   * Check if the quick hint button is visible.
   *
   * @return - false if the quick hint button is disabled, true otherwise
   */
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
}
