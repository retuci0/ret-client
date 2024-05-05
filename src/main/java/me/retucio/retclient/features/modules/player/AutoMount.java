package me.retucio.retclient.features.modules.player;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.common.eventbus.Subscribe;

import me.retucio.retclient.event.Stage;
import me.retucio.retclient.event.events.UpdateWalkingPlayerEvent;
import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import me.retucio.retclient.util.EntityUtil;
import me.retucio.retclient.util.PlayerUtil;
import me.retucio.retclient.util.models.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;

public class AutoMount extends Module {

	private Timer timer = new Timer();
    public final Setting<Boolean> boats = register(new Setting<>("Boats", true));
    public final Setting<Boolean> horses = register(new Setting<>("Horses", true));
    public final Setting<Boolean> skeletonHorses = register(new Setting<>("SkeletonHorses", true));
    public final Setting<Boolean> donkeys = register(new Setting<>("Donkeys", true));
    public final Setting<Boolean> pigs = register(new Setting<>("Pigs", true));
    public final Setting<Boolean> llamas = register(new Setting<>("Llamas", true));
    public final Setting<Boolean> striders = register(new Setting<>("Striders", true));
    public final Setting<Integer> range = register(new Setting<>("Range", 4, 0, 10));
    public final Setting<Float> delay = register(new Setting<>("Delay", 1.0f, 0.0f, 10.0f));
	
	public AutoMount() {
		super("AutoMount", "Automatically mounts ridable entities", Category.PLAYER, true, false, false);
	}
	
	@Subscribe
	public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
		if (event.getStage() == Stage.PRE) return;
		
		if (mc.player.isRiding()) return;
		
		if (!timer.passedMs(delay.getValue().longValue() * 1000)) return;
		
		timer.reset();
		
		ArrayList<Entity> entities = new ArrayList<>();
        mc.world.getEntities().forEach(entities::add);
        
        Entity entity = entities.stream()
                .filter(playerEntity -> isValidEntity(playerEntity))
                .min(Comparator.comparing(playerEntity -> mc.player.squaredDistanceTo(playerEntity)))
                .orElse(null);
        
        if (entity != null) {
            event.cancel();

            final double entityPos[] = EntityUtil.calculateLookAt(
                    entity.getX(),
                    entity.getY(),
                    entity.getZ(),
                    mc.player);

            PlayerUtil.packetFacePitchAndYaw((float) entityPos[0], (float) entityPos[1]);

            mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.interact(entity, mc.player.isSneaking(), Hand.MAIN_HAND));
            mc.player.swingHand(Hand.MAIN_HAND);
        }
	}
	
    private boolean isValidEntity(Entity entity) {
        if (entity.distanceTo(mc.player) > range.getValue())
            return false;

        if (entity instanceof AbstractHorseEntity horseEntity) {
            if (horseEntity.isBaby())
                return false;
        }

        if (entity instanceof BoatEntity && boats.getValue()) return true;
        if (entity instanceof SkeletonHorseEntity && skeletonHorses.getValue()) return true;
        if (entity instanceof HorseEntity && horses.getValue()) return true;
        if (entity instanceof DonkeyEntity && donkeys.getValue()) return true;

        if (entity instanceof PigEntity pig && pigs.getValue()) {

            if (pig.isSaddled())
                return true;

            return false;
        }

        if (entity instanceof LlamaEntity llama && llamas.getValue()) {
            if (!llama.isBaby())
                return true;
        }

        if (entity instanceof StriderEntity strider && striders.getValue()) {
            if (!strider.isBaby() && strider.isSaddled())
                return true;
        }

        return false;
    }
}