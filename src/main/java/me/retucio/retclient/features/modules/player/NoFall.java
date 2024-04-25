package me.retucio.retclient.features.modules.player;

import me.retucio.retclient.features.modules.Module;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;

public class NoFall extends Module {
	
	public static final NoFall INSTANCE = new NoFall();
	
	public NoFall() {
		super("NoFall", "Prevents you from taking fall damage", Category.PLAYER, true, false, false);
	}

	@Override
	public void onUpdate() {
		if (mc.player.fallDistance > 2f) {
			mc.player.networkHandler.sendPacket(new OnGroundOnly(true));
		}
	}
	
}