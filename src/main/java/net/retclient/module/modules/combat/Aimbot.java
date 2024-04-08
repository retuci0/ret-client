package net.retclient.module.modules.combat;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.RenderEvent;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.RenderListener;
import net.retclient.event.listeners.TickListener;
import net.retclient.misc.RenderUtils;
import net.retclient.module.Module;
import net.retclient.settings.types.BooleanSetting;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.util.InputUtil;

public class Aimbot extends Module implements TickListener, RenderListener {

	private LivingEntity temp = null;

	private BooleanSetting targetAnimals;
	private BooleanSetting targetPlayers;
	private FloatSetting frequency;
	
	private int currentTick = 0;
	
	public Aimbot() {
		super(new KeybindSetting("key.aimbot", "Aimbot Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		this.setName("Aimbot");
		
		this.setCategory(Category.Combat);
		this.setDescription("Locks your crosshair towards a desire player or entity.");
		
		targetAnimals = new BooleanSetting("aimbot_target_mobs", "Target Mobs", "Target mobs.", false);
		targetPlayers = new BooleanSetting("aimbot_target_players", "Target Players", "Target players.", true);
		frequency = new FloatSetting("aimbot_frequency", "Ticks", "How frequent the aimbot updates (Lower = Laggier)", 1.0f, 1.0f, 20.0f, 1.0f);
		this.addSetting(targetAnimals);
		this.addSetting(targetPlayers);
		this.addSetting(frequency);
	}

	@Override
	public void onDisable() {
		Main.getInstance().eventManager.RemoveListener(TickListener.class, this);
		Main.getInstance().eventManager.RemoveListener(RenderListener.class, this);
	}

	@Override
	public void onEnable() {
		Main.getInstance().eventManager.AddListener(TickListener.class, this);
		Main.getInstance().eventManager.AddListener(RenderListener.class, this);
	}

	@Override
	public void onToggle() {

	}

	@Override
	public void OnRender(RenderEvent event) {
		if(temp != null) {
			Vec3d offset = RenderUtils.getEntityPositionOffsetInterpolated(temp, event.GetPartialTicks());
			MC.player.lookAt(EntityAnchor.EYES, temp.getEyePos().add(offset));
		}
	}
	
	@Override
	public void OnUpdate(TickEvent event) {
		currentTick++;
		if(currentTick >= frequency.getValue()) {
			if (this.targetPlayers.getValue()) {
				if (MC.world.getPlayers().size() == 2) {
					temp = MC.world.getPlayers().get(1);
				} else if (MC.world.getPlayers().size() > 2) {
					for (int x = 0; x < MC.world.getPlayers().size(); x++) {
						for (int y = 1; y < MC.world.getPlayers().size(); y++) {
							if (MC.world.getPlayers().get(x).distanceTo(MC.player) < MC.world.getPlayers().get(y)
									.distanceTo(MC.player)) {
								temp = MC.world.getPlayers().get(x);
							}
						}
					}
				}
			}
			if (this.targetAnimals.getValue()) {
				LivingEntity tempEntity = null;
				for (Entity entity : MC.world.getEntities()) {
					if (!(entity instanceof LivingEntity))
						continue;
					if (entity instanceof ClientPlayerEntity)
						continue;
					if (tempEntity == null) {
						tempEntity = (LivingEntity) entity;
					} else {
						if (entity.distanceTo(MC.player) < tempEntity.distanceTo(MC.player)) {
							tempEntity = (LivingEntity) entity;
						}
					}
				}
				temp = tempEntity;
			}
			
			currentTick = 0;
		}
	}
}