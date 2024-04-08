package net.retclient.gui.tabs.components;

import java.util.List;
import net.retclient.Main;
import net.retclient.event.events.LeftMouseDownEvent;
import net.retclient.event.listeners.LeftMouseDownListener;
import net.retclient.gui.Color;
import net.retclient.gui.IGuiElement;
import net.retclient.misc.RenderUtils;
import net.retclient.settings.types.StringSetting;
import net.minecraft.client.gui.DrawContext;

public class ListComponent extends Component implements LeftMouseDownListener {
	private StringSetting listSetting;

	private List<String> options;
	private int selectedIndex;

	public ListComponent(IGuiElement parent, List<String> options) {
		super(parent);
		this.setLeft(2);
		this.setRight(2);
		this.setHeight(30);
		Main.getInstance().eventManager.AddListener(LeftMouseDownListener.class, this);

		this.options = options;
	}

	public ListComponent(IGuiElement parent, List<String> options, StringSetting listSetting) {
		super(parent);
		this.listSetting = listSetting;

		this.setLeft(2);
		this.setRight(2);
		this.setHeight(30);

		Main.getInstance().eventManager.AddListener(LeftMouseDownListener.class, this);

		this.options = options;
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		float stringWidth = Main.getInstance().fontManager.GetRenderer().getWidth(listSetting.getValue());
		RenderUtils.drawString(drawContext, listSetting.getValue(), actualX + (actualWidth / 2.0f) - stringWidth,
				actualY + 8, 0xFFFFFF);
		RenderUtils.drawString(drawContext, "<<", actualX + 8, actualY + 4, 0xFFFFFF);
		RenderUtils.drawString(drawContext, ">>", actualX + 8 + (actualWidth - 34), actualY + 4, 0xFFFFFF);
	}

	@Override
	public void OnLeftMouseDown(LeftMouseDownEvent event) {
		double mouseX = event.GetMouseX();

		// Mouse is on the left
		if(this.hovered) {			
			if (mouseX > actualX && mouseX < (actualX + 32)) {
				setSelectedIndex(Math.max(--selectedIndex, 0));
			} else if (mouseX > (actualX + actualWidth - 32) && mouseX < (actualX + actualWidth)) {
				setSelectedIndex(Math.min(++selectedIndex, options.size() - 1));
			}
		}
	}

	public void setSelectedIndex(int index) {
		selectedIndex = index;
		listSetting.setValue(options.get(selectedIndex));
	}
}
