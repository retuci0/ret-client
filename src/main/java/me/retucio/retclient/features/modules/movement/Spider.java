package me.retucio.retclient.features.modules.movement;

import me.retucio.retclient.features.modules.Module;
import net.minecraft.util.math.Vec3d;

public class Spider extends Module {

	public Spider() {
		super("Spider", "Allows you to climb walls", Category.MOVEMENT, true, false, false);
	}
	
	@Override
	public void onTick() {
		if (mc.player.horizontalCollision) {
			mc.player.getVelocity().multiply(new Vec3d(1, 0, 1));
			mc.player.getVelocity().add(new Vec3d(0, 0.2, 0));
			mc.player.setOnGround(true);
		}
	}
}