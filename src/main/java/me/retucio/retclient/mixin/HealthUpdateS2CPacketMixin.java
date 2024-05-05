package me.retucio.retclient.mixin;

import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.retucio.retclient.features.Feature;
import me.retucio.retclient.features.modules.exploit.DeathScreenInvincibility;
import me.retucio.retclient.manager.ModuleManager;

@Mixin(HealthUpdateS2CPacket.class)
public abstract class HealthUpdateS2CPacketMixin {

    @Inject(at = @At("HEAD"), method = "apply(Lnet/minecraft/network/listener/ClientPlayPacketListener;)V", cancellable = true)
    public void apply(ClientPlayPacketListener clientPlayPacketListener, CallbackInfo ci) {
    	
    	DeathScreenInvincibility invincibility = ModuleManager.invincibility;
    	
        if (invincibility.isEnabled()) {
            if (!Feature.nullCheck()) {
                ci.cancel();
            }
        }
    }
}