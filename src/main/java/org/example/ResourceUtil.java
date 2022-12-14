package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class ResourceUtil {
    public static Parent LoadFXML(String filename) {
        final String path = "/layout/";

        try {
            return FXMLLoader.load(Objects.requireNonNull(ResourceUtil.class.getResource(path + filename)));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(e.hashCode());
            return null;
        }
    }
    
    public static String LoadCSS(String filename) {
        final String path = "/css/";
        
        return Objects.requireNonNull(ResourceUtil.class.getResource(path + filename)).toExternalForm();
    }
}
