/**
 * Main class.
 *
 * @author Medusa Dempsey
 * @version 1.1
 */

package org.example.reversi;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    /**
     * Main method.
     * Launches JavaFX application with the supplied arguments.
     *
     * @param args Program arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * JavaFX start method.
     * Functions as this application's main method.
     * Calls StartStage start method to start the application.
     *
     * @param stage Default stage object. Unused in this application.
     * @since 1.1
     */
    @Override
    public void start(Stage stage) {
        StartStage.getInstance().start();
    }
}
