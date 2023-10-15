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

  private void setGeneralInformation() {
    String context =
        "The adventure begins within the confines of a bustling, heavily guarded bank, the"
            + " objective is to steal a substantial sum of money and make a daring escape"
            + " without getting caught. With security systems, vigilant guards, and unexpected"
            + " obstacles lurking around every corner, players must employ their wits, stealth,"
            + " and strategic thinking to outsmart their adversaries and execute the perfect"
            + " heist. As the tension rises and the stakes get higher, the bank becomes a"
            + " labyrinth of secrets and surprises, making every decision crucial and every"
            + " moment heart-pounding.";
    contextParagraph.setText(context);

    String storyline =
        "There are 3 phases to the game; players must first disable the bank's formidable security"
            + " systems, then execute a daring money heist, and finally, navigate the"
            + " tense escape.";
    storylineParagraph.setText(storyline);

    String disableSecurity =
        "In the initial phase of the game, the player must discretely gather information,"
            + " infiltrate the bank's computer systems, and secure authorized access, with their"
            + " performance directly influencing the potential haul in the subsequent heist.";
    disableSecurityParagraph.setText(disableSecurity);

    String executeHeist =
        "During the second phase, players will select and unlock vaults, solving intricate puzzles"
            + " to acquire the loot. They can then choose to continue raiding additional vaults or"
            + " secure the collected loot, which will initiate the third phase. Players must get"
            + " level 3 vault access from the computer to access all 3 vaults.";
    executingHeistParagraph.setText(executeHeist);

    String escaping =
        "In the final phase, players must navigate the intense escape scenario triggered either by"
            + " collecting the loot or when only 30 seconds remain on the timer. Their mission: to"
            + " disable the alarm and set off a bomb for a daring getaway. Failing to do so within"
            + " the time limit results in capture and no stolen money.";
    escapingParagraph.setText(escaping);
  }

  @FXML
  public void goGeneralInformation() {
    instructionsScroll.setVvalue(0);
    setActiveTab(generalInfoTab);
  }

  @FXML
  private void goGameMechanics() {
    instructionsScroll.setVvalue(gameMechanicsVertValue);
    setActiveTab(gameMechanicsTab);
  }

  @FXML
  private void goPuzzles() {
    instructionsScroll.setVvalue(1);
    setActiveTab(puzzlesTab);
  }

  @FXML
  private void onSwitchToMain() {
    App.setUI(Scenes.MAIN_MENU);
    goGeneralInformation();
  }

  @FXML
  private void hoverButtonEntry() {
    closeButton.getStyleClass().clear();
    closeButton.getStyleClass().add("hoverCloseButton");
  }

  @FXML
  private void hoverButtonExit() {
    closeButton.getStyleClass().clear();
    closeButton.getStyleClass().add("closeButton");
  }

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
