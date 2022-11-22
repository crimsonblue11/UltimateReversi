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

    private GridButton[][] arrayButt = new GridButton[8][8];
    private JFrame guiFrame = new JFrame();
    private boolean isBlack;
    private boolean isTurn;
    private JLabel screenTitle;

    public StringBuffer title;

    // constructor takes parent model and boolean for if the view is reversed or not
    public View(boolean isBlack) {
        Model.get().storeView(this);
        this.isBlack = isBlack;

        // when update is called, the current turn will switch
        // the requirements state white must go first, hence when initialised the roles
        // must be switched
        guiFrame.setTitle("Reversi");
        if (isBlack) {
            isTurn = true;
        } else {
            isTurn = false;
        }
    }

    // method for creating the GUI
    public void createGUI() {
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setLayout(new BorderLayout());
        guiFrame.setPreferredSize(new Dimension(600, 700)); // this is a good size

        // panel for the 8x8 grid of spaces
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 8));

        // loop through each arrayButt index and instantiate them
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

                        // no need to handle different inputs since the method does that for us
                        Model.get().bState.setState(row, col, isBlack);
                        Model.get().bState.captureCounters(row, col, isBlack);

                        // update both views so they're kept consistent
                        Model.get().updateAllViews();
                    }
                });

                panel.add(arrayButt[i][j]);
            }
        }

        // code for greedy ai button
        // no need to subclass jbutton, this is a normal button
        JButton button = new JButton("Make greedy AI move");
        button.setFont(new Font("Ariel", Font.BOLD, 20));

        // implementing functionality with lambda expression
        button.addActionListener((e) -> {
            if (isTurn) {
                GridButton highest = arrayButt[0][0];

                // algorithm will find the highest value on the board
                // and ignore subsequent spaces of equal value
                // hence the first occurence of the highest value will always be played
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        int curr;
                        curr = Model.get().bState.countCapture(i, j, isBlack);
                        if (curr > highest.getState()) {
                            highest = arrayButt[i][j];
                        }
                    }
                }

                // playing the piece, this is almost identical to the code
                // for manually playing a piece
                int row = highest.getRow();
                int col = highest.getCol();

                // no need to differentiate parameters since methods handle that
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

        // add everything to the jframe
        guiFrame.add(panel, BorderLayout.CENTER);
        guiFrame.add(button, BorderLayout.SOUTH);
        guiFrame.add(screenTitle, BorderLayout.NORTH);

        guiFrame.pack();
        guiFrame.setVisible(true);

        // initial update to make sure everything is enabled correctly
        update();

    }

    // called every time a piece is played, for both views
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

        // loop through the entire button array
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
                if (isTurn && Model.get().bState.countCapture(i, j, isBlack) > 0) {
                    arrayButt[i][j].setEnabled(true);
                } else {
                    // if neither of these cases are fulfiled, the button is disabled
                    // i.e. the space is not playable
                    arrayButt[i][j].setEnabled(false);
                }
            }
        }

        // repaint the guiframe to reflect the changes
        guiFrame.repaint();

        // game will end if the entire board is full, or both players are in a stalemate
        // i.e. neither player can place a single piece
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