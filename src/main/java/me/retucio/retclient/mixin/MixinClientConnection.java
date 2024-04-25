package me.retucio.retclient.mixin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import me.retucio.retclient.event.events.PacketEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;

import static me.retucio.retclient.util.traits.Util.EVENT_BUS;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( ClientConnection.class )
public class MixinClientConnection {

    @Shadow private Channel channel;
    @Shadow @Final private NetworkSide side;

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    public void channelRead0(ChannelHandlerContext chc, Packet<?> packet, CallbackInfo ci) {
        if (this.channel.isOpen() && packet != null) {
        	
            try {
                PacketEvent.Receive event = new PacketEvent.Receive(packet);
                EVENT_BUS.post(event);
                
                if (event.isCancelled())
                    ci.cancel();
            } 
            
            catch (Exception e) {} // empty catch blocks, idc
        }
    }

    @Inject(method = "sendImmediately", at = @At("HEAD"), cancellable = true)
    private void sendImmediately(Packet<?> packet, PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
        if (this.side != NetworkSide.CLIENTBOUND) return;
        
        try {
            PacketEvent.Send event = new PacketEvent.Send(packet);
            EVENT_BUS.post(event);
            
            if (event.isCancelled()) ci.cancel();
        } 
        
        catch (Exception e) {}
    }
}