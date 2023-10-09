package nz.ac.auckland.se206.controllers;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
      // styleManager.getItem("silverDoor").setVisible(false);
      // styleManager.getItem("silverDoorHolder").setDisable(true);
    }
    // handle visibility if chemical mixing is bypassed
    if (GameState.isChemicalMixingBypassed) {
      // styleManager.getItem("bronzeDoor").setVisible(false);
      // styleManager.getItem("bronzeDoorHolder").setDisable(true);
    }
    // handle visibility if eye scanner is bypassed
    if (GameState.isEyeScannerBypassed) {
      // styleManager.getItem("goldDoor").setVisible(false);
      // styleManager.getItem("goldDoorHolder").setDisable(true);
    }
    // handle visibility if any door is bypassed
    if (GameState.isLaserCuttingBypassed
        || GameState.isChemicalMixingBypassed
        || GameState.isEyeScannerBypassed) {
      // styleManager.getItem("lootBtnHolder").setVisible(true);
      GameState.isAnyDoorOpen = true;
    }
    // handle visibility if all door is bypassed
    if (GameState.isLaserCuttingBypassed
        && GameState.isChemicalMixingBypassed
        && GameState.isEyeScannerBypassed) {
      // Label label = (Label) styleManager.getItem("lootLbl");
      // label.setText("Collect all the loot and escape");
    }
    App.setUI(Scenes.VAULT);
  }

  @FXML
  protected void grantAccess() {
    // setting style for when access is granted by disabling firewall
    GameState.isFirewallDisabled = true;
    // styleManager.setDisable(true, "computer");
    // styleManager.setItemsState(
    //     HoverColour.GREEN, "bronzeDoorHolder", "silverDoorHolder", "goldDoorHolder");
    // styleManager.setItemsMessage(
    //     "We can go in", "bronzeDoorHolder", "silverDoorHolder", "goldDoorHolder");
    App.setUI(Scenes.SECURITY);
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
    // styleManager.setClueHover("securityRoomSwitch", false);
    // if the computer is not logged in and credentials not found set style
    if (!GameState.isSecurityComputerLoggedIn && GameState.isCredentialsFound) {
      handleComputerHover("computer");
      // GameState.isComputerHoverPressed = true;
      // handle alarm tripped scenario
    } else if (GameState.isAlarmTripped) {
      handleElectricityHover("electricityBox");
    }
  }

  @FXML
  protected void switchToLobby() {
    // Switch the scene to the Lobby
    App.setUI(Scenes.LOBBY);
    // If the alarm has been tripped, handle the guard pocket hover
    if (GameState.isAlarmTripped) {
      handleGuardPocketHover("guardpocket");
    }
  }

  // Handle hovering over the computer item
  private void handleComputerHover(String item) {
    // Check if the computer hover action hasn't been pressed yet
    // if (!GameState.isComputerHoverPressed) {
    //   // Set the hover effect and mark as pressed
    //   // styleManager.setClueHover(item, true);
    //   GameState.isComputerHoverPressed = true;
    //   // Set the item's state to green (assuming it represents a clue)
    //   styleManager.setItemsState(HoverColour.GREEN, item);
    }
  

  // Handle hovering over the electricity item
  private void handleElectricityHover(String item) {
    // Check if the electricity hover action hasn't been pressed yet
    // if (!GameState.isElectricityHoverPressed) {
    //   // Set the hover effect and mark as pressed
    //   // styleManager.setClueHover(item, true);
    //   GameState.isElectricityHoverPressed = true;
    // }
  }

  // Handle hovering over the guard pocket item
  private void handleGuardPocketHover(String item) {
    // Check if the guard pocket hover action hasn't been pressed yet
    // if (!GameState.isGuardPocketHoverPressed) {
    //   // Set the hover effect and mark as pressed
    //   // styleManager.setClueHover(item, true);
    //   GameState.isGuardPocketHoverPressed = true;
    // }
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
