package net.retclient.mixin;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.retclient.Main;
import net.retclient.event.events.KeyDownEvent;
import net.minecraft.client.Keyboard;

@Mixin(Keyboard.class)
public class KeyboardMixin {
	
	@Inject(at = {@At("HEAD")}, method = {"onKey(JIIII)V" }, cancellable = true)
	private void OnKeyDown(long window, int key, int scancode,
			int action, int modifiers, CallbackInfo ci) {
		if(action == GLFW.GLFW_PRESS) {
			KeyDownEvent event = new KeyDownEvent(window, key, scancode, action, modifiers);
			Main.getInstance().eventManager.Fire(event);
			if(event.IsCancelled()) {
				ci.cancel();
			}
		}
	}
}