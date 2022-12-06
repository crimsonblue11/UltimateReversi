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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class View {
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

    /**
     * Constructor method.
     *
     * @param isBlack sets member variable isBlack
     */
    public View(boolean isBlack) {
        Model.GetSelf().StoreView(this);
        this.IS_BLACK = isBlack;

        // when update is called, the current turn will switch
        // the requirements state white must go first, hence when initialised the roles
        // must be switched
        m_Frame.setTitle("Reversi");
        IS_TURN = isBlack;
    }

    /**
     * Method for creating the GUI of this view.
     */
    public void CreateGUI() {
        m_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m_Frame.setLayout(new BorderLayout());
        m_Frame.setPreferredSize(new Dimension(600, 700));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 8));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // need to access black in reverse since view is upside down
                if (IS_BLACK) {
                    m_ButtonArray[i][j] = new GridButton(i, j, Model.GetSelf().GetBoardState().GetState(7 - i, 7 - j));
                } else {
                    m_ButtonArray[i][j] = new GridButton(i, j, Model.GetSelf().GetBoardState().GetState(i, j));
                }

                m_ButtonArray[i][j].addActionListener((e) -> {
                    if (IS_TURN) {
                        gridButtonAction((GridButton) e.getSource());
                    }
                });

                panel.add(m_ButtonArray[i][j]);
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

        m_Frame.add(panel, BorderLayout.CENTER);
        m_Frame.add(button, BorderLayout.SOUTH);
        m_Frame.add(m_Title, BorderLayout.NORTH);

        m_Frame.pack();
        m_Frame.setVisible(true);

        Update();
    }

    private void gridButtonAction(GridButton b) {
        int row = b.GetRow();
        int col = b.GetCol();

        Model.GetSelf().GetBoardState().SetState(row, col, IS_BLACK);
        Model.GetSelf().GetBoardState().CaptureCounters(row, col, IS_BLACK);

        Model.GetSelf().UpdateViews();
    }

    private void aiButtonAction() {
        GridButton highest = m_ButtonArray[0][0];

        // algorithm will find the highest value on the board
        // and ignore subsequent spaces of equal value
        // hence the first occurrence of the highest value will always be played

        // todo : what fuck
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int curr;
                curr = Model.GetSelf().GetBoardState().CountCapture(i, j, IS_BLACK);
                if (curr > highest.GetState()) {
                    highest = m_ButtonArray[i][j];
                }
            }
        }

        int row = highest.GetRow();
        int col = highest.GetCol();

        Model.GetSelf().GetBoardState().SetState(row, col, IS_BLACK);
        Model.GetSelf().GetBoardState().CaptureCounters(row, col, IS_BLACK);

        Model.GetSelf().UpdateViews();
    }

    /**
     * Update method. Called every time a piece is played, for both views.
     */
    public void Update() {
        String newTitle;
        if (IS_TURN) {
            IS_TURN = false;
            newTitle = m_Title.getText().replace(" (your turn)", "");
            m_Title.setText(newTitle);
        } else {
            IS_TURN = true;
            newTitle = m_Title.getText().concat(" (your turn)");
            m_Title.setText(newTitle);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // code for updating button array states depending on external state array
                // again the black player's board must be accessed in reverse
                if (IS_BLACK) {
                    m_ButtonArray[i][j].SetState(Model.GetSelf().GetBoardState().GetState(7 - i, 7 - j));
                } else {
                    m_ButtonArray[i][j].SetState(Model.GetSelf().GetBoardState().GetState(i, j));
                }

                // code for enabling/disabling buttons
                // a button will be enabled if it is the current view's turn, and
                // the space will capture at least 1 opposing piece
                // if neither of these cases are fulfilled, the button is disabled
                // i.e. the space is not playable
                m_ButtonArray[i][j].setEnabled(IS_TURN && Model.GetSelf().GetBoardState().CountCapture(i, j, IS_BLACK) > 0);
            }
        }

        m_Frame.repaint();

        // game will end if the entire board is full, or both players are in a stalemate
//        if (Model.GetSelf().GetBoardState().CheckGameOver()) {
//            String outDialog;
//            int whiteScore = Model.GetSelf().GetBoardState().GetWhiteScore();
//            int blackScore = Model.GetSelf().GetBoardState().GetBlackScore();
//
//            String winner_colour = (whiteScore > blackScore) ? "White" : "Black";
//
//            outDialog = winner_colour + " wins: " + whiteScore + " : " + blackScore;
//
//            JOptionPane.showMessageDialog(null, outDialog);
//            System.exit(0);
//        }
    }
}