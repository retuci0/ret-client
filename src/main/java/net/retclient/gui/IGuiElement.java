package net.retclient.gui;

public interface IGuiElement {

	public float getX();
	public float getY();
	public float getWidth();
	public float getHeight();
	
	public void setX(float x);
	public void setY(float y);
	public void setWidth(float width);
	public void setHeight(float height);
	
	public void OnChildChanged(IGuiElement child);
}
