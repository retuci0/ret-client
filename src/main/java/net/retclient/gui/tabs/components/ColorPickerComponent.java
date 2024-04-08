package net.retclient.gui.tabs.components;

import net.retclient.Main;
import net.retclient.event.events.LeftMouseDownEvent;
import net.retclient.event.events.LeftMouseUpEvent;
import net.retclient.event.events.MouseMoveEvent;
import net.retclient.event.listeners.LeftMouseDownListener;
import net.retclient.event.listeners.LeftMouseUpListener;
import net.retclient.event.listeners.MouseMoveListener;
import net.retclient.gui.Color;
import net.retclient.gui.GuiManager;
import net.retclient.gui.IGuiElement;
import net.retclient.misc.RenderUtils;
import net.retclient.settings.types.ColorSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class ColorPickerComponent extends Component implements LeftMouseDownListener, LeftMouseUpListener, MouseMoveListener {

	private String text;
	private boolean isSliding = false;
	private boolean collapsed = true;
	private float hue = 0.0f;
	private float saturation = 0.0f;
	private float luminance = 0.0f;
	private float alpha = 0.0f;
	
	private ColorSetting color;

	public ColorPickerComponent(String text, IGuiElement parent) {
		super(parent);
		this.text = text;
		
		this.setHeight(145);
		this.setLeft(4);
		this.setRight(4);

		Main.getInstance().eventManager.AddListener(LeftMouseDownListener.class, this);
		Main.getInstance().eventManager.AddListener(LeftMouseUpListener.class, this);
	}
	
	public ColorPickerComponent(IGuiElement parent, ColorSetting color) {
		super(parent);
		
		this.text = color.displayName;
		this.color = color;
		this.color.setOnUpdate((Color newColor) -> ensureGuiUpdated(newColor));
	
		this.hue = color.getValue().hue;
		this.saturation = color.getValue().saturation;
		this.luminance = color.getValue().luminance;
		
		this.setHeight(30);
		this.setLeft(4);
		this.setRight(4);
		
		Main.getInstance().eventManager.AddListener(LeftMouseDownListener.class, this);
		Main.getInstance().eventManager.AddListener(LeftMouseUpListener.class, this);
	}
	
	public void ensureGuiUpdated(Color newColor) {
		this.hue = newColor.hue;
		this.saturation = newColor.saturation;
		this.luminance = newColor.luminance;
		this.alpha = newColor.alpha;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	@Override
	public void OnLeftMouseDown(LeftMouseDownEvent event) {
		double mouseY = event.GetMouseY();
		
		if(hovered && Main.getInstance().hudManager.isClickGuiOpen()) {
			if(mouseY < actualY + 29) {
				collapsed = !collapsed;
				if(collapsed) 
					this.setHeight(30);
				else
					this.setHeight(145);
			}else {
				if(!collapsed)
					isSliding = true;
			}
		}
	}
	
	@Override
	public void OnLeftMouseUp(LeftMouseUpEvent event) {
		isSliding = false;
	}
	
	@Override
	public void OnMouseMove(MouseMoveEvent event) {
		super.OnMouseMove(event);
		
		double mouseX = event.GetHorizontal();
		double mouseY = event.GetVertical();
		if (Main.getInstance().hudManager.isClickGuiOpen() && this.isSliding) {

			float vertical = (float) Math.min(Math.max(1.0f - (((mouseY - (actualY + 29)) - 1) / (actualHeight - 33)), 0.0f), 1.0f);
			
			// If inside of saturation/lightness box.
			if(mouseX >= actualX + 4 && mouseX <= actualX + actualWidth - 68) {
				float horizontal = (float) Math.min(Math.max(((mouseX - (actualX + 4)) - 1) / (actualWidth - 68), 0.0f), 1.0f);
				
				this.luminance = vertical;
				this.saturation = horizontal;
			}else if(mouseX >= actualX + actualWidth - 72 && mouseX <= actualX + actualWidth - 38) {
				this.hue = (1.0f - vertical) * 360.0f;
			}else if(mouseX >= actualX + actualWidth - 34 && mouseX <= actualX + actualWidth - 4) {
				this.alpha = (vertical) * 255.0f;
			}
			
			this.color.getValue().setHSV(hue, saturation, luminance);
			this.color.getValue().setAlpha((int) alpha);
		}
	}


	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		MatrixStack matrixStack = drawContext.getMatrices();
		
		RenderUtils.drawString(drawContext, this.text, actualX + 6, actualY + 6, 0xFFFFFF);
		RenderUtils.drawString(drawContext, collapsed ?  ">>" :  "<<", (actualX + actualWidth - 24), actualY + 6, GuiManager.foregroundColor.getValue().getColorAsInt());
		
		if(!collapsed) {
			Color newColor = new Color(255, 0, 0);
			newColor.setHSV(this.hue, 1.0f, 1.0f);
			RenderUtils.drawHorizontalGradient(matrixStack, actualX + 4, actualY + 29, actualWidth - 76, actualHeight - 33, new Color(255, 255, 255), newColor);
			RenderUtils.drawVerticalGradient(matrixStack, actualX + 4, actualY+ 29, actualWidth - 76, actualHeight - 33, new Color(0, 0, 0, 0), new Color(0, 0, 0));
			
			// Draw Hue Rectangle
			float increment = ((this.actualHeight - 33) / 6.0f);
			RenderUtils.drawVerticalGradient(matrixStack, actualX + actualWidth - 68, actualY + 29, 30, increment, new Color(255, 0, 0), new Color(255, 255, 0));
			RenderUtils.drawVerticalGradient(matrixStack, actualX + actualWidth - 68, actualY + 29 + increment, 30,increment, new Color(255, 255, 0), new Color(0, 255, 0));
			RenderUtils.drawVerticalGradient(matrixStack, actualX + actualWidth - 68, actualY + 29 + (2 * increment), 30, increment, new Color(0, 255, 0), new Color(0, 255, 255));
			RenderUtils.drawVerticalGradient(matrixStack, actualX + actualWidth - 68, actualY + 29 + (3 * increment), 30, increment, new Color(0, 255, 255), new Color(0, 0, 255));
			RenderUtils.drawVerticalGradient(matrixStack, actualX + actualWidth - 68, actualY + 29 + (4 * increment), 30, increment, new Color(0, 0, 255), new Color(255, 0, 255));
			RenderUtils.drawVerticalGradient(matrixStack, actualX + actualWidth - 68, actualY + 29 + (5 * increment), 30, increment, new Color(255, 0, 255), new Color(255, 0, 0));
		
			// Draw Alpha Rectangle
			RenderUtils.drawVerticalGradient(matrixStack, actualX + actualWidth - 34 , actualY + 29, 30, actualHeight - 33, new Color(255, 255, 255), new Color(0, 0, 0));
			
			// Draw Outlines
			RenderUtils.drawOutline(matrixStack, actualX + 4, actualY + 29, actualWidth - 76, actualHeight - 33);
			RenderUtils.drawOutline(matrixStack, actualX + actualWidth - 68, actualY + 29, 30, actualHeight - 33);
			RenderUtils.drawOutline(matrixStack, actualX + actualWidth - 34, actualY + 29, 30, actualHeight - 33);
			
			// Draw Indicators
			RenderUtils.drawCircle(matrixStack, actualX + 4 + (saturation * (actualWidth - 72)), actualY + 29 + ((1.0f - luminance) * (actualHeight - 33)), 3, new Color(255, 255, 255, 255));
			RenderUtils.drawOutlinedBox(matrixStack, actualX + actualWidth - 68, actualY + 29 + ((hue / 360.0f) * (actualHeight - 33)), 30, 3, new Color(255, 255, 255, 255));
			RenderUtils.drawOutlinedBox(matrixStack, actualX + actualWidth - 34, actualY + 29 + (((255.0f - alpha) / 255.0f) * (actualHeight - 33)), 30, 3, new Color(255, 255, 255, 255));
		}
		
	}
}