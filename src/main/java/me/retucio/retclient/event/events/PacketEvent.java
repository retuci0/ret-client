package me.retucio.retclient.event.events;

import me.retucio.retclient.event.Event;
import net.minecraft.network.packet.Packet;

public abstract class PacketEvent extends Event {

    private Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
    
    public void setPacket(Packet<?> packet) {
    	this.packet = packet;
    }

    public static class Receive extends PacketEvent {
        public Receive(Packet<?> packet) {
            super(packet);
        }
    }

    public static class Send extends PacketEvent {
        public Send(Packet<?> packet) {
            super(packet);
        }
    }
}