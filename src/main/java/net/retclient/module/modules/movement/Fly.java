package net.retclient.module.modules.movement;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.TickListener;
import net.retclient.module.Module;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class Fly extends Module implements TickListener {

	private FloatSetting flySpeed;
	
	public Fly() {
		super(new KeybindSetting("key.fly", "Fly Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_V, 0)));
	
		this.setName("Fly");
		this.setCategory(Category.Movement);
		this.setDescription("Allows the player to fly.");
		
		flySpeed = new FloatSetting("fly_speed", "Speed", "Fly speed.", 2f, 0.1f, 15f, 0.5f);
		this.addSetting(flySpeed);
	}

	public void setSpeed(float speed) {
		this.flySpeed.setValue(speed);
	}
	
	public double getSpeed() {
		return this.flySpeed.getValue();
	}

	
	@Override
	public void onDisable() {
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
		float speed = this.flySpeed.getValue().floatValue();
		if(MC.player.isRiding()) {
			Entity riding = MC.player.getRootVehicle();
			Vec3d velocity = riding.getVelocity();
			double motionY = MC.options.jumpKey.isPressed() ? 0.3 : 0;
			riding.setVelocity(velocity.x, motionY, velocity.z);
		}else {
			if (MC.options.sprintKey.isPressed()) {
				speed *= 1.5;
			}
			player.getAbilities().flying = false;
			player.setVelocity(new Vec3d(0, 0, 0));
			
			Vec3d vec = new Vec3d(0, 0, 0);
			
				
			if (MC.options.jumpKey.isPressed()) {
				vec = new Vec3d(0, speed, 0);
			}
			if (MC.options.sneakKey.isPressed()) {
				vec = new Vec3d(0, -speed, 0);
			}
			player.setVelocity(vec);
		}
	}
}