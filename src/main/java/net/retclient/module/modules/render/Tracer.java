package net.retclient.module.modules.render;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.RenderEvent;
import net.retclient.event.listeners.RenderListener;
import net.retclient.gui.Color;
import net.retclient.misc.RenderUtils;
import net.retclient.module.Module;
import net.retclient.settings.types.ColorSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.Vec3d;

public class Tracer extends Module implements RenderListener {
	private ColorSetting color_player = new ColorSetting("tracer_color_player", "Player Color",  "Player Color", new Color(1f, 1f, 0f));
	private ColorSetting color_passive = new ColorSetting("tracer_color_passive", "Passive Color",  "Passive Color", new Color(0, 1f, 1f));
	private ColorSetting color_enemies = new ColorSetting("tracer_color_enemy", "Enemy Color", "Enemy Color", new Color(0, 1f, 1f));
	private ColorSetting color_misc = new ColorSetting("tracer_color_misc", "Misc. Color", "Misc. Color", new Color(0, 1f, 1f));
	
	public Tracer() {
		super(new KeybindSetting("key.tracer", "Tracer Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("Tracer");
		this.setCategory(Category.Render);
		this.setDescription("Points toward other players and entities with a line.");
		
		this.addSetting(color_player);
		this.addSetting(color_passive);
		this.addSetting(color_enemies);
		this.addSetting(color_misc);
	}

	@Override
	public void onDisable() {
		Main.getInstance().eventManager.RemoveListener(RenderListener.class, this);
	}

	@Override
	public void onEnable() {
		Main.getInstance().eventManager.AddListener(RenderListener.class, this);
	}

	@Override
	public void onToggle() {

	}
	
	@Override
	public void OnRender(RenderEvent event) {
		Vec3d eyePosition = new Vec3d(0, 0, 1);
		Camera camera = MC.gameRenderer.getCamera();
		Vec3d offset = RenderUtils.getEntityPositionOffsetInterpolated(MC.cameraEntity, event.GetPartialTicks());
		eyePosition = eyePosition.rotateX((float) -Math.toRadians(camera.getPitch()));
		eyePosition = eyePosition.rotateY((float) -Math.toRadians(camera.getYaw()));
		eyePosition = eyePosition.add(MC.cameraEntity.getEyePos());
		eyePosition = eyePosition.subtract(offset);
		for (Entity entity : MC.world.getEntities()) {
			if(entity instanceof LivingEntity && (entity != MC.player)) {
				Vec3d interpolated = RenderUtils.getEntityPositionInterpolated(entity, MinecraftClient.getInstance().getTickDelta());
				if (entity instanceof AnimalEntity) {
					RenderUtils.drawLine3D(event.GetMatrixStack(), eyePosition, interpolated, color_passive.getValue());
				} else if (entity instanceof Monster) {
					RenderUtils.drawLine3D(event.GetMatrixStack(), eyePosition, interpolated, color_enemies.getValue());
				} else {
					RenderUtils.drawLine3D(event.GetMatrixStack(), eyePosition, interpolated, color_misc.getValue());
				}
			}
		}
		
		for(AbstractClientPlayerEntity player : MC.world.getPlayers()) {
			Vec3d interpolated = RenderUtils.getEntityPositionInterpolated(player, MinecraftClient.getInstance().getTickDelta());
			RenderUtils.drawLine3D(event.GetMatrixStack(), eyePosition, interpolated, color_player.getValue());
		}
	}
}