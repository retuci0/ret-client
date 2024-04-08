package net.retclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.retclient.Main;
import net.retclient.event.events.PlayerDeathEvent;
import net.retclient.gui.GuiManager;
import net.minecraft.client.gui.screen.DeathScreen;

@Mixin(DeathScreen.class)
public class DeathScreenMixin{
	
	@Inject(at = { @At("HEAD") }, method = "init()V", cancellable = true)
	private void onInit(CallbackInfo ci) {
		GuiManager hudManager = Main.getInstance().hudManager;
		if(hudManager.isClickGuiOpen()) {
			hudManager.setClickGuiOpen(false);
		}
		
		PlayerDeathEvent event = new PlayerDeathEvent();
		Main.getInstance().eventManager.Fire(event);
			
		if(event.IsCancelled()) {
			ci.cancel();
		}
	}
}