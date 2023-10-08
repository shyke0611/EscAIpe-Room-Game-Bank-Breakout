package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.Scenes;

public class InstructionsController extends Controller {

  @FXML private Label contextParagraph;
  @FXML private Label storylineParagraph;
  @FXML private Label disableSecurityParagraph;
  @FXML private Label executingHeistParagraph;
  @FXML private Label escapingParagraph;

  public void initialize() {
    SceneManager.setController(Scenes.INSTRUCTIONS, this);
    setGeneralInformation();
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
}
