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
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public class Jetpack extends Module implements TickListener {

	private FloatSetting jetpackSpeed;
	
	public Jetpack() {
		super(new KeybindSetting("key.jetpack", "Jetpack Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
	
		this.setName("Jetpack");
		this.setCategory(Category.Movement);
		this.setDescription("Like fly, but a lot more fun!");
		
		jetpackSpeed = new FloatSetting(""
				+ "jetpack_speed", "Speed", "Jetpack Speed", 0.5f, 0.1f, 5.0f, 0.1f);
		this.addSetting(jetpackSpeed);
	}

	public void setSpeed(float speed) {
		this.jetpackSpeed.setValue(speed);
	}
	
	public double getSpeed() {
		return this.jetpackSpeed.getValue();
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
		float speed = this.jetpackSpeed.getValue().floatValue();
		
		if(MC.player.fallDistance > 2f) {
			MC.player.networkHandler.sendPacket(new OnGroundOnly(true));
		}
		
		if(MC.player.isRiding()) {
			Entity riding = MC.player.getRootVehicle();
			Vec3d velocity = riding.getVelocity();
			double motionY = MC.options.jumpKey.isPressed() ? 0.3 : 0;
			riding.setVelocity(velocity.x, motionY, velocity.z);
		}else {
			player.getAbilities().flying = false;
			
			Vec3d playerSpeed = player.getVelocity();
			if (MC.options.jumpKey.isPressed()) {
				double angle = -player.bodyYaw;
				float leftThrusterX = (float) Math.sin(Math.toRadians(angle + 90)) * 0.25f;
				float leftThrusterZ = (float) Math.cos(Math.toRadians(angle + 90)) * 0.25f;
				float rightThrusterX = (float) Math.sin(Math.toRadians(angle + 270)) * 0.25f;
				float rightThrusterZ = (float) Math.cos(Math.toRadians(angle + 270)) * 0.25f;
				
				MC.world.addParticle(ParticleTypes.FLAME, player.getX() + leftThrusterX, player.getY() + 0.5f, player.getZ() + leftThrusterZ, leftThrusterX, -0.5f, leftThrusterZ);
				MC.world.addParticle(ParticleTypes.FLAME, player.getX() + rightThrusterX, player.getY() + 0.5f, player.getZ() + rightThrusterZ, rightThrusterX, -0.5f, rightThrusterZ);
				playerSpeed = playerSpeed.add(0, speed / 20.0f, 0);
			}
			player.setVelocity(playerSpeed);
		}
	}
}