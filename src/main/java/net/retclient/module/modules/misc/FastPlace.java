package net.retclient.module.modules.misc;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.TickListener;
import net.retclient.mixin.interfaces.IMinecraftClient;
import net.retclient.module.Module;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class FastPlace extends Module implements TickListener{
	IMinecraftClient iMC;
	
	public FastPlace() {
		super(new KeybindSetting("key.fastplace", "FastPlace Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("FastPlace");
		this.setCategory(Category.Misc);
		this.setDescription("Places blocks exceptionally fast");
	}

	@Override
	public void onDisable() {
		IMC.setItemUseCooldown(4);
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
		IMC.setItemUseCooldown(0);
	}
}