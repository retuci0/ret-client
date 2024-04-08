package net.retclient.event.listeners;

import net.retclient.event.events.TickEvent;

public interface TickListener extends AbstractListener {
	public abstract void OnUpdate(TickEvent event);
}