/**
 * Child class of JButton used as individual board squares.
 *
 * @author Medusa Dempsey
 * @version 1.0
 */
package org.example;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JButton;


public class GridButton extends JButton {
    /**
     * Integer representing state of the grid space.
     * 1 - black, 2 - white, 0 - empty.
     */
    private int m_State;
    /**
     * Member variable representing row of the space.
     */
    private final int m_Row;
    /**
     * Member variable representing column of the space.
     */
    private final int m_Col;

    /**
     * Accessor method for space state.
     *
     * @return State of the space.
     */
    public int GetState() {
        return m_State;
    }

    /**
     * Accessor method for the row of a space.
     *
     * @return Row of the space.
     */
    public int GetRow() {
        return m_Row;
    }

    /**
     * Accessor method for the column of a space.
     *
     * @return Column of the space.
     */
    public int GetCol() {
        return m_Col;
    }

    /**
     * Mutator method for the state of the space.
     *
     * @param inState State to change the space to.
     */
    public void SetState(int inState) {
        m_State = inState;
    }

    /**
     * Constructor.
     * Takes the column and row of the space, as well as its initial state.
     *
     * @param row     Row of the space.
     * @param col     Column of the space.
     * @param inState Initial state of the space
     */
    public GridButton(int row, int col, int inState) {
        m_State = inState;
        m_Row = row;
        m_Col = col;
        this.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        this.setBackground(Color.green);
    }

    /**
     * Override of paintComponent method.
     * Used to paint component with (or without) counter inside it.
     *
     * @param g Graphics object to draw the button to
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (m_State == 2) {
            // if white, paint a white circle with a black border (oval)
            g.setColor(Color.white);
            g.fillOval(0, 0, getSize().width, getSize().height);
            g.setColor(Color.black);
            g.drawOval(0, 0, getSize().width, getSize().height);
        } else if (m_State == 1) {
            // if black, paint a black circle with a white border (oval)
            g.setColor(Color.black);
            g.fillOval(0, 0, getSize().width, getSize().height);
            g.setColor(Color.white);
            g.drawOval(0, 0, getSize().width, getSize().height);
        }
        // if neither, the square is empty and nothing else needs to be done
    }
}