package net.retclient.module.modules.combat;

import org.lwjgl.glfw.GLFW;
import net.retclient.module.Module;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class NoOverlay extends Module {

	public NoOverlay() {
		super(new KeybindSetting("key.nooverlay", "NoOverlay Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("NoOverlay");
		this.setCategory(Category.Combat);
		this.setDescription("Disables all overlays and potion effects.");
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