package net.retclient.event.listeners;

import net.retclient.event.events.RenderEvent;

public interface RenderListener extends AbstractListener {
	public abstract void OnRender(RenderEvent event);
}
