package net.retclient.event.listeners;

import net.retclient.event.events.FoodLevelEvent;

public interface FoodLevelListener extends AbstractListener {
	public abstract void OnFoodLevelChanged(FoodLevelEvent readPacketEvent);
}
