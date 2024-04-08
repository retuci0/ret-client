package net.retclient.gui.tabs;

import net.retclient.Main;
import net.retclient.event.events.LeftMouseDownEvent;
import net.retclient.event.listeners.LeftMouseDownListener;
import net.retclient.event.listeners.MouseMoveListener;
import net.retclient.gui.AbstractGui;
import net.retclient.gui.Color;
import net.retclient.gui.GuiManager;
import net.retclient.gui.tabs.components.Component;
import net.retclient.misc.RenderUtils;
import net.retclient.settings.SettingManager;
import net.retclient.settings.types.BooleanSetting;
import net.retclient.utils.types.Vector2;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ClickGuiTab extends AbstractGui implements LeftMouseDownListener, MouseMoveListener {
	protected String title;

	protected boolean pinnable = true;
	protected boolean drawBorder = true;

	private BooleanSetting isPinned;
	private Identifier icon = null;
	
	public ClickGuiTab(String title, int x, int y, boolean pinnable) {
		super(title + "_tab", x, y, 180, 0);
		this.title = title;

		this.pinnable = pinnable;

		isPinned = new BooleanSetting(title + "_pinned", "IS PINNED", false);
		SettingManager.registerSetting(isPinned, Main.getInstance().settingManager.hidden_category);
	}
	
	public ClickGuiTab(String title, int x, int y, boolean pinnable, String iconName) {
		super(title + "_tab", x, y, 180, 0);
		this.title = title;

		this.pinnable = pinnable;

		isPinned = new BooleanSetting(title + "_pinned", "IS PINNED", false);
		SettingManager.registerSetting(isPinned, Main.getInstance().settingManager.hidden_category);
		icon = new Identifier("ret", "/textures/" + iconName.trim().toLowerCase() + ".png");
	}

	public final String getTitle() {
		return title;
	}

	public final boolean isPinned() {
		return isPinned.getValue();
	}

	public final void setPinned(boolean pin) {
		this.isPinned.setValue(pin);
	}

	public final void setTitle(String title) {

		this.title = title;
	}

	public final boolean isGrabbed() {
		return (GuiManager.currentGrabbed == this);
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
		
		
		if (drawBorder) {
			// Draws background depending on components width and height
			RenderUtils.drawRoundedBox(matrixStack, pos.x, pos.y, width, height + 30, 6, GuiManager.backgroundColor.getValue());
			RenderUtils.drawRoundedOutline(matrixStack, pos.x, pos.y, width, height + 30, 6, GuiManager.borderColor.getValue());
			
			if(icon != null) {
				RenderUtils.drawTexturedQuad(drawContext, icon, pos.x + 8, pos.y + 4, 22, 22, GuiManager.foregroundColor.getValue());
				RenderUtils.drawString(drawContext, this.title, pos.x + 38, pos.y + 8, GuiManager.foregroundColor.getValue());
			}else
				RenderUtils.drawString(drawContext, this.title, pos.x + 8, pos.y + 8, GuiManager.foregroundColor.getValue());
			
			RenderUtils.drawLine(matrixStack, pos.x, pos.y + 30, pos.x + width, pos.y + 30, new Color(0, 0, 0, 100));

			if (this.pinnable) {
				if (this.isPinned.getValue()) {
					RenderUtils.drawRoundedBox(matrixStack, pos.x + width - 23, pos.y + 8, 15, 15, 6f, new Color(154, 0, 0, 200));
					RenderUtils.drawRoundedOutline(matrixStack, pos.x + width - 23, pos.y + 8, 15, 15, 6f, new Color(0, 0, 0, 200));
				} else {
					RenderUtils.drawRoundedBox(matrixStack, pos.x + width - 23, pos.y + 8, 15, 15, 6f, new Color(128, 128, 128, 50));
					RenderUtils.drawRoundedOutline(matrixStack, pos.x + width - 23, pos.y + 8, 15, 15, 6f, new Color(0, 0, 0, 50));
				}
			}
		}
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
			// Allow the user to move the clickgui if it within the header bar and NOT pinned.
			if(!isPinned.getValue()) {
				if(mouseX >= pos.x && mouseX <= pos.x + width) {
					if(mouseY >= pos.y && mouseY <= pos.y + 24) {
						lastClickOffsetX = mouseX - pos.x;
						lastClickOffsetY = mouseY - pos.y;
						GuiManager.currentGrabbed = this;
					}
				}
			}
			
			// If the GUI is pinnable, allow the user to click the pin button to pin a gui
			if (pinnable) {
				if (mouseX >= (pos.x + width - 24) && mouseX <= (pos.x + width - 2)) {
					if (mouseY >= (pos.y + 4) && mouseY <= (pos.y + 20)) {
						GuiManager.currentGrabbed = null;
						isPinned.silentSetValue(!isPinned.getValue());
					}
				}
			}
		}
	}
}
