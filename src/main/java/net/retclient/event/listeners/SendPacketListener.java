package net.retclient.event.listeners;

import net.retclient.event.events.SendPacketEvent;

public interface SendPacketListener extends AbstractListener {
	public abstract void OnSendPacket(SendPacketEvent event);
}
