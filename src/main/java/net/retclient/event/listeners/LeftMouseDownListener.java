package net.retclient.event.listeners;

import net.retclient.event.events.LeftMouseDownEvent;

public interface LeftMouseDownListener extends AbstractListener {
	public abstract void OnLeftMouseDown(LeftMouseDownEvent event);
}
