package net.retclient.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.netty.channel.ChannelHandlerContext;
import net.retclient.Main;
import net.retclient.event.events.ReceivePacketEvent;
import net.retclient.event.events.SendPacketEvent;
import net.retclient.misc.FakePlayerEntity;
import net.retclient.module.modules.movement.Freecam;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

	@Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;handlePacket(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;)V", ordinal = 0) }, method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", cancellable = true)
	protected void onChannelRead(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
		
		ReceivePacketEvent event = new ReceivePacketEvent(packet);
		Main.getInstance().eventManager.Fire(event);
		
		//Aoba.getInstance().moduleManager.recievePacket(packet);
		if(Main.getInstance().moduleManager.freecam.getState()){
			if(packet instanceof PlayerPositionLookS2CPacket) {
				PlayerPositionLookS2CPacket convertedPacket = (PlayerPositionLookS2CPacket)packet;
				Freecam freecam = (Freecam)Main.getInstance().moduleManager.freecam;
				FakePlayerEntity fake = freecam.getFakePlayer();
				fake.setPos(convertedPacket.getX(), convertedPacket.getY(), convertedPacket.getZ());
				fake.setPitch(convertedPacket.getPitch());
				fake.setYaw(convertedPacket.getYaw());
				ci.cancel();
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", cancellable = true)
	private void onSend(Packet<?> packet, @Nullable PacketCallbacks callback, CallbackInfo ci)
	{
		SendPacketEvent event = new SendPacketEvent(packet);
		Main.getInstance().eventManager.Fire(event);
		
		if(event.IsCancelled()) {
			ci.cancel();
		}
	}
	

}