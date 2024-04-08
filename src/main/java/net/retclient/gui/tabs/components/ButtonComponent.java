package net.retclient.gui.tabs.components;

import net.retclient.Main;
import net.retclient.event.events.LeftMouseDownEvent;
import net.retclient.event.listeners.LeftMouseDownListener;
import net.retclient.gui.Color;
import net.retclient.gui.IGuiElement;
import net.retclient.misc.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class ButtonComponent extends Component implements LeftMouseDownListener {

	private String text;
	private Runnable onClick;
	private Color borderColor = new Color(128, 128, 128);
	private Color backgroundColor = borderColor;
	
	/**
	 * Constructor for button component.
	 * @param parent Parent Tab that this Component resides in.
	 * @param text Text contained in this button element.
	 * @param onClick OnClick delegate that will run when the button is pressed.
	 */
	public ButtonComponent(IGuiElement parent, String text, Runnable onClick) {
		super(parent);
		
		this.setLeft(2);
		this.setRight(2);
		this.setHeight(30);
		
		this.text = text;
		this.onClick = onClick;
	}
	
	public ButtonComponent(IGuiElement parent, String text, Runnable onClick, Color borderColor, Color backgroundColor) {
		super(parent);
		
		this.setLeft(2);
		this.setRight(2);
		this.setHeight(30);
		
		this.text = text;
		this.onClick = onClick;
		
		this.borderColor = borderColor;
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Sets the text of the button.
	 * @param text Text to set.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Sets the OnClick delegate of the button.
	 * @param onClick Delegate to set.
	 */
	public void setOnClick(Runnable onClick) {
		this.onClick = onClick;
	}

	public void setBorderColor(Color color) {
		this.borderColor = color;
	}
	
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}
	
	/**
	 * Draws the button to the screen.
	 * @param offset The offset (Y location relative to parent) of the Component.
	 * @param drawContext The current draw context of the game.
	 * @param partialTicks The partial ticks used for interpolation.
	 * @param color The current Color of the UI.
	 */
	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		MatrixStack matrixStack = drawContext.getMatrices();
		RenderUtils.drawOutlinedBox(matrixStack, actualX + 2, actualY, actualWidth - 4, actualHeight - 2, borderColor, backgroundColor);
		RenderUtils.drawString(drawContext, this.text, actualX + 8, actualY + 8, 0xFFFFFF);
	}

	/**
	 * Triggered when the user clicks the Left Mouse Button (LMB)
	 * @param event Event fired.
	 */
	@Override
	public void OnLeftMouseDown(LeftMouseDownEvent event) {
		if(this.hovered && this.isVisible() && onClick != null)  {
			this.onClick.run();
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
