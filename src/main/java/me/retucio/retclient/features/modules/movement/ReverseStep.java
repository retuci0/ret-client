package me.retucio.retclient.features.modules.movement;

import me.retucio.retclient.features.modules.Module;

public class ReverseStep extends Module {
	
    public ReverseStep() {
        super("ReverseStep", "Step, but reversed ._.", Category.MOVEMENT, true, false, false);
    }

    @Override 
    public void onUpdate() {
        if (nullCheck()) return;
        if (mc.player.isInLava() || mc.player.isTouchingWater() || !mc.player.isOnGround()) return;
        mc.player.addVelocity(0, -1, 0);
    }
}
