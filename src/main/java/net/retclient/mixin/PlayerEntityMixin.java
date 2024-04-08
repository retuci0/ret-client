package net.retclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import net.retclient.Main;
import net.retclient.Client;
import net.retclient.event.events.PlayerHealthEvent;
import net.retclient.module.modules.misc.FastBreak;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.tag.FluidTags;

@Mixin (PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {

	@Shadow 
	private PlayerInventory inventory;
	
	@Shadow
	public abstract boolean isSpectator();
	
	@Inject(method = "getBlockBreakingSpeed", at = @At("HEAD"), cancellable = true)
	public void onGetBlockBreakingSpeed(BlockState blockState, CallbackInfoReturnable<Float> ci) {
		Client ret = Main.getInstance();
		FastBreak fastBreak = (FastBreak)ret.moduleManager.fastbreak;
		if(fastBreak.getState()) {
			float speed = inventory.getBlockBreakingSpeed(blockState);
			speed *= fastBreak.multiplier.getValue();
			
			if(!fastBreak.ignoreWater.getValue()) {
				if(isSubmergedIn(FluidTags.WATER) || isSubmergedIn(FluidTags.LAVA) ||!isOnGround()) {
					speed /= 5.0F;
				}
			}
			
			ci.setReturnValue(speed);
		}
	}
	
	@Inject(at = {@At("HEAD")}, method="getOffGroundSpeed()F", cancellable = true)
	protected void onGetOffGroundSpeed(CallbackInfoReturnable<Float> cir) {
		return;
	}
}