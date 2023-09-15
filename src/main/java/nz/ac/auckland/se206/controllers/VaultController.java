package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class VaultController extends Controller {
  
  public enum Doors{
    EASY,
    MEDIUM,
    HARD
  }

  private Doors selectedDoor;

  @FXML private ImageView Lobby;
  @FXML private ImageView Security;

  @FXML private VBox walkietalkie;
  @FXML private VBox walkietalkieText;

  

  public void initialize() {
    SceneManager.setController(Scenes.VAULT, this);
  }

  //   handling mouse events on walkie talkie
  //   open and closes when walkie talkie is clicked
  @FXML
  void onWalkieTalkie(MouseEvent event) {
    SceneManager.toggleWalkieTalkieOpen();
  }

  public void synchWalkieTalkie(boolean isOpen) {
    walkietalkieText.setVisible(isOpen);
  }

  public void switchToLobby() {
    App.setUI(Scenes.LOBBY);
  }

  public void switchToSecurity() {
    App.setUI(Scenes.SECURITY);
  }

  public void onSwitchToHacker() {
    SceneManager.setPreviousScene(Scenes.HACKERVAN, Scenes.VAULT);
    App.setUI(Scenes.HACKERVAN);
  }

  public Doors getSelectedDoor() {
    return selectedDoor;
  }
}
