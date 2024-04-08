package net.retclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import net.retclient.Main;
import net.retclient.Client;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
	
	protected TitleScreenMixin(Text title) {
		super(title);
	}

	@Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/GameVersion;getName()Ljava/lang/String;", ordinal = 0) }, method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V")
	public void onRender(DrawContext context, int mouseX, int mouseY,float delta, CallbackInfo ci) {
		Main.getInstance();
		context.drawTextWithShadow(this.textRenderer, "Ret Client " + Client.CLIENT_VERSION, 2, this.height - 20, 0xFF00FF);
	}
}