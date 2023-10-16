package nz.ac.auckland.se206.controllers;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.AnimationManager;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;

/** Abstract Controller class. */
public abstract class Controller {
  private Label timerLabel;
  private int format;

  /**
   * Set the timer label and time format for the controller.
   *
   * @param timerLabel The label to display the timer.
   * @param format The format for the timer (e.g., minutes, seconds).
   */
  public void setTimerLabel(Label timerLabel, int format) {
    // setting timer label
    this.timerLabel = timerLabel;
    this.format = format;
  }

  /**
   * Get the timer label for the controller.
   *
   * @return The timer label.
   */
  public Label getTimerLabel() {
    return timerLabel;
  }

  /**
   * Get the time format for the timer.
   *
   * @return The time format (e.g., minutes, seconds).
   */
  public int getFormat() {
    return format;
  }

  /**
   * Handle the "setVault" action when interacting with doors to access the vault.
   *
   * @param event The mouse event triggered by interacting with the door.
   */
  @FXML
  protected void setVault(MouseEvent event) {
    // handle visibility if laser cutting is bypassed
    if (GameState.isLaserCuttingBypassed) {
      StyleManager.getHoverItem("silverDoor").setVisible(false);
      StyleManager.getHoverItem("silverDoorHolder").setDisable(true);
    }
    // handle visibility if chemical mixing is bypassed
    if (GameState.isChemicalMixingBypassed) {
      StyleManager.getHoverItem("bronzeDoor").setVisible(false);
      StyleManager.getHoverItem("bronzeDoorHolder").setDisable(true);
    }
    // handle visibility if eye scanner is bypassed
    if (GameState.isEyeScannerBypassed) {
      StyleManager.getHoverItem("goldDoor").setVisible(false);
      StyleManager.getHoverItem("goldDoorHolder").setDisable(true);
    }
    // handle visibility if any door is bypassed
    if (GameState.isLaserCuttingBypassed
        || GameState.isChemicalMixingBypassed
        || GameState.isEyeScannerBypassed) {
      StyleManager.getHoverItem("lootBtnHolder").setVisible(true);
      GameState.isAnyDoorOpen = true;
    }
    // handle visibility if all door is bypassed
    if (GameState.isLaserCuttingBypassed
        && GameState.isChemicalMixingBypassed
        && GameState.isEyeScannerBypassed) {
      Text label = (Text) StyleManager.getHoverItem("lootLbl");
      label.setText("Collect all the loot and escape");
    }
    App.setUI(Scenes.VAULT);
  }

  /**
   * Set up hover animation listeners for the provided nodes.
   *
   * @param items The nodes to apply hover animations to.
   */
  protected void setUpListener(Node... items) {
    for (Node node : items) {
      ScaleTransition scaleTransition = AnimationManager.createScaleTransition(node);
      // Add hover listeners to start and stop the animation
      node.setStyle("-fx-cursor: hand;");
      // setting animation on mouse event
      node.setOnMouseEntered(event -> AnimationManager.playAnimationForward(scaleTransition));
      node.setOnMouseExited(event -> AnimationManager.playAnimationReverse(scaleTransition));
    }
  }

  /** Switch to the Security scene. */
  @FXML
  protected void switchToSecurity() {
    App.setUI(Scenes.SECURITY);
  }

  /** Switch to the Lobby scene. */
  @FXML
  protected void switchToLobby() {
    // Switch the scene to the Lobby
    App.setUI(Scenes.LOBBY);
  }

  /** Switch to the Vault scene. */
  @FXML
  protected void switchToVault() {
    // Switch the scene to the Vault
    App.setUI(Scenes.VAULT);
    if (GameState.isAlarmDisabled) {
      StyleManager.setVisible(true, "bombHolder");
      StyleManager.setVisible(false, "switchHolder", "walkietalkieText");
      StyleManager.setDisable(true, "vaultwalkietalkie");
      ImageView bomblogo = (ImageView) StyleManager.getHoverItem("bomblogo");
      AnimationManager.fadeTransition(bomblogo, 2, 0.0, 1.0);
    }
  }
}
