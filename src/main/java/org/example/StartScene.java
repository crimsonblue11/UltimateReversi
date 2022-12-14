package org.example;

import javafx.scene.Scene;

public class StartScene extends Scene {
    public StartScene() {
        super(ResourceUtil.LoadFXML("startScreen.fxml"), View.WIN_WIDTH, View.WIN_HEIGHT);

        getStylesheets().add(ResourceUtil.LoadCSS("style.css"));
    }
}
