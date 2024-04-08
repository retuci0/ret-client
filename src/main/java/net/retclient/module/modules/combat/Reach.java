package net.retclient.module.modules.combat;

import org.lwjgl.glfw.GLFW;
import net.retclient.module.Module;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class Reach extends Module {
	
	private FloatSetting distance;
	
	public Reach() {
		super(new KeybindSetting("key.reach", "Reach Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("Reach");
		this.setCategory(Category.Combat);
		this.setDescription("Reaches further.");
		
		distance = new FloatSetting("reach_distance", "Distance", "Distance, in blocks, that you can reach.", 5f, 1f, 15f, 1f);
		this.addSetting(distance);
	}

	public float getReach() {
		return distance.getValue().floatValue();
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
	
	public void setReachLength(float reach) {
		this.distance.setValue(reach);
	}
}