/**
 * Child class of JButton used as individual board squares.
 *
 * @author Medusa Dempsey
 * @version 1.0
 */
package org.example;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GridButton extends Canvas {
    final static int SIZE = 75;
    private final GraphicsContext GR = getGraphicsContext2D();
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
        super(SIZE, SIZE);

        m_State = inState;
        m_Row = row;
        m_Col = col;

        Color backgroundColor = Color.GREEN;
        GR.setFill(backgroundColor);
        GR.fillRect(0, 0, SIZE, SIZE);

        drawBorder();
    }

    private void drawBorder() {
        Color borderColour = Color.BLACK;
        int borderThickness = 2;
        GR.setFill(borderColour);
        GR.setLineWidth(borderThickness);
        GR.strokeRect(0, 0, SIZE, SIZE);
        GR.setLineWidth(1);
    }

    /**
     * Override of paintComponent method.
     * Used to paint component with counter inside it, if needed. If the space is empty,
     * then do nothing.
     */
    public void Update() {
        GR.setFill(Color.GREEN);
        GR.fillRect(0, 0, SIZE, SIZE);
        drawBorder();

        if (m_State == SpaceState.WHITE) {
            // if white, paint a white circle with a black border (oval)
            GR.setFill(Color.WHITE);
            GR.fillOval(0, 0, getWidth(), getHeight());
            GR.setFill(Color.BLACK);
            GR.strokeOval(0, 0, getWidth(), getHeight());
        } else if (m_State == SpaceState.BLACK) {
            // if black, paint a black circle with a white border (oval)
            GR.setFill(Color.BLACK);
            GR.fillOval(0, 0, getWidth(), getHeight());
            GR.setFill(Color.WHITE);
            GR.strokeOval(0, 0, getWidth(), getHeight());
        }
        // if neither, the square is empty and nothing else needs to be done
    }
}