package net.retclient.gui.tabs.components;

import net.retclient.Main;
import net.retclient.event.events.LeftMouseDownEvent;
import net.retclient.event.listeners.LeftMouseDownListener;
import net.retclient.gui.Color;
import net.retclient.gui.IGuiElement;
import net.retclient.gui.hud.AbstractHud;
import net.retclient.misc.RenderUtils;
import net.minecraft.client.gui.DrawContext;

public class HudComponent extends Component implements LeftMouseDownListener {
	private String text;
	private AbstractHud hud;

	public HudComponent(String text, IGuiElement parent, AbstractHud hud) {
		super(parent);
		this.text = text;
		this.hud = hud;
		
		this.setHeight(30);
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		super.draw(drawContext, partialTicks);
		RenderUtils.drawString(drawContext, this.text, actualX + 8, actualY + 8, 0xFFFFFF);
		
		if(this.hud.activated.getValue()) {
			RenderUtils.drawString(drawContext, "-", actualX + actualWidth - 16, actualY + 8, 0xFF0000);
		}else {
			RenderUtils.drawString(drawContext, "+", actualX + actualWidth - 16, actualY + 8, 0x00FF00);
		}
	}
	
	@Override
	public void OnLeftMouseDown(LeftMouseDownEvent event) {
		if(this.hovered && Main.getInstance().hudManager.isClickGuiOpen()) {
			boolean visibility = hud.activated.getValue();
			Main.getInstance().hudManager.SetHudActive(hud, !visibility);
		}
	}
	
	@Override
	public void OnVisibilityChanged() {
		if(this.isVisible()) {
			Main.getInstance().eventManager.AddListener(LeftMouseDownListener.class, this);
		}else {
			Main.getInstance().eventManager.RemoveListener(LeftMouseDownListener.class, this);
		}
	}
}