package net.retclient.event.events;

import java.util.ArrayList;
import java.util.List;

import net.retclient.event.listeners.AbstractListener;
import net.retclient.event.listeners.PlayerHealthListener;
import net.minecraft.entity.damage.DamageSource;

public class PlayerHealthEvent extends AbstractEvent {
	private float health;
	private DamageSource source;
	
	public PlayerHealthEvent(DamageSource source, float health) {
		this.source = source;
		this.health = health;
	}
	
	public float getHealth() {
		return health;
	}
	
	public DamageSource getDamageSource() {
		return source;
	}
	
	@Override
	public void Fire(ArrayList<? extends AbstractListener> listeners) {
		for(AbstractListener listener : List.copyOf(listeners)) {
			PlayerHealthListener playerHealthListener = (PlayerHealthListener) listener;
			playerHealthListener.OnHealthChanged(this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<PlayerHealthListener> GetListenerClassType() {
		return PlayerHealthListener.class;
	}
}