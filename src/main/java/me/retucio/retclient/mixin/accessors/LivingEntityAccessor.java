package me.retucio.retclient.mixin.accessors;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
	
    @Accessor("jumpingCooldown")
    int getJumpCooldown();

    @Accessor("jumpingCooldown")
    void setJumpCooldown(int val);
}