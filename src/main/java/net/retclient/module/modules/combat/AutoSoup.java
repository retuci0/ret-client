package net.retclient.module.modules.combat;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.PlayerHealthEvent;
import net.retclient.event.listeners.PlayerHealthListener;
import net.retclient.module.Module;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.StewItem;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

public class AutoSoup extends Module implements PlayerHealthListener {

	private FloatSetting health;
	
	private int previousSlot = -1;
	
	public AutoSoup() {
		super(new KeybindSetting("key.autosoup", "AutoSoup Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("AutoSoup");
		this.setCategory(Category.Combat);
		this.setDescription("Automatically consumes soup when health is low. (KitPVP)");
		
		health = new FloatSetting("autosoup_health", "Min. Health", "Minimum health that the AutoSoup will trigger.", 6f, 1f, 20f, 1f);
		this.addSetting(health);
	}

	@Override
	public void onDisable() {
		Main.getInstance().eventManager.RemoveListener(PlayerHealthListener.class, this);
	}

	@Override
	public void onEnable() {
		Main.getInstance().eventManager.AddListener(PlayerHealthListener.class, this);
	}

	@Override
	public void onToggle() {

	}

	public void sortInventory() {
		for(int i = 0; i < PlayerInventory.getHotbarSize(); i++) {
			ItemStack stack = MC.player.getInventory().getStack(i);
			if(stack == null || stack.getItem() == Items.BOWL) {
				int nextSoup = findSoup();
				if(nextSoup >= 0) {
					MC.interactionManager.clickSlot(0, nextSoup, 0, SlotActionType.PICKUP, MC.player);
					MC.interactionManager.clickSlot(0, i, 0, SlotActionType.PICKUP, MC.player);
				}
			}
		}
	}
	
	public int findSoup() {
		for(int i = 0; i < 36; i++)
		{
			ItemStack stack = MC.player.getInventory().getStack(i);
			if(stack != null && stack.getItem() == Items.MUSHROOM_STEW) {
				return i;
			}
		}
		return -1;
	}
	
	public void setHunger(int hunger) {
		
	}

	@Override
	public void OnHealthChanged(PlayerHealthEvent readPacketEvent) {
		System.out.println("autosoup");
		float playerHealth = readPacketEvent.getHealth();
		
		// If the players HP is below the given threshold.
		if(playerHealth < health.getValue()) {
			System.out.println("autosoup enabled");
			// Find the first item in the hotbar that is a Stew item.
			int foodSlot= -1;
			for(int i = 0; i< PlayerInventory.getHotbarSize(); i++) {
				Item item = MC.player.getInventory().getStack(i).getItem();
				
				if(item instanceof StewItem) {
					foodSlot = i;
					break;
				}
			}
			
			// If a Stew item was found, switch to it and use it.
			if(foodSlot >= 0) {
				previousSlot = MC.player.getInventory().selectedSlot;
				
				MC.player.getInventory().selectedSlot = foodSlot;
			    MC.options.useKey.setPressed(true);
			    MC.interactionManager.interactItem(MC.player, Hand.MAIN_HAND);
			    
			    // Return the player's selected slot back to the previous slot.
				if(previousSlot != -1) {
					MC.options.useKey.setPressed(false);
					MC.player.getInventory().selectedSlot = previousSlot;
					previousSlot = -1;
				}
			}else {
			// Otherwise, sort the inventory to try and find some.
				sortInventory();
			}
		}
	}
}