/**
 * Enum representing the state of a GridButton.
 *
 * @see org.example.reversi.GridButton
 */

package org.example.reversi;

public enum SpaceState {
    /**
     * This state represents a black counter in a space.
     */
    BLACK,
    /**
     * This state represents a white counter in a space.
     */
    WHITE,
    /**
     * This state represents no counter in a space.
     */
    EMPTY,
    /**
     * This state is returned only when the accessed space is not inside the array of spaces.
     */
    INVALID,
}
