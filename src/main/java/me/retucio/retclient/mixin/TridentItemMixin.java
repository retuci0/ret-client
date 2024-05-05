package me.retucio.retclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import me.retucio.retclient.features.modules.movement.TridentBoost;
import me.retucio.retclient.manager.ModuleManager;
import me.retucio.retclient.util.MiscUtil;
import me.retucio.retclient.util.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;

import static me.retucio.retclient.util.traits.Util.mc;

@Mixin(TridentItem.class)
public class TridentItemMixin {
	
    @Inject(method = "onStoppedUsing", at = @At("HEAD"))
    private void onStoppedUsingHead(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo info) {
        if (user == mc.player) MiscUtil.isReleasingTrident = true;
    }

    @Inject(method = "onStoppedUsing", at = @At("TAIL"))
    private void onStoppedUsingTail(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo info) {
        if (user == mc.player) MiscUtil.isReleasingTrident = false;
    }

    @ModifyArgs(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addVelocity(DDD)V"))
    private void modifyVelocity(Args args) {
        TridentBoost tridentBoost = ModuleManager.tridentBoost;

        args.set(0, (double) args.get(0) * tridentBoost.getMultiplier());
        args.set(1, (double) args.get(1) * tridentBoost.getMultiplier());
        args.set(2, (double) args.get(2) * tridentBoost.getMultiplier());
    }

    @ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
    private boolean isInWaterUse(boolean original) {
        TridentBoost tridentBoost = ModuleManager.tridentBoost;
        return tridentBoost.allowOutOfWater() || original;
    }

    @ModifyExpressionValue(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
    private boolean isInWaterPostUse(boolean original) {
        TridentBoost tridentBoost = ModuleManager.tridentBoost;
        return tridentBoost.allowOutOfWater() || original;
    }
}