/**
 * Class for drawing the window, game board, and everything else for each window.
 * Also handles some logic.
 *
 * @author Medusa Dempsey
 * @version 1.0
 */

package org.example;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PlayerView extends Stage {
    private final Model m_Model;
    /**
     * Array containing all spaces on the board, as drawn from this view.
     */
    private final GridButton[][] m_ButtonArray = new GridButton[8][8];
    /**
     * Boolean representing the colour of this view's player - true if black, false if white.
     */
    private final boolean IS_BLACK;
    /**
     * Boolean set to true if the current turn is this view's turn.
     */
    private boolean IS_TURN;
    /**
     * Title of the screen of this view.
     */
    private Label m_Title;
    final static int WIN_WIDTH = 600;
    final static int WIN_HEIGHT = 700;

    /**
     * Constructor method.
     *
     * @param isBlack sets member variable isBlack
     */
    public PlayerView(boolean isBlack) {
        m_Model = Model.GetSelf();
        m_Model.StoreView(this);

        setTitle("Reversi");

        // when update is called, the current turn will switch
        // white goes first, so when initialised the roles must be switched
        IS_TURN = isBlack;
        IS_BLACK = isBlack;
    }

    /**
     * Method for creating the GUI of this view.
     */
    public void CreateGUI() {
        setOnCloseRequest(e -> System.exit(0));
        setWidth(WIN_WIDTH);
        setHeight(WIN_HEIGHT);

        BorderPane root = new BorderPane();

        GridPane grid = new GridPane();
        root.setCenter(grid);

        for (int r_pos = 0; r_pos < BoardState.GRID_SIZE; r_pos++) {
            for (int c_pos = 0; c_pos < BoardState.GRID_SIZE; c_pos++) {
                int r = IS_BLACK ? 7 - r_pos : r_pos;
                int c = IS_BLACK ? 7 - c_pos : c_pos;
                m_ButtonArray[r_pos][c_pos] = new GridButton(r_pos, c_pos, m_Model.GetBoardState().GetState(r, c));

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

        String player_colour = IS_BLACK ? "Black" : "White";
        m_Title = new Label(player_colour + " player");

        root.setCenter(grid);
        root.setBottom(aiButton);
        root.setTop(m_Title);

        Scene m_Scene = new Scene(root);
        setScene(m_Scene);

        show();

        Update();
    }

    /**
     * Method that runs every time a space is clicked, if it is the player's turn.
     * Captures all available counters from the given space, and updates both views.
     *
     * @param b Button that has been clicked.
     */
    private void gridButtonAction(GridButton b) {
        int row = b.GetRow();
        int col = b.GetCol();

        m_Model.GetBoardState().SetState(row, col, IS_BLACK);
        m_Model.GetBoardState().CaptureCounters(row, col, IS_BLACK);

        m_Model.UpdateViews();
    }

    /**
     * Method called when the AI player button is pressed.
     * Finds the highest score position on the board, then plays that.
     */
    private void aiButtonAction() {
        GridButton highestButton = getHighestScoreSpace();
        int row = highestButton.GetRow();
        int col = highestButton.GetCol();

        m_Model.GetBoardState().SetState(row, col, IS_BLACK);
        m_Model.GetBoardState().CaptureCounters(row, col, IS_BLACK);

        m_Model.UpdateViews();
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
                int curr = m_Model.GetBoardState().CountCapture(r_pos, c_pos, IS_BLACK);

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
    public void Update() {
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
                // code for updating button array states depending on external state array
                // again the black player's board must be accessed in reverse
                int r_model = IS_BLACK ? 7 - r_pos : r_pos;
                int c_model = IS_BLACK ? 7 - c_pos : c_pos;

                m_ButtonArray[r_pos][c_pos].SetState(m_Model.GetBoardState().GetState(r_model, c_model));

                // space is enabled if it is the current view's turn, and the space will capture
                // at least 1 opposing piece - otherwise, the button is disabled
                boolean buttonEnabled = IS_TURN && m_Model.GetBoardState().CountCapture(r_pos, c_pos, IS_BLACK) > 0;
                m_ButtonArray[r_pos][c_pos].setDisable(!buttonEnabled);

                m_ButtonArray[r_pos][c_pos].Update();
            }
        }

//        m_Frame.repaint();
        // might need jfx equivalent of this
    }

    @Override
    public void close() {
        for(GridButton[] l : m_ButtonArray) {
            for(GridButton g : l) {
                g.SetState(SpaceState.EMPTY);
            }
        }

        Update();

        super.close();
    }
}