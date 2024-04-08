package net.retclient.module.modules.misc;

import org.lwjgl.glfw.GLFW;
import net.retclient.module.Module;
import net.retclient.settings.types.BooleanSetting;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class FastBreak extends Module {

	public FloatSetting multiplier;
	public BooleanSetting ignoreWater;
	
	public FastBreak() {
		super(new KeybindSetting("key.fastbreak", "FastBreak Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("FastBreak");
		this.setCategory(Category.Misc);
		this.setDescription("Breaks blocks quicker based on a multiplier.");
		
		multiplier = new FloatSetting("fastbreak_multiplier", "Multiplier", "Multiplier for how fast the blocks will break.", 1.25f, 1f, 10f, 0.05f);
		ignoreWater = new BooleanSetting("fastbreak_ignore_water", "Ignore Water", "Ignores the slowdown that being in water causes.", false);
		
		this.addSetting(multiplier);
		this.addSetting(ignoreWater);
	}

	public void setMultiplier(float multiplier) { this.multiplier.setValue(multiplier); }
	
	public float getMultiplier() {
		return this.multiplier.getValue().floatValue();
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
