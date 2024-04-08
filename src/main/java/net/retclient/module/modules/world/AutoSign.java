package net.retclient.module.modules.world;

import org.lwjgl.glfw.GLFW;
import net.retclient.cmd.CommandManager;
import net.retclient.module.Module;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class AutoSign extends Module {
	String[] text;

	public AutoSign() {
		super(new KeybindSetting("key.autosign", "AutoSign Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("AutoSign");
		this.setCategory(Category.World);
		this.setDescription("Automatically places sign.");
	}

	public void setText(String[] text) {
		this.text = text;
	}
	
	public String[] getText() {
		return this.text;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		CommandManager.sendChatMessage("Place down a sign to set text!");
		this.text = null;
	}

	@Override
	public void onToggle() {
	}
}