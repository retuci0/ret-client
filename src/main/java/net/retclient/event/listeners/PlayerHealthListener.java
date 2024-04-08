package net.retclient.event.listeners;

import net.retclient.event.events.PlayerHealthEvent;

public interface PlayerHealthListener extends AbstractListener {
	public abstract void OnHealthChanged(PlayerHealthEvent readPacketEvent);
}
