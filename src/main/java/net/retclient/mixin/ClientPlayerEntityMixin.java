package net.retclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.retclient.Main;
import net.retclient.event.events.PlayerHealthEvent;
import net.retclient.gui.GuiManager;
import net.retclient.misc.FakePlayerEntity;
import net.retclient.module.modules.movement.Fly;
import net.retclient.module.modules.movement.Freecam;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntityMixin {
	@Shadow
	private ClientPlayNetworkHandler networkHandler;
	
	@Inject(at = { @At("HEAD") }, method = "tick()V", cancellable = true)
	private void onPlayerTick(CallbackInfo ci) {
		if (Main.getInstance().moduleManager.freecam.getState()) {
			Freecam freecam = (Freecam) Main.getInstance().moduleManager.freecam;
			FakePlayerEntity fakePlayer = freecam.getFakePlayer();
			if(fakePlayer != null) {
				this.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(fakePlayer.getX(), fakePlayer.getY(),
						fakePlayer.getZ(), fakePlayer.isOnGround()));
				this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(fakePlayer.getYaw(),
						fakePlayer.getPitch(), fakePlayer.isOnGround()));
			}
		}
	}
	
	@Inject(at = {@At("HEAD") }, method="sendMovementPackets()V", cancellable = true)
	private void sendMovementPackets(CallbackInfo ci) {
		if (Main.getInstance().moduleManager.freecam.getState()) {
			ci.cancel();
		}
	}
	
	
	
	
	@Inject (at = {@At("HEAD")}, method="setShowsDeathScreen(Z)V")
	private void onShowDeathScreen(boolean state, CallbackInfo ci) {
		GuiManager hudManager = Main.getInstance().hudManager;

		if(state && hudManager.isClickGuiOpen()) {
			hudManager.setClickGuiOpen(false);
		}
	}

	
	@Override
	public void onIsSpectator(CallbackInfoReturnable<Boolean> cir) {
		if(Main.getInstance().moduleManager.freecam.getState()) {
			cir.setReturnValue(true);
		}
	}
	
	@Override
	public void onSetHealth(float health, CallbackInfo ci) {
		PlayerHealthEvent event = new PlayerHealthEvent(null, health);
		Main.getInstance().eventManager.Fire(event);
	}


	@Override
	protected void onGetOffGroundSpeed(CallbackInfoReturnable<Float> cir) {
		if(Main.getInstance().moduleManager.fly.getState()) {
			Fly fly = (Fly)Main.getInstance().moduleManager.fly;
			System.out.println("FLY IS ON!");
			cir.setReturnValue((float)fly.getSpeed());
		}else if(Main.getInstance().moduleManager.freecam.getState()) {
			Freecam freecam = (Freecam)Main.getInstance().moduleManager.freecam;
			cir.setReturnValue((float)freecam.getSpeed());
		}
	}
}