package net.retclient;

import net.retclient.altmanager.AltManager;
import net.retclient.cmd.CommandManager;
import net.retclient.cmd.GlobalChat;
import net.retclient.event.EventManager;
import net.retclient.gui.GuiManager;
import net.retclient.gui.font.FontManager;
import net.retclient.misc.RenderUtils;
import net.retclient.mixin.interfaces.IMinecraftClient;
import net.retclient.module.ModuleManager;
import net.retclient.settings.SettingManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class Client {
	public static final String NAME = "Ret";
	public static final String VERSION = "1.20.4";
	public static final String CLIENT_VERSION = "1.4.0";
	
	public static MinecraftClient MC;
	public static IMinecraftClient IMC;
	
	// Systems
	public ModuleManager moduleManager;
	public CommandManager commandManager;
	public AltManager altManager;
	public GuiManager hudManager;
	public FontManager fontManager;
	public SettingManager settingManager;
	public RenderUtils renderUtils;
	public GlobalChat globalChat;
	public EventManager eventManager;
	
	private boolean ghostMode;
	
	public void Initialize() {
		// Gets instance of Minecraft
		MC = MinecraftClient.getInstance();
		IMC = (IMinecraftClient)MC;
	}
	
	public void loadAssets() {
		System.out.println("[Ret] Starting Client");
		
		eventManager = new EventManager();
		
		renderUtils = new RenderUtils();
		System.out.println("[Ret] Reading Settings");
		settingManager = new SettingManager();
		System.out.println("[Ret] Initializing Modules");
		moduleManager = new ModuleManager();
		System.out.println("[Ret] Initializing Commands");
		commandManager = new CommandManager();
		System.out.println("[Ret] Initializing Font Manager");
		fontManager = new FontManager();
		fontManager.Initialize();
		System.out.println("[Ret] Initializing GUI");
		hudManager = new GuiManager();
		hudManager.Initialize();
		System.out.println("[Ret] Loading Alts");
		altManager = new AltManager();
		System.out.println("[Ret] Ret is hungry, feed him some unprotected servers");
 
		SettingManager.loadSettings("config_category", settingManager.config_category);
		SettingManager.loadSettings("modules_category", settingManager.modules_category);
		SettingManager.loadSettings("hidden_category", settingManager.hidden_category);

		globalChat = new GlobalChat();
		globalChat.StartListener();
	}
	
	public void update() {
		moduleManager.update();
		hudManager.update();
	}

	/**
	 * Renders the HUD every frame
	 * @param context The current Matrix Stack
	 * @param partialTicks Delta between ticks
	 */
	public void drawHUD(DrawContext context, float partialTicks) {
		// If the program is not in Ghost Mode, draw UI.
		if (!ghostMode) {
			hudManager.draw(context, partialTicks);
		}
	}

	/**
	 * Toggles Ghost Mode. (No UI)
	 */
	public void toggleGhostMode() {
		ghostMode = !ghostMode;
	}
	
	/**
	 * Returns whether Aoba is currently in Ghost Mode. (No UI)
	 * @return Ghost Mode
	 */
	public boolean isGhosted() {
		return this.ghostMode;
	}
	
	/**
	 * Called when the client is shutting down.
	 */
	public void endClient() {
		try {
			SettingManager.saveSettings("config_category", settingManager.config_category);
			SettingManager.saveSettings("modules_category", settingManager.modules_category);
			SettingManager.saveSettings("hidden_category", settingManager.hidden_category);
			altManager.saveAlts();
			moduleManager.modules.forEach(s -> s.onDisable());
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("[Ret] Shutting down...");
	}
}