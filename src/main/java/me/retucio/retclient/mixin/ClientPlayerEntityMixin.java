package me.retucio.retclient.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;

import static me.retucio.retclient.util.traits.Util.EVENT_BUS;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.retucio.retclient.event.Stage;
import me.retucio.retclient.event.events.UpdateEvent;
import me.retucio.retclient.event.events.UpdateWalkingPlayerEvent;

import static me.retucio.retclient.util.traits.Util.mc;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
	
    @Inject(method = "tick", at = @At("TAIL"))
    private void tickHook(CallbackInfo ci) {
        EVENT_BUS.post(new UpdateEvent());
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift = At.Shift.AFTER))
    private void tickHook2(CallbackInfo ci) {
        EVENT_BUS.post(new UpdateWalkingPlayerEvent(Stage.PRE, MovementType.PLAYER, mc.player.getPos()));
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendMovementPackets()V", shift = At.Shift.AFTER))
    private void tickHook3(CallbackInfo ci) {
        EVENT_BUS.post(new UpdateWalkingPlayerEvent(Stage.POST, MovementType.PLAYER, mc.player.getPos()));
    }
}