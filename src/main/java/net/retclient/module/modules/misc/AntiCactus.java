package net.retclient.module.modules.misc;

import org.lwjgl.glfw.GLFW;
import net.retclient.module.Module;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class AntiCactus extends Module {
	
	public AntiCactus() {
		super(new KeybindSetting("key.anticactus", "AntiCactus Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("AntiCactus");
		this.setCategory(Category.Misc);
		this.setDescription("Prevents blocks from hurting you.");
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