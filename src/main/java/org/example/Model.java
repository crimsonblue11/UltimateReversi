/**
 * Class for the many variables relating to the board.
 * Also, this class is a singleton.
 *
 * @author Medusa Dempsey
 * @version 1.0
 */
package org.example;

import javax.swing.*;
import java.util.ArrayList;

public class Model {
    /**
     * ArrayList of all views to update.
     */
    private final ArrayList<View> m_ViewArray = new ArrayList<>();
    /**
     * Static instance.
     */
    private static final Model m_Self = new Model();
    /**
     * BoardState object.
     */
    private final BoardState m_BoardState = new BoardState();

    public BoardState GetBoardState() {
        return m_BoardState;
    }

    /**
     * Accessor method for static instance.
     *
     * @return Static instance.
     */
    public static Model GetSelf() {
        return m_Self;
    }

    /**
     * Private constructor. Prevents external methods instantiating this class.
     */
    private Model() {
    }

    /**
     * Method for adding a view to the list of views to update.
     *
     * @param view View to add to the list of views.
     */
    public void StoreView(View view) {
        m_ViewArray.add(view);
    }

    /**
     * Method to update all views in the list of views.
     * Also checks if the game is over via {@code BoardState.CheckGameOver}.
     * This is done here to ensure all views are updated accurately before the game ends.
     *
     * @see BoardState#CheckGameOver()
     */
    public void UpdateViews() {
        for (View listOfView : m_ViewArray) {
            listOfView.Update();
        }

        if (m_BoardState.CheckGameOver()) {
            int whiteScore = m_BoardState.GetWhiteScore();
            int blackScore = m_BoardState.GetBlackScore();

            String winner_colour = (whiteScore > blackScore) ? "White" : "Black";

            String outDialog = winner_colour + " wins: " + whiteScore + " : " + blackScore;

            JOptionPane.showMessageDialog(null, outDialog);
            System.exit(0);
        }
    }
}
