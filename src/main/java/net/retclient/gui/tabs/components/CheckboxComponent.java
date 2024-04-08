package net.retclient.gui.tabs.components;

import net.retclient.Main;
import net.retclient.event.events.LeftMouseDownEvent;
import net.retclient.event.listeners.LeftMouseDownListener;
import net.retclient.gui.Color;
import net.retclient.gui.GuiManager;
import net.retclient.gui.IGuiElement;
import net.retclient.misc.RenderUtils;
import net.retclient.settings.types.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class CheckboxComponent extends Component implements LeftMouseDownListener {
	private String text;
	private BooleanSetting checkbox;
	private Runnable onClick;
	
	public CheckboxComponent(IGuiElement parent, BooleanSetting checkbox) {
		super(parent);
		this.text = checkbox.displayName;
		this.checkbox = checkbox;

		this.setLeft(2);
		this.setRight(2);
		this.setHeight(30);
		
		Main.getInstance().eventManager.AddListener(LeftMouseDownListener.class, this);
	}

	/**
	 * Draws the checkbox to the screen.
	 * @param offset The offset (Y location relative to parent) of the Component.
	 * @param drawContext The current draw context of the game.
	 * @param partialTicks The partial ticks used for interpolation.
	 * @param color The current Color of the UI.
	 */
	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		super.draw(drawContext, partialTicks);
		
		MatrixStack matrixStack = drawContext.getMatrices();
		RenderUtils.drawString(drawContext, this.text, actualX + 6, actualY + 8, 0xFFFFFF);
		if (this.checkbox.getValue()) {
			RenderUtils.drawOutlinedBox(matrixStack, actualX + actualWidth - 24, actualY + 5, 20, 20,
					new Color(0, 154, 0, 200));
		} else {
			RenderUtils.drawOutlinedBox(matrixStack, actualX + actualWidth - 24, actualY + 5, 20, 20,
					new Color(154, 0, 0, 200));
		}
	}

	/**
	 * Handles updating the Checkbox component.
	 * @param offset The offset (Y position relative to parent) of the Checkbox.
	 */
	@Override
	public void update() {
		super.update();
	}

	/**
	 * Triggered when the user clicks the Left Mouse Button (LMB)
	 * 
	 * @param event Event fired.
	 */
	@Override
	public void OnLeftMouseDown(LeftMouseDownEvent event) {
		if (hovered && Main.getInstance().hudManager.isClickGuiOpen()) {
			checkbox.toggle();
			if(onClick != null) {
				onClick.run();
			}
		}
	}
}
