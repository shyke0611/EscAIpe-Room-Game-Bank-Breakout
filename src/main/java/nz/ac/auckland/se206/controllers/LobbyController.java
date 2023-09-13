package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class LobbyController extends Controller {

  @FXML private ImageView Vault;
  @FXML private ImageView Security;

  public void initialize() {}

  public void switchToSecurity() {
    App.setUI(Scenes.SECURITY);
  }

  public void switchToVault() {
    App.setUI(Scenes.VAULT);
  }
}
