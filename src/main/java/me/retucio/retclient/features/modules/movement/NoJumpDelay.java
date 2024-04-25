package me.retucio.retclient.features.modules.movement;

import me.retucio.retclient.features.settings.Setting;
import me.retucio.retclient.mixin.accessors.LivingEntityAccessor;
import me.retucio.retclient.features.modules.Module;

public class NoJumpDelay extends Module {

	private final Setting<Float> delay = register(new Setting<>("Delay (ms)", 0f, 0f, 20f, v -> true));
	
	public NoJumpDelay() {
		super("NoJumpDelay", "Gets rid of the annoying jump delay", Category.MOVEMENT, true, false, false);
	}
	
	@Override
	public void onTick() {
		LivingEntityAccessor player = (LivingEntityAccessor) mc.player;
		
		if (player.getJumpCooldown() > delay.getValue()) {
			player.setJumpCooldown(delay.getValue().intValue());
		}
	}
}