package me.retucio.retclient.features.modules.combat;

import com.google.common.eventbus.Subscribe;

import me.retucio.retclient.event.events.PacketEvent;
import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import me.retucio.retclient.util.models.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Criticals extends Module {
	
    private final Timer timer = new Timer();
    private final Setting<Boolean> forceParticles = register(new Setting<>("Force particles", true));
    
    public Criticals() {
        super("Criticals", "Makes every hit a crit", Category.COMBAT, true, false, false);
    }
    @Subscribe private void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket packet && packet.type.getType() == PlayerInteractEntityC2SPacket.InteractType.ATTACK) {
            Entity entity = mc.world.getEntityById(packet.entityId);
            if (entity == null
                    || entity instanceof EndCrystalEntity
                    || !mc.player.isOnGround()
                    || !(entity instanceof LivingEntity)
                    || !timer.passedMs(0)) 
            	return;

            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + (double) 0.1f, mc.player.getZ(), false));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
            
            if (forceParticles.getValue().booleanValue()) mc.player.addCritParticles(entity);
            
            timer.reset();
        }
    }

    @Override public String getDisplayInfo() {
        return "Packet";
    }
}
