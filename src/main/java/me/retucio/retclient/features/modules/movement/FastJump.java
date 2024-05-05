package me.retucio.retclient.features.modules.movement;

import me.retucio.retclient.features.settings.Setting;
import me.retucio.retclient.mixin.accessors.LivingEntityAccessor;
import me.retucio.retclient.features.modules.Module;

public class FastJump extends Module {

	private int originalDelay;
	private final Setting<Integer> delay = register(new Setting<>("Delay (ms)", 0, 0, 20));
	
	public FastJump() {
		super("FastJump", "Gets rid of the annoying jump delay", Category.MOVEMENT, true, false, false);
	}
	
	@Override
	public void onEnable() {
		LivingEntityAccessor player = (LivingEntityAccessor) mc.player;
		originalDelay = player.getJumpCooldown();
	}
	
	@Override
	public void onTick() {
		LivingEntityAccessor player = (LivingEntityAccessor) mc.player;
		if (player.getJumpCooldown() > delay.getValue()) {
			player.setJumpCooldown(delay.getValue());
		}
	}
	
	@Override
	public void onDisable() {
		LivingEntityAccessor player = (LivingEntityAccessor) mc.player;
		player.setJumpCooldown(originalDelay);
	}
}