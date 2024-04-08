package net.retclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.retclient.Main;
import net.minecraft.client.render.LightmapTextureManager;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
	@Inject(at = { @At("HEAD") }, method = { "getDarknessFactor(F)F" }, cancellable = true)
	private void onGetDarknessFactor(float delta, CallbackInfoReturnable<Float> cir) {
		if (Main.getInstance().moduleManager.nooverlay.getState())
			cir.setReturnValue(0F);
	}
}