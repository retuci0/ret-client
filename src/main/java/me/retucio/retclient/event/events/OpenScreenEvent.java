package me.retucio.retclient.event.events;

import me.retucio.retclient.event.Event;
import net.minecraft.client.gui.screen.Screen;

public class OpenScreenEvent extends Event {
	
    private final Screen screen;

    public OpenScreenEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }
}