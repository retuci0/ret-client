package me.retucio.retclient.features.modules.player;

import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import me.retucio.retclient.mixin.accessors.ClientPlayerInteractionManagerAccessor;

public class FastBreak extends Module {

	private int originalDelay;
	private final Setting<Integer> delay = register(new Setting<>("Delay (ms)", 0, 0, 20));
	
	public FastBreak() {
		super("FastBreak", "Gets rid of the block breaking delay", Category.PLAYER, true, false, false);
	}
	
	@Override
	public void onEnable() {
		ClientPlayerInteractionManagerAccessor interactionManager = ((ClientPlayerInteractionManagerAccessor) mc.interactionManager);
		originalDelay = interactionManager.getBlockBreakingCooldown();
	}
	
	@Override
	public void onTick() {
		ClientPlayerInteractionManagerAccessor interactionManager = ((ClientPlayerInteractionManagerAccessor) mc.interactionManager);
		interactionManager.setBlockBreakingCooldown(delay.getValue());
	}
	
	
	@Override
	public void onDisable() {
		ClientPlayerInteractionManagerAccessor interactionManager = ((ClientPlayerInteractionManagerAccessor) mc.interactionManager);
		interactionManager.setBlockBreakingCooldown(originalDelay);
	}
}