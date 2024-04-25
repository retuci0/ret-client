package me.retucio.retclient.event.events;

import me.retucio.retclient.event.Event;

public class KeyEvent extends Event {
	
    private final int key;

    public KeyEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
