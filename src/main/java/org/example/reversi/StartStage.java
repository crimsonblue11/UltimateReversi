/**
 * Stage singleton containing the start scene as well as some logic.
 */

package org.example.reversi;

import javafx.stage.Stage;

public class StartStage extends Stage {
    /**
     * Static instance.
     */
    private final static StartStage m_Self = new StartStage();

    /**
     * Accessor method for {@code m_Self}.
     *
     * @return Static instance.
     */
    public static StartStage getInstance() {
        return m_Self;
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
