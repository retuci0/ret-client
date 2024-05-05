package me.retucio.retclient.features.modules.combat;

import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import me.retucio.retclient.manager.RotationManager;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class AimBot extends Module {

	static PlayerEntity target = null;
	
	float tempYaw = 0;
	float bestYaw = 0;
	
	float tempPitch = 0;
	float bestPitch = 0;
	
    public Setting<Float> smoothing = register(new Setting<>("Smoothing", 3f, 0.5f, 50f));
    
	public AimBot() {
		super("AimBot", "Aims for you", Category.COMBAT, true, false, false);
	}
	
	@Override
	public void onTick() {
		HitResult hitResult = mc.crosshairTarget;
		
		if (!nullCheck()) {
			
			if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
			    if (((EntityHitResult) hitResult).getEntity() instanceof PlayerEntity player) target = player;
			} 
			
			else if (target == null) return;
			
			int maxDistance = 8;
			if (target != null) {
				if (target.isDead() || mc.player.squaredDistanceTo(target) > maxDistance) target = null;
				
				tempPitch = mc.player.getPitch();
				tempYaw = mc.player.getYaw();
				
				mc.player.setYaw(newYaw());
				mc.player.setPitch(newPitch());
			}
		}
	}
	

	private float newYaw() {
		return RotationManager.getSmoothRotations(target)[0];
	}
	
	private float newPitch() {
		return RotationManager.getSmoothRotations(target)[1];
	}
}