package org.example;

import javafx.stage.Stage;

public class StartStage extends Stage {
    private final static StartStage m_Self = new StartStage();

    public static StartStage getInstance() {
        return m_Self;
    }

    private StartStage() {
        super();
    }

    public void start() {
        setScene(new StartScene());
        show();
    }

    public void loadGame() {
        new PlayerView(true).CreateGUI();
        new PlayerView(false).CreateGUI();

        this.hide();
    }
}
