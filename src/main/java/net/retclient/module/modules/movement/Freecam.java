package net.retclient.module.modules.movement;

import java.util.UUID;
import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.TickListener;
import net.retclient.misc.FakePlayerEntity;
import net.retclient.module.Module;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.util.math.Vec3d;

public class Freecam extends Module implements TickListener {
	private FakePlayerEntity fakePlayer;
	private FloatSetting flySpeed;
	
	public Freecam() {
		super(new KeybindSetting("key.freecam", "Freecam Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("Freecam");
		this.setCategory(Category.Movement);
		this.setDescription("Allows the player to clip through blocks (Only work clientside).");
		flySpeed = new FloatSetting("freecam_speed", "Speed", "Speed of the Freecam.", 2f, 0.1f, 15f, 0.5f);
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
		if(MC.world == null || fakePlayer == null) return;
		ClientPlayerEntity player = MC.player;
		MC.player.noClip = false;
		player.setVelocity(0, 0, 0);
		player.copyFrom(fakePlayer);
		fakePlayer.despawn();
		MC.world.removeEntity(-3, RemovalReason.DISCARDED);
		
		Main.getInstance().eventManager.RemoveListener(TickListener.class, this);
	}

	@Override
	public void onEnable() {
		ClientPlayerEntity player = MC.player;
		fakePlayer = new FakePlayerEntity();
		fakePlayer.copyFrom(player);
		fakePlayer.setUuid(UUID.randomUUID());
		fakePlayer.headYaw = player.headYaw;
		MC.world.addEntity(fakePlayer);
		
		Main.getInstance().moduleManager.fly.setState(false);
		Main.getInstance().moduleManager.noclip.setState(false);
		
		Main.getInstance().eventManager.AddListener(TickListener.class, this);
	}

	@Override
	public void onToggle() {

	}
	
	public FakePlayerEntity getFakePlayer() {
		return this.fakePlayer;
	}

	@Override
	public void OnUpdate(TickEvent event) {
		ClientPlayerEntity player = MC.player;
		player.noClip = true;
		player.setOnGround(false);
		float speed = this.flySpeed.getValue().floatValue();
		if (MC.options.sprintKey.isPressed()) {
			speed *= 1.5;
		}
		
		player.getAbilities().flying = false;
		player.setVelocity(new Vec3d(0, 0, 0));
		Vec3d vec = new Vec3d(0,0,0);
		if (MC.options.jumpKey.isPressed()) {
			vec = new Vec3d(0,speed * 0.2f,0);
		}
		if (MC.options.sneakKey.isPressed()) {
			vec = new Vec3d(0,-speed * 0.2f,0);
		}

		player.setVelocity(vec);
	}
}