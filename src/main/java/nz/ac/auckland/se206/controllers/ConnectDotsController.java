package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class ConnectDotsController extends Controller {

  @FXML private VBox walkietalkieText;
  @FXML private GridPane gridPane;

  // 0 = empty, 1 = red, 2 = blue, 3 = green, 4 = purple, negative = node
  private int[][] grid = new int[6][6];
  private int[][] solution = new int[6][6];

  private Point2D lastPoint = null;

  public void initialize() {

    SceneManager.setController(Scenes.EYESCANNER, this);
    setSolution();

    // Sync Background walkie talkie
    // WalkieTalkieManager.addWalkieTalkie(this, walkietalkieText);
  }

  public void switchToVault() {
    App.setUI(Scenes.VAULT);
  }

  private void setSolution() {
    int[][] solution = {
      {3, 3, 3, 3, 3, 3},
      {-3, -1, -4, 4, -4, 3},
      {-2, 1, 1, 1, 1, 3},
      {2, 2, 2, -2, 1, 3},
      {0, 0, -3, -1, 1, 3},
      {0, 0, 3, 3, 3, 3}
    };

    this.solution = solution;

    // Copy nodes
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {

        if (solution[i][j] < 0) {
          grid[i][j] = solution[i][j];
          Rectangle node = (Rectangle) getNodeFromGridPane(gridPane, i, j);
          node.setFill(Paint.valueOf("red"));
        }
      }
    }
  }

  private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
    for (Node node : gridPane.getChildren()) {
      int columnIndex = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
      int rowIndex = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);
      if (columnIndex == col && rowIndex == row) {
        return node;
      }
    }
    return null;
  }
}
