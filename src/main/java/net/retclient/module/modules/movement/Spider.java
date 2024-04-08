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

public class Spider extends Module implements TickListener {

	public Spider() {
		super(new KeybindSetting("key.spider", "Spider Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("Spider");
		this.setCategory(Category.Movement);
		this.setDescription("Allows players to climb up blocks.");
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
		if(player.horizontalCollision) {
			player.getVelocity().multiply(new Vec3d(1,0,1));
			player.getVelocity().add(new Vec3d(0,0.2,0));
			player.setOnGround(true);
		}
	}
}