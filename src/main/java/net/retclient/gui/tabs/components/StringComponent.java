package net.retclient.gui.tabs.components;

import java.util.ArrayList;

import net.retclient.Main;
import net.retclient.event.events.FontChangedEvent;
import net.retclient.event.listeners.FontChangedListener;
import net.retclient.event.listeners.LeftMouseDownListener;
import net.retclient.gui.Color;
import net.retclient.gui.IGuiElement;
import net.retclient.misc.Colors;
import net.retclient.misc.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;

public class StringComponent extends Component implements FontChangedListener {
	private String originalText;
	private ArrayList<String> text;
	private boolean bold;
	private Color color;
	
	public StringComponent(String text, IGuiElement parent) {
		super(parent);
		setText(text);
		this.color = Colors.White;
		this.bold = false;
		
		Main.getInstance().eventManager.AddListener(FontChangedListener.class, this);
	}

	public StringComponent(String text, IGuiElement parent, boolean bold) {
		super(parent);
		setText(text);
		this.color = Colors.White;
		this.bold = bold;
		
		Main.getInstance().eventManager.AddListener(FontChangedListener.class, this);
	}
	
	public StringComponent(String text, IGuiElement parent, Color color, boolean bold) {
		super(parent);
		setText(text);
		this.color = color;
		this.bold = bold;
		
		Main.getInstance().eventManager.AddListener(FontChangedListener.class, this);
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		int i = 0;
		for (String str : text) {
			if(bold)
				str = Formatting.BOLD + str;
			RenderUtils.drawString(drawContext, str, actualX + 8, actualY + 8 + i, this.color.getColorAsInt());
			i += 30;
		}
	}

	/**
	 * Sets the text of the String Component.
	 * 
	 * @param text The text to set.
	 */
	public void setText(String text) {
		this.originalText = text;
		this.text = new ArrayList<String>();
		
		float textWidth = Main.getInstance().fontManager.GetRenderer().getWidth(text) * 2.0f;
		int strings = (int) Math.ceil(textWidth / this.actualWidth);
		if (strings == 0) {
			this.text.add(text);
			this.setHeight(30);
		} else {
			int lengthOfEachSegment = text.length() / strings;

			for (int i = 0; i < strings; i++) {
				this.text.add(text.substring(lengthOfEachSegment * i, (lengthOfEachSegment * i) + lengthOfEachSegment));
			}
			this.setHeight(strings * 30);
		}
	}

	/**
	 * Gets the text of the String Component.
	 * 
	 * @return Text of the String Component as a string.
	 */
	
	public String getText() {
		return this.originalText;
	}

	@Override
	public void update() {

	}

	@Override
	public void OnParentWidthChanged() {
		setText(originalText);
	}

	@Override
	public void OnFontChanged(FontChangedEvent event) {
		setText(this.originalText);
	}
}