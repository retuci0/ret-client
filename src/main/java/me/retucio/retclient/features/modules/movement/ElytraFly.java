package me.retucio.retclient.features.modules.movement;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ElytraFly extends Module {
	
	private Setting<Mode> mode = register(new Setting<>("Mode", Mode.STATIC));
	private Setting<Float> boostSetting = new Setting<>("Boost", 0.05f, 0f, 0.15f);
	private Setting<Float> maxBoost = register(new Setting<>("MaxBoost", 2.5f, 0f, 5f));
	private Setting<Float> speed = register(new Setting<>("Speed", 2f, 0f, 10f));
	private Setting<Integer> packets = register(new Setting<>("Packets", 2, 1, 10));

	public ElytraFly() {
		super("ElytraFly", "Hacks into the CIA to hack into the FBI to hack into Minecraft to hack into the elytras to make popbob hack into the Matrix and give you flying perms", Category.MOVEMENT, true, false, false);
	}
	
	@Override
	public void onTick() {
		Vec3d vec3d = new Vec3d(0, 0, packets.getValue()).rotateY(- (float) Math.toRadians(mc.player.getYaw()));

		double currentVel = Math.abs(
				mc.player.getVelocity().x) 
				+ Math.abs(mc.player.getVelocity().y) 
				+ Math.abs(mc.player.getVelocity().z
		);
		
		float radianYaw = (float) Math.toRadians(mc.player.getYaw());
		float boost = boostSetting.getValue();

		switch (mode.getValue()) {
				
			case Mode.BOOST:
				
				if (shouldPacketFly()) {
					mc.player.setVelocity(vec3d);
					
					mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(
							mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
					
					mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
							mc.player.getX() + vec3d.x, mc.player.getY() + vec3d.y, mc.player.getZ() + vec3d.z, true));
				}

				break;
			
			case Mode.STATIC:
				if (mc.player.isFallFlying() && wearingElytra()) {
		            	float yaw = mc.player.getYaw();
		            	int mx = 0, my = 0, mz = 0;
		     
		            	if (mc.options.rightKey.isPressed()) mx++;
		            	if (mc.options.leftKey.isPressed()) mx--;
		            	
		            	if (mc.options.jumpKey.isPressed()) my++;
		            	if (mc.options.sneakKey.isPressed()) my--;
		            	
		            	if (mc.options.backKey.isPressed()) mz++;
		            	if (mc.options.forwardKey.isPressed()) mz--;
		            	
		            	double ts = speed.getValue() / 2;
		                double s = Math.sin(Math.toRadians(yaw));
		                double c = Math.cos(Math.toRadians(yaw));
		                double nx = ts * mz * s;
		                double nz = ts * mz * -c;
		                double ny = ts * my;
		                
		                nx += ts * mx * -c;
		                nz += ts * mx * -s;
		                
		                Vec3d nv3 = new Vec3d(nx, ny, nz);
		                mc.player.setVelocity(nv3);
				}
		}
		
	}

	private boolean shouldPacketFly() {
		return !mc.player.isOnGround()
				&& !mc.options.sneakKey.isPressed()
				&& mc.player.getInventory().getArmorStack(2).getItem() == Items.ELYTRA;
	}
	
	private boolean wearingElytra() {
        ItemStack equippedStack = mc.player.getEquippedStack(EquipmentSlot.CHEST);
        return equippedStack != null && equippedStack.getItem() == Items.ELYTRA;
	}
	
	private enum Mode {
		STATIC,
		BOOST,
	}
}