/**
 * Main class.
 *
 * @author Medusa Dempsey
 * @version 1.0
 */

package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

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
        stage.setScene(new StartScene());
        stage.show();
//        new View(false).CreateGUI();
//        new View(true).CreateGUI();
    }
}
