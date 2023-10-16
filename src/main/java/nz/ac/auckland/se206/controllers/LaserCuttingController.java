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
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import nz.ac.auckland.se206.App;
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
  @FXML private Circle meltedEdge;

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

  /** Initialize the laser Cutting Controller. Sets up the initial state of the scene. */
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
  }

  /**
   * Handles the mouse release event.
   *
   * @param event The MouseEvent object.
   */
  @FXML
  private void mouseReleased(MouseEvent event) {
    clearCursorLine();
    gc.clearRect(0, 0, 1000, 700);
  }

  /**
   * Handles the drawing action with the mouse.
   *
   * @param event The MouseEvent object.
   */
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

      insideVault.setVisible(true);
      meltedEdge.setVisible(true);
      outerCircle.setVisible(false);
      innerCircle.setVisible(false);
      // Clear the canvas
      gc.clearRect(0, 0, 1000, 700);
    }
    // Update the previous mouse coordinates for the next iteration
    prevX = x;
    prevY = y;
  }

  /** Clears the cursor line. */
  private void clearCursorLine() {
    // Clear the cursor line by setting its end point to its start point
    cursorLine.setEndX(cursorLine.getStartX());
    cursorLine.setEndY(cursorLine.getStartY());
  }

  /**
   * Handles the mouse press event.
   *
   * @param event The MouseEvent object.
   */
  @FXML
  private void mousePressed(MouseEvent event) {
    prevX = event.getX();
    prevY = event.getY();
    points.clear();
    angles.clear();
  }

  /**
   * Checks if the mouse is inside the larger circle but outside the smaller circle.
   *
   * @param mousePosition The mouse position.
   * @param largerCircle The larger circle.
   * @param smallerCircle The smaller circle.
   * @return true if the mouse is inside the larger circle but outside the smaller circle, false
   *     otherwise.
   */
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

  /**
   * Checks a condition while the user is drawing.
   *
   * @param mousePosition The current mouse position.
   * @return true if a specific condition is met, false otherwise.
   */
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
        meltedEdge.setVisible(true);
        insideVault.setVisible(true);
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

  /**
   * Calculates the angle between two points and the center of a circle.
   *
   * @param point1 The first point.
   * @param point2 The second point.
   * @param circle The circle.
   * @return The angle between the two points and the center of the circle.
   */
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

  /**
   * Applies a glow effect to the given line.
   *
   * @param line The Line to which the glow effect is applied.
   */
  private void applyGlowEffect(Line line) {
    // Create a GaussianBlur effect
    GaussianBlur blur = new GaussianBlur(15);
    Blend blend = new Blend();
    blend.setMode(BlendMode.ADD);
    blend.setTopInput(blur);
    line.setEffect(blend);
  }

  /** Navigates to the Vault scene. */
  @FXML
  public void setVault() {
    App.setUi(Scenes.VAULT);
  }
}
