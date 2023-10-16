package nz.ac.auckland.se206.controllers;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

/** Controller class for the Intructions scene. */
public class InstructionsController extends Controller {

  // Tab Headers
  @FXML private StackPane generalInfoTab;
  @FXML private StackPane gameMechanicsTab;
  @FXML private StackPane puzzlesTab;

  private StackPane activeTab;

  // General Information labels
  @FXML private Label contextParagraph;
  @FXML private Label storylineParagraph;
  @FXML private Label disableSecurityParagraph;
  @FXML private Label executingHeistParagraph;
  @FXML private Label escapingParagraph;

  @FXML private ScrollPane instructionsScroll;
  @FXML private Button closeButton;
  @FXML private VBox gameMechanics;

  private double gameMechanicsVertValue = 0;

  /** Initialize the Instructions Controller. Sets up the initial state of the scene. */
  public void initialize() {
    SceneManager.setController(Scenes.INSTRUCTIONS, this);
    setGeneralInformation();
    goGeneralInformation();

    // Update layout bounds
    instructionsScroll.requestFocus();
    instructionsScroll.layout();
    gameMechanics.getParent().getParent().layout();
    gameMechanics.getParent().layout();
    gameMechanics.getParent().layout();
    gameMechanics.layout();

    // calculate relative position of game mechanics section
    double totalHeight = instructionsScroll.getContent().getBoundsInLocal().getMaxY();
    double vertPosition = gameMechanics.getBoundsInParent().getMinY();
    vertPosition += .52 * instructionsScroll.getPrefHeight();
    gameMechanicsVertValue = (vertPosition / totalHeight);

    instructionsScroll
        .vvalueProperty()
        .addListener(
            (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
              double vertValue = newValue.doubleValue();
              if (vertValue < gameMechanicsVertValue - 0.03) {
                setActiveTab(generalInfoTab);
              } else if (vertValue > 0.93) {
                setActiveTab(puzzlesTab);
              } else {
                setActiveTab(gameMechanicsTab);
              }
            });
  }

  /** Sets the general information paragraphs with detailed instructions about the game. */
  private void setGeneralInformation() {
    // Set general information message
    String context =
        "The adventure begins within the confines of a bustling, heavily guarded bank, the"
            + " objective is to steal a substantial sum of money and make a daring escape"
            + " without getting caught. With security systems, vigilant guards, and unexpected"
            + " obstacles lurking around every corner, players must employ their wits, stealth,"
            + " and strategic thinking to outsmart their adversaries and execute the perfect"
            + " heist. As the tension rises and the stakes get higher, the bank becomes a"
            + " labyrinth of secrets and surprises, making every decision crucial and every"
            + " moment heart-pounding.";
    // setting the text to the required node
    contextParagraph.setText(context);

    // Set the story line message
    String storyline =
        "There are 3 phases to the game; players must first disable the bank's formidable security"
            + " systems, then execute a daring money heist, and finally, navigate the"
            + " tense escape.";
    // setting the text to the required node
    storylineParagraph.setText(storyline);

    // set how to disable security message
    String disableSecurity =
        "In the initial phase of the game, the player must discretely gather information,"
            + " infiltrate the bank's computer systems, and secure authorized access, with their"
            + " performance directly influencing the potential haul in the subsequent heist.";
    // setting the text to the required node
    disableSecurityParagraph.setText(disableSecurity);
    // set the how the execute heist works message
    String executeHeist =
        "During the second phase, players will select and unlock vaults, solving intricate puzzles"
            + " to acquire the loot. They can then choose to continue raiding additional vaults or"
            + " secure the collected loot, which will initiate the third phase. Players must get"
            + " level 3 vault access from the computer to access all 3 vaults.";
    // setting the text to the required node
    executingHeistParagraph.setText(executeHeist);

    // set the escape sequence message
    String escaping =
        "In the final phase, players must navigate the intense escape scenario triggered either by"
            + " collecting the loot or when only 30 seconds remain on the timer. Their mission: to"
            + " disable the alarm and set off a bomb for a daring getaway. Failing to do so within"
            + " the time limit results in capture and no stolen money.";
    // setting the text to the required node
    escapingParagraph.setText(escaping);
  }

  /** Switches to the General Information tab and scrolls to the top of the instructions. */
  @FXML
  public void goGeneralInformation() {
    instructionsScroll.setVvalue(0);
    setActiveTab(generalInfoTab);
  }

  /** Switches to the Game Mechanics tab and scrolls to the relevant section in the instructions. */
  @FXML
  private void goGameMechanics() {
    instructionsScroll.setVvalue(gameMechanicsVertValue);
    setActiveTab(gameMechanicsTab);
  }

  /** Switches to the Puzzles tab and scrolls to the end of the instructions. */
  @FXML
  private void goPuzzles() {
    instructionsScroll.setVvalue(1);
    setActiveTab(puzzlesTab);
  }

  /** Switches to the Main Menu scene and returns to the General Information tab. */
  @FXML
  private void onSwitchToMain() {
    App.setUi(Scenes.MAIN_MENU);
    goGeneralInformation();
  }

  /** Handles mouse hover entry on the Close Button. */
  @FXML
  private void hoverButtonEntry() {
    closeButton.getStyleClass().clear();
    closeButton.getStyleClass().add("hoverCloseButton");
  }

  /** Handles mouse hover exit from the Close Button. */
  @FXML
  private void hoverButtonExit() {
    closeButton.getStyleClass().clear();
    closeButton.getStyleClass().add("closeButton");
  }

  /**
   * Sets the active tab in the instructions view and updates the tab styling.
   *
   * @param newActiveTab The StackPane representing the new active tab.
   */
  private void setActiveTab(StackPane newActiveTab) {

    // Change old active tab to normal tab styling
    if (activeTab != newActiveTab) {
      // Set new tab to active tab styling
      ObservableList<Node> children = newActiveTab.getChildren();
      Node activeRectangle = children.get(0);
      activeRectangle.getStyleClass().clear();
      activeRectangle.getStyleClass().add("activeRectangle");
      Node activeText = children.get(1);
      activeText.getStyleClass().clear();
      activeText.getStyleClass().add("activeTab");

      // Remove the styling on old active tab
      if (activeTab != null) {
        ObservableList<Node> children2 = activeTab.getChildren();
        Node otherRectangle = children2.get(0);
        otherRectangle.getStyleClass().clear();
        otherRectangle.getStyleClass().add("Rectangle");
        Node otherText = children2.get(1);
        otherText.getStyleClass().clear();
        otherText.getStyleClass().add("tab");
      }
      activeTab = newActiveTab;
    }
  }
}
