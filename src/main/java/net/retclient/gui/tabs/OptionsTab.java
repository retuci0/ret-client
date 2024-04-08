package net.retclient.gui.tabs;

import java.util.ArrayList;
import net.retclient.Main;
import net.retclient.event.events.MouseScrollEvent;
import net.retclient.event.listeners.MouseScrollListener;
import net.retclient.gui.AbstractGui;
import net.retclient.gui.Color;
import net.retclient.gui.GuiManager;
import net.retclient.misc.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.retclient.module.Module;
import net.retclient.utils.types.Vector2;

public class OptionsTab extends AbstractGui implements MouseScrollListener {

	int visibleScrollElements;
	int currentScroll;

	public OptionsTab() {
		super("Options", 40, 220, 100, 100);
		Main.getInstance().eventManager.AddListener(MouseScrollListener.class, this);
	}

	@Override
	public void update() {
		Window window = mc.getWindow();
		this.setWidth(window.getWidth() - 240);
		this.setHeight(window.getHeight() - 240);

		visibleScrollElements = (int) ((this.height - 30) / 30);
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		MatrixStack matrixStack = drawContext.getMatrices();

		Vector2 pos = position.getValue();

		RenderUtils.drawRoundedBox(matrixStack, pos.x, pos.y, width, height, 6, GuiManager.backgroundColor.getValue());
		RenderUtils.drawRoundedOutline(matrixStack, pos.x, pos.y, width, height, 6, GuiManager.borderColor.getValue());
		RenderUtils.drawLine(matrixStack, pos.x + 480, pos.y, pos.x + 480, pos.y + height, new Color(0, 0, 0, 200));

		ArrayList<Module> modules = Main.getInstance().moduleManager.modules;

		int yHeight = 30;
		for (int i = currentScroll; i < Math.min(modules.size(), currentScroll + visibleScrollElements); i++) {
			Module module = modules.get(i);
			RenderUtils.drawString(drawContext, module.getName(), pos.x + 10, pos.y + yHeight, GuiManager.foregroundColor.getValue());
			yHeight += 30;
		}
	}

	@Override
	public void OnMouseScroll(MouseScrollEvent event) {
		 ArrayList<Module> modules = Main.getInstance().moduleManager.modules;
		 
		 if(event.GetVertical() > 0) 
			 this.currentScroll = Math.min(currentScroll + 1, modules.size() - visibleScrollElements - 1); 
		 else if(event.GetVertical() < 0) 
			 this.currentScroll = Math.max(currentScroll - 1, 0);
	}
}
