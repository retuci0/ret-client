package net.retclient.gui.tabs.components;

import org.lwjgl.glfw.GLFW;

import net.retclient.Main;
import net.retclient.event.events.KeyDownEvent;
import net.retclient.event.events.LeftMouseDownEvent;
import net.retclient.event.listeners.KeyDownListener;
import net.retclient.event.listeners.LeftMouseDownListener;
import net.retclient.gui.Color;
import net.retclient.gui.IGuiElement;
import net.retclient.misc.RenderUtils;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;

public class KeybindComponent extends Component implements LeftMouseDownListener, KeyDownListener {
	private boolean listeningForKey;
	private KeybindSetting keyBind;
	
	public KeybindComponent(IGuiElement parent, KeybindSetting keyBind) {
		super(parent);
		this.keyBind = keyBind;
		
		Main.getInstance().eventManager.AddListener(LeftMouseDownListener.class, this);
		Main.getInstance().eventManager.AddListener(KeyDownListener.class, this);
	}

	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		super.draw(drawContext, partialTicks);
		RenderUtils.drawString(drawContext, "Keybind", actualX + 8, actualY + 8, 0xFFFFFF);
		RenderUtils.drawBox(drawContext.getMatrices(), actualX + actualWidth - 100, actualY + 2, 98, actualHeight - 4, new Color(115, 115, 115, 200));
		RenderUtils.drawOutline(drawContext.getMatrices(), actualX + actualWidth - 100, actualY + 2, 98, actualHeight - 4);
		
		String keyBindText = this.keyBind.getValue().getLocalizedText().getString();
		if(keyBindText.equals("scancode.0") || keyBindText.equals("key.keyboard.0"))
			keyBindText = "N/A";
		
		RenderUtils.drawString(drawContext, keyBindText, actualX + actualWidth - 90, actualY + 8, 0xFFFFFF);
	}

	@Override
	public void OnLeftMouseDown(LeftMouseDownEvent event) {
		if (hovered && Main.getInstance().hudManager.isClickGuiOpen()) {
			listeningForKey = !listeningForKey;
		}
	}

	@Override
	public void OnKeyDown(KeyDownEvent event) {
		if(listeningForKey) {
			int key = event.GetKey();
			int scanCode = event.GetScanCode();
			
			if(key == GLFW.GLFW_KEY_ESCAPE) {
				keyBind.setValue(InputUtil.UNKNOWN_KEY);
			}else {
				keyBind.setValue(InputUtil.fromKeyCode(key, scanCode));
			}
			
			listeningForKey = false;
			
			event.SetCancelled(true);
		}
	}
}