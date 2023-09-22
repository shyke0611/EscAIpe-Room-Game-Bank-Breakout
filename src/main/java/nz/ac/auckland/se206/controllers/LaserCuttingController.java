package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.StyleManager.HoverColour;

public class LaserCuttingController extends Controller {

  @FXML private AnchorPane root;

  @FXML private Circle outerCircle;
  @FXML private Circle innerCircle;
  @FXML private Circle blackCircle;

  @FXML private Button takeLootBtn;

  @FXML private ImageView insideVault;
  @FXML private ImageView laserGun;

  @FXML private Label equipLaserGun;

  @FXML private Canvas canvas;
  @FXML private GraphicsContext gc;
  @FXML private Label timerLabel;

  private Boolean gunEquppied = false;
  private double prevX, prevY;
  private double totalAngle = 0.0;

  private Line cursorLine = new Line();
  private List<Double> angles = new ArrayList<>();
  private List<Point2D> points = new ArrayList<>();

  private StyleManager styleManager = StyleManager.getInstance();

  public void initialize() {
    SceneManager.setController(Scenes.LASERCUTTING, this);
    super.setTimerLabel(timerLabel, 3);
    gc = canvas.getGraphicsContext2D();

    root.getChildren().add(cursorLine);

    cursorLine.setStartX(500);
    cursorLine.setStartY(700);
    cursorLine.setEndX(500);
    cursorLine.setEndY(700);
    cursorLine.setStrokeWidth(3);
    cursorLine.setStroke(Color.RED);

    applyGlowEffect(cursorLine);
    setUpListener(laserGun);
    styleManager.addItems(laserGun);
    styleManager.setItemsState(HoverColour.GREEN, "laserGun");
    formatBlackCirlce();
  }

  public void laserGunClicked() {
    gunEquppied = true;
    canvas.setVisible(true);
    laserGun.setVisible(false);
    equipLaserGun.setVisible(false);
  }

  public void mouseReleased(MouseEvent event) {
    clearCursorLine();
    gc.clearRect(0, 0, 1000, 700);
  }

  public void draw(MouseEvent event) {

    double x = event.getX();
    double y = event.getY();

    gc.setStroke(Color.RED);
    gc.setLineWidth(3);
    gc.strokeLine(prevX, prevY, x, y);

    Point2D mousePosition = new Point2D(x, y);

    if (gunEquppied
        && isMouseInsideLargerCircleOutsideSmallerCircle(mousePosition, outerCircle, innerCircle)) {
      cursorLine.setEndX(x);
      cursorLine.setEndY(y);
    }

    if (!isMouseInsideLargerCircleOutsideSmallerCircle(mousePosition, outerCircle, innerCircle)) {
      gc.clearRect(0, 0, 1000, 700);
      points.clear();
      angles.clear();
    }

    if (whileUserisDrawing(mousePosition)) {
      blackCircle.setVisible(true);
      insideVault.setVisible(true);
      outerCircle.setVisible(false);
      innerCircle.setVisible(false);
      gc.clearRect(0, 0, 1000, 700);
    }

    prevX = x;
    prevY = y;
  }

  public void clearCursorLine() {
    // Clear the cursor line by setting its end point to its start point
    cursorLine.setEndX(cursorLine.getStartX());
    cursorLine.setEndY(cursorLine.getStartY());
  }

  public void mousePressed(MouseEvent event) {
    prevX = event.getX();
    prevY = event.getY();
    points.clear();
    angles.clear();
  }

  public boolean isMouseInsideLargerCircleOutsideSmallerCircle(
      Point2D mousePosition, Circle largerCircle, Circle smallerCircle) {
    // Calculate the distance from the mouse position to the center of the larger circle
    double distanceToLargerCircle =
        mousePosition.distance(largerCircle.getCenterX(), largerCircle.getCenterY());

    // Calculate the distance from the mouse position to the center of the smaller circle
    double distanceToSmallerCircle =
        mousePosition.distance(smallerCircle.getCenterX(), smallerCircle.getCenterY());

    // Check if the mouse position is inside the larger circle and outside the smaller circle
    return (distanceToLargerCircle <= largerCircle.getRadius())
        && (distanceToSmallerCircle > smallerCircle.getRadius());
  }

  public boolean whileUserisDrawing(Point2D mousePosition) {
    points.add(mousePosition);

    if (points.size() > 1) {
      totalAngle = 0.0;

      for (int i = 0; i < points.size() - 1; i++) {
        Point2D point1 = points.get(i);
        Point2D point2 = points.get(i + 1);

        // Calculate the angle between point1 and point2
        double angle = calculateAngle(point1, point2, outerCircle);

        // Add the angle to the total angle
        totalAngle += angle;
      }

      // Check if the total angle exceeds a threshold for a completed circle
      if (totalAngle > 2 * Math.PI
          || totalAngle < -(2 * Math.PI)) { // Adjust the threshold as needed
        // Reset the points and angles for the next circle
        takeLootBtn.setVisible(true);
        GameState.isLaserCuttingBypassed = true;
        // $10 Million
        GameManager.increaseMoneyToGain(10000000);
        GameManager.completeObjective();
        // updateMoneyStolen(5000000);
        points.clear();
        angles.clear();
        return true;
      }
    }

    return false;
  }

  public double calculateAngle(Point2D point1, Point2D point2, Circle circle) {
    double centerX = circle.getCenterX();
    double centerY = circle.getCenterY();

    // Calculate the angles between each point and the center of the circle
    double angle1 = Math.atan2(point1.getY() - centerY, point1.getX() - centerX);
    double angle2 = Math.atan2(point2.getY() - centerY, point2.getX() - centerX);

    // Calculate the angle difference between the two angles
    double angleDifference = angle2 - angle1;

    // If the angle has decreased significantly, assume the user has moved backward
    // and reset it to 0 to continue counting forward
    if (Math.abs(angleDifference) > Math.PI) {
      if (angleDifference < 0) {
        // User has crossed from -PI to PI
        angleDifference += 2 * Math.PI;
      } else {
        // User has crossed from PI to -PI
        angleDifference -= 2 * Math.PI;
      }
    }

    return angleDifference;
  }

  @FXML
  private void formatBlackCirlce() {
    RadialGradient gradientOutside =
        new RadialGradient(
            0,
            0,
            0.5,
            0.5,
            0.98,
            true,
            CycleMethod.NO_CYCLE,
            new Stop(0, Color.ORANGERED),
            new Stop(1, Color.BLACK));

    // Apply a drop shadow effect for added depth
    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(20);
    dropShadow.setSpread(0.2);
    dropShadow.setColor(Color.DARKRED);

    Stop[] stops = {
      new Stop(0, Color.TRANSPARENT), // Center color (transparent)
      new Stop(0.9, Color.TRANSPARENT), // Hot color
      new Stop(1, Color.RED) // Molten color
    };

    // Create a radial gradient fill
    RadialGradient gradient =
        new RadialGradient(0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, stops);

    // Set the gradient fill as the circle's fill
    blackCircle.setFill(gradient);

    blackCircle.setEffect(dropShadow);
    blackCircle.setStroke(gradientOutside);
    blackCircle.setStrokeWidth(10);
  }

  public void applyGlowEffect(Line line) {

    // Create a GaussianBlur effect
    GaussianBlur blur = new GaussianBlur(15);
    Blend blend = new Blend();
    blend.setMode(BlendMode.ADD);
    blend.setTopInput(blur);
    line.setEffect(blend);
  }
}
