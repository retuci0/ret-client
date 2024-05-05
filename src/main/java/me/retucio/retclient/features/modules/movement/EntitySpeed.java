package me.retucio.retclient.features.modules.movement;

import com.google.common.eventbus.Subscribe;

import me.retucio.retclient.event.events.UpdateLivingEntityEvent;
import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import me.retucio.retclient.util.PlayerUtil;
import me.retucio.retclient.util.interfaces.IVec3d;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d; 

public class EntitySpeed extends Module {
	
//	private final Setting<Boolean> fly = register(new Setting<>("Fly", false));
	private final Setting<Float> speed = register(new Setting<>("Speed", 10f, 1f, 50f));
	private final Setting<Boolean> onGround = register(new Setting<>("onGround", false));

	public EntitySpeed() {
		super("EntitySpeed", "horses go brrrrrr", Category.MOVEMENT, true, true, false);
	}

    @Subscribe
    public void onUpdateLivingEntityPost(UpdateLivingEntityEvent.Post event) {
    	if (nullCheck()) return;
    	
        if (event.getEntity().getControllingPassenger() != mc.player) return;

        LivingEntity entity = (LivingEntity) event.getEntity();
        if (onGround.getValue() && !entity.isOnGround()) return;
//        if (!inWater.get() && entity.isTouchingWater()) return;

        Vec3d vel = PlayerUtil.getHorizontalVelocity(speed.getValue());
        ((IVec3d) event.getMovement()).setXZ(vel.x, vel.z);
    }
}