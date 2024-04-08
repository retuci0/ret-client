package net.retclient.event.listeners;

import net.retclient.event.events.MouseMoveEvent;

public interface MouseMoveListener extends AbstractListener {
	public abstract void OnMouseMove(MouseMoveEvent mouseMoveEvent);
}
