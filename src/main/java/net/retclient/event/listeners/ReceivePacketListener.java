package net.retclient.event.listeners;

import net.retclient.event.events.ReceivePacketEvent;

public interface ReceivePacketListener extends AbstractListener {
	public abstract void OnReceivePacket(ReceivePacketEvent readPacketEvent);
}
