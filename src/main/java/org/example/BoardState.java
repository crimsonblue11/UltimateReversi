/**
 * Class to handle the state of the game board.
 *
 * @author Medusa Dempsey
 * @version 1.0
 */
package org.example;

public class BoardState {
    /**
     * 2D array representing each square of the board.
     * This is an array of fixed size, so that the board size doesn't change at runtime.
     */
    private final int[][] m_BoardArray = new int[8][8];
    /**
     * Integer to keep track of black player's score.
     */
    private int m_BlackStore = 0;
    /**
     * Integer to keep track of white player's score.
     */
    private int m_WhiteScore = 0;

    /**
     * Accessor method for the state of a given space.
     *
     * @param row Row of the queried space.
     * @param col Column of the queried space.
     * @return Value of the queried space.
     */
    public int GetState(int row, int col) {
        if (row > 7 || col > 7 || row < 0 || col < 0) {
            return -1;
        }
        return m_BoardArray[row][col];
    }

    /**
     * Accessor method for scores.
     *
     * @return An array of integers containing white score and black score.
     */
    public int[] GetScore() {
        return new int[]{m_WhiteScore, m_BlackStore};
    }

    /**
     * Mutator method for individual board spaces.
     *
     * @param row     Row of the space to change.
     * @param col     Column of the space to change.
     * @param isBlack Boolean representing the colour to change the space to - true if black, false if white.
     */
    public void SetState(int row, int col, boolean isBlack) {
        if (isBlack) {
            m_BoardArray[7 - row][7 - col] = 1;
        } else {
            m_BoardArray[row][col] = 2;
        }

    }

    /**
     * Constructor method to place initial counters on the board.
     */
    public BoardState() {
        // hardcode the counters that start on the board
        m_BoardArray[3][3] = 2;
        m_BoardArray[3][4] = 1;
        m_BoardArray[4][3] = 1;
        m_BoardArray[4][4] = 2;
    }

    /**
     * Method containing the logic for capturing a counter.
     * Inputs represent the counter that has been placed, and the method will calculate
     * how many of the enemy's pieces will be captured.
     *
     * @param row     The row of the counter that has been placed.
     * @param col     The column of the counter that has been placed.
     * @param isBlack Boolean representing the colour of the placed piece - true if black, false if white.
     */
    public void CaptureCounters(int row, int col, boolean isBlack) {
        // access spaces in reverse if black view, since upside-down
        if (isBlack) {
            row = 7 - row;
            col = 7 - col;
        }

        // loops through all possible offsets from input space
        // i.e. the 8 adjacent spaces
        for (int rowoffset = -1; rowoffset <= 1; rowoffset++) {
            for (int coloffset = -1; coloffset <= 1; coloffset++) {
                if (rowoffset == 0 && coloffset == 0) {
                    continue;
                }

                int count = 1;
                int rowpos = row + rowoffset;
                int colpos = col + coloffset;

                int curr = GetState(rowpos, colpos);
                if (curr == -1 || curr == 0 || curr == m_BoardArray[row][col]) {
                    // invalid, empty, or the same colour
                    continue;
                }

                while (curr != 0 && curr != m_BoardArray[row][col]) {
                    curr = GetState(rowpos += rowoffset, colpos += coloffset);

                    if (curr == -1) {
                        break;
                    }

                    count++;
                }

                if (curr == m_BoardArray[row][col]) {
                    // same colour found, capture everything inbetween
                    for (int i = 0; i < count; i++) {
                        m_BoardArray[row + (i * rowoffset)][col + (i * coloffset)] = m_BoardArray[row][col];
                    }
                }

            }
        }
    }

    /**
     * Method to count how many pieces can be captured if a space is played.
     *
     * @param row     Row of the space that is being calculated.
     * @param col     Column of the space that is being calculated.
     * @param isBlack Boolean representing which side the potential play is coming from - true if black, false if white.
     * @return Number of spaces that can be captured if the input move is played.
     * @since 1.0
     */
    public int CountCapture(int row, int col, final boolean isBlack) {
        // this avoids having to put differing inputs in for different views
        // black view's inputs are reversed since the view is upside-down
        int counter;
        if (isBlack) {
            row = 7 - row;
            col = 7 - col;
            counter = 1;
        } else {
            counter = 2;
        }

        // return null for spaces that aren't empty or out of bounds
        if (GetState(row, col) > 0 || GetState(row, col) == -1) {
            return 0;
        }

        // this block loops through the possible offsets from the input space
        // i.e., check the 8 spaces around it
        int finalCount = 0; // variable to keep track of how many pieces will be captured if played
        for (int rowoffset = -1; rowoffset <= 1; rowoffset++) {
            for (int coloffset = -1; coloffset <= 1; coloffset++) {
                // double zero offset = no offset, hence ignore
                if (rowoffset == 0 && coloffset == 0) {
                    continue;
                }

                int rowpos = row + rowoffset;
                int colpos = col + coloffset;

                // if out of bounds, skip over it
                if (GetState(rowpos, colpos) == -1) {
                    continue;
                }

                int curr = m_BoardArray[rowpos][colpos]; // variable to keep track of the counter that's being examined
                int count = 0; // variable to keep track of how many have been checked

                // if the current counter is empty or the same colour, skip to the next
                // cases for empty adjacent space and adjacent space of the same colour
                if (curr <= 0 || curr == counter) {
                    continue;
                }

                // keep going in the same direction if the curr is of the opposite colour
                // i.e. not empty, not the same colour
                while (curr > 0 && curr != counter) {
                    curr = GetState(rowpos += rowoffset, colpos += coloffset);
                    if (curr == -1) {
                        break;
                    }
                    count++;
                }

                // if the new curr is of the same colour, add the current count to the final
                // count
                if (curr == counter) {
                    finalCount += count;
                }
            }
        }

        return finalCount;
    }

    /**
     * Method for checking if it is still possible to make moves (i.e. if the game is over or not).
     *
     * @return True if the game is over, false if not.
     * @since 1.0
     */
    public boolean CheckGameOver() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int curr = 0;

                // need to check both views for playable spaces
                curr += CountCapture(i, j, false);
                curr += CountCapture(i, j, true);

                // only makes sense to count pieces when the game is over, so do that here too
                if (m_BoardArray[i][j] == 1) {
                    m_BlackStore++;
                } else if (m_BoardArray[i][j] == 2) {
                    m_WhiteScore++;
                }

                // if either of the current spaces are playable, the game is not over
                // return false since there is no need to check the others
                if (curr != 0) {
                    // game is not over - scores are reset, so they don't carry over
                    m_WhiteScore = 0;
                    m_BlackStore = 0;

                    return false;
                }
            }
        }

        // at this point all places will have been checked
        // none are playable, so the game must be over - return true
        return true;
    }
}
