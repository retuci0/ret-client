package net.retclient.module.modules.combat;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.PlayerDeathEvent;
import net.retclient.event.listeners.PlayerDeathListener;
import net.retclient.module.Module;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class AutoRespawn extends Module implements PlayerDeathListener {
	
	public AutoRespawn() {
		super(new KeybindSetting("key.autorespawn", "AutoRespawn Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("AutoRespawn");

		this.setCategory(Category.Combat);
		this.setDescription("Automatically respawns when you die.");
	}

	@Override
	public void onDisable() {
		Main.getInstance().eventManager.RemoveListener(PlayerDeathListener.class, this);
	}

	@Override
	public void onEnable() {
		Main.getInstance().eventManager.AddListener(PlayerDeathListener.class, this);
	}

	@Override
	public void onToggle() {

	}
	
	@Override
	public void OnPlayerDeath(PlayerDeathEvent readPacketEvent) {
		MC.player.requestRespawn();
		MC.setScreen(null);
	}
}