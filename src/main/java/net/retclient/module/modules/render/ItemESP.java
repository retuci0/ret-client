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
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;

public class ItemESP extends Module implements RenderListener {

	private ColorSetting color = new ColorSetting("itemesp_color", "Color", "Color", new Color(0, 1f, 1f));
	
	
	public ItemESP() {
		super(new KeybindSetting("key.itemesp", "ItemESP Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("ItemESP");
		this.setCategory(Category.Render);
		this.setDescription("Allows the player to see items with an ESP.");
		
		this.addSetting(color);
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
		for (Entity entity : MC.world.getEntities()) {
			if(entity instanceof ItemEntity) {
				RenderUtils.draw3DBox(event.GetMatrixStack(), entity.getBoundingBox(), color.getValue());
			}
		}
	}

}