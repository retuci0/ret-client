package net.retclient.module.modules.render;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.RenderEvent;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.RenderListener;
import net.retclient.event.listeners.TickListener;
import net.retclient.gui.Color;
import net.retclient.misc.RenderUtils;
import net.retclient.module.Module;
import net.retclient.settings.types.BooleanSetting;
import net.retclient.settings.types.ColorSetting;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class EntityESP extends Module implements RenderListener, TickListener {
	private ColorSetting color_passive = new ColorSetting("entityesp_color_passive", "Passive Color",  "Passive Color", new Color(0, 1f, 1f));
	private ColorSetting color_enemies = new ColorSetting("entityesp_color_enemy", "Enemy Color", "Enemy Color", new Color(0, 1f, 1f));
	private ColorSetting color_misc = new ColorSetting("entityesp_color_misc", "Misc. Color", "Misc. Color", new Color(0, 1f, 1f));
	
	public BooleanSetting rainbow = new BooleanSetting("entityesp_rainbow", "Rainbow","Rainbow", false);
	public FloatSetting effectSpeed = new FloatSetting("entityesp_effectspeed", "Effect Speed", "Effect Speed", 4f, 1f, 20f, 0.1f);
	
	public EntityESP() {
		super(new KeybindSetting("key.entityesp", "EntityESP Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("EntityESP");
		this.setCategory(Category.Render);
		this.setDescription("Allows the player to see entities with an ESP.");
		
		this.addSetting(color_passive);
		this.addSetting(color_enemies);
		this.addSetting(color_misc);
		
		this.addSetting(rainbow);
		this.addSetting(effectSpeed);
	}

	@Override
	public void onDisable() {
		Main.getInstance().eventManager.RemoveListener(RenderListener.class, this);
		Main.getInstance().eventManager.RemoveListener(TickListener.class, this);
	}

	@Override
	public void onEnable() {
		Main.getInstance().eventManager.AddListener(RenderListener.class, this);
		Main.getInstance().eventManager.AddListener(TickListener.class, this);
	}

	@Override
	public void onToggle() {

	}

	@Override
	public void OnRender(RenderEvent event) {
		MatrixStack matrixStack = event.GetMatrixStack();
		float partialTicks = event.GetPartialTicks();
		
		matrixStack.push();
		
		for (Entity entity : MC.world.getEntities()) {
			if (entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
				
				Box boundingBox = entity.getBoundingBox(); 
				Vec3d offset = RenderUtils.getEntityPositionOffsetInterpolated(entity, partialTicks);
				boundingBox = boundingBox.offset(offset);
				
				if (entity instanceof AnimalEntity) {
					RenderUtils.draw3DBox(matrixStack, boundingBox, color_passive.getValue());
				} else if (entity instanceof Monster) {
					RenderUtils.draw3DBox(matrixStack, boundingBox, color_enemies.getValue());
				} else {
					RenderUtils.draw3DBox(matrixStack, boundingBox, color_misc.getValue());
				}
			}
		}
		matrixStack.pop();
	}

	@Override
	public void OnUpdate(TickEvent event) {
		/*
		 * if(this.rainbow.getValue()) {
		 * this.rainbowColor.update(this.effectSpeed.getValue().floatValue()); }else {
		 * 
		 * this.color.setHSV(hue.getValue().floatValue(), 1f, 1f); }
		 */
	}
}