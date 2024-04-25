package me.retucio.retclient.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;

import static me.retucio.retclient.util.traits.Util.EVENT_BUS;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.retucio.retclient.event.events.ChatEvent;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
	
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void sendChatMessageHook(String content, CallbackInfo ci) {
        ChatEvent event = new ChatEvent(content);
        EVENT_BUS.post(event);
        
        if (event.isCancelled()) ci.cancel();
    }
}