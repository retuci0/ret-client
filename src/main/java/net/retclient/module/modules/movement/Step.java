package net.retclient.module.modules.movement;

import org.lwjgl.glfw.GLFW;
import net.retclient.module.Module;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class Step extends Module {

	private FloatSetting stepHeight;
	
	public Step() {
		super(new KeybindSetting("key.step", "Step Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("Step");
		this.setCategory(Category.Movement);
		this.setDescription("Steps up blocks.");
		
		stepHeight = new FloatSetting("step_height", "Height", "Height that the player will step up.", 1f, 0.0f, 2f, 0.5f);
		
		this.addSetting(stepHeight);
	}

	@Override
	public void onDisable() {
		if(MC.world != null) {
			MC.player.setStepHeight(.5f);
		}
	}

	@Override
	public void onEnable() {
		MC.player.setStepHeight(stepHeight.getValue().floatValue());
	}

	@Override
	public void onToggle() {

	}
	
	public void setStepHeight(float height) {
		this.stepHeight.setValue(height);
	}
}
