package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
import nz.ac.auckland.se206.GameManager.Objectives;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

/** Controller class for the Laser Cutting scene. */
public class LaserCuttingController extends Controller {

  @FXML private AnchorPane root;

  @FXML private Circle outerCircle;
  @FXML private Circle innerCircle;
  @FXML private Circle blackCircle;

  @FXML private ImageView insideVault;
  @FXML private ImageView laserGun;

  @FXML private Label equipLaserGun;
  @FXML private Label moneyCount;

  @FXML private Canvas canvas;
  @FXML private GraphicsContext gc;
  @FXML private Label timerLabel;

  private double prevY;
  private double prevX;
  private double totalAngle = 0.0;

  private Line cursorLine = new Line();
  private List<Double> angles = new ArrayList<>();
  private List<Point2D> points = new ArrayList<>();

  public void initialize() {
    SceneManager.setController(Scenes.LASERCUTTING, this);
    GameManager.addMoneyGainedLabel(null, moneyCount);

    super.setTimerLabel(timerLabel, 1);
    gc = canvas.getGraphicsContext2D();
    root.getChildren().add(cursorLine);
    // Set the cursor line's start and end points to the same point
    cursorLine.setStartX(500);
    cursorLine.setStartY(700);
    cursorLine.setEndX(500);
    cursorLine.setEndY(700);
    cursorLine.setStrokeWidth(3);
    cursorLine.setStroke(Color.RED);
    // applying effects on the gun
    applyGlowEffect(cursorLine);

    canvas.setVisible(true);
    formatBlackCirlce();
  }

  // @FXML
  // private void laserGunClicked() {
  //   // handling when the user clicks on the laser gun
  //   gunEquppied = true;
  //   canvas.setVisible(true);
  //   laserGun.setVisible(false);
  //   equipLaserGun.setVisible(false);
  // }

  @FXML
  private void mouseReleased(MouseEvent event) {
    clearCursorLine();
    gc.clearRect(0, 0, 1000, 700);
  }

  @FXML
  private void draw(MouseEvent event) {
    // Get the current mouse coordinates (x, y)
    double x = event.getX();
    double y = event.getY();
    // Set drawing properties: red color and line width of 3
    gc.setStroke(Color.RED);
    gc.setLineWidth(3);
    // Draw a line from the previous mouse position to the current position
    gc.strokeLine(prevX, prevY, x, y);
    // Create a Point2D object to represent the current mouse position
    Point2D mousePosition = new Point2D(x, y);
    // Check if a gun is equipped and the mouse is inside a specific region
    if (isMouseInsideLargerCircleOutsideSmallerCircle(mousePosition, outerCircle, innerCircle)) {
      // Update the cursor line's end coordinates to follow the mouse
      cursorLine.setEndX(x);
      cursorLine.setEndY(y);
    }
    // Check if the mouse is outside a specific region
    if (!isMouseInsideLargerCircleOutsideSmallerCircle(mousePosition, outerCircle, innerCircle)) {
      // Clear the canvas and reset some data structures
      gc.clearRect(0, 0, 1000, 700);
      points.clear();
      angles.clear();
    }
    // Check if a specific condition is met while the user is drawing
    if (whileUserisDrawing(mousePosition)) {
      // Toggle the visibility of certain UI elements
      blackCircle.setVisible(true);
      insideVault.setVisible(true);
      outerCircle.setVisible(false);
      innerCircle.setVisible(false);
      // Clear the canvas
      gc.clearRect(0, 0, 1000, 700);
    }
    // Update the previous mouse coordinates for the next iteration
    prevX = x;
    prevY = y;
  }

  private void clearCursorLine() {
    // Clear the cursor line by setting its end point to its start point
    cursorLine.setEndX(cursorLine.getStartX());
    cursorLine.setEndY(cursorLine.getStartY());
  }

  @FXML
  private void mousePressed(MouseEvent event) {
    prevX = event.getX();
    prevY = event.getY();
    points.clear();
    angles.clear();
  }

  private boolean isMouseInsideLargerCircleOutsideSmallerCircle(
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

  private boolean whileUserisDrawing(Point2D mousePosition) {
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
        GameState.isLaserCuttingBypassed = true;
        // $10 Million
        GameManager.increaseMoneyToGain(10000000);
        GameManager.setMoneyGained();
        GameManager.setCurrentObjective(Objectives.SELECT_VAULT_DOOR);
        // updateMoneyStolen(5000000);
        points.clear();
        angles.clear();
        return true;
      }
    }

    return false;
  }

  private double calculateAngle(Point2D point1, Point2D point2, Circle circle) {
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
    // create new gradient for the outside of the circle
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

  private void applyGlowEffect(Line line) {
    // Create a GaussianBlur effect
    GaussianBlur blur = new GaussianBlur(15);
    Blend blend = new Blend();
    blend.setMode(BlendMode.ADD);
    blend.setTopInput(blur);
    line.setEffect(blend);
  }
}
