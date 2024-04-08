package net.retclient.module.modules.movement;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.TickListener;
import net.retclient.module.Module;
import net.retclient.settings.types.BooleanSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class Jesus extends Module implements TickListener {

	public BooleanSetting legit;

	public Jesus() {
		super(new KeybindSetting("key.jesus", "Jesus Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("Jesus");
		this.setCategory(Category.Movement);
		this.setDescription("Allows the player to walk on water.");

		// Add Settings
		legit = new BooleanSetting("jesus_legit", "Legit", "Whether or not the player will swim as close to the surface as possible.", true);
		this.addSetting(legit);
	}

	@Override
	public void onDisable() {
		Main.getInstance().eventManager.RemoveListener(TickListener.class, this);
	}

	@Override
	public void onEnable() {
		Main.getInstance().eventManager.AddListener(TickListener.class, this);
	}

	@Override
	public void onToggle() {

	}

	@Override
	public void OnUpdate(TickEvent event) {
		// If Legit is enabled, simply swim.
		if (this.legit.getValue()) {
			if (MC.player.isInLava() || MC.player.isTouchingWater()) {
				MC.options.jumpKey.setPressed(true);
			}
		}
	}
}