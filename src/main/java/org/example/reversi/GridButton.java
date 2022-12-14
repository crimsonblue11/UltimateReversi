/**
 * Child class of JButton used as individual board squares.
 *
 * @author Medusa Dempsey
 * @version 1.1
 */
package org.example.reversi;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GridButton extends Canvas {
    /**
     * Constant representing size of an individual grid space.
     * Note that this is height and width, since the spaces are square.
     * The value of this is {@value}.
     */
    final static int SIZE = 75;
    /**
     * GraphicsContext object.
     * Stored as constant for cleaner accessing.
     */
    private final GraphicsContext GR = getGraphicsContext2D();
    /**
     * Enumerable representing state of the grid space.
     * No value is set by default, since this is set in the constructor.
     *
     * @see SpaceState
     */
    private SpaceState m_State;
    /**
     * Member variable representing grid row of the space.
     */
    private final int m_Row;
    /**
     * Member variable representing grid column of the space.
     */
    private final int m_Col;

    /**
     * Accessor method for the row of a space.
     *
     * @return Value of {@code m_Row}.
     */
    public int getRow() {
        return m_Row;
    }

    /**
     * Accessor method for the column of a space.
     *
     * @return Value of {@code m_Col}.
     */
    public int getCol() {
        return m_Col;
    }

    /**
     * Mutator method for the state of the space.
     *
     * @param inState Value to change {@code m_State} to.
     */
    public void setState(SpaceState inState) {
        m_State = inState;
    }

    /**
     * Constructor method.
     * Sets initial values, as well as drawing in the grid's background.
     *
     * @param row     Row of the space.
     * @param col     Column of the space.
     * @param inState Initial state of the space.
     */
    public GridButton(int row, int col, SpaceState inState) {
        super(SIZE, SIZE);

        m_State = inState;
        m_Row = row;
        m_Col = col;

        drawSpace();
    }

    /**
     * Method to draw background of the space.
     * In other words, this method draws a green square over the entire canvas,
     * and then a black border of width 2 around it.
     *
     * @since 1.1
     */
    private void drawSpace() {
        Color backgroundColor = Color.GREEN;

        GR.setFill(backgroundColor);
        GR.fillRect(0, 0, SIZE, SIZE);

        Color borderColour = Color.BLACK;
        int borderThickness = 2;

        GR.setFill(borderColour);
        GR.setLineWidth(borderThickness);
        GR.strokeRect(0, 0, SIZE, SIZE);
        GR.setLineWidth(1);
    }

    /**
     * Update method.
     * This repaints the space's background over what was previously painted, so that if the state has changed
     * there is no need to worry about it.
     * Then, it checks if a counter needs to be painted at all - if yes, then it will be painted with the
     * appropriate colour. Otherwise, nothing else happens.
     */
    public void update() {
        GR.setFill(Color.GREEN);
        GR.fillRect(0, 0, SIZE, SIZE);
        drawSpace();

        if (m_State == SpaceState.PLAYER_2) {
            GR.setFill(StartStage.getP2Color());
            GR.fillOval(0, 0, getWidth(), getHeight());
        } else if (m_State == SpaceState.PLAYER_1) {
            GR.setFill(StartStage.getP1Color());
            GR.fillOval(0, 0, getWidth(), getHeight());
        }
    }
}
