package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;

public class StartController {
    @FXML
    private Label gamemodelabel;
    @FXML
    private Label gamemodedesc;
    @FXML
    private ColorPicker p1colour;
    @FXML
    private ColorPicker p2colour;

    private int m_GMCounter = 0;

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

    @FXML
    private void initialize() {
        updateGMText();
    }

    @FXML
    private void handleModeLeft() {
        m_GMCounter--;
        if (m_GMCounter < 0) {
            m_GMCounter = gameModeText.length - 1;
        }

        updateGMText();
    }

    @FXML
    private void handleModeRight() {
        m_GMCounter++;
        if (m_GMCounter > gameModeText.length - 1) {
            m_GMCounter = 0;
        }

        updateGMText();
    }

    @FXML
    private void handlePlay() {
        StartStage.getInstance().loadGame();
    }

    private void updateGMText() {
        gamemodelabel.setText(gameModeText[m_GMCounter][0]);
        gamemodedesc.setText(gameModeText[m_GMCounter][1]);
    }
}
