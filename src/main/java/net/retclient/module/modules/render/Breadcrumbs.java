package net.retclient.module.modules.render;

import java.util.ArrayList;
import java.util.List;
import net.retclient.Main;
import net.retclient.event.events.RenderEvent;
import net.retclient.event.events.TickEvent;
import net.retclient.event.listeners.RenderListener;
import net.retclient.event.listeners.TickListener;
import org.lwjgl.glfw.GLFW;
import net.retclient.module.Module;
import net.retclient.settings.types.BooleanSetting;
import net.retclient.settings.types.ColorSetting;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Vec3d;
import net.retclient.gui.Color;
import net.retclient.misc.RainbowColor;
import net.retclient.misc.RenderUtils;

public class Breadcrumbs extends Module implements RenderListener, TickListener {
	private Color currentColor;
	
	private ColorSetting color = new ColorSetting("breadcrumbs_color", "Color",  "Color", new Color(0, 1f, 1f));
	
	private RainbowColor rainbowColor;

	public BooleanSetting rainbow = new BooleanSetting("breadcrumbs_rainbow", "Rainbow", "Rainbow", false);
	public FloatSetting effectSpeed = new FloatSetting("breadcrumbs_effectspeed", "Effect Spd.", "Effect Spd", 4f, 1f, 20f, 0.1f);
	
	private float timer = 10;
	private float currentTick = 0;
	private List<Vec3d> positions = new ArrayList<Vec3d>();
	
	public Breadcrumbs() {
		super(new KeybindSetting("key.breadcrumbs", "Breadcrumbs Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("Breadcrumbs");
		this.setCategory(Category.Render);
		this.setDescription("Shows breadcrumbs of where you last stepped;");
		currentColor = color.getValue();
		rainbowColor = new RainbowColor();
		
		this.addSetting(color);
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
		for(int i = 0; i < this.positions.size() - 1; i++) {
			RenderUtils.drawLine3D(event.GetMatrixStack(), this.positions.get(i), this.positions.get(i + 1), this.currentColor);
		}
	}

	@Override
	public void OnUpdate(TickEvent event) {
		currentTick++;
		if(timer == currentTick) {
			currentTick = 0;
			if(!Main.getInstance().moduleManager.freecam.getState()) {
				positions.add(MC.player.getPos());
			}
		}
		if(this.rainbow.getValue()) {
			this.rainbowColor.update(this.effectSpeed.getValue().floatValue());
			this.currentColor = this.rainbowColor.getColor();
		}else {
			this.currentColor = color.getValue();
		}
	}
}