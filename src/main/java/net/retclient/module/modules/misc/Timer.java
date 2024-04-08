package net.retclient.module.modules.misc;

import org.lwjgl.glfw.GLFW;
import net.retclient.module.Module;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class Timer extends Module {
	private FloatSetting multiplier;
	
	public Timer() {
		super(new KeybindSetting("key.timer", "Timer Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("Timer");
		this.setCategory(Category.Misc);
		this.setDescription("Increases the speed of Minecraft.");
		
		this.multiplier = new FloatSetting("timer_multiplier", "Multiplier", "The multiplier that will affect the game speed.", 1f, 0.1f, 15f, 0.1f);
		this.addSetting(multiplier);
	}

	public float getMultiplier() {
		return this.multiplier.getValue().floatValue();
	}
	
	public void setMultipler(float multiplier) {
		this.multiplier.setValue(multiplier);
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