package net.retclient.gui;

import java.util.ArrayList;
import net.retclient.Main;
import net.retclient.event.events.LeftMouseDownEvent;
import net.retclient.event.events.MouseMoveEvent;
import net.retclient.event.listeners.LeftMouseDownListener;
import net.retclient.event.listeners.MouseMoveListener;
import net.retclient.gui.tabs.components.Component;
import net.retclient.settings.SettingManager;
import net.retclient.settings.types.Vector2Setting;
import net.retclient.utils.types.Vector2;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class AbstractGui implements IGuiElement, LeftMouseDownListener, MouseMoveListener {
	protected static MinecraftClient mc = MinecraftClient.getInstance();

	protected String ID;

	protected Vector2Setting position;
	
	protected float width;
	protected float height;

	protected boolean isMouseOver = false;
	public boolean moveable = true;
	
	protected boolean alwaysVisible = false;
	protected boolean visible = false;
	
	// Mouse Variables
	protected double lastClickOffsetX;
	protected double lastClickOffsetY;
	protected boolean inheritHeightFromChildren = true;
	
	
	protected ArrayList<Component> children = new ArrayList<>();
	
	public AbstractGui(String ID, float x, float y, float width, float height) {
		this.ID = ID;
		this.position = new Vector2Setting(ID + "_position", ID + "Position", new Vector2(x, y), (Vector2 vec) -> UpdateAll(vec));
		this.width = width;
		this.height = height;
		SettingManager.registerSetting(position, Main.getInstance().settingManager.config_category);
	}

	public void UpdateAll(Vector2 vec) {
		for(Component component : this.children) {
			component.OnParentXChanged();
			component.OnParentYChanged();
		}
	}
	
	public String getID() {
		return ID;
	}
	
	@Override
	public float getHeight() {
		return this.height;
	}

	@Override
	public float getX() {
		return position.getValue().x;
	}

	@Override
	public float getY() {
		return position.getValue().y;
	}

	@Override
	public float getWidth() {
		return this.width;
	}

	@Override
	public void setX(float x) {
		if(this.position.getValue().x != x) {
			position.silentSetX(x);
			for(Component component : this.children) {
				component.OnParentXChanged();
			}
		}
	}

	public void setY(float y) {
		if(this.position.getValue().y != y) {
			position.silentSetY(y);
			for(Component component : this.children) {
				component.OnParentYChanged();
			}
		}
	}

	public void setWidth(float width) {
		if(this.width != width) {
			this.width = width;
			for(Component component : this.children) {
				component.OnParentWidthChanged();
			}
		}
	}

	public void setHeight(float height) {
		if(this.height != height) {
			this.height = height;
			for(Component component : this.children) {
				component.OnParentHeightChanged();
			}
		}
	}
	
	public boolean getVisible() {
		return this.visible;
	}
	
	public void setVisible(boolean state) {
		if(alwaysVisible) state = true;
		
		if(visible != state) {
			this.visible = state;
			for(Component component : children){
				component.setVisible(state);
			}
			
			// Binds/Unbinds respective listeners depending on whether it is visible.
			if(state) {
				Main.instance.eventManager.AddListener(LeftMouseDownListener.class, this);
				Main.instance.eventManager.AddListener(MouseMoveListener.class, this);
			}else {
				Main.instance.eventManager.RemoveListener(LeftMouseDownListener.class, this);
				Main.instance.eventManager.RemoveListener(MouseMoveListener.class, this);
			}
		}
	}
	
	public void setAlwaysVisible(boolean state) {
		this.alwaysVisible = state;
		if(this.alwaysVisible) {
			this.setVisible(true);
		}
	}

	public abstract void update();

	public abstract void draw(DrawContext drawContext, float partialTicks);

	@Override
	public void OnLeftMouseDown(LeftMouseDownEvent event) {
		double mouseX = event.GetMouseX();
		double mouseY = event.GetMouseY();

		Vector2 pos = position.getValue();

		if (Main.getInstance().hudManager.isClickGuiOpen()) {
			if (GuiManager.currentGrabbed == null) {
				if (mouseX >= pos.x && mouseX <= (pos.x + width)) {
					if (mouseY >= pos.y && mouseY <= (pos.y + height)) {
						GuiManager.currentGrabbed = this;
						this.lastClickOffsetX = mouseX - pos.x;
						this.lastClickOffsetY = mouseY - pos.y;
					}
				}
			}
		}
	}

	@Override
	public void OnMouseMove(MouseMoveEvent event) {
		if (this.visible && Main.getInstance().hudManager.isClickGuiOpen()) {
			double mouseX = event.GetHorizontal();
			double mouseY = event.GetVertical();

			Vector2 pos = position.getValue();

			if (GuiManager.currentGrabbed == this && this.moveable) {
				 this.setX((float)(mouseX - this.lastClickOffsetX));
				 this.setY((float) (mouseY - this.lastClickOffsetY));
			}

			if (mouseX >= pos.x && mouseX <= pos.x + width) {
				if (mouseY >= pos.y && mouseY <= pos.y + height) {
					isMouseOver = true;
				}else {
					isMouseOver = false;
				}
			}else {
				isMouseOver = false;
			}
		}else {
			isMouseOver = false;
		}
	}
	
	@Override
	public void OnChildChanged(IGuiElement child) {
		
	}
}
