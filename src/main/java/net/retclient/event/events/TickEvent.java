package net.retclient.event.events;

import java.util.ArrayList;
import java.util.List;
import net.retclient.event.listeners.AbstractListener;
import net.retclient.event.listeners.TickListener;

public class TickEvent extends AbstractEvent {
	@Override
	public void Fire(ArrayList<? extends AbstractListener> listeners) {
		for(AbstractListener listener : List.copyOf(listeners)) {
			TickListener tickListener = (TickListener) listener;
			tickListener.OnUpdate(this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<TickListener> GetListenerClassType() {
		return TickListener.class;
	}
}