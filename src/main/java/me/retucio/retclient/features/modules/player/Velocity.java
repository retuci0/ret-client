package me.retucio.retclient.features.modules.player;

import com.google.common.eventbus.Subscribe;

import me.retucio.retclient.event.events.PacketEvent;
import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import me.retucio.retclient.mixin.accessors.EntityVelocityUpdateS2CPacketAccessor;
import me.retucio.retclient.mixin.accessors.ExplosionS2CPacketAccessor;
import me.retucio.retclient.util.interfaces.IVec3d;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

public class Velocity extends Module {

	private final Setting<Double> horizontal = register(new Setting<>("Horizontal", 0d, 0d, 1d));
	private final Setting<Double> vertical = register(new Setting<>("Vertical", 0d, 0d, 1d));
	private final Setting<Boolean> sinking = register(new Setting<>("Sink", false));
	
	public Velocity() {
		super("Velocity", "Cancels knockback", Category.PLAYER, true, false, false);
	}
	
    @Override
    public void onTick() {
        if (sinking.getValue()) return;
        if (mc.options.jumpKey.isPressed() || mc.options.sneakKey.isPressed()) return;

        if ((mc.player.isTouchingWater() || mc.player.isInLava()) && mc.player.getVelocity().y < 0) {
            ((IVec3d) mc.player.getVelocity()).setY(0);
        }
    }

    @Subscribe
    private void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet && packet.getId() == mc.player.getId()) {
        	
            double velX = (packet.getVelocityX() / 8000d - mc.player.getVelocity().x) * horizontal.getValue();
            double velY = (packet.getVelocityY() / 8000d - mc.player.getVelocity().y) * vertical.getValue();
            double velZ = (packet.getVelocityZ() / 8000d - mc.player.getVelocity().z) * horizontal.getValue();
            
            ((EntityVelocityUpdateS2CPacketAccessor) packet).setX((int) (velX * 8000 + mc.player.getVelocity().x * 8000));
            ((EntityVelocityUpdateS2CPacketAccessor) packet).setY((int) (velY * 8000 + mc.player.getVelocity().y * 8000));
            ((EntityVelocityUpdateS2CPacketAccessor) packet).setZ((int) (velZ * 8000 + mc.player.getVelocity().z * 8000));
        } 
        
        else if (event.getPacket() instanceof ExplosionS2CPacket packet) {
        	ExplosionS2CPacketAccessor accessor = (ExplosionS2CPacketAccessor) event.getPacket();
        	
            accessor.setPlayerVelocityX((float) (packet.getPlayerVelocityX() * horizontal.getValue()));
            accessor.setPlayerVelocityY((float) (packet.getPlayerVelocityY() * vertical.getValue()));
            accessor.setPlayerVelocityZ((float) (packet.getPlayerVelocityZ() * horizontal.getValue()));
        }
    }
}