package net.retclient.gui.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.retclient.Main;
import net.retclient.event.events.MouseScrollEvent;
import net.retclient.event.listeners.MouseScrollListener;
import net.retclient.gui.Color;
import net.retclient.gui.GuiManager;
import net.retclient.gui.font.FontManager;
import net.retclient.gui.hud.AbstractHud;
import net.retclient.gui.tabs.components.ColorPickerComponent;
import net.retclient.gui.tabs.components.HudComponent;
import net.retclient.gui.tabs.components.KeybindComponent;
import net.retclient.gui.tabs.components.ListComponent;
import net.retclient.gui.tabs.components.StackPanelComponent;
import net.retclient.gui.tabs.components.StringComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.retclient.module.Module;
import net.retclient.settings.types.StringSetting;

public class HudsTab extends ClickGuiTab implements MouseScrollListener {

	int visibleScrollElements;
	int currentScroll;

	public HudsTab(AbstractHud[] abstractHuds) {
		super("Hud Options", 50, 50, false);

		Main.getInstance().eventManager.AddListener(MouseScrollListener.class, this);
		StackPanelComponent stackPanel = new StackPanelComponent(this);
		stackPanel.setTop(30);
		
		List<String> test = new ArrayList<String>();
		
		
		HashMap<String, TextRenderer> fontRenderers = Main.getInstance().fontManager.fontRenderers;
		Set<String> set = fontRenderers.keySet();
		
		for(String s : set) {
			test.add(s);
		}
		
		stackPanel.addChild(new StringComponent("Toggle HUD", stackPanel, GuiManager.foregroundColor.getValue(), true));
		
		for(AbstractHud hud : abstractHuds) {
			HudComponent hudComponent = new HudComponent(hud.getID(), stackPanel, hud);
			stackPanel.addChild(hudComponent);
		}
		
		// Keybinds Header
		stackPanel.addChild(new StringComponent("Keybinds", stackPanel, GuiManager.foregroundColor.getValue(), true));
		
		KeybindComponent clickGuiKeybindComponent = new KeybindComponent(stackPanel, Main.getInstance().hudManager.clickGuiButton);
		clickGuiKeybindComponent.setHeight(30);
		stackPanel.addChild(clickGuiKeybindComponent);

		// Hud Font Header
		stackPanel.addChild(new StringComponent("HUD Font", stackPanel, GuiManager.foregroundColor.getValue(), true));
		

		
		ListComponent listComponent = new ListComponent(stackPanel, test, Main.getInstance().fontManager.fontSetting);
		stackPanel.addChild(listComponent);
		
		stackPanel.addChild(new StringComponent("HUD Colors", stackPanel, GuiManager.foregroundColor.getValue(), true));
		
		stackPanel.addChild(new ColorPickerComponent(stackPanel, GuiManager.foregroundColor));
		stackPanel.addChild(new ColorPickerComponent(stackPanel, GuiManager.backgroundColor));
		stackPanel.addChild(new ColorPickerComponent(stackPanel, GuiManager.borderColor));
		
		this.children.add(stackPanel);
		this.setWidth(300);
	}
	
	@Override
	public void OnMouseScroll(MouseScrollEvent event) {
		 ArrayList<Module> modules = Main.getInstance().moduleManager.modules;
		 
		 if(event.GetVertical() > 0) 
			 this.currentScroll = Math.min(currentScroll + 1, modules.size() - visibleScrollElements - 1); 
		 else if(event.GetVertical() < 0) 
			 this.currentScroll = Math.max(currentScroll - 1, 0);
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		super.draw(drawContext, partialTicks);
	}
}
