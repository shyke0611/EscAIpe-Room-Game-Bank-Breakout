package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class VaultController extends Controller {
  @FXML private ImageView Lobby;
  @FXML private ImageView Security;
  @FXML private Rectangle whiteBackground;
  @FXML private Rectangle laserCuttingScene;
  @FXML private ImageView VaultDoor;

  @FXML private VBox walkietalkie;
  @FXML private VBox walkietalkieText;

  private Canvas canvas;
  private GraphicsContext gc;
  private double prevX, prevY;
  private boolean cutting = false;

  public void initialize() {
    SceneManager.setController(Scenes.VAULT, this);
  }

  // handling mouse events on walkie talkie
  // open and closes when walkie talkie is clicked
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

  public void laserCuttingScene(MouseEvent click) {
    // whiteBackground.setVisible(true);
    // whiteBackground.setOpacity(1);
    // VaultDoor.setOpacity(1);

    Stage primaryStage = (Stage) whiteBackground.getScene().getWindow();

    canvas = new Canvas(1200, 700);
    gc = canvas.getGraphicsContext2D();
    StackPane root = new StackPane();
    Scene scene = new Scene(root, 1200, 700);

    String imagePath =
        "https://img.freepik.com/free-vector/metallic-bank-vault-door-isolated-white_1284-51692.jpg?w=1380&t=st=1694744809~exp=1694745409~hmac=1a3a63231f4b2ee660e5999f9ca6f19173e4bc9ee0d2c3555a7a068d5373567d";
    String imagePath2 = "https://www.pngegg.com/en/png-dxxbv";
    Image image = new Image(imagePath);
    Image image2 = new Image(imagePath2);
    gc.drawImage(image, 100, 0, 750, 650);
    gc.drawImage(image2, 200, 150, 750, 650);

    root.getChildren().addAll(canvas);

    scene.setOnMouseDragged(
        event -> {
          double x = event.getX();
          double y = event.getY();
          int counter = 0;

          // Clear the canvas and draw the red line from (0,0) to the mouse cursor's position
          //   if (counter % 500000 == 0) {
          //     gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
          //   }

          gc.setStroke(Color.RED);
          gc.setLineWidth(3); // Set line width as desired
          // gc.strokeLine(300, 450, x, y);

          gc.strokeLine(prevX, prevY, x, y);

          prevX = x;
          prevY = y;
          counter++;
        });

    scene.setOnMousePressed(
        event -> {
          prevX = event.getX();
          prevY = event.getY();
        });

    // scene.setOnMouseDragged(
    //     event -> {
    //       double x = event.getX();
    //       double y = event.getY();

    //       gc.setStroke(Color.RED);
    //       gc.setLineWidth(2);
    //     });

    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
