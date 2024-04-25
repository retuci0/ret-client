package me.retucio.retclient.features.gui.items.buttons;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import me.retucio.retclient.RetClient;
import me.retucio.retclient.features.gui.GUI;
import me.retucio.retclient.features.modules.client.ClickGUI;
import me.retucio.retclient.features.settings.Bind;
import me.retucio.retclient.features.settings.Setting;
import me.retucio.retclient.util.ColorUtil;
import me.retucio.retclient.util.RenderUtil;

public class BindButton extends Button {
	
    private final Setting<Bind> setting;
    public boolean isListening;

    public BindButton(Setting<Bind> setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @SuppressWarnings("unused")
	@Override
    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {
    	
        int color = ColorUtil.toARGB(ClickGUI.getInstance().red.getValue(), ClickGUI.getInstance().green.getValue(), ClickGUI.getInstance().blue.getValue(), 255);
        
        RenderUtil.rect(context.getMatrices(), this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515) : (!this.isHovering(mouseX, mouseY) ? RetClient.colorManager.getColorWithAlpha(RetClient.moduleManager.getModuleByClass(ClickGUI.class).hoverAlpha.getValue()) : RetClient.colorManager.getColorWithAlpha(RetClient.moduleManager.getModuleByClass(ClickGUI.class).alpha.getValue())));
        
        if (this.isListening) {
            drawString("Press a Key...", this.x + 2.3f, this.y - 1.7f - (float) GUI.getClickGui().getTextOffset(), -1);
            
        } else {
            String str = this.setting.getValue().toString().toUpperCase();
            str = str.replace("KEY.KEYBOARD", "").replace(".", " ");
            drawString(this.setting.getName() + " " + Formatting.GRAY + str, this.x + 2.3f, this.y - 1.7f - (float) GUI.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        
        if (this.isHovering(mouseX, mouseY)) {
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f));
        }
    }

    @Override 
    public void onKeyPressed(int key) {
        if (this.isListening) {
            Bind bind = new Bind(key);
            
            if (key == GLFW.GLFW_KEY_DELETE
                    || key == GLFW.GLFW_KEY_BACKSPACE
                    || key == GLFW.GLFW_KEY_ESCAPE) {
                bind = new Bind(-1);
            }
            
            this.setting.setValue(bind);
            this.onMouseClick();
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }

    @Override
    public boolean getState() {
        return !this.isListening;
    }
}