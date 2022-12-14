/**
 * Scene containing start menu.
 *
 * @author Medusa Dempsey
 * @verison 1.0
 */

package org.example.reversi;

import javafx.scene.Scene;

public class StartScene extends Scene {
    /**
     * Constructor method.
     * Sets layout from startScreen.fxml via {@code ResourceUtil.LoadFXML}.
     * Also sets stylesheet from style.css via {@code ResourceUtil.LoadCSS}.
     *
     * @see ResourceUtil#loadFXML(String)
     * @see ResourceUtil#loadCSS(String)
     */
    public StartScene() {
        super(ResourceUtil.loadFXML("startScreen.fxml"), PlayerView.WIN_WIDTH, PlayerView.WIN_HEIGHT);

        getStylesheets().add(ResourceUtil.loadCSS("style.css"));
    }
}
