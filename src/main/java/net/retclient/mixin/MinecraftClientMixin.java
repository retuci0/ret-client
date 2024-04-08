package net.retclient.mixin;

import net.retclient.Main;
import net.retclient.event.events.TickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.session.Session;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin{

	@Shadow
	private int itemUseCooldown;
	@Shadow
	@Final
	private Session session;
	
	@Shadow
	@Final
	private Mouse mouse;
	
	@Shadow
	public ClientWorld world;

	private Session aobaSession;

	@Inject(at = @At("TAIL"), method = "initFont(Z)V")
	private void init(boolean forcesUnicode, CallbackInfo info) {
		Main.getInstance().loadAssets();
	}
	
	@Inject(at = @At("TAIL"), method = "tick()V")
	public void tick(CallbackInfo info) {
		if (this.world != null) {
			TickEvent updateEvent = new TickEvent();
			Main.getInstance().eventManager.Fire(updateEvent);
			
			Main.getInstance().update();
		}
	}

	@Inject(at = {@At("HEAD")}, method = {"getSession()Lnet/minecraft/client/session/Session;"}, cancellable = true)
		private void onGetSession(CallbackInfoReturnable<Session> cir)
		{
			if(aobaSession == null) return;
			cir.setReturnValue(aobaSession);
		}
	
	@Redirect(at = @At(value = "FIELD",target = "Lnet/minecraft/client/MinecraftClient;session:Lnet/minecraft/client/session/Session;",opcode = Opcodes.GETFIELD,ordinal = 0),method = {"getSession()Lnet/minecraft/client/session/Session;"})
	private Session getSessionForSessionProperties(MinecraftClient mc)
	{
		if(aobaSession != null)return aobaSession;
		return session;
	}
	
	@Inject(at = {@At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;crosshairTarget:Lnet/minecraft/util/hit/HitResult;", ordinal = 0)}, method = {"doAttack()Z"}, cancellable = true)
	private void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
		//double mouseX = Math.ceil(mouse.getX());
		//double mouseY = Math.ceil(mouse.getY());
		
		//System.out.println("DOuble Click?");
		//MouseLeftClickEvent event = new MouseLeftClickEvent(mouseX, mouseY);
		
		//Aoba.getInstance().eventManager.Fire(event);
		
		//if(event.IsCancelled()) {
		//	cir.setReturnValue(false);
		//	cir.cancel();
		//}
	}
	
	@Inject(at = {@At(value = "HEAD")}, method = {"close()V"})
	private void onClose(CallbackInfo ci) {
		try {
			Main.getInstance().endClient();
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Inject(at = {@At(value="HEAD")}, method = {"openGameMenu(Z)V"})
	private void onOpenPauseMenu(boolean pause, CallbackInfo ci) {
		Main.getInstance().hudManager.setClickGuiOpen(false);
	}
}
