/**
 * Class to handle the state of the game board, independent of each view.
 *
 * @author Medusa Dempsey
 * @version 1.1
 */
package org.example.reversi;

public class BoardState {
    /**
     * Constant representing the number of grids per row or column.
     * Note this value is the same, as it is a square grid.
     * Set to {@value} by default.
     */
    static final int GRID_SIZE = 8;
    /**
     * 2D array representing each square of the board.
     */
    private final SpaceState[][] m_BoardArray = new SpaceState[GRID_SIZE][GRID_SIZE];
    /**
     * Integer storing player 1's score.
     */
    private int m_P1Score = 0;
    /**
     * Integer storing player 2's score.
     */
    private int m_P2Score = 0;

    /**
     * Accessor method for the state of a given space.
     * If the input value is invalid, -1 (invalid) will be returned.
     *
     * @param row Row of the queried space.
     * @param col Column of the queried space.
     * @return Value of the queried space.
     */
    public SpaceState getState(int row, int col) {
        if (row > 7 || col > 7 || row < 0 || col < 0) {
            return SpaceState.INVALID;
        }
        return m_BoardArray[row][col];
    }

    /**
     * Accessor method for Player 1's score.
     */
    public int getPlayer1Score() {
        return m_P1Score;
    }

    /**
     * Accessor method for Player 2's score.
     */
    public int getPlayer2Score() {
        return m_P2Score;
    }

    /**
     * Mutator method for individual board spaces.
     *
     * @param row       Row of the space to change.
     * @param col       Column of the space to change.
     * @param isPlayer1 Boolean representing the player of the space - true if player 1, false if player 2.
     */
    public void setState(int row, int col, boolean isPlayer1) {
        if (isPlayer1) {
            m_BoardArray[7 - row][7 - col] = SpaceState.PLAYER_1;
        } else {
            m_BoardArray[row][col] = SpaceState.PLAYER_2;
        }
    }

    /**
     * Constructor method.
     * Calls setBoard to set pieces to initial positions.
     *
     * @see #setBoard()
     */
    public BoardState() {
        setBoard();
    }

    /**
     * Method containing the logic for capturing counters.
     * Inputs represent the counter that has been placed, and the method will calculate
     * how many of the enemy's pieces will be captured, and then capture them.
     *
     * @param row       The row of the counter that has been placed.
     * @param col       The column of the counter that has been placed.
     * @param isPlayer1 Boolean representing the playing player - true if player 1, false if player 2.
     */
    public void captureCounters(int row, int col, boolean isPlayer1) {
        // access spaces in reverse if player 1, since upside-down
        if (isPlayer1) {
            row = 7 - row;
            col = 7 - col;
        }

        // loop through all adjacent spaces
        for (int r_offset = -1; r_offset <= 1; r_offset++) {
            for (int c_offset = -1; c_offset <= 1; c_offset++) {

                if (r_offset == 0 && c_offset == 0) {
                    // no offset - ignore
                    continue;
                }

                int r_pos = row + r_offset;
                int c_pos = col + c_offset;

                SpaceState currentSpace = getState(r_pos, c_pos);

                SpaceState CURRENT_COLOUR = m_BoardArray[row][col];
                SpaceState OPPOSITE_COLOUR = isPlayer1 ? SpaceState.PLAYER_2 : SpaceState.PLAYER_1;
                if (currentSpace != OPPOSITE_COLOUR) {
                    continue;
                }

                int count = 1;
                while (currentSpace == OPPOSITE_COLOUR) {
                    currentSpace = getState(r_pos += r_offset, c_pos += c_offset);
                    count++;
                }

                if (currentSpace == CURRENT_COLOUR) {
                    // loop through each space and convert to capturing colour
                    for (int i = 0; i < count; i++) {
                        int r_toChange = row + (i * r_offset);
                        int c_toChange = col + (i * c_offset);

                        m_BoardArray[r_toChange][c_toChange] = CURRENT_COLOUR;
                    }
                }
            }
        }
    }

