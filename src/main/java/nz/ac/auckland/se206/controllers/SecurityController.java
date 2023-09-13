package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.Scenes;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class SecurityController extends Controller {

  @FXML private ImageView Lobby;
  @FXML private ImageView Vault;
  
  @FXML private VBox walkietalkie;
  @FXML private VBox walkietalkieText;

  private boolean isWalkieTalkieOpened = false;
  
  public void initialize() {}

  // handling mouse events on walkie talkie
  // open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    isWalkieTalkieOpened = !isWalkieTalkieOpened;
    walkietalkieText.setVisible(isWalkieTalkieOpened);


  public void switchToLobby() {
    App.setUI(Scenes.LOBBY);
  }

  public void switchToVault() {
    App.setUI(Scenes.VAULT);
  }
}
