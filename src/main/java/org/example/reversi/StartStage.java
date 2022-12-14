/**
 * Stage singleton containing the start scene as well as some logic.
 *
 * @author Medusa Dempsey
 * @version 1.1
 */

package org.example.reversi;

import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class StartStage extends Stage {
    /**
     * Player 1 counter colour.
     */
    private static Color m_P1Color;
    /**
     * Player 2 counter colour.
     */
    private static Color m_P2Color;
    /**
     * Static instance.
     */
    private final static StartStage m_Self = new StartStage();

    /**
     * Accessor method for {@code m_P1Color}.
     *
     * @return Player 1 counter colour.
     */
    public static Color getP1Color() {
        return m_P1Color;
    }

    /**
     * Accessor methd for {@code m_P2Color}.
     *
     * @return Player 2 counter colour.
     */
    public static Color getP2Color() {
        return m_P2Color;
    }

    /**
     * Accessor method for {@code m_Self}.
     *
     * @return Static instance.
     */
    public static StartStage getInstance() {
        return m_Self;
    }

    /**
     * Method to set player colours at the start of a game.
     *
     * @param p1 Player 1 colour.
     * @param p2 Player 2 colour.
     */
    public static void setPlayerColours(Color p1, Color p2) {
        m_P1Color = p1;
        m_P2Color = p2;
    }

    /**
     * Private constructor.
     * This just calls the parent constructor, and is only defined here to enforce the
     * singleton pattern.
     */
    private StartStage() {
        super();
    }

    /**
     * Method to start the application.
     * Loads in a StartScene and shows the stage.
     *
     * @see StartScene
     */
    public void start() {
        setScene(new StartScene());
        show();
    }

    /**
     * Method to load the main game.
     * Makes two new PlayerViews, and hides this stage.
     *
     * @see PlayerView
     */
    public void loadGame() {
        new PlayerView(true).createGUI();
        new PlayerView(false).createGUI();

        this.hide();
    }
}
