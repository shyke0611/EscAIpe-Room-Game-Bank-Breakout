package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameManager;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;

public class ConnectDotsController extends Controller {

  @FXML private GridPane gridPane;
  @FXML private StackPane disableSecurity;
  @FXML private Label timerLabel;
  @FXML private Button resetButton;

  private StyleManager styleManager = StyleManager.getInstance();

  // 0 = empty, 1 = red, 2 = blue, 3 = green, 4 = purple, negative = node
  private int[][] grid = new int[6][6];
  private int[][] solution;

  private int initialColumn = -1;
  private int initialRow = -1;

  private List<Integer> columnPath = new ArrayList<>();
  private List<Integer> rowPath = new ArrayList<>();

  private boolean nodeSelected = false;
  private int nodeValue = 0;

  public void initialize() {

    SceneManager.setController(Scenes.CONNECTDOTS, this);
    super.setTimerLabel(timerLabel, 3);
    disableSecurity.setVisible(false);
    setSolution();
  }

  public void startDrag(MouseEvent e) {
    initialColumn = getIndex(e.getX(), false);
    initialRow = getIndex(e.getY(), true);

    nodeSelected = grid[initialRow][initialColumn] < 0;

    if (nodeSelected) {
      nodeValue = grid[initialRow][initialColumn];
      columnPath.add(initialColumn);
      rowPath.add(initialRow);
    }
  }

  public void drag(MouseEvent e) {
    if (!nodeSelected) {
      return;
    }

    int currentColumn = getIndex(e.getX(), false);
    int currentRow = getIndex(e.getY(), true);

    // If the current cell is not valid return
    if (!isCellValid(currentColumn, currentRow)) {
      return;
    }

    // If the current cell is not empty return;
    if (grid[currentRow][currentColumn] != 0) {
      return;
    }

    // Continue path
    columnPath.add(currentColumn);
    rowPath.add(currentRow);
    grid[currentRow][currentColumn] = Math.abs(nodeValue);
    Rectangle node = getNodeFromGridPane(gridPane, currentRow, currentColumn);
    node.setFill(getColour(nodeValue));
  }

  public void endDrag(MouseEvent e) {
    if (!nodeSelected) {
      return;
    }

    int finalColumn = getIndex(e.getX(), false);
    int finalRow = getIndex(e.getY(), true);

    // Add final cell to path if valid - can be a node
    if (isCellValid(finalColumn, finalRow)) {
      if (grid[finalRow][finalColumn] == nodeValue) {
        columnPath.add(finalColumn);
        rowPath.add(finalRow);
      }
    }

    int lastIndex = columnPath.size() - 1;

    // If end cell is not same type as start node then clear path cant be start node
    if (grid[rowPath.get(lastIndex)][columnPath.get(lastIndex)] != nodeValue
        || (finalRow == initialRow && finalColumn == initialColumn)) {
      for (int i = 0; i < columnPath.size(); i++) {
        int column = columnPath.get(i);
        int row = rowPath.get(i);

        // If the current cell is a node continue;
        if (grid[row][column] < 0) {
          continue;
        }
        grid[row][column] = 0;
        Rectangle node = getNodeFromGridPane(gridPane, row, column);
        node.setFill(Paint.valueOf("white"));
      }
    }

    // Reset variables
    nodeSelected = false;
    nodeValue = 0;
    initialColumn = -1;
    initialRow = -1;
    rowPath.clear();
    columnPath.clear();

    if (isGameComplete()) {
      disableSecurity.setVisible(true);
      GameState.isSecondUserAuthenticated = true;
      resetButton.setDisable(true);
      GameManager.completeObjective();
      App.textToSpeech("Security Disabled, Vault Access Granted");
    }
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
    copyStartEndNodes();
  }

  private void copyStartEndNodes() {
    // Copy start/end nodes and colour them
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {

        if (solution[i][j] < 0) {
          grid[i][j] = solution[i][j];
          Rectangle node = getNodeFromGridPane(gridPane, i, j);
          node.setFill(getColour(grid[i][j]));
        }
      }
    }
  }

  private int getIndex(double coordinate, boolean isRow) {
    if (isRow) {
      // 80 is the cell height
      return (int) Math.floor(coordinate / 80);
    }
    // 90 is the cell width
    return (int) Math.floor(coordinate / 90);
  }

  private Rectangle getNodeFromGridPane(GridPane gridPane, int row, int col) {
    for (Node node : gridPane.getChildren()) {
      int columnIndex = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
      int rowIndex = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);
      if (columnIndex == col && rowIndex == row) {
        return (Rectangle) node;
      }
    }
    return null;
  }

  private boolean isCellValid(int currentColumn, int currentRow) {
    int columnPathSize = columnPath.size();
    int rowPathSize = rowPath.size();

    // If the current cell is out of bounds return false;
    if (currentColumn > 5 || currentRow > 5 || currentColumn < 0 || currentRow < 0) {
      return false;
    }

    // If the current cell is different to previous check if it is valid
    if (currentColumn != columnPath.get(columnPathSize - 1)
        || currentRow != rowPath.get(rowPathSize - 1)) {

      // If the current cell is not adjacent to the previous cell return false;
      if (Math.abs(currentColumn - columnPath.get(columnPathSize - 1)) > 1
          || Math.abs(currentRow - rowPath.get(rowPathSize - 1)) > 1) {
        return false;
      }

      // If the current cell is the diagonal to the previous cell return false;
      if (Math.abs(currentColumn - columnPath.get(columnPathSize - 1))
          == Math.abs(currentRow - rowPath.get(rowPathSize - 1))) {
        return false;
      }

      return true;
    }
    return false;
  }

  private Paint getColour(int value) {
    value = Math.abs(value);
    switch (value) {
      case 1:
        return Paint.valueOf("red");
      case 2:
        return Paint.valueOf("blue");
      case 3:
        return Paint.valueOf("green");
      case 4:
        return Paint.valueOf("purple");
      default:
        return Paint.valueOf("white");
    }
  }

  public void resetGame() {
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        if (grid[i][j] < 0) {
          continue;
        }
        grid[i][j] = 0;
        Rectangle node = getNodeFromGridPane(gridPane, i, j);
        node.setFill(Paint.valueOf("white"));
      }
    }
  }

  private boolean isGameComplete() {
    for (int r = 0; r < 6; r++) {
      for (int c = 0; c < 6; c++) {

        if (solution[r][c] <= 0) {
          continue;
        }

        if (grid[r][c] != solution[r][c]) {
          return false;
        }
      }
    }
    return true;
  }

  public void switchToSecurity() {
    if (GameState.isSecondUserAuthenticated) {
      styleManager.getItem("computer").setDisable(true);
    }
    App.setUI(Scenes.SECURITY);
  }
}
