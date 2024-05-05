package me.retucio.retclient.features.modules.player;

import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;

public class LockRotation extends Module {

    public final Setting<Integer> yawLock = new Setting<>("Yaw", 0, 0, 360);
    public final Setting<Integer> yawLockNegative = new Setting<>("Negative Yaw", 0, 0, -360);

    public final Setting<Integer> pitchLock = new Setting<>("Pitch", 0, 0, 90);
    public final Setting<Integer> pitchLockNegative = new Setting<>("Negative Pitch", 0, 0, -90);
	
	public LockRotation() {
		super("LockRotation", "Locks your yaw and pitch to a fixed value", Category.PLAYER, true, false, false);
	}
	
	@Override
	public void onTick() {
        if (!nullCheck()) {
            mc.player.setYaw((float) yawLock.getValue() + yawLockNegative.getValue());
            mc.player.setPitch((float) pitchLock.getValue() + pitchLockNegative.getValue());
        }
	}
}