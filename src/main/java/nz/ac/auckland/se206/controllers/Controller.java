package nz.ac.auckland.se206.controllers;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.AnimationManager;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.StyleManager.HoverColour;

public abstract class Controller {
  private Label timerLabel;
  private int format;
  private StyleManager styleManager = StyleManager.getInstance();

  public void setTimerLabel(Label timerLabel, int format) {
    // setting timer label
    this.timerLabel = timerLabel;
    this.format = format;
  }

  public Label getTimerLabel() {
    return timerLabel;
  }

  public int getFormat() {
    return format;
  }

  @FXML
  protected void setVault(MouseEvent event) {
    // handle visibility if laser cutting is bypassed
    if (GameState.isLaserCuttingBypassed) {
      styleManager.getHoverItem("silverDoor").setVisible(false);
      styleManager.getHoverItem("silverDoorHolder").setDisable(true);
    }
    // handle visibility if chemical mixing is bypassed
    if (GameState.isChemicalMixingBypassed) {
      styleManager.getHoverItem("bronzeDoor").setVisible(false);
      styleManager.getHoverItem("bronzeDoorHolder").setDisable(true);
    }
    // handle visibility if eye scanner is bypassed
    if (GameState.isEyeScannerBypassed) {
      styleManager.getHoverItem("goldDoor").setVisible(false);
      styleManager.getHoverItem("goldDoorHolder").setDisable(true);
    }
    // handle visibility if any door is bypassed
    if (GameState.isLaserCuttingBypassed
        || GameState.isChemicalMixingBypassed
        || GameState.isEyeScannerBypassed) {
      styleManager.getHoverItem("lootBtnHolder").setVisible(true);
      GameState.isAnyDoorOpen = true;
    }
    // handle visibility if all door is bypassed
    if (GameState.isLaserCuttingBypassed
        && GameState.isChemicalMixingBypassed
        && GameState.isEyeScannerBypassed) {
      Text label = (Text) styleManager.getHoverItem("lootLbl");
      label.setText("Collect all the loot and escape");
    }
    App.setUI(Scenes.VAULT);
  }


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

  @FXML
  protected void switchToSecurity() {
    App.setUI(Scenes.SECURITY);
    // if the computer is not logged in and credentials not found set style
  }

  @FXML
  protected void switchToLobby() {
    // Switch the scene to the Lobby
    App.setUI(Scenes.LOBBY);
  }

  @FXML
  protected void switchToVault() {
    // If the alarm has been disabled, configure Vault scene elements
    if (GameState.isAlarmDisabled) {
      // Show the bomb holder and disable certain doors
      // styleManager.getItem("bombHolder").setVisible(true);
      // Disable the clue hover effect for the vault room switch
      // styleManager.setClueHover("vaultRoomSwitch", false);
    }
    // Switch the scene to the Vault
    App.setUI(Scenes.VAULT);
  }

}
