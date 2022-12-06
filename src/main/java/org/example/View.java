/**
 * Class for drawing the window, game board, and everything else for each window.
 * Also handles some logic.
 *
 * @author Medusa Dempsey
 * @version 1.0
 */

package org.example;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class View {
    private final Model m_Model;
    /**
     * Array containing all spaces on the board, as drawn from this view.
     */
    private final GridButton[][] m_ButtonArray = new GridButton[8][8];
    /**
     * JFrame containing the view.
     */
    private final JFrame m_Frame = new JFrame();
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
    private JLabel m_Title;
    final static int WIN_WIDTH = 600;
    final static int WIN_HEIGHT = 700;

    /**
     * Constructor method.
     *
     * @param isBlack sets member variable isBlack
     */
    public View(boolean isBlack) {
        m_Model = Model.GetSelf();
        m_Model.StoreView(this);

        m_Frame.setTitle("Reversi");

        // when update is called, the current turn will switch
        // white goes first, so when initialised the roles must be switched
        IS_TURN = isBlack;
        IS_BLACK = isBlack;
    }

    /**
     * Method for creating the GUI of this view.
     */
    public void CreateGUI() {
        m_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m_Frame.setLayout(new BorderLayout());
        m_Frame.setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(BoardState.GRID_SIZE, BoardState.GRID_SIZE));

        for (int r_pos = 0; r_pos < BoardState.GRID_SIZE; r_pos++) {
            for (int c_pos = 0; c_pos < BoardState.GRID_SIZE; c_pos++) {
                int r = IS_BLACK ? 7 - r_pos : r_pos;
                int c = IS_BLACK ? 7 - c_pos : c_pos;
                m_ButtonArray[r_pos][c_pos] = new GridButton(r_pos, c_pos, m_Model.GetBoardState().GetState(r, c));

                m_ButtonArray[r_pos][c_pos].addActionListener(e -> {
                    if (IS_TURN) {
                        gridButtonAction((GridButton) e.getSource());
                    }
                });

                panel.add(m_ButtonArray[r_pos][c_pos]);
            }
        }

        // code for greedy AI button
        JButton button = new JButton("Make greedy AI move");
        button.setFont(new Font("Ariel", Font.BOLD, 20));

        button.addActionListener(e -> {
            if (IS_TURN) {
                aiButtonAction();
            }
        });

        String player_colour = IS_BLACK ? "Black" : "White";
        m_Title = new JLabel(player_colour + " player");

        // add everything to the panel
        m_Frame.add(panel, BorderLayout.CENTER);
        m_Frame.add(button, BorderLayout.SOUTH);
        m_Frame.add(m_Title, BorderLayout.NORTH);

        m_Frame.pack();
        m_Frame.setVisible(true);

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
                m_ButtonArray[r_pos][c_pos].setEnabled(buttonEnabled);
            }
        }

        m_Frame.repaint();
    }
}