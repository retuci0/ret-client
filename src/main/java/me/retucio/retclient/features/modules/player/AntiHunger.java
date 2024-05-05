package me.retucio.retclient.features.modules.player;

import com.google.common.eventbus.Subscribe;

import me.retucio.retclient.event.events.PacketEvent;
import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.mixin.accessors.PlayerMoveC2SPacketAccessor;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class AntiHunger extends Module {

	public AntiHunger() {
		super("AntiHunger", "Prevents hunger", Category.PLAYER, true, false, false);
	}

	@Subscribe
	public void onPacketSend(PacketEvent.Send event) {
		
		if (nullCheck()) return;
		if (mc.player.hasVehicle() || mc.player.isTouchingWater() || mc.player.isSubmergedInWater()) return;
		
		if (event.getPacket() instanceof ClientCommandC2SPacket) {
			ClientCommandC2SPacket packet = (ClientCommandC2SPacket) event.getPacket();
			if (packet.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING) event.cancel();
		}
		
		if (event.getPacket() instanceof PlayerMoveC2SPacket packet 
				&& mc.player.isOnGround() 
				&& mc.player.fallDistance <= 0.0 
				&& !mc.interactionManager.isBreakingBlock()) 
	    
		((PlayerMoveC2SPacketAccessor) packet).setOnGround(false);
	}
}