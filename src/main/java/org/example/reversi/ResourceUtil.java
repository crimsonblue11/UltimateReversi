/**
 * Class for statically loading files from resources directory.
 *
 * @author Medusa Dempsey
 * @version 1.0
 */

package org.example.reversi;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class ResourceUtil {
    /**
     * Method for loading FXML files as parent layout objects.
     *
     * @param filename Name of the FXML file to load - note this is NOT the path, since all FXML files should be stored
     *                 in /resources/layout
     * @return Parent object with a layout defined in the given file
     */
    public static Parent loadFXML(String filename) {
        final String path = "/layout/";

        try {
            return FXMLLoader.load(Objects.requireNonNull(ResourceUtil.class.getResource(path + filename)));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(e.hashCode());
            return null;
        }
    }

    /**
     * Method for loading CSS files.
     *
     * @param filename Name of the CSS file to load - note this is NOT the path, since all CSS files should be stored
     *                 in /resources/css
     * @return URL to the given CSS file
     */
    public static String loadCSS(String filename) {
        final String path = "/css/";

        return Objects.requireNonNull(ResourceUtil.class.getResource(path + filename)).toExternalForm();
    }
}
