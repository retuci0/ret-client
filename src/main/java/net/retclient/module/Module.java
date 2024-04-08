package net.retclient.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import net.retclient.Main;
import net.retclient.Client;
import net.retclient.mixin.interfaces.IMinecraftClient;
import net.retclient.settings.Setting;
import net.retclient.settings.SettingManager;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.MinecraftClient;

public abstract class Module {
	private String name;
	private String description;
	private Category category;
	private boolean state;

	protected KeybindSetting keyBind;
	private List<Setting<?>> settings = new ArrayList<Setting<?>>();
	
	protected final MinecraftClient MC = Client.MC;
	protected final IMinecraftClient IMC = Client.IMC;
	
	public Module(KeybindSetting keyBind) {
		this.keyBind = keyBind;
		this.addSetting(keyBind);
		SettingManager.registerSetting(this.keyBind, Main.getInstance().settingManager.modules_category);
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Module.Category getCategory() {
		return this.category;
	}

	public void setCategory(Module.Category category) {
		this.category = category;
	}

	public KeybindSetting getBind() {
		return this.keyBind;
	}


	public boolean getState() {
		return this.state;
	}

	public void setState(boolean state) {
		this.onToggle();
		if(this.state = state) return;
		if (state) {
			this.onEnable();
			this.state = true;
		} else {
			this.onDisable();
			this.state = false;
		}

	}

	public void addSetting(Setting<?> setting) {
		this.settings.add(setting);
	}
	
	public List<Setting<?>> getSettings() {
		return this.settings;
	}
	
	public boolean hasSettings() {
		return !this.settings.isEmpty();
	}

	public abstract void onDisable();

	public abstract void onEnable();

	public abstract void onToggle();
	
	public void toggle() {
		if(this.state) {
			this.onDisable();
		}else {
			this.onEnable();
		}
		this.setState(!this.getState());
	}

	public final boolean isCategory(Module.Category category) {
		return category == this.category;
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface ModInfo {
		String name();

		String description();

		Module.Category category();

		int bind();
	}

	public static enum Category {
		Combat(), Movement(), Render(), World(), Misc();
		Module module;
		
		private Category() {
			
		}
	}
}