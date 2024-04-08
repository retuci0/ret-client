package net.retclient.event.listeners;

import net.retclient.event.events.KeyDownEvent;

public interface KeyDownListener extends AbstractListener {
	public abstract void OnKeyDown(KeyDownEvent event);
}