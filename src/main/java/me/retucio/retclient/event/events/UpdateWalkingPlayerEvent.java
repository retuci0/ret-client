package me.retucio.retclient.event.events;

import me.retucio.retclient.event.Event;
import me.retucio.retclient.event.Stage;

public class UpdateWalkingPlayerEvent extends Event {
	
    private final Stage stage;

    public UpdateWalkingPlayerEvent(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}
