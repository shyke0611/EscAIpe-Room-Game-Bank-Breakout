package nz.ac.auckland.se206.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GameFinishController extends Controller{
    @FXML
    private Button exitButton;
    @FXML
    private TextArea leaderboard;
    @FXML
    private Button mainmenuBtn;
    @FXML
    private Label moneyLbl;
    @FXML
    private Label outcomeLbl;
    @FXML
    private Label timeLbl;

    @FXML
    void onExit(ActionEvent event) {
        // exit code here

    }

    @FXML
    void onSwitchToMainMenu(ActionEvent event) {
     App.setUI(Scenes.MAIN_MENU);
     // reset game code here
    }

    public void initialize() {
        SceneManager.setController(Scenes.GAMEFINISH, this);
    }
    
}
