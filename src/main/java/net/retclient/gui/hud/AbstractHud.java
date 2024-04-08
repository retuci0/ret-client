package net.retclient.gui.hud;

import net.retclient.Main;
import net.retclient.gui.AbstractGui;
import net.retclient.gui.Color;
import net.retclient.settings.SettingManager;
import net.retclient.settings.types.BooleanSetting;
import net.minecraft.client.gui.DrawContext;

public class AbstractHud extends AbstractGui {

	public BooleanSetting activated;
	
	public AbstractHud(String ID, float x, float y, float width, float height) {
		super(ID, x, y, width, height);
		this.setVisible(true);
		this.activated = new BooleanSetting(ID + "_activated", ID + " Activated", false, (Boolean val) -> onActivatedChanged(val));
		SettingManager.registerSetting(activated, Main.getInstance().settingManager.config_category);
	}

	private void onActivatedChanged(Boolean state) {
		Main.getInstance().hudManager.SetHudActive(this, state.booleanValue());
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		
	}
}
