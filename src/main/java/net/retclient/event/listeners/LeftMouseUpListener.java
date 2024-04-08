package net.retclient.event.listeners;

import net.retclient.event.events.LeftMouseUpEvent;

public interface LeftMouseUpListener extends AbstractListener {
	public abstract void OnLeftMouseUp(LeftMouseUpEvent event);
}