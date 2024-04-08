package net.retclient.event.listeners;

import net.retclient.event.events.PlayerDeathEvent;

public interface PlayerDeathListener extends AbstractListener {
	public abstract void OnPlayerDeath(PlayerDeathEvent readPacketEvent);
}