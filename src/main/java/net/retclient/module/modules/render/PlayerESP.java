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
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.InputUtil;

public class PlayerESP extends Module implements RenderListener {
	
	private ColorSetting color_default = new ColorSetting("playeresp_color_default", "Default Color",  "Default Color", new Color(1f, 1f, 0f));
	private ColorSetting color_friendly = new ColorSetting("playeresp_color_friendly", "Friendly Color",  "Friendly Color", new Color(0f, 1f, 0f));
	private ColorSetting color_enemy = new ColorSetting("playeresp_color_enemy", "Enemy Color", "Enemy Color", new Color(1f, 0f, 0f));
	
	public PlayerESP() {
		super(new KeybindSetting("key.playeresp", "PlayerESP Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("PlayerESP");
		this.setCategory(Category.Render);
		this.setDescription("Allows the player to see other players with an ESP.");
		
		this.addSetting(color_default);
		this.addSetting(color_friendly);
		this.addSetting(color_enemy);
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
		for (AbstractClientPlayerEntity entity : MC.world.getPlayers()) {
			if(entity != MC.player) {
				RenderUtils.draw3DBox(event.GetMatrixStack(), entity.getBoundingBox(), color_default.getValue());
			}
		}
	}
}