    /**
     * Method to count how many pieces can be captured if a space is played.
     * Used by AI players to find the highest value space to play, and also
     * the system to keep a track of how many counters have been captured, which is used to calculate
     * who has one and is displayed after the game is over.
     *
     * @param row       Row of the space that is being calculated.
     * @param col       Column of the space that is being calculated.
     * @param isPlayer1 Boolean representing which side the potential play is coming from -
     *                  true if player 1, false if player 2.
     * @return Number of spaces that can be captured if the input move is played.
     */
    public int countCapture(int row, int col, boolean isPlayer1) {
        // player 1's inputs are reversed since the view is upside-down
        if (isPlayer1) {
            row = 7 - row;
            col = 7 - col;
        }

        SpaceState COUNTER_COLOUR = isPlayer1 ? SpaceState.PLAYER_1 : SpaceState.PLAYER_2;
        SpaceState OPPOSITE_COLOUR = isPlayer1 ? SpaceState.PLAYER_2 : SpaceState.PLAYER_1;

        // return zero for non-empty (unplayable) spaces
        if (getState(row, col) != SpaceState.EMPTY) {
            return 0;
        }

        // loop through possible offsets from the input space
        // i.e., check the 8 spaces around it
        int finalCount = 0; // variable to keep track of how many pieces will be captured if played
        for (int r_offset = -1; r_offset <= 1; r_offset++) {
            for (int c_offset = -1; c_offset <= 1; c_offset++) {
                int r_pos = row + r_offset;
                int c_pos = col + c_offset;


                // if | offset | = 0 or space is invalid, skip to next one
                if ((r_offset == 0 && c_offset == 0) || getState(r_pos, c_pos) == SpaceState.INVALID) {
                    continue;
                }
                // if out of bounds, skip over it

                SpaceState currentCounter = m_BoardArray[r_pos][c_pos];
                int numChecked = 0; // variable to keep track of how many have been checked

                // keep going in the same direction if the curr is of the opposite colour
                while (currentCounter == OPPOSITE_COLOUR) {
                    r_pos += r_offset;
                    c_pos += c_offset;
                    currentCounter = getState(r_pos, c_pos);
                    numChecked++;
                }

                // if same colour is found, this line has pieces to capture, so add to final count
                if (currentCounter == COUNTER_COLOUR) {
                    finalCount += numChecked;
                }
            }
        }

        return finalCount;
    }

    /**
     * Method for checking if it is still possible to make moves (i.e. if the game is over or not).
     * Loops through the entire play space for both views and counts the number of playable spaces.
     * If this is zero for both views, the game is over. If not, it may continue.
     *
     * @return True if the game is over, false if otherwise.
     */
    public boolean checkGameOver() {
        // local to the function, so they're reset if false is returned
        int p1Score = 0;
        int p2Score = 0;

        for (int r_pos = 0; r_pos < 8; r_pos++) {
            for (int c_pos = 0; c_pos < 8; c_pos++) {
                int playableSpaces = 0;

                playableSpaces += countCapture(r_pos, c_pos, false);
                playableSpaces += countCapture(r_pos, c_pos, true);

                // game cannot be over, so return false and ignore all other spaces
                if (playableSpaces != 0) {
                    return false;
                }

                if (m_BoardArray[r_pos][c_pos] == SpaceState.PLAYER_1) {
                    p1Score++;
                } else if (m_BoardArray[r_pos][c_pos] == SpaceState.PLAYER_2) {
                    p2Score++;
                }
            }
        }

        // no playable spaces, game must be over
        // set member variables
        m_P1Score = p1Score;
        m_P2Score = p2Score;
        return true;
    }

    /**
     * Method to set the game board with initial piece positions.
     * Called whenever the game is started.
     *
     * @since 1.1
     */
    public void setBoard() {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                m_BoardArray[r][c] = SpaceState.EMPTY;
            }
        }

        m_BoardArray[3][3] = SpaceState.PLAYER_2;
        m_BoardArray[4][4] = SpaceState.PLAYER_2;

        m_BoardArray[3][4] = SpaceState.PLAYER_1;
        m_BoardArray[4][3] = SpaceState.PLAYER_1;
    }
}
