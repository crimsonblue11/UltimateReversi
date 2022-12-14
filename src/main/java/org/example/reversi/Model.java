/**
 * Class for the many variables relating to the board.
 * Also, this class is a singleton.
 *
 * @author Medusa Dempsey
 * @version 1.1
 */
package org.example.reversi;

import javafx.scene.control.Alert;

import java.util.ArrayList;

public class Model {
    /**
     * ArrayList of all views to update.
     */
    private final ArrayList<PlayerView> m_ViewArray = new ArrayList<>();
    /**
     * Static instance.
     */
    private static final Model m_Self = new Model();
    /**
     * BoardState object.
     */
    private final BoardState m_BoardState = new BoardState();

    /**
     * Accessor method for {@code m_BoardState}.
     *
     * @return BoardState object.
     */
    public BoardState getBoardState() {
        return m_BoardState;
    }

    /**
     * Accessor method for static instance.
     *
     * @return Static instance.
     */
    public static Model getSelf() {
        return m_Self;
    }

    /**
     * Private constructor. Prevents external methods instantiating this singleton.
     */
    private Model() {
    }

    /**
     * Method for adding a view to the list of views to update.
     *
     * @param view View to add to the list of views.
     */
    public void storeView(PlayerView view) {
        m_ViewArray.add(view);
    }

    /**
     * Method to update all views in the list of views.
     * Also checks if the game is over via {@code BoardState.CheckGameOver}.
     * This is done here to ensure all views are updated accurately before the game ends.
     *
     * @see BoardState#checkGameOver()
     */
    public void updateViews() {
        for (PlayerView v : m_ViewArray) {
            v.update();
        }

        if (m_BoardState.checkGameOver()) {
            endGame();
        }
    }

    /**
     * Method to handle the game ending.
     * Gets both player scores, calculates who won,
     * displays this information in an alert window,
     * closes game stages, and re-loads start stage.
     *
     * @since 1.1
     */
    private void endGame() {
        int p2Score = m_BoardState.getPlayer2Score();
        int p1Score = m_BoardState.getPlayer1Score();

        String winner_text = (p2Score > p1Score) ? "Player 2" : "Player 1";

        String outDialog = winner_text + " wins: " + p2Score + " : " + p1Score;

        Alert winner_alert = new Alert(Alert.AlertType.INFORMATION, outDialog);
        winner_alert.showAndWait();
        StartStage.getInstance().show();

        for (PlayerView v : m_ViewArray) {
            v.close();
        }

        m_BoardState.setBoard();
    }
}
