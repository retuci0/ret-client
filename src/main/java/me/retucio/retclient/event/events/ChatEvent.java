package me.retucio.retclient.event.events;

import me.retucio.retclient.event.Event;

public class ChatEvent extends Event {
	
    private final String content;

    public ChatEvent(String content) {
        this.content = content;
    }

    public String getMessage() {
        return content;
    }
}
