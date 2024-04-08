package net.retclient.event.listeners;

import net.retclient.event.events.FontChangedEvent;
import net.retclient.event.events.KeyDownEvent;

public interface FontChangedListener extends AbstractListener {
	public abstract void OnFontChanged(FontChangedEvent event);
}
