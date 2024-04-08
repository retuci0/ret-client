package net.retclient.gui.hud;

import net.retclient.utils.types.Vector2;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.DefaultedList;

public class ArmorHud extends AbstractHud{

	public ArmorHud(int x, int y, int width, int height) {
		super("ArmorHud", x,y,width,height);
		this.width = 60;
		this.height = 256;
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		if(this.visible) {
			DefaultedList<ItemStack> armors = mc.player.getInventory().armor;
			
			Vector2 pos = position.getValue();
			int yOff = 16;
			for(ItemStack armor : armors) {
				if(armor.getItem() == Items.AIR) continue;
				drawContext.drawItem(armor, (int)pos.x, (int)(pos.y + this.height - yOff));
				yOff += 16;
			}
		}
	}
}
