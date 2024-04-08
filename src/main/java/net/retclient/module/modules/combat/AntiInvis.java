package net.retclient.module.modules.combat;

import org.lwjgl.glfw.GLFW;

import net.retclient.module.Module;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class AntiInvis extends Module {
	
	public AntiInvis() {
		super(new KeybindSetting("key.antiinvis", "AntiInvis Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("AntiInvis");
		this.setCategory(Category.Combat);
		this.setDescription("Reveals players who are invisible.");
	}

	@Override
	public void onDisable() {
		
	}

	@Override
	public void onEnable() {
		
	}

	@Override
	public void onToggle() {

	}
}