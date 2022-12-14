/**
 * Class for drawing the window, game board, and everything else for each player's window.
 * Also handles some game logic.
 *
 * @author Medusa Dempsey
 * @version 1.1
 */

package org.example.reversi;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PlayerView extends Stage {
    /**
     * Model object reference.
     */
    private final Model m_Model;
    /**
     * Array containing all spaces on the board, as drawn from this view.
     */
    private final GridButton[][] m_ButtonArray = new GridButton[8][8];
    /**
     * Boolean representing this view's player - true if player 1, false if player 2.
     */
    private final boolean IS_PLAYER1;
    /**
     * Boolean set to true if the current turn is this view's turn.
     */
    private boolean IS_TURN;
    /**
     * Title of the screen of this view.
     */
    private Label m_Title;
    /**
     * Constant representing width of the game window.
     */
    final static int WIN_WIDTH = 600; // todo : move to another class
    /**
     * Constant representing height of the game window.
     */
    final static int WIN_HEIGHT = 700; // todo : move to another class

    /**
     * Constructor method.
     *
     * @param isPlayer1 Value to set {@code IS_PLAYER1}.
     */
    public PlayerView(boolean isPlayer1) {
        m_Model = Model.getSelf();
        m_Model.storeView(this);

        setTitle("Reversi");

        // when update is called, the current turn will switch
        // player 1 goes first, so when initialised the roles must be switched
        IS_TURN = !isPlayer1;
        IS_PLAYER1 = isPlayer1;
    }

    /**
     * Method for creating this view's GUI.
     */
    public void createGUI() {
        setOnCloseRequest(e -> System.exit(0));
        setWidth(WIN_WIDTH);
        setHeight(WIN_HEIGHT);

        BorderPane root = new BorderPane();

        GridPane grid = new GridPane();
        root.setCenter(grid);

        for (int r_pos = 0; r_pos < BoardState.GRID_SIZE; r_pos++) {
            for (int c_pos = 0; c_pos < BoardState.GRID_SIZE; c_pos++) {
                int r = IS_PLAYER1 ? 7 - r_pos : r_pos;
                int c = IS_PLAYER1 ? 7 - c_pos : c_pos;
                m_ButtonArray[r_pos][c_pos] = new GridButton(r_pos, c_pos, m_Model.getBoardState().getState(r, c));

                m_ButtonArray[r_pos][c_pos].setOnMouseClicked(e -> {
                    if (IS_TURN) {
                        gridButtonAction((GridButton) e.getSource());
                    }
                });

                grid.add(m_ButtonArray[r_pos][c_pos], r_pos, c_pos);
            }
        }

        // code for greedy AI button
        Button aiButton = new Button("Make greedy AI move");
        aiButton.setFont(Font.font("Ariel", FontWeight.BOLD, FontPosture.REGULAR, 20));

        aiButton.setOnMouseClicked(e -> {
            if (IS_TURN) {
                aiButtonAction();
            }
        });

        m_Title = new Label(IS_PLAYER1 ? "Player 1" : "Player 2");

        root.setCenter(grid);
        root.setBottom(aiButton);
        root.setTop(m_Title);

        Scene m_Scene = new Scene(root);
        setScene(m_Scene);

        show();

        update();
    }

    /**
     * Method that runs every time a space is clicked, if it is the player's turn.
     * Captures all available counters from the given space, and updates both views.
     *
     * @param b Button that has been clicked.
     */
    private void gridButtonAction(GridButton b) {
        int row = b.getRow();
        int col = b.getCol();

        m_Model.getBoardState().setState(row, col, IS_PLAYER1);
        m_Model.getBoardState().captureCounters(row, col, IS_PLAYER1);

        m_Model.updateViews();
    }

    /**
     * Method called when the AI player button is pressed.
     * Finds the highest score position on the board, then plays that.
     */
    private void aiButtonAction() {
        GridButton highestButton = getHighestScoreSpace();
        int row = highestButton.getRow();
        int col = highestButton.getCol();

        m_Model.getBoardState().setState(row, col, IS_PLAYER1);
        m_Model.getBoardState().captureCounters(row, col, IS_PLAYER1);

        m_Model.updateViews();
    }

    /**
     * Method to find the highest score position on the board, used by the greedy AI.
     *
     * @return GridButton of the position with the highest score, if played.
     */
    private GridButton getHighestScoreSpace() {
        GridButton highestButton = m_ButtonArray[0][0];
        int highest = 0;

        // loop through each position on the board
        for (int r_pos = 0; r_pos < 8; r_pos++) {
            for (int c_pos = 0; c_pos < 8; c_pos++) {
                // get potential score from current space
                int curr = m_Model.getBoardState().countCapture(r_pos, c_pos, IS_PLAYER1);

                // set the highest space to current, and highest potential score to current
                if (curr > highest) {
                    highestButton = m_ButtonArray[r_pos][c_pos];
                    highest = curr;
                }
            }
        }

        return highestButton;
    }

    /**
     * Update method. Called every time a piece is played, for both views.
     */
    public void update() {
        String newTitle;
        if (IS_TURN) {
            IS_TURN = false;
            newTitle = m_Title.getText().replace(" (your turn)", "");
        } else {
            IS_TURN = true;
            newTitle = m_Title.getText().concat(" (your turn)");
        }
        m_Title.setText(newTitle);

        for (int r_pos = 0; r_pos < 8; r_pos++) {
            for (int c_pos = 0; c_pos < 8; c_pos++) {
                int r_model = IS_PLAYER1 ? 7 - r_pos : r_pos;
                int c_model = IS_PLAYER1 ? 7 - c_pos : c_pos;

                m_ButtonArray[r_pos][c_pos].setState(m_Model.getBoardState().getState(r_model, c_model));

                boolean buttonEnabled = IS_TURN && m_Model.getBoardState().countCapture(r_pos, c_pos, IS_PLAYER1) > 0;
                m_ButtonArray[r_pos][c_pos].setDisable(!buttonEnabled);

                m_ButtonArray[r_pos][c_pos].update();
            }
        }
    }

    /**
     * Override for Stage close method.
     * Sets each button to empty, updates the view, and then
     * calls Stage.close().
     *
     * @since 1.1
     */
    @Override
    public void close() {
        for (GridButton[] l : m_ButtonArray) {
            for (GridButton g : l) {
                g.setState(SpaceState.EMPTY);
            }
        }

        update();

        super.close();
    }
}