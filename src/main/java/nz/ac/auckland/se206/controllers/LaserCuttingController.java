package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class LaserCuttingController extends Controller {

  @FXML private AnchorPane root;
  @FXML private Circle outerCircle;
  @FXML private Circle innerCircle;
  @FXML private Label winningLabel;

  @FXML private Canvas canvas;
  @FXML private GraphicsContext gc;
  private double prevX, prevY, startX, startY;
  private boolean cutting = false;
  private int count = 0;
  private double totalAngle = 0.0;

  private List<Line> lines = new ArrayList<>();
  List<Double> angles = new ArrayList<>();
  private List<Point2D> points = new ArrayList<>();

  public void initialize() {
    SceneManager.setController(Scenes.LASERCUTTING, this);
    gc = canvas.getGraphicsContext2D();

    System.out.println(innerCircle.getCenterX());

    Scene scene = (Scene) root.getScene();

    // For local images use
    // image = new Image(getClass().getResource(#Path#).openStream());

    // Clear away portions as the user drags the mouse

  }

  public void draw(MouseEvent event) {

    cutting = true;
    double x = event.getX();
    double y = event.getY();

    gc.setStroke(Color.RED);
    gc.setLineWidth(3);

    gc.strokeLine(prevX, prevY, x, y);

    Point2D mousePosition = new Point2D(x, y);

    if (!isMouseInsideLargerCircleOutsideSmallerCircle(mousePosition, outerCircle, innerCircle)) {
      gc.clearRect(0, 0, 1000, 700);
      points.clear();
      angles.clear();
    }

    if (whileUserisDrawing(mousePosition)) {
      winningLabel.setText("you win");
      gc.clearRect(0, 0, 1000, 700);
    }

    // if (isInRangeFirstMarker((int) x, (int) y)) {
    //   System.out.println("X is in range");
    // }

    // if (count >= 1 && isInRangeSecondMarker((int) x, (int) y)) {
    //   System.out.println("User has completed circle");
    // }

    prevX = x;
    prevY = y;
  }

  public void mousePressed(MouseEvent event) {
    prevX = event.getX();
    prevY = event.getY();
    startX = event.getX();
    startY = event.getY();
    points.clear();
    angles.clear();
  }

  public void mouseReleased() {
    gc.clearRect(0, 0, 1000, 700);
  }

  public Boolean isInRangeFirstMarker(int x, int y) {

    if (x > innerCircle.getCenterX() + innerCircle.getRadius()
        && x < outerCircle.getCenterX() + outerCircle.getRadius()) {
      if (startY == y) {
        count++;
        return true;
      }
    }

    return false;
  }

  public Boolean isInRangeSecondMarker(int x, int y) {
    if (x < startX + 20 && x > startX - 20) {
      if (y < startY + 20 && y > startY - 20) {

        return true;
      }
    }
    return false;
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

  public boolean hasUserCompletedCircle() {

    return false;
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

      // Ensure the total angle is positive (between 0 and 2*PI)
      if (totalAngle < 0) {
        totalAngle += 2 * Math.PI;
      }

      // Check if the total angle exceeds a threshold for a completed circle
      if (totalAngle > 6.2) { // Adjust the threshold as needed
        // Reset the points and angles for the next circle
        points.clear();
        angles.clear();
        return true;
      }
    }

    return false;
  }

  // ... (other code)

  public double calculateAngle(Point2D point1, Point2D point2, Circle circle) {
    double centerX = circle.getCenterX();
    double centerY = circle.getCenterY();

    // Calculate the angles between each point and the center of the circle
    double angle1 = Math.atan2(point1.getY() - centerY, point1.getX() - centerX);
    double angle2 = Math.atan2(point2.getY() - centerY, point2.getX() - centerX);

    // Calculate the angle difference between the two angles
    double angleDifference = angle2 - angle1;

    // Ensure the angle difference is positive (between 0 and 2*PI)

    return angleDifference;
  }
}
