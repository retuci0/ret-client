package me.retucio.retclient.features.modules.movement;

import me.retucio.retclient.features.settings.Setting;
import me.retucio.retclient.util.PlayerUtil;
import me.retucio.retclient.util.interfaces.IVec3d;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.util.math.Vec3d;

import com.google.common.eventbus.Subscribe;

import me.retucio.retclient.event.events.BoatMoveEvent;
import me.retucio.retclient.event.events.PacketEvent;
import me.retucio.retclient.features.modules.Module;

public class BoatFly extends Module {

    private final Setting<Float> speed = register(new Setting<>("Speed", 10f, 0f, 50f));
    private final Setting<Float> vSpeed = register(new Setting<>("Vertical Speed", 6f, 0f, 20f));
    private final Setting<Float> fallSpeed = register(new Setting<>("Fall Speed", 0.1f, 0f, 1f));
    private final Setting<Boolean> cancelS2CPackets = register(new Setting<>("Cancel S2C packets", false));

    public BoatFly() {
        super("BoatFly", "Turns your boat into an airplane", Category.MOVEMENT, true, true, false);
    }

    @Subscribe
    private void onBoatMove(BoatMoveEvent event) {
        if (event.getBoat().getControllingPassenger() != mc.player) return;
        event.getBoat().setYaw(mc.player.getYaw());

        Vec3d vel = PlayerUtil.getHorizontalVelocity(speed.getValue());
        double velX = vel.getX();
        double velY = 0;
        double velZ = vel.getZ();

        if (mc.options.jumpKey.isPressed()) velY += vSpeed.getValue() / 20;
        if (mc.options.sprintKey.isPressed()) velY -= vSpeed.getValue() / 20;
        else velY -= fallSpeed.getValue() / 20;

        ((IVec3d) event.getBoat().getVelocity()).set(velX, velY, velZ);
    }

    @Subscribe
    private void onReceivePacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof VehicleMoveS2CPacket && cancelS2CPackets.getValue()) {
            event.cancel();
        }
    }
}