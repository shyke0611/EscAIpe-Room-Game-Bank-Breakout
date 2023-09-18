package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class WireCuttingController extends Controller{



    public void initialize() {
        SceneManager.setController(Scenes.WIRECUTTING, this);
    }


    @FXML
    void switchToSecurity(MouseEvent event) {
        App.setUI(Scenes.SECURITY);
    }
    
}
