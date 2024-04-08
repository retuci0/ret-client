package net.retclient.gui.tabs;

import net.retclient.Main;
import net.retclient.event.events.LeftMouseDownEvent;
import net.retclient.event.listeners.LeftMouseDownListener;
import net.retclient.event.listeners.MouseMoveListener;
import net.retclient.gui.AbstractGui;
import net.retclient.gui.Color;
import net.retclient.gui.GuiManager;
import net.retclient.gui.tabs.components.BlocksComponent;
import net.retclient.gui.tabs.components.CheckboxComponent;
import net.retclient.gui.tabs.components.ColorPickerComponent;
import net.retclient.gui.tabs.components.Component;
import net.retclient.gui.tabs.components.KeybindComponent;
import net.retclient.gui.tabs.components.ListComponent;
import net.retclient.gui.tabs.components.SliderComponent;
import net.retclient.gui.tabs.components.StackPanelComponent;
import net.retclient.misc.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.retclient.module.Module;
import net.retclient.settings.Setting;
import net.retclient.settings.types.BlocksSetting;
import net.retclient.settings.types.BooleanSetting;
import net.retclient.settings.types.ColorSetting;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.IndexedStringListSetting;
import net.retclient.settings.types.StringListSetting;
import net.retclient.utils.types.Vector2;

public class ModuleSettingsTab extends AbstractGui implements LeftMouseDownListener, MouseMoveListener {
	protected String title;
	protected Module module;

	public ModuleSettingsTab(String title, float x, float y, Module module) {
		super(title + "_tab", x, y, 180, 0);
		this.title = title + " Settings";
		this.module = module;
		this.setWidth(260);

		StackPanelComponent stackPanel = new StackPanelComponent(this);
		stackPanel.setTop(30);
		
		KeybindComponent keybindComponent = new KeybindComponent(stackPanel, module.getBind());
		keybindComponent.setHeight(30);
		stackPanel.addChild(keybindComponent);
		
		for (Setting<?> setting : this.module.getSettings()) {
			Component c;
			if (setting instanceof FloatSetting) {
				c = new SliderComponent(stackPanel, (FloatSetting) setting);
			} else if (setting instanceof BooleanSetting) {
				c = new CheckboxComponent(stackPanel, (BooleanSetting) setting);
			//}else if (setting instanceof StringListSetting) {
			//c = new ListComponent(stackPanel, (IndexedStringListSetting) setting);
			} else if (setting instanceof ColorSetting) {
				c = new ColorPickerComponent(stackPanel, (ColorSetting) setting);
			} else if (setting instanceof BlocksSetting) {
				c = new BlocksComponent(stackPanel, (BlocksSetting)setting);
			} else {
				c = null;
			}
			
			if(c != null) {
				stackPanel.addChild(c);
			}
		}
		
		this.addChild(stackPanel);
	}

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {

		this.title = title;
	}

	public final void addChild(Component component) {
		this.children.add(component);
	}

	@Override
	public void update() {
		if (this.inheritHeightFromChildren) {
			float tempHeight = 0;
			for (Component child : children) {
				tempHeight += (child.getHeight());
			}
			this.setHeight(tempHeight);
		}
		
		if (Main.getInstance().hudManager.isClickGuiOpen()) {
			for (Component child : this.children) {
				child.update();
			}
		}
	}

	public void preupdate() {
	}

	public void postupdate() {
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		MatrixStack matrixStack = drawContext.getMatrices();

		Vector2 pos = position.getValue();

		// Draws background depending on components width and height
		GuiManager hudManager = Main.getInstance().hudManager;
		RenderUtils.drawRoundedBox(matrixStack, pos.x, pos.y, width, height + 30, 6, GuiManager.backgroundColor.getValue());
		RenderUtils.drawRoundedOutline(matrixStack, pos.x, pos.y, width, height + 30, 6, GuiManager.borderColor.getValue());
		
		RenderUtils.drawString(drawContext, this.title, pos.x + 8, pos.y + 8, GuiManager.foregroundColor.getValue());
		RenderUtils.drawLine(matrixStack, pos.x, pos.y + 30, pos.x + width, pos.y + 30, new Color(0, 0, 0, 100));

		RenderUtils.drawLine(matrixStack, pos.x + width - 23, pos.y + 8, pos.x + width - 8, pos.y + 23, new Color(255, 0, 0, 255));
		RenderUtils.drawLine(matrixStack, pos.x + width - 23, pos.y + 23, pos.x + width - 8, pos.y + 8, new Color(255, 0, 0, 255));
		
		for (Component child : children) {
			child.draw(drawContext, partialTicks);
		}
	}

	@Override
	public void OnLeftMouseDown(LeftMouseDownEvent event) {
		double mouseX = mc.mouse.getX();
		double mouseY = mc.mouse.getY();
		Vector2 pos = position.getValue();

		if (Main.getInstance().hudManager.isClickGuiOpen()) {
			if (mouseX >= pos.x && mouseX <= pos.x + width) {
				if (mouseY >= pos.y && mouseY <= pos.y + 24) {
					this.lastClickOffsetX = mouseX - pos.x;
					this.lastClickOffsetY = mouseY - pos.y;
					GuiManager.currentGrabbed = this;
				}
			}

			if (mouseX >= (pos.x + width - 24) && mouseX <= (pos.x + width - 2)) {
				if (mouseY >= (pos.y + 4) && mouseY <= (pos.y + 20)) {
					GuiManager.currentGrabbed = null;
					Main.getInstance().hudManager.RemoveHud(this, "Modules");
				}
			}
		}
	}
}
