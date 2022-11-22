package org.example;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JButton;


public class GridButton extends JButton {
    // subclass of JButton to store row, column, and state
    // as well as to override paintComponent method
    private int state;
    private int row;
    private int col;

    // instantiate the button with row, col, state data
    // and with a black line border and a green background
    public GridButton(int row, int col, int inState) {
        state = inState;
        this.row = row;
        this.col = col;
        this.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        this.setBackground(Color.green);
    }

    // paintcomponent override
    // called when a swing component is created, and on repaint
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // paint the jbutton
        if (state == 2) { // if white, paint a white circle with a black border (oval)
            g.setColor(Color.white);
            g.fillOval(0, 0, getSize().width, getSize().height);
            g.setColor(Color.black);
            g.drawOval(0, 0, getSize().width, getSize().height);
        } else if (state == 1) { // if black, paint a black circle with a white border (oval)
            g.setColor(Color.black);
            g.fillOval(0, 0, getSize().width, getSize().height);
            g.setColor(Color.white);
            g.drawOval(0, 0, getSize().width, getSize().height);
        }
        // if neither, the square is empty and nothing else needs to be done

    }

    // GETTERS

    // getter method for state
    public int getState() {
        return state;
    }

    // getter method for row
    public int getRow() {
        return row;
    }

    // getter method for column
    public int getCol() {
        return col;
    }

    // SETTERS

    // setter method for state
    public void setState(int inState) {
        state = inState;
    }

}