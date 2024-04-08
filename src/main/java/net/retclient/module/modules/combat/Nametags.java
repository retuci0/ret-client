package net.retclient.module.modules.combat;

import org.lwjgl.glfw.GLFW;
import net.retclient.module.Module;
import net.retclient.settings.types.BooleanSetting;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;

public class Nametags extends Module {

	private FloatSetting scale;
	private BooleanSetting onlyPlayers;
	private BooleanSetting alwaysVisible;
	
	public Nametags() {
		super(new KeybindSetting("key.nametags", "NameTags Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));
		
		this.setName("Nametags");
		this.setCategory(Category.Combat);
		this.setDescription("Scales the nametags to be larger.");
		
		scale = new FloatSetting("nametags_scale", "Scale", "Scale of the NameTags", 0f, 0f, 5f, 0.25f);
		onlyPlayers = new BooleanSetting("nametags_onlyPlayers", "Only Players", "Whether Nametags are only enlarged for players.", false);
		alwaysVisible = new BooleanSetting("nametags_alwaysVisible", "Always Visible", "Whether Nametags will always be displayed.", false);
		this.addSetting(scale);
		this.addSetting(onlyPlayers);
		this.addSetting(alwaysVisible);
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onToggle() {

	}
	
	public double getNametagScale() {
		return this.scale.getValue();
	}
	
	public boolean getPlayersOnly() {
		return this.onlyPlayers.getValue();
	}
	
	public boolean getAlwaysVisible() {
		return this.alwaysVisible.getValue();
	}
}