package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public abstract class Controller {
  private Label timerLabel;
  StyleManager styleManager = StyleManager.getInstance();

  public void setTimerLabel(Label timerLabel) {
    this.timerLabel = timerLabel;
  }

  public Label getTimerLabel() {
    return timerLabel;
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


}
