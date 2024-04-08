package net.retclient.module.modules.movement;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.TickListener;
import net.retclient.module.Module;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class Sprint extends Module implements TickListener {

	public Sprint() {
		super(new KeybindSetting("key.sprinthack", "Sprint Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("Sprint");
		this.setCategory(Category.Movement);
		this.setDescription("Permanently keeps player in sprinting mode.");
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
		MC.player.setSprinting(true);
	}
}