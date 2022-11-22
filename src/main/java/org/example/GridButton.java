/**
 * Child class of JButton used as individual board squares.
 *
 * @Project Ultimate Reversi
 * @Author Medusa Dempsey
 * @Version 1.0
 * @since 1.0
 */
package org.example;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JButton;


public class GridButton extends JButton {
    // subclass of JButton to store row, column, and state
    // as well as to override paintComponent method
    /**
     * Integer representing state of the grid space.
     * 1 - black, 2 - white, 0 - empty.
     */
    private int state;
    /**
     * Member variable representing row of the space.
     */
    private final int row;
    /**
     * Member variable representing column of the space.
     */
    private final int col;

    // instantiate the button with row, col, state data
    // and with a black line border and a green background

    /**
     * Constructor.
     * Takes the column and row of the space, as well as its initial state.
     *
     * @param row     Row of the space.
     * @param col     Column of the space.
     * @param inState Initial state of the space
     * @since 1.0
     */
    public GridButton(int row, int col, int inState) {
        state = inState;
        this.row = row;
        this.col = col;
        this.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        this.setBackground(Color.green);
    }

    // paintcomponent override
    // called when a swing component is created, and on repaint

    /**
     * Override of paintComponent method.
     * Used to paint component with (or without) counter inside it.
     *
     * @param g Graphics object to draw the button to
     * @since 1.0
     */
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

    /**
     * Accessor method for space state.
     *
     * @return State of the space.
     * @since 1.0
     */
    public int getState() {
        return state;
    }

    /**
     * Accessor method for the row of a space.
     *
     * @return Row of the space.
     * @since 1.0
     */
    public int getRow() {
        return row;
    }

    // getter method for column

    /**
     * Accessor method for the column of a space.
     *
     * @return Column of the space.
     * @since 1.0
     */
    public int getCol() {
        return col;
    }

    // SETTERS

    // setter method for state

    /**
     * Mutator method for the state of the space.
     *
     * @param inState State to change the space to.
     * @since 1.0
     */
    public void setState(int inState) {
        state = inState;
    }

}