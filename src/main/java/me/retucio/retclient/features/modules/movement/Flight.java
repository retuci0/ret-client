package me.retucio.retclient.features.modules.movement;

import com.google.common.eventbus.Subscribe;

import me.retucio.retclient.event.events.PacketEvent;
import me.retucio.retclient.event.events.UpdateWalkingPlayerEvent;
import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import me.retucio.retclient.mixin.accessors.PlayerPositionLookS2CPacketAccessor;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;

public class Flight extends Module {

	private Vec3d cachedPos;
	private int timer = 0;
	private float originalFlySpeed;
	
	private final Setting<Mode> mode = register(new Setting<>("Mode", Mode.PACKET)); // don't use phase mode yet lmao
	private final Setting<Float> hSpeed = register(new Setting<>("HSpeed", 2f, 0.05f, 2f));
	private final Setting<Float> vSpeed =register(new Setting<>("VSpeed", 2f, 0.05f, 2f));
	private final Setting<Integer> fall = register(new Setting<>("Fall", 20, 0, 40));
	private final Setting<Boolean> cancelRubberband = register(new Setting<>("Cancel Rubberband", false));

	public Flight() {
		super("PacketFly", "Makes you fly", Category.MOVEMENT, true, false, false);
	}

	@Override
	public void onEnable() {
		if (nullCheck())
			return;
		
		if (mode.getValue() != Mode.VANILLA)
			cachedPos = mc.player.getRootVehicle().getPos();
		else
			mc.player.getAbilities().allowFlying = true;
			originalFlySpeed = mc.player.getAbilities().getFlySpeed();
			mc.player.getAbilities().setFlySpeed(Math.max(hSpeed.getValue(), vSpeed.getValue()));
	}

	@Subscribe
	public void onPacketSend(PacketEvent.Send event) {
		if (event.getPacket() instanceof PlayerMoveC2SPacket) {
			mc.player.setVelocity(Vec3d.ZERO);
			event.cancel();
		}
		
		if (event.getPacket() instanceof PlayerMoveC2SPacket.LookAndOnGround) {
			event.cancel();
			return;
		}

		if (event.getPacket() instanceof PlayerMoveC2SPacket.Full) {
			event.cancel();
			PlayerMoveC2SPacket packet = (PlayerMoveC2SPacket) event.getPacket();
			mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
					packet.getX(0), 
					packet.getY(0),
					packet.getZ(0), 
					packet.isOnGround())
			);
		}
	}

	@Subscribe
	public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
		event.cancel();
	}

	@Subscribe
	public void onPacketReceive(PacketEvent.Receive event) {
		if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
			PlayerPositionLookS2CPacket packet = (PlayerPositionLookS2CPacket) event.getPacket();
			
			PlayerPositionLookS2CPacketAccessor accessor = (PlayerPositionLookS2CPacketAccessor) packet;

			accessor.setYaw(mc.player.getYaw());
			accessor.setPitch(mc.player.getPitch());

			if (cancelRubberband.getValue()) {
				event.cancel();
			}
		}
	}

	@Override
	public void onTick() {
		if (nullCheck() || !mc.player.isAlive())
			return;

		double hspeed = hSpeed.getValue();
		double vspeed = vSpeed.getValue();
		timer++;

		Vec3d forward = new Vec3d(0, 0, hspeed).rotateY(-(float) Math.toRadians(mc.player.getYaw()));
		Vec3d moveVec = Vec3d.ZERO;

		if (mc.player.input.pressingForward) moveVec = moveVec.add(forward);
		
		if (mc.player.input.pressingBack) moveVec = moveVec.add(forward.negate());
		
		if (mc.player.input.jumping) moveVec = moveVec.add(0, vspeed, 0);
		
		if (mc.player.input.sneaking) moveVec = moveVec.add(0, -vspeed, 0);
		
		if (mc.player.input.pressingLeft) moveVec = moveVec.add(forward.rotateY((float) Math.toRadians(90)));
		
		if (mc.player.input.pressingRight) moveVec = moveVec.add(forward.rotateY((float) -Math.toRadians(90)));
		

		Entity target = mc.player.getRootVehicle();
		if (mode.getValue() == Mode.PACKET) {
			
			if (timer > fall.getValue()) {
				moveVec = moveVec.add(0, -vspeed, 0);
				timer = 0;
			}

			cachedPos = cachedPos.add(moveVec);
			target.updatePositionAndAngles(cachedPos.x, cachedPos.y, cachedPos.z, mc.player.getYaw(), mc.player.getPitch());
			
			if (target != mc.player) {
				mc.player.networkHandler.sendPacket(new VehicleMoveC2SPacket(target));
			} 
			
			else {
				mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(cachedPos.x, cachedPos.y, cachedPos.z, false));
				mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(cachedPos.x, cachedPos.y - 0.01, cachedPos.z, true));
			}
		} 
		
		else if (mode.getValue() == Mode.PHASE) {
			
			if (timer > fall.getValue()) {
				moveVec = new Vec3d(0, -vspeed, 0);
				timer = 0;
			}

			mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
					mc.player.getX() + moveVec.x, mc.player.getY() + moveVec.y, mc.player.getZ() + moveVec.z, false));

			mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
					mc.player.getX() + moveVec.x, mc.player.getY() - 420.69, mc.player.getZ() + moveVec.z, true));
		}
	}
	
	@Override
	public void onDisable() {
		if (mode.getValue() == Mode.VANILLA) {
			mc.player.getAbilities().allowFlying = false;
			mc.player.getAbilities().setFlySpeed(originalFlySpeed);
		}
	}
	
	private enum Mode {
		VANILLA,
		PACKET,
		PHASE;
	}
}