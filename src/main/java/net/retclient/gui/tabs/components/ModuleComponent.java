package net.retclient.gui.tabs.components;

import net.retclient.Main;
import net.retclient.event.events.LeftMouseDownEvent;
import net.retclient.event.listeners.LeftMouseDownListener;
import net.retclient.module.Module;
import net.retclient.gui.Color;
import net.retclient.gui.GuiManager;
import net.retclient.gui.IGuiElement;
import net.retclient.gui.tabs.ModuleSettingsTab;
import net.retclient.misc.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ModuleComponent extends Component implements LeftMouseDownListener {
	private String text;
	private Module module;

	private ModuleSettingsTab lastSettingsTab = null;
	
	public final Identifier gear;
	
	public ModuleComponent(String text, IGuiElement parent, Module module) {
		super(parent);
		
		gear = new Identifier("aoba", "/textures/gear.png");
		this.text = text;
		this.module = module;
		
		this.setLeft(2);
		this.setRight(2);
		this.setHeight(30);
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		super.draw(drawContext, partialTicks);
		RenderUtils.drawString(drawContext, this.text, actualX + 8, actualY + 8, module.getState() ? 0x00FF00 : this.hovered ? GuiManager.foregroundColor.getValue().getColorAsInt() : 0xFFFFFF);
		if(module.hasSettings()) {
			Color hudColor = GuiManager.foregroundColor.getValue();
			RenderUtils.drawTexturedQuad(drawContext, gear, (actualX + actualWidth - 20), (actualY + 6), 16, 16, hudColor);
		}
	}
	
	@Override
	public void OnLeftMouseDown(LeftMouseDownEvent event) {
		double mouseX = event.GetMouseX();
		if (hovered && Main.getInstance().hudManager.isClickGuiOpen()) {
				boolean isOnOptionsButton = (mouseX >= (actualX + actualWidth - 34) && mouseX <= (actualX + actualWidth));
				if (isOnOptionsButton) {
					if(lastSettingsTab == null) {
						lastSettingsTab = new ModuleSettingsTab(this.module.getName(), this.actualX + this.actualWidth + 1, this.actualY, this.module);
						lastSettingsTab.setVisible(true);
						Main.getInstance().hudManager.AddHud(lastSettingsTab, "Modules");
					}else {
						Main.getInstance().hudManager.RemoveHud(lastSettingsTab, "Modules");
						lastSettingsTab = null;
					}
				} else {
					module.toggle();
					return;
				}
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