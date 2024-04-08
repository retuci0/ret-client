package net.retclient.event.events;

import java.util.ArrayList;
import java.util.List;

import net.retclient.event.listeners.AbstractListener;
import net.retclient.event.listeners.FoodLevelListener;
import net.retclient.event.listeners.PlayerHealthListener;
import net.minecraft.entity.damage.DamageSource;

public class FoodLevelEvent extends AbstractEvent {
	private float foodLevel;
	
	public FoodLevelEvent(float foodLevel) {
		this.foodLevel = foodLevel;
	}
	
	public float getFoodLevel() {
		return foodLevel;
	}
	
	
	@Override
	public void Fire(ArrayList<? extends AbstractListener> listeners) {
		for(AbstractListener listener : List.copyOf(listeners)) {
			FoodLevelListener foodLevelListener = (FoodLevelListener) listener;
			foodLevelListener.OnFoodLevelChanged(this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<FoodLevelListener> GetListenerClassType() {
		return FoodLevelListener.class;
	}
}