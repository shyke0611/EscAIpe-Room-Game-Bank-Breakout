package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class VaultController {

  @FXML private VBox walkietalkie;

  @FXML private VBox walkietalkieText;

  private boolean isWalkieTalkieOpened = false;

  // handling mouse events on walkie talkie
  // open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    isWalkieTalkieOpened = !isWalkieTalkieOpened;
    walkietalkieText.setVisible(isWalkieTalkieOpened);
  }
}
