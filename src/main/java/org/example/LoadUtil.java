package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class LoadUtil {
    public static Parent getFXML(String filename) {
        final String path = "/layout/";

        try {
            return FXMLLoader.load(Objects.requireNonNull(StartScene.class.getResource(path + filename)));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(e.hashCode());
            return null;
        }
    }
}
