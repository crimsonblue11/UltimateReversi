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
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/layout/startScreen.fxml"))));
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/layout/style.css")).toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(e.hashCode());
        }

        Stage mainStage = new Stage();
        mainStage.setScene(scene);
        mainStage.show();
//        new View(false).CreateGUI();
//        new View(true).CreateGUI();
    }
}
