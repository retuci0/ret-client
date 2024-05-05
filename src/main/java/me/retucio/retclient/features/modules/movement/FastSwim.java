package me.retucio.retclient.features.modules.movement;

import com.google.common.eventbus.Subscribe;

import me.retucio.retclient.event.events.UpdateWalkingPlayerEvent;
import me.retucio.retclient.features.modules.Module;
import net.minecraft.util.math.Vec3d;

public class FastSwim extends Module {
	
	public FastSwim() {
		super("FastSwim", "Makes you submerge faster in water", Category.MOVEMENT, true, false, false);
	}

	@Subscribe
	public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
		if (fullNullCheck()) return;
		
		if (mc.player.isSubmergedInWater() && mc.options.sneakKey.isPressed()) {
			Vec3d velocity = mc.player.getVelocity();
			mc.player.setVelocity(velocity.x, -0.44000000000000006, velocity.z);
		}
		
		if (mc.player.isInLava() && mc.options.sneakKey.isPressed()) {
			Vec3d velocity = mc.player.getVelocity();
			mc.player.setVelocity(velocity.x, -0.182, velocity.z);
        }
	}
}