package net.retclient.event.events;

import java.util.ArrayList;
import java.util.List;
import net.retclient.event.listeners.AbstractListener;
import net.retclient.event.listeners.PlayerDeathListener;

public class PlayerDeathEvent extends AbstractEvent {
	public PlayerDeathEvent() {
	}
	
	@Override
	public void Fire(ArrayList<? extends AbstractListener> listeners) {
		for(AbstractListener listener : List.copyOf(listeners)) {
			PlayerDeathListener playerDeathListener = (PlayerDeathListener) listener;
			playerDeathListener.OnPlayerDeath(this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<PlayerDeathListener> GetListenerClassType() {
		return PlayerDeathListener.class;
	}
}