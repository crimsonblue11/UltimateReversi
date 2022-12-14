/**
 * Main class.
 *
 * @author Medusa Dempsey
 * @version 1.0
 */

package org.example;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    /**
     * Main method.
     * Instantiates two views, one black and one white.
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        new View(false).CreateGUI();
        new View(true).CreateGUI();
    }
}
