/**
 * Enum representing the state of a GridButton.
 *
 * @see org.example.reversi.GridButton
 */

package org.example.reversi;

public enum SpaceState {
    /**
     * This state represents player 1's counter in a space.
     */
    PLAYER_1,
    /**
     * This state represents player 2's counter in a space.
     */
    PLAYER_2,
    /**
     * This state represents no counter in a space.
     */
    EMPTY,
    /**
     * This state is returned only when the accessed space is not inside the array of spaces.
     */
    INVALID,
}
