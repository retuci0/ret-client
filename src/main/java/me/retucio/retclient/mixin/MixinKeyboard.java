package me.retucio.retclient.mixin;

import net.minecraft.client.Keyboard;

import static me.retucio.retclient.util.traits.Util.EVENT_BUS;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.retucio.retclient.event.events.KeyEvent;

@Mixin(Keyboard.class)
public class MixinKeyboard {
	
    @Inject(method = "onKey", at = @At("TAIL"), cancellable = true)
    private void onKey(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        KeyEvent event = new KeyEvent(key);
        EVENT_BUS.post(event);
        
        if (event.isCancelled()) ci.cancel();
    }
}
