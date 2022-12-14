package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public class StartScene extends Scene {
    public StartScene() {
        super(LoadUtil.getFXML("startScreen.fxml"), View.WIN_WIDTH, View.WIN_HEIGHT);

        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/layout/style.css")).toExternalForm());
    }
}
