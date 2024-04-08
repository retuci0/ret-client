package net.retclient.event.listeners;

import net.retclient.event.events.MouseScrollEvent;

public interface MouseScrollListener extends AbstractListener {
	public abstract void OnMouseScroll(MouseScrollEvent event);
}
