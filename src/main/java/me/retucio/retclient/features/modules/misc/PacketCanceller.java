package me.retucio.retclient.features.modules.misc;

import com.google.common.eventbus.Subscribe;

import me.retucio.retclient.event.events.PacketEvent;
import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdatePlayerAbilitiesC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;

public class PacketCanceller extends Module {

	private final Setting<Boolean> vehicleMove = register(new Setting<>("VehicleMoveC2SPacket", true));
	private final Setting<Boolean> entityAction = register(new Setting<>("PlayerActionC2SPacket", true));
	private final Setting<Boolean> entityInteract = register(new Setting<>("PlayerInteractEntityC2SPacket", true));
	private final Setting<Boolean> playerInput = register(new Setting<>("PlayerInputC2SPacket", true));
	private final Setting<Boolean> playerMove = register(new Setting<>("PlayerMoveC2SPacket", true));
	private final Setting<Boolean> updatePlayerAbilities = register(new Setting<>("UpdatePlayerAbilitiesC2SPacket", true));
	private final Setting<Boolean> handSwing = register(new Setting<>("HandSwingC2SPacket", true));
	private final Setting<Boolean> playerInteractItem = register(new Setting<>("PlayerInteractItemC2SPacket", true));
	private final Setting<Boolean> playerInteractBlock = register(new Setting<>("PlayerInteractBlockC2SPacket", true));
	private final Setting<Boolean> chunkDeltaUpdate = register(new Setting<>("ChunkDeltaUpdate", true));
	
    public PacketCanceller() {
		super("PacketCanceller", "Cancels certain packets", Category.MISC, true, false, false);
	}

    @Subscribe
    public void onPacketSend(PacketEvent.Send event) {
    	if (nullCheck()) return;
    	
        if (event.getPacket() instanceof VehicleMoveC2SPacket && vehicleMove.getValue()) event.cancel();
        else if (event.getPacket() instanceof PlayerActionC2SPacket && entityAction.getValue()) event.cancel();
        else if (event.getPacket() instanceof PlayerInteractEntityC2SPacket && entityInteract.getValue()) event.cancel();
        else if (event.getPacket() instanceof PlayerInputC2SPacket && playerInput.getValue()) event.cancel();
        else if (event.getPacket() instanceof PlayerMoveC2SPacket && playerMove.getValue()) event.cancel();
        else if (event.getPacket() instanceof UpdatePlayerAbilitiesC2SPacket && updatePlayerAbilities.getValue()) event.cancel();
        else if (event.getPacket() instanceof HandSwingC2SPacket && handSwing.getValue()) event.cancel();
        else if (event.getPacket() instanceof PlayerInteractItemC2SPacket && playerInteractItem.getValue()) event.cancel();
        else if (event.getPacket() instanceof PlayerInteractBlockC2SPacket && playerInteractBlock.getValue()) event.cancel();
    }
    
    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
    	if (nullCheck()) return;
    	
    	if (event.getPacket() instanceof ChunkDeltaUpdateS2CPacket && chunkDeltaUpdate.getValue()) event.cancel();
    }
}