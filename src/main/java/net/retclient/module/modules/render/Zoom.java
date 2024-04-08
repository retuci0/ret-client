package net.retclient.module.modules.render;

import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.TickListener;
import net.retclient.module.Module;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;

public class Zoom extends Module implements TickListener {

	private int lastFov;
	
	private FloatSetting zoomFactor;
	
	public Zoom() {
		super(new KeybindSetting("key.zoom", "Zoom Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("Zoom");
		this.setCategory(Category.Render);
		this.setDescription("Zooms the players camera to see further.");
		
		zoomFactor = new FloatSetting("zoom_factor", "Factor", "The zoom factor that the zoom will use.", 2.0f, 1.0f, 3.6f, 0.1f);
		
		this.addSetting(zoomFactor);
	}

	@Override
	public void onDisable() {
		MC.options.getFov().setValue(lastFov);
		Main.getInstance().eventManager.RemoveListener(TickListener.class, this);
	}

	@Override
	public void onEnable() {
		lastFov = MC.options.getFov().getValue();
		Main.getInstance().eventManager.AddListener(TickListener.class, this);
	}

	@Override
	public void onToggle() {

	}

	@Override
	public void OnUpdate(TickEvent event) {
		SimpleOption<Integer> fov = MC.options.getFov();
		int newZoom = (int)(lastFov / zoomFactor.getValue());
		fov.setValue(newZoom);
	}
}