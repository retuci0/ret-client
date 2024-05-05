package me.retucio.retclient.event.events;

import me.retucio.retclient.event.Event;
import net.minecraft.network.packet.Packet;

public abstract class PlayerEvent extends Event {
	
    private String name;
    private String uuid;

    public PlayerEvent(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }
    
    public static class Join extends PlayerEvent {
        public Join(String name, String uuid) {
            super(name, uuid);
        }
    }

    public static class Leave extends PlayerEvent {
        public Leave(String name, String uuid) {
            super(name, uuid);
        }
    }
    
}