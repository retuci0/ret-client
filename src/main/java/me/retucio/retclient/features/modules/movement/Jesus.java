package me.retucio.retclient.features.modules.movement;

import com.google.common.eventbus.Subscribe;

import me.retucio.retclient.event.events.BlockShapeEvent;
import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;

public class Jesus extends Module {
	
	private final Setting<Mode> mode = register(new Setting<>("Mode", Mode.SOLID));

	public Jesus() {
		super("Jesus", "Allows you to walk over water", Category.MOVEMENT, true, false, false);
	}

	@Override
	public void onTick() {
		Entity entity = mc.player.getRootVehicle();

		if (entity.isSneaking() || entity.fallDistance > 3f) 
			return;

		if (isSubmerged(entity.getPos().add(0, 0.3, 0))) {
			entity.setVelocity(entity.getVelocity().x, 0.08, entity.getVelocity().z);
		} 
		
		else if (isSubmerged(entity.getPos().add(0, 0.1, 0))) {
			entity.setVelocity(entity.getVelocity().x, 0.05, entity.getVelocity().z);
		} 
		
		else if (isSubmerged(entity.getPos().add(0, 0.05, 0))) {
			entity.setVelocity(entity.getVelocity().x, 0.01, entity.getVelocity().z);
		} 
		
		else if (isSubmerged(entity.getPos())) {
			entity.setVelocity(entity.getVelocity().x, -0.005, entity.getVelocity().z);
			entity.setOnGround(true);
		}
	}

	@Subscribe
	public void onBlockShape(BlockShapeEvent event) {
		if (mode.getValue() == Mode.SOLID
				&& !mc.world.getFluidState(event.getPos()).isEmpty()
				&& !mc.player.isSneaking()
				&& !mc.player.isTouchingWater()
				&& mc.player.getY() >= event.getPos().getY() + 0.9) {
			
			event.setShape(VoxelShapes.cuboid(0, 0, 0, 1, 0.9, 1));
		}
	}
	
	private boolean isSubmerged(Vec3d pos) {
		BlockPos blockPos = BlockPos.ofFloored(pos);
		FluidState state = mc.world.getFluidState(blockPos);

		return !state.isEmpty() && pos.y - blockPos.getY() <= state.getHeight();
	}
	
	private enum Mode {
		VIBRATE,
		SOLID;
	}
}