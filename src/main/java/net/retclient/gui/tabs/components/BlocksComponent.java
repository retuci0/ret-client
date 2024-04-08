package net.retclient.gui.tabs.components;

import net.retclient.Main;
import net.retclient.event.events.LeftMouseDownEvent;
import net.retclient.event.events.MouseScrollEvent;
import net.retclient.event.listeners.LeftMouseDownListener;
import net.retclient.event.listeners.MouseScrollListener;
import net.retclient.gui.Color;
import net.retclient.gui.GuiManager;
import net.retclient.gui.IGuiElement;
import net.retclient.misc.RenderUtils;
import net.retclient.settings.types.BlocksSetting;
import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

public class BlocksComponent extends Component implements MouseScrollListener, LeftMouseDownListener {

	private BlocksSetting blocks;
	private String text;
	private int visibleRows;
	private int visibleColumns;
	private int scroll = 0;
	
	private boolean collapsed = true;
	private float collapsedHeight = 30;
	private float expandedHeight = 135;
	/**
	 * Constructor for button component.
	 * @param parent Parent Tab that this Component resides in.
	 * @param text Text contained in this button element.
	 * @param onClick OnClick delegate that will run when the button is pressed.
	 */
	public BlocksComponent(IGuiElement parent, BlocksSetting setting) {
		super(parent);
		this.text = setting.displayName;
		blocks = setting;
		
		this.setLeft(2);
		this.setRight(2);
		this.setHeight(collapsedHeight);
		
		visibleRows = (int)expandedHeight / 36;
		visibleColumns = (int)actualWidth / 36;
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
		RenderUtils.drawString(drawContext, text, actualX + 6, actualY + 6, 0xFFFFFF);
		RenderUtils.drawString(drawContext, collapsed ? ">>" :  "<<", (actualX + actualWidth - 24), actualY + 6, GuiManager.foregroundColor.getValue().getColorAsInt());
		
		if(!collapsed) {
			MatrixStack matrixStack = drawContext.getMatrices();
			matrixStack.push();
			matrixStack.scale(2.0f, 2.0f, 2.0f);
			for(int i = scroll; i < visibleRows + scroll; i++) {
				for(int j = 0; j < visibleColumns; j++) {
					int index = (i * visibleColumns) + j;
					if(index > Registries.BLOCK.size())
						continue;
					
					Block block = Registries.BLOCK.get(index);
					
					if(blocks.getValue().contains(block)) {
						RenderUtils.drawBox(matrixStack, ((actualX + (j * 36) + 4) / 2.0f), ((actualY + ((i-scroll) * 36) + 25) / 2.0f), 16, 16, new Color(0, 255, 0, 55));
					}
					drawContext.drawItem(new ItemStack(block.asItem()), (int) ((actualX + (j * 36) + 6) / 2.0f), (int) ((actualY + ((i-scroll) * 36) + 25) / 2.0f) );
				}
			}
			
			matrixStack.pop();
		}
	}

	@Override
	public void OnMouseScroll(MouseScrollEvent event) {
		if(Main.getInstance().hudManager.isClickGuiOpen() && this.hovered) {
			if(event.GetVertical() > 0 && scroll > 0) {
				scroll--;
			}else if (event.GetVertical() < 0 && (scroll + visibleRows) < (Registries.BLOCK.size() / visibleColumns)) {
				scroll++;
			}
			event.SetCancelled(true);
		}
	}
	
	@Override
	public void OnVisibilityChanged() {
		if(this.isVisible()) {
			Main.getInstance().eventManager.AddListener(MouseScrollListener.class, this);
			Main.getInstance().eventManager.AddListener(LeftMouseDownListener.class, this);
		}else {
			Main.getInstance().eventManager.RemoveListener(MouseScrollListener.class, this);
			Main.getInstance().eventManager.RemoveListener(LeftMouseDownListener.class, this);
		}
	}

	@Override
	public void OnLeftMouseDown(LeftMouseDownEvent event) {
		double mouseX = event.GetMouseX();
		double mouseY = event.GetMouseY();
		
		if(mouseX > (actualX + 4) && mouseY < (actualX + (36 * visibleColumns) + 4)) {
			if(mouseY > actualY && mouseY < actualY + 25) {
				collapsed = !collapsed;
				if(collapsed) 
					this.setHeight(collapsedHeight);
				else
					this.setHeight(expandedHeight);
			}else if(mouseY > (actualY + 25) && mouseY < (actualY + (36 * visibleRows) + 25)) {
				int col =  (int) (mouseX - actualX - 8) / 36;
				int row =  (int) ((mouseY - actualY - 24) / 36) + scroll;
				
				int index = (row * visibleColumns) + col;
				if(index > Registries.BLOCK.size())
					return;
				
				Block block = Registries.BLOCK.get(index);
				if(this.blocks.getValue().contains(block)) {
					this.blocks.getValue().remove(block);
					this.blocks.update();
				}else {
					this.blocks.getValue().add(block);
					this.blocks.update();
				}
			}
		}
	}
}
