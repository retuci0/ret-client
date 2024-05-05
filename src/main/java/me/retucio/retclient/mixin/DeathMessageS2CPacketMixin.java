package me.retucio.retclient.mixin;

import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.retucio.retclient.features.modules.exploit.DeathScreenInvincibility;
import me.retucio.retclient.manager.ModuleManager;

@Mixin(DeathMessageS2CPacket.class)
public class DeathMessageS2CPacketMixin {
	
    @Inject(at = @At("HEAD"), method = "apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V", cancellable = true)
    public void apply(ClientPlayPacketListener clientPlayPacketListener, CallbackInfo ci) {
    	
    	DeathScreenInvincibility invincibility = ModuleManager.invincibility;
    	
        if (invincibility.isEnabled()) {
            ci.cancel();
        }
    }
}