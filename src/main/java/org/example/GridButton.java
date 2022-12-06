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
    private SpaceState m_State;
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
    public SpaceState GetState() {
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
    public void SetState(SpaceState inState) {
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
    public GridButton(int row, int col, SpaceState inState) {
        m_State = inState;
        m_Row = row;
        m_Col = col;

        Color backgroundColor = Color.green;
        setBackground(backgroundColor);

        Color borderColour = Color.black;
        int borderThickness = 2;
        setBorder(BorderFactory.createLineBorder(borderColour, borderThickness));
    }

    /**
     * Override of paintComponent method.
     * Used to paint component with counter inside it, if needed. If the space is empty,
     * then do nothing.
     *
     * @param g Graphics object to draw the button to.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (m_State == SpaceState.WHITE) {
            // if white, paint a white circle with a black border (oval)
            g.setColor(Color.white);
            g.fillOval(0, 0, getSize().width, getSize().height);
            g.setColor(Color.black);
            g.drawOval(0, 0, getSize().width, getSize().height);
        } else if (m_State == SpaceState.BLACK) {
            // if black, paint a black circle with a white border (oval)
            g.setColor(Color.black);
            g.fillOval(0, 0, getSize().width, getSize().height);
            g.setColor(Color.white);
            g.drawOval(0, 0, getSize().width, getSize().height);
        }
        // if neither, the square is empty and nothing else needs to be done
    }
}