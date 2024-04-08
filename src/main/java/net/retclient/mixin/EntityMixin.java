package net.retclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.retclient.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.tag.TagKey;

@Mixin(Entity.class)
public abstract class EntityMixin{
	
	@Shadow
	protected DataTracker dataTracker;
	
	@Shadow
	public abstract boolean isSubmergedIn(TagKey<Fluid> fluidTag);
	
	@Shadow 
	public abstract boolean isOnGround();
	
	@Inject(at = { @At("HEAD") }, method = "isInvisibleTo(Lnet/minecraft/entity/player/PlayerEntity;)Z", cancellable = true)
	private void onIsInvisibleCheck(PlayerEntity message, CallbackInfoReturnable<Boolean> cir) {
		if(Main.getInstance().moduleManager.antiinvis.getState()) {
			cir.setReturnValue(false);
		}
	}
	
	@Inject(at = {@At("HEAD")}, method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true)
	public void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {

	}
}
