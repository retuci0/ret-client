package net.retclient.module.modules.misc;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.FoodLevelEvent;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.FoodLevelListener;
import net.retclient.event.listeners.TickListener;
import net.retclient.module.Module;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public class AutoEat extends Module implements FoodLevelListener {
	private FloatSetting hungerSetting;
	
	public AutoEat() {
		super(new KeybindSetting("key.autoeat", "AntiCactus Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("AutoEat");
		this.setCategory(Category.Misc);
		this.setDescription("Automatically eats the best food in your inventory.");
		
		hungerSetting = new FloatSetting("autoeat_hunger", "Hunger", "Determines when AutoEat will trigger.", 6, 1, 20, 1);
		
		this.addSetting(hungerSetting);
	}

	@Override
	public void onDisable() {
		Main.getInstance().eventManager.RemoveListener(FoodLevelListener.class, this);
	}

	@Override
	public void onEnable() {
		Main.getInstance().eventManager.AddListener(FoodLevelListener.class, this);
	}

	@Override
	public void onToggle() {

	}
	
	public void setHunger(int hunger) {
		hungerSetting.setValue((float)hunger);
	}

	@Override
	public void OnFoodLevelChanged(FoodLevelEvent readPacketEvent) {
		if(readPacketEvent.getFoodLevel() <= hungerSetting.getValue()) {
			int foodSlot= -1;
			FoodComponent bestFood = null;
			for(int i = 0; i< 9; i++) {
				Item item = MC.player.getInventory().getStack(i).getItem();
				
				if(!item.isFood()) {
					continue;
				}
				FoodComponent food = item.getFoodComponent();
				if(bestFood != null) {
					if(food.getHunger() > bestFood.getHunger()) {
						bestFood = food;
						foodSlot = i;
					}
				}else {
					bestFood = food;
					foodSlot = i;
				}
				
			}
			
		    if(bestFood != null) {
		    	MC.player.getInventory().selectedSlot = foodSlot;
		    	MC.options.useKey.setPressed(true);
		    }
		}
	}
}
