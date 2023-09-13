package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class SecurityController extends Controller {

  @FXML private ImageView Lobby;
  @FXML private ImageView Vault;

  public void initialize() {}

  public void switchToLobby() {
    App.setUI(Scenes.LOBBY);
  }

  public void switchToVault() {
    App.setUI(Scenes.VAULT);
  }
}
