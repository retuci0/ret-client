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
import net.retclient.settings.types.FloatSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class SliderComponent extends Component implements LeftMouseDownListener, LeftMouseUpListener, MouseMoveListener {

	private String text;
	private float currentSliderPosition = 0.4f;
	float r;
	float g;
	float b;
	private boolean isSliding = false;
	
	
	FloatSetting slider;

	public SliderComponent(String text, IGuiElement parent) {
		super(parent);
		this.text = text;
		this.slider = null;
		
		this.setHeight(50);
		this.setLeft(4);
		this.setRight(4);

		Main.getInstance().eventManager.AddListener(LeftMouseDownListener.class, this);
		Main.getInstance().eventManager.AddListener(LeftMouseUpListener.class, this);
	}

	public SliderComponent(IGuiElement parent, FloatSetting slider) {
		super(parent);
		this.text = slider.displayName;
		this.slider = slider;
		this.currentSliderPosition = (float) ((slider.getValue() - slider.min_value)
				/ (slider.max_value - slider.min_value));
		
		this.setHeight(50);
		this.setLeft(4);
		this.setRight(4);
		
		Main.getInstance().eventManager.AddListener(LeftMouseDownListener.class, this);
		Main.getInstance().eventManager.AddListener(LeftMouseUpListener.class, this);
	}

	public float getSliderPosition() {
		return this.currentSliderPosition;
	}

	public void setSliderPosition(float pos) {
		this.currentSliderPosition = pos;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public void setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	@Override
	public void OnLeftMouseDown(LeftMouseDownEvent event) {
		if (hovered && Main.getInstance().hudManager.isClickGuiOpen()) {
			isSliding = true;
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
		if (Main.getInstance().hudManager.isClickGuiOpen() && this.isSliding) {
			this.currentSliderPosition = (float) Math.min((((mouseX - (actualX + 4)) - 1) / (actualWidth - 8)),1f);
			this.currentSliderPosition = (float) Math.max(0f, this.currentSliderPosition);
			this.slider.setValue((this.currentSliderPosition * (slider.max_value - slider.min_value)) + slider.min_value);
		}
	}


	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		MatrixStack matrixStack = drawContext.getMatrices();

		// Draw the rest of the box.
		float xLength = ((actualWidth - 18) * (float) ((slider.getValue() - slider.min_value) / (slider.max_value - slider.min_value)));
		
		GuiManager hudManager = Main.getInstance().hudManager;
		RenderUtils.drawBox(matrixStack, actualX + 10, actualY + 35, xLength, 2, GuiManager.foregroundColor.getValue());
		RenderUtils.drawBox(matrixStack, actualX + 10 + xLength, actualY + 35, (actualWidth - xLength - 18), 2, new Color(255, 255, 255, 255));
		RenderUtils.drawCircle(matrixStack, actualX + 10 + xLength, actualY + 35, 6, GuiManager.foregroundColor.getValue());
		
		if (this.slider == null)
			return;
		RenderUtils.drawString(drawContext, this.text, actualX + 6, actualY + 6, 0xFFFFFF);
		
		String valueText = String.format("%.02f", this.slider.getValue());
		int textSize = MinecraftClient.getInstance().textRenderer.getWidth(valueText) * MinecraftClient.getInstance().options.getGuiScale().getValue();
		RenderUtils.drawString(drawContext, valueText, actualX + actualWidth - 6 - textSize, actualY + 6, 0xFFFFFF);
	}
}
