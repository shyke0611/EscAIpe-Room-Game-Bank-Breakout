package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.application.Platform;
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
import nz.ac.auckland.se206.GameManager.Objectives;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;
import nz.ac.auckland.se206.StyleManager;
import nz.ac.auckland.se206.WalkieTalkieManager;
import nz.ac.auckland.se206.gpt.ChatMessage;

public class ConnectDotsController extends Controller {

  @FXML private GridPane gridPane;
  @FXML private StackPane disableSecurity;
  @FXML private Label timerLabel;
  @FXML private Button resetButton;
  @FXML private Label closebtn;

  // 0 = empty, negative = node
  private int[][] grid = new int[6][6];
  private int[][] solution;
  private final int size = 6;
  // [0] = red, [1] = blue, [2] = green, [3] = purple
  private List<Integer> colours = new ArrayList<Integer>(4);

  // initialising instances
  private int initialColumn = -1;
  private int initialRow = -1;

  private List<Integer> columnPath = new ArrayList<>();
  private List<Integer> rowPath = new ArrayList<>();

  private boolean nodeSelected = false;
  private int nodeValue = 0;

  private WalkieTalkieManager walkieTalkieManager = WalkieTalkieManager.getInstance();

  public void initialize() {
    // initialising the methods to play this puzzle
    SceneManager.setController(Scenes.CONNECTDOTS, this);
    super.setTimerLabel(timerLabel, 1);
    disableSecurity.setVisible(false);
    setSolution();
    randomiseColours();
    randomiseRotation();
    flipSolution();
    copyStartEndNodes();
  }

  public void startDrag(MouseEvent e) {
    // drag event for the puzzle
    initialColumn = getIndex(e.getX(), false);
    initialRow = getIndex(e.getY(), true);
    nodeSelected = grid[initialRow][initialColumn] < 0;
    // handle when node is selected
    if (nodeSelected) {
      nodeValue = grid[initialRow][initialColumn];
      columnPath.add(initialColumn);
      rowPath.add(initialRow);
    }
  }

  public void drag(MouseEvent e) {
    // if no node is selected
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
    // setting variables
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
    // if game is completed
    if (isGameComplete()) {
      // handle the relevant methods
      disableSecurity.setVisible(true);
      GameState.isConnectDotsSolved = true;
      resetButton.setDisable(true);
      closebtn.setDisable(true);
      GameManager.setCurrentObjective(Objectives.SELECT_VAULT_DOOR);
      App.textToSpeech("Security Disabled, Level 1 Vault Access Granted");
      Platform.runLater(
          () -> {
            WalkieTalkieManager.setWalkieTalkieNotifcationOn();
            walkieTalkieManager.setWalkieTalkieText(
                new ChatMessage(
                    "assistant",
                    "Nice job! 2 out of the 3 vault security systems have now been disabled"));
          });
    }
  }

  private void setSolution() {
    // 0 = empty, 1 = colour1, 2 = colour2, 3 = colour3, 4 = colour4, negative = node
    int[][] solution = {
      {3, 3, 3, 3, 3, 3},
      {-3, -1, -4, 4, -4, 3},
      {-2, 1, 1, 1, 1, 3},
      {2, 2, 2, -2, 1, 3},
      {0, 0, -3, -1, 1, 3},
      {0, 0, 3, 3, 3, 3}
    };
    // setting it to the solution
    this.solution = solution;
  }

  private void randomiseColours() {
    int i = 1;
    Random random = new Random();
    colours.add(random.nextInt(4) + 1);

    // Random int for each colour
    while (i < 4) {
      int colour = random.nextInt(4) + 1;
      if (colours.contains(colour)) {
        continue;
      }
      i++;
      colours.add(colour);
    }
  }

  private void randomiseRotation() {
    Random random = new Random();
    int rotations = random.nextInt(4);
    for (int i = 0; i < rotations; i++) {
      rotate90Clockwise();
    }
  }

  private void rotate90Clockwise() {

    int[][] rotation = new int[size][size];

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        rotation[j][size - 1 - i] = solution[i][j];
      }
    }

    solution = rotation;
  }

  private void flipSolution() {
    Random random = new Random();
    // 0 = nothing, 1 = flip horizontally, 2 - flip vertically
    int value = random.nextInt(3);
    if (value == 0) {
      return;
    }

    // flip horizontally
    if (value == 1) {
      for (int r = 0; r < size; r++) {
        for (int c = 0; c < size / 2; c++) {
          int temp = solution[r][c];
          solution[r][c] = solution[r][size - 1 - c];
          solution[r][size - 1 - c] = temp;
        }
      }
      return;
    }

    // flip vertically
    if (value == 2) {
      for (int r = 0; r < size / 2; r++) {
        for (int c = 0; c < size; c++) {
          int temp = solution[r][c];
          solution[r][c] = solution[size - 1 - r][c];
          solution[size - 1 - r][c] = temp;
        }
      }
    }
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
      // 45 is the cell height
      return (int)
          Math.floor(coordinate / gridPane.getChildren().get(0).getBoundsInParent().getHeight());
    }
    // 50 is the cell width
    return (int)
        Math.floor(coordinate / gridPane.getChildren().get(0).getBoundsInParent().getWidth());
  }

  private Rectangle getNodeFromGridPane(GridPane gridPane, int row, int col) {
    // Get the node from the gridpane
    for (Node node : gridPane.getChildren()) {
      int columnIndex = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
      int rowIndex = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);
      // if the column and row is the same as the node return the node
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
    // 0 = empty, 1 = red, 2 = blue, 3 = green, 4 = purple, negative = node
    value = Math.abs(value);

    // getting paint value from int
    if (value == colours.get(0)) {
      return Paint.valueOf("red");
    }

    if (value == colours.get(1)) {
      return Paint.valueOf("blue");
    }

    if (value == colours.get(2)) {
      return Paint.valueOf("green");
    }

    if (value == colours.get(3)) {
      return Paint.valueOf("purple");
    }

    return Paint.valueOf("white");
  }

  public void resetGame() {
    // Reset grid for resetting game
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        if (grid[i][j] < 0) {
          continue;
        }
        // resetting by painting it white again
        grid[i][j] = 0;
        Rectangle node = getNodeFromGridPane(gridPane, i, j);
        node.setFill(Paint.valueOf("white"));
      }
    }
  }

  private boolean isGameComplete() {
    // Check if the grid is the same as the solution
    for (int r = 0; r < 6; r++) {
      for (int c = 0; c < 6; c++) {

        // If the cell is a node continue;
        if (solution[r][c] <= 0) {
          continue;
        }

        // If the cell is not the same as the solution return false;
        if (grid[r][c] != solution[r][c]) {
          return false;
        }
      }
    }
    return true;
  }

  public void switchToSecurity() {
    App.setUI(Scenes.SECURITY);
  }

  @FXML
  protected void grantAccess() {
    if (GameState.isConnectDotsSolved) {
      StyleManager.setDisable(true, "computer");
    }
    // setting style for when access is granted by disabling firewall
    GameState.isFirewallDisabled = true;
    App.setUI(Scenes.SECURITY);
  }
}
