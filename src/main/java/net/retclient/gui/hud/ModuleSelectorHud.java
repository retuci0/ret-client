package net.retclient.gui.hud;

import java.util.ArrayList;
import org.lwjgl.glfw.GLFW;
import net.retclient.Main;
import net.retclient.Client;
import net.retclient.gui.Color;
import net.retclient.gui.GuiManager;
import net.retclient.misc.RenderUtils;
import net.retclient.module.Module;
import net.retclient.module.Module.Category;
import net.retclient.utils.types.Vector2;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

public class ModuleSelectorHud extends AbstractHud {
	private KeyBinding keybindUp;
	private KeyBinding keybindDown;
	private KeyBinding keybindLeft;
	private KeyBinding keybindRight;

	private Client ret;
	
	int index = 0;
	int indexMods = 0;
	boolean isCategoryMenuOpen = false;

	Category[] categories;
	ArrayList<Module> modules = new ArrayList<Module>();

	public ModuleSelectorHud() {
		super("ModuleSelectorHud", 0, 0, 150, 30);
		this.keybindUp = new KeyBinding("key.tabup", GLFW.GLFW_KEY_UP, "key.categories.ret");
		this.keybindDown = new KeyBinding("key.tabdown", GLFW.GLFW_KEY_DOWN, "key.categories.ret");
		this.keybindLeft = new KeyBinding("key.tableft", GLFW.GLFW_KEY_LEFT, "key.categories.ret");
		this.keybindRight = new KeyBinding("key.tabright", GLFW.GLFW_KEY_RIGHT, "key.categories.ret");

		categories = Module.Category.values();
		
		this.ret = Main.getInstance();
	}

	@Override
	public void update() {
			if (!ret.isGhosted()) {
				if (this.keybindUp.isPressed()) {
					if (!isCategoryMenuOpen) {
						if (index == 0) {
							index = categories.length - 1;
						} else {
							index -= 1;
						}
					} else {
						if (indexMods == 0) {
							indexMods = modules.size() - 1;
						} else {
							indexMods -= 1;
						}
					}
					this.keybindUp.setPressed(false);
				} else if (this.keybindDown.isPressed()) {
					if (!isCategoryMenuOpen) {
						index = (index + 1) % categories.length;
					} else {
						indexMods = (indexMods + 1) % modules.size();
					}
					this.keybindDown.setPressed(false);
				} else if (this.keybindRight.isPressed()) {
					if (!isCategoryMenuOpen) {
						isCategoryMenuOpen = !isCategoryMenuOpen;
						if (modules.isEmpty()) {
							for (Module module : ret.moduleManager.modules) {
								if (module.isCategory(this.categories[this.index])) {
									modules.add(module);
								}
							}
						}
					} else {
						modules.get(indexMods).toggle();
					}
					this.keybindRight.setPressed(false);
				} else if (this.keybindLeft.isPressed()) {
					if (this.isCategoryMenuOpen) {
						this.indexMods = 0;
						this.modules.clear();
						this.isCategoryMenuOpen = false;
					}
					this.keybindLeft.setPressed(false);
				}
			}
		
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		// Gets the client and window.
		MinecraftClient mc = MinecraftClient.getInstance();
		MatrixStack matrixStack = drawContext.getMatrices();
		Window window = mc.getWindow();
		
		Vector2 pos = position.getValue();

		RenderUtils.drawString(drawContext, "Ret Client " + Client.VERSION, 8, 8, GuiManager.foregroundColor.getValue());

		// Draws the table including all of the categories.
		GuiManager hudManager = Main.getInstance().hudManager;
		RenderUtils.drawRoundedBox(matrixStack, pos.x, pos.y, width, height * this.categories.length, 6f, GuiManager.backgroundColor.getValue());
		RenderUtils.drawRoundedOutline(matrixStack, pos.x, pos.y, width, height * this.categories.length, 6f, GuiManager.borderColor.getValue());
		
		// For every category, draw a cell for it.
		for (int i = 0; i < this.categories.length; i++) {
			RenderUtils.drawString(drawContext, ">>", pos.x + width - 24, pos.y + (height * i) + 8, GuiManager.foregroundColor.getValue());
			// Draws the name of the category dependent on whether it is selected.
			if (this.index == i) {
				RenderUtils.drawString(drawContext, "> " + this.categories[i].name(), pos.x + 8, pos.y + (height * i) + 8, GuiManager.foregroundColor.getValue());
			} else {
				RenderUtils.drawString(drawContext, this.categories[i].name(), pos.x + 8, pos.y + (height * i) + 8, 0xFFFFFF);
			}
		}
		
		// If any particular category menu is open.
		if (isCategoryMenuOpen) {
			// Draw the table underneath
			RenderUtils.drawRoundedBox(matrixStack, pos.x + width, pos.y + (height * this.index), 165, height * modules.size(), 6f, GuiManager.backgroundColor.getValue());
			RenderUtils.drawRoundedOutline(matrixStack, pos.x + width, pos.y + (height * this.index), 165, height * modules.size(), 6f, GuiManager.borderColor.getValue());
			
			// For every mod, draw a cell for it.
			for (int i = 0; i < modules.size(); i++) {
				if (this.indexMods == i) {
					RenderUtils.drawString(drawContext, "> " + modules.get(i).getName(), pos.x + width + 5,
							pos.y + (i * height) + (this.index * height) + 8,
							modules.get(i).getState() ? 0x00FF00 : GuiManager.foregroundColor.getValue().getColorAsInt());
				} else {
					RenderUtils.drawString(drawContext, modules.get(i).getName(), pos.x + width + 5,
							pos.y + (i * height) + (this.index * height) + 8,
							modules.get(i).getState() ? 0x00FF00 : 0xFFFFFF);
				}
			}
		}
		
		// Draws the active mods in the top right of the screen.
		int iteration = 0;
		for(int i = 0; i < ret.moduleManager.modules.size(); i++) {
			Module mod = ret.moduleManager.modules.get(i);
			if(mod.getState()) {
				RenderUtils.drawString(drawContext, mod.getName(),
						(float) (window.getWidth() - ((mc.textRenderer.getWidth(mod.getName()) + 5) * 2)), 10 + (iteration*20),
						GuiManager.foregroundColor.getValue().getColorAsInt());
				iteration++;
			}
		}
	}
}
