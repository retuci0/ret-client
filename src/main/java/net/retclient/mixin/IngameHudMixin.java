package net.retclient.mixin;

import net.retclient.Main;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class IngameHudMixin {

	@Inject(at = {@At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V", remap = false,ordinal = 3) }, method = {"render(Lnet/minecraft/client/gui/DrawContext;F)V" })
	private void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
		Main.getInstance().drawHUD(context, tickDelta);
	}
}