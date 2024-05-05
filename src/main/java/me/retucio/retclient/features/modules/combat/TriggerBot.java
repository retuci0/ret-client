package me.retucio.retclient.features.modules.combat;

import java.util.Objects;

import me.retucio.retclient.features.modules.Module;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;

public class TriggerBot extends Module {
	
	public TriggerBot() {
		super("Trigger", "Automaticly attack entities when looking at them", Category.COMBAT, true, false, false);
	}
	
	@Override
	public void onTick() {
		if (!(mc.crosshairTarget instanceof EntityHitResult) || Objects.requireNonNull(mc.player).getAttackCooldownProgress(0) < 1) {
    		return;
    	}
		
   		if (mc.player.getAttackCooldownProgress(0.5F) == 1) {
   			Objects.requireNonNull(mc.interactionManager).attackEntity(mc.player, 
   					((EntityHitResult) mc.crosshairTarget).getEntity()
   			);
   			
    		mc.player.swingHand(Hand.MAIN_HAND);
    	}
	}
}