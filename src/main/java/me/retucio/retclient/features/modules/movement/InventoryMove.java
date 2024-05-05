package me.retucio.retclient.features.modules.movement;

import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class InventoryMove extends Module {
	
	private final Setting<Boolean> detectSneak = register(new Setting<>("Detect Sneak", true));
	
	public InventoryMove() {
		super("InventoryMove", "Allows the player to move when the inventory is opened", Category.MOVEMENT, false, false, false);
	}
	
	 @Override
	 public void onTick() {
		 if (mc.currentScreen != null 
	        	&& !(mc.currentScreen instanceof ChatScreen) 
	       		&& !(mc.currentScreen instanceof SignEditScreen) 
	       		&& !(mc.currentScreen instanceof BookScreen)) {
			 for (KeyBinding keyBind : new KeyBinding[] {
					 mc.options.forwardKey, mc.options.backKey,
	                 mc.options.leftKey, mc.options.rightKey, 
	                 mc.options.jumpKey, mc.options.sprintKey}) {
				keyBind.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(),
						InputUtil.fromTranslationKey(keyBind.getBoundKeyTranslationKey()).getCode()));
	            }
			 
	            if (detectSneak.getValue()) { 
	            	mc.options.sneakKey.setPressed(InputUtil.isKeyPressed(
	            					mc.getWindow().getHandle(), InputUtil.fromTranslationKey(mc.options.sneakKey.getBoundKeyTranslationKey()).getCode())
	            	);
	            }
	    }
	}
}