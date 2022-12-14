/**
 * Controller class for startScreen.fxml, i.e. the start menu for the game.
 *
 * @author Medusa Dempsey
 * @version 1.0
 */

package org.example.reversi;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class StartController {
    /**
     * Label displaying the name of the currently selected game mode.
     * Accessed via FXML tag.
     */
    @FXML
    private Label gamemodelabel;
    /**
     * Label displaying a description of the currently selected game mode.
     * Accessed via FXML tag.
     */
    @FXML
    private Label gamemodedesc;
    /**
     * ColorPicker element for player 1.
     * Used to pick colour of counters.
     * Accessed via FXML tag.
     */
    @FXML
    private ColorPicker p1colour;
    /**
     * ColorPicker element for player 1.
     * Used to pick colour of counters.
     * Accessed via FXML tag.
     */
    @FXML
    private ColorPicker p2colour;
    /**
     * Integer to keep track of currently selected game mode.
     * Used to display associated title and description.
     */
    private int m_GMCounter = 0;
    /**
     * Array of string arrays containing text to be displayed for each game mode.
     * String arrays are stored in the format &#123; title, description &#125;
     */
    private final String[][] gameModeText = {
            {
                    "Player vs Player",
                    "Classic player vs player Reversi!"
            },
            {
                    "Player vs Computer",
                    "Single player against a computer player (not implemented yet!)"
            },
    };

//    FLISS WAS HERE
//    private String[][] kjm,,,,,,,,,,,,jhn

    /**
     * FXML initialise method.
     * Sets initial value of gamemode text via {@code updateGameModeText()}.
     *
     * @see #updateGameModeText()
     */
    @FXML
    private void initialize() {
        updateGameModeText();
    }

    /**
     * Method to handle moving the game mode back, via the &lt; button.
     * Decrements {@code m_GMCounter}, then calls {@code updateGameModeText()}.
     *
     * @see #updateGameModeText()
     */
    @FXML
    private void handleModeLeft() {
        m_GMCounter--;
        if (m_GMCounter < 0) {
            m_GMCounter = gameModeText.length - 1;
        }

        updateGameModeText();
    }

    /**
     * Method to handle moving the game mode forward, via the &gt; button.
     * Increments {@code m_GMCounter}, then calls {@code updateGameModeText()}.
     *
     * @see #updateGameModeText()
     */
    @FXML
    private void handleModeRight() {
        m_GMCounter++;
        if (m_GMCounter > gameModeText.length - 1) {
            m_GMCounter = 0;
        }

        updateGameModeText();
    }

    /**
     * Method to handle 'Play' button action.
     * Calls {@code StartStage.loadGame()} and sets player counter colours.
     *
     * @see StartStage#loadGame()
     */
    @FXML
    private void handlePlay() {
        StartStage.setPlayerColours(p1colour.getValue(), p2colour.getValue());
        StartStage.getInstance().loadGame();
    }

    /**
     * Method to update {@code gamemodelabel}'s text depending on the value of {@code m_GMCounter}.
     */
    private void updateGameModeText() {
        gamemodelabel.setText(gameModeText[m_GMCounter][0]);
        gamemodedesc.setText(gameModeText[m_GMCounter][1]);
    }
}
