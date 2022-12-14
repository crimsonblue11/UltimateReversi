/**
 * Class to handle the state of the game board, independent of each view.
 *
 * @author Medusa Dempsey
 * @version 1.0
 */
package org.example;

public class BoardState {
    static final int GRID_SIZE = 8;
    /**
     * 2D array representing each square of the board.
     */
    private final SpaceState[][] m_BoardArray = new SpaceState[GRID_SIZE][GRID_SIZE];
    /**
     * Keeps track of black player's score.
     */
    private int m_BlackStore = 0;
    /**
     * Keeps track of white player's score.
     */
    private int m_WhiteScore = 0;

    /**
     * Accessor method for the state of a given space.
     * If the input value is invalid, -1 (invalid) will be returned.
     *
     * @param row Row of the queried space.
     * @param col Column of the queried space.
     * @return Value of the queried space.
     */
    public SpaceState GetState(int row, int col) {
        if (row > 7 || col > 7 || row < 0 || col < 0) {
            return SpaceState.INVALID;
        }
        return m_BoardArray[row][col];
    }

    public int GetBlackScore() {
        return m_BlackStore;
    }

    public int GetWhiteScore() {
        return m_WhiteScore;
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
            m_BoardArray[7 - row][7 - col] = SpaceState.BLACK;
        } else {
            m_BoardArray[row][col] = SpaceState.WHITE;
        }
    }

    /**
     * Constructor method to place initial counters on the board.
     */
    public BoardState() {
        setBoard();
    }

    /**
     * Method containing the logic for capturing counters.
     * Inputs represent the counter that has been placed, and the method will calculate
     * how many of the enemy's pieces will be captured, and then capture them.
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

        // loop through all adjacent spaces
        for (int r_offset = -1; r_offset <= 1; r_offset++) {
            for (int c_offset = -1; c_offset <= 1; c_offset++) {

                if (r_offset == 0 && c_offset == 0) {
                    // no offset - ignore
                    continue;
                }

                int r_pos = row + r_offset;
                int c_pos = col + c_offset;

                SpaceState currentSpace = GetState(r_pos, c_pos);

                SpaceState CURRENT_COLOUR = m_BoardArray[row][col];
                SpaceState OPPOSITE_COLOUR = isBlack ? SpaceState.WHITE : SpaceState.BLACK;
                if (currentSpace != OPPOSITE_COLOUR) {
                    continue;
                }

                int count = 1;
                while (currentSpace == OPPOSITE_COLOUR) {
                    currentSpace = GetState(r_pos += r_offset, c_pos += c_offset);
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
     * @param row     Row of the space that is being calculated.
     * @param col     Column of the space that is being calculated.
     * @param isBlack Boolean representing which side the potential play is coming from - true if black, false if white.
     * @return Number of spaces that can be captured if the input move is played.
     */
    public int CountCapture(int row, int col, boolean isBlack) {
        // black inputs are reversed since the view is upside-down
        if (isBlack) {
            row = 7 - row;
            col = 7 - col;
        }

        SpaceState COUNTER_COLOUR = isBlack ? SpaceState.BLACK : SpaceState.WHITE;
        SpaceState OPPOSITE_COLOUR = isBlack ? SpaceState.WHITE : SpaceState.BLACK;

        // return zero for non-empty (unplayable) spaces
        if (GetState(row, col) != SpaceState.EMPTY) {
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
                if ((r_offset == 0 && c_offset == 0) || GetState(r_pos, c_pos) == SpaceState.INVALID) {
                    continue;
                }
                // if out of bounds, skip over it

                SpaceState currentCounter = m_BoardArray[r_pos][c_pos];
                int numChecked = 0; // variable to keep track of how many have been checked

                // keep going in the same direction if the curr is of the opposite colour
                while (currentCounter == OPPOSITE_COLOUR) {
                    r_pos += r_offset;
                    c_pos += c_offset;
                    currentCounter = GetState(r_pos, c_pos);
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
    public boolean CheckGameOver() {
        // local to the function, so they're reset if false is returned
        int blackScore = 0;
        int whiteScore = 0;

        for (int r_pos = 0; r_pos < 8; r_pos++) {
            for (int c_pos = 0; c_pos < 8; c_pos++) {
                int playableSpaces = 0;

                playableSpaces += CountCapture(r_pos, c_pos, false);
                playableSpaces += CountCapture(r_pos, c_pos, true);

                // game cannot be over, so return false and ignore all other spaces
                if (playableSpaces != 0) {
                    return false;
                }

                if (m_BoardArray[r_pos][c_pos] == SpaceState.BLACK) {
                    blackScore++;
                } else if (m_BoardArray[r_pos][c_pos] == SpaceState.WHITE) {
                    whiteScore++;
                }
            }
        }

        // no playable spaces, game must be over
        // set member variables
        m_BlackStore = blackScore;
        m_WhiteScore = whiteScore;
        return true;
    }

    public void setBoard() {
        for(int r = 0; r < GRID_SIZE; r++) {
            for(int c = 0; c < GRID_SIZE; c++) {
                m_BoardArray[r][c] = SpaceState.EMPTY;
            }
        }

        m_BoardArray[3][3] = SpaceState.WHITE;
        m_BoardArray[3][4] = SpaceState.BLACK;
        m_BoardArray[4][3] = SpaceState.BLACK;
        m_BoardArray[4][4] = SpaceState.WHITE;
    }
}
