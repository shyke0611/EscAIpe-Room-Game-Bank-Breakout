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
  public void setVault(MouseEvent event) {
    if (GameState.isLaserCuttingBypassed) {
      styleManager.getItem("silverDoor").setVisible(false);
      styleManager.getItem("silverDoorHolder").setDisable(true);
    }
    if (GameState.isChemicalMixingBypassed) {
      styleManager.getItem("bronzeDoor").setVisible(false);
      styleManager.getItem("bronzeDoorHolder").setDisable(true);
    }
    if (GameState.isEyeScannerBypassed) {
      styleManager.getItem("goldDoor").setVisible(false);
      styleManager.getItem("goldDoorHolder").setDisable(true);
    }
    if (GameState.isLaserCuttingBypassed
        || GameState.isChemicalMixingBypassed
        || GameState.isEyeScannerBypassed) {
      styleManager.getItem("lootBtnHolder").setVisible(true);
      GameState.isAnyDoorOpen = true;
    }
    if (GameState.isLaserCuttingBypassed
        && GameState.isChemicalMixingBypassed
        && GameState.isEyeScannerBypassed) {
      Label label = (Label) styleManager.getItem("lootLbl");
      label.setText("Collect all the loot and escape");
    }
    App.setUI(Scenes.VAULT);
  }

  @FXML
  public void grantAccess() {
    GameState.isFirewallDisabled = true;
    styleManager.setDisable(true, "computer");
    styleManager.setItemsState(
        HoverColour.GREEN, "bronzeDoorHolder", "silverDoorHolder", "goldDoorHolder");
    styleManager.setItemsMessage(
        "We can go in", "bronzeDoorHolder", "silverDoorHolder", "goldDoorHolder");
    App.setUI(Scenes.SECURITY);
  }

  public void setUpListener(Node... items) {
    for (Node node : items) {
      ScaleTransition scaleTransition = AnimationManager.createScaleTransition(node);
      // Add hover listeners to start and stop the animation
      node.setStyle("-fx-cursor: hand;");
      node.setOnMouseEntered(event -> AnimationManager.playAnimationForward(scaleTransition));
      node.setOnMouseExited(event -> AnimationManager.playAnimationReverse(scaleTransition));
    }
  }

  @FXML
  public void switchToSecurity() {
    App.setUI(Scenes.SECURITY);
    styleManager.setClueHover("securityRoomSwitch", false);

    if (!GameState.isSecurityComputerLoggedIn && GameState.isCredentialsFound) {
      handleComputerHover("computer");
      GameState.isComputerHoverPressed = true;
    } else if (GameState.isAlarmTripped) {
      handleElectricityHover("electricityBox");
    }
  }

  @FXML
  public void switchToLobby() {
    App.setUI(Scenes.LOBBY);

    if (GameState.isAlarmTripped) {
      handleGuardPocketHover("guardpocket");
    }
  }

  private void handleComputerHover(String item) {
    if (!GameState.isComputerHoverPressed) {
      styleManager.setClueHover(item, true);
      GameState.isComputerHoverPressed = true;
      styleManager.setItemsState(HoverColour.GREEN, item);
    }
  }

  private void handleElectricityHover(String item) {
    if (!GameState.isElectricityHoverPressed) {
      styleManager.setClueHover(item, true);
      GameState.isElectricityHoverPressed = true;
    }
  }

  private void handleGuardPocketHover(String item) {
    if (!GameState.isGuardPocketHoverPressed) {
      styleManager.setClueHover(item, true);
      GameState.isGuardPocketHoverPressed = true;
    }
  }

  @FXML
  public void switchToVault() {
    if (GameState.isAlarmDisabled) {
      styleManager.getItem("bombHolder").setVisible(true);
      styleManager.setDisable(true, "bronzeDoor", "silverDoor", "goldDoor");
      styleManager.setClueHover("vaultRoomSwitch", false);
    }
    App.setUI(Scenes.VAULT);
  }
}
