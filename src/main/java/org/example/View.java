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
    private final GridButton[][] arrayButt = new GridButton[8][8];
    /**
     * JFrame containing the view.
     */
    private final JFrame guiFrame = new JFrame();
    /**
     * Boolean representing the colour of this view's player - true if black, false if white.
     */
    private final boolean isBlack;
    /**
     * Boolean set to true if the current turn is this view's turn.
     */
    private boolean isTurn;
    /**
     * Title of the screen of this view.
     */
    private JLabel screenTitle;

    /**
     * Constructor method.
     *
     * @param isBlack sets member variable isBlack
     */
    public View(boolean isBlack) {
        Model.get().storeView(this);
        this.isBlack = isBlack;

        // when update is called, the current turn will switch
        // the requirements state white must go first, hence when initialised the roles
        // must be switched
        guiFrame.setTitle("Reversi");
        isTurn = isBlack;
    }

    /**
     * Method for creating the GUI of this view.
     */
    public void createGUI() {
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setLayout(new BorderLayout());
        guiFrame.setPreferredSize(new Dimension(600, 700));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 8));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // need to access black in reverse since view is upside down
                if (isBlack) {
                    arrayButt[i][j] = new GridButton(i, j, Model.get().bState.getState(7 - i, 7 - j));
                } else {
                    arrayButt[i][j] = new GridButton(i, j, Model.get().bState.getState(i, j));
                }

                // implementing button functionality with lambda expression
                arrayButt[i][j].addActionListener((e) -> {
                    if (isTurn) {
                        GridButton b = (GridButton) e.getSource();
                        int row = b.getRow();
                        int col = b.getCol();

                        Model.get().bState.setState(row, col, isBlack);
                        Model.get().bState.captureCounters(row, col, isBlack);

                        Model.get().updateAllViews();
                    }
                });

                panel.add(arrayButt[i][j]);
            }
        }

        // code for greedy AI button
        JButton button = new JButton("Make greedy AI move");
        button.setFont(new Font("Ariel", Font.BOLD, 20));

        button.addActionListener((e) -> {
            if (isTurn) {
                GridButton highest = arrayButt[0][0];

                // algorithm will find the highest value on the board
                // and ignore subsequent spaces of equal value
                // hence the first occurrence of the highest value will always be played

                // todo : what fuck
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        int curr;
                        curr = Model.get().bState.countCapture(i, j, isBlack);
                        if (curr > highest.getState()) {
                            highest = arrayButt[i][j];
                        }
                    }
                }

                int row = highest.getRow();
                int col = highest.getCol();

                Model.get().bState.setState(row, col, isBlack);
                Model.get().bState.captureCounters(row, col, isBlack);

                Model.get().updateAllViews();
            }
        });

        if (isBlack) {
            screenTitle = new JLabel("Black player");
        } else {
            screenTitle = new JLabel("White player");
        }

        guiFrame.add(panel, BorderLayout.CENTER);
        guiFrame.add(button, BorderLayout.SOUTH);
        guiFrame.add(screenTitle, BorderLayout.NORTH);

        guiFrame.pack();
        guiFrame.setVisible(true);

        update();
    }

    /**
     * Update method. Called every time a piece is played, for both views.
     *
     */
    public void update() {
        String newTitle;
        if (isTurn) {
            isTurn = false;
            newTitle = screenTitle.getText().replace(" (your turn)", "");
            screenTitle.setText(newTitle);
        } else {
            isTurn = true;
            newTitle = screenTitle.getText().concat(" (your turn)");
            screenTitle.setText(newTitle);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // code for updating button array states depending on external state array
                // again the black player's board must be accessed in reverse
                if (isBlack) {
                    arrayButt[i][j].setState(Model.get().bState.getState(7 - i, 7 - j));
                } else {
                    arrayButt[i][j].setState(Model.get().bState.getState(i, j));
                }

                // code for enabling/disabling buttons
                // a button will be enabled if it is the current view's turn, and
                // the space will capture at least 1 opposing piece
                // if neither of these cases are fulfiled, the button is disabled
                // i.e. the space is not playable
                arrayButt[i][j].setEnabled(isTurn && Model.get().bState.countCapture(i, j, isBlack) > 0);
            }
        }

        guiFrame.repaint();

        // game will end if the entire board is full, or both players are in a stalemate
        if (Model.get().bState.isGameOver()) {
            String outDialog;
            int[] scores = Model.get().bState.getScore();
            if (scores[0] > scores[1]) {
                outDialog = "White wins: " + scores[0] + " : " + scores[1];
            } else {
                outDialog = "Black wins: " + scores[0] + " : " + scores[1];
            }
            JOptionPane.showMessageDialog(null, outDialog);
            System.exit(0);
        }
    }
}