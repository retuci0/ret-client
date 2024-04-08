package net.retclient.module.modules.movement;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.TickListener;
import net.retclient.module.Module;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Vec3d;

public class Noclip extends Module implements TickListener {
	private float flySpeed = 5;
	
	public Noclip() {
		super(new KeybindSetting("key.noclip", "Noclip Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("Noclip");
		this.setCategory(Category.Movement);
		this.setDescription("Allows the player to clip through blocks (Only work clientside).");
	}

	public void setSpeed(float speed) {
		this.flySpeed = speed;
	}
	
	@Override
	public void onDisable() {
		if(MC.player != null) {
			MC.player.noClip = false;
		}
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
		ClientPlayerEntity player = MC.player;
		player.noClip = true;
		if (MC.options.sprintKey.isPressed()) {
			this.flySpeed *= 1.5;
		}
		player.setVelocity(new Vec3d(0,0,0));

		Vec3d vec = new Vec3d(0,0,0);
		if (MC.options.jumpKey.isPressed()) {
			vec = new Vec3d(0,flySpeed * 0.2f,0);
		}
		if (MC.options.sneakKey.isPressed()) {
			vec = new Vec3d(0,-flySpeed * 0.2f,0);
		}
		if (MC.options.sprintKey.isPressed()) {
			this.flySpeed /= 1.5;
		}
		player.setVelocity(vec);
	}
}
