package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.AnimationManager;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager.HoverColour;

public abstract class Controller {
  private Label timerLabel;
  private int format;
  StyleManager styleManager = StyleManager.getInstance();

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
        styleManager.getItem("bronzeDoor").setVisible(false);
        styleManager.getItem("bronzeDoorHolder").setDisable(true);
    }
    if (GameState.isChemicalMixingBypassed) {
        styleManager.getItem("goldDoor").setVisible(false);
        styleManager.getItem("goldDoorHolder").setDisable(true);
    }
    if (GameState.isEyeScannerBypassed) {
        styleManager.getItem("silverDoor").setVisible(false);
        styleManager.getItem("silverDoorHolder").setDisable(true);
    }
    if (GameState.isLaserCuttingBypassed || GameState.isChemicalMixingBypassed || GameState.isEyeScannerBypassed) {
        styleManager.getItem("lootBtnHolder").setVisible(true);
        GameState.isAnyDoorOpen = true;
    }
    if (GameState.isLaserCuttingBypassed && GameState.isChemicalMixingBypassed && GameState.isEyeScannerBypassed) {
        Label label = (Label) styleManager.getItem("lootLbl");
        label.setText("Collect it all the loot and escape");
    }
    App.setUI(Scenes.VAULT);
}

// move this code somewhere else (temporary firewall diable method (click rectangle))
@FXML
public void grantAccess() {
  GameState.isFirewallDisabled = true;
  styleManager.setDisable(true, "computer");
  styleManager.setItemsState(
  HoverColour.GREEN, "bronzeDoorHolder", "silverDoorHolder", "goldDoorHolder");
  styleManager.setItemsMessage("We can go in", "bronzeDoorHolder", "silverDoorHolder", "goldDoorHolder");
  App.setUI(Scenes.SECURITY);
}

protected void setupListeners(Node... items) {
  for (Node node : items) {
  ScaleTransition scaleTransition = AnimationManager.createScaleTransition(node);
  // Add hover listeners to start and stop the animation
  node.setStyle("-fx-cursor: hand;");
  node.setOnMouseEntered(event -> AnimationManager.playAnimationForward(scaleTransition));
  node.setOnMouseExited(event -> AnimationManager.playAnimationReverse(scaleTransition));
  }
}


}
