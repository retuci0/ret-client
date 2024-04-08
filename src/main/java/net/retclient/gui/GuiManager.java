package net.retclient.gui;

import java.util.HashMap;
import net.retclient.event.events.KeyDownEvent;
import net.retclient.event.events.LeftMouseDownEvent;
import net.retclient.event.events.LeftMouseUpEvent;
import net.retclient.event.listeners.LeftMouseDownListener;
import net.retclient.event.listeners.LeftMouseUpListener;
import net.retclient.event.listeners.KeyDownListener;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import net.retclient.module.Module;
import net.retclient.Main;
import net.retclient.gui.hud.AbstractHud;
import net.retclient.gui.hud.ArmorHud;
import net.retclient.gui.hud.InfoHud;
import net.retclient.gui.hud.ModuleSelectorHud;
import net.retclient.gui.hud.RadarHud;
import net.retclient.gui.tabs.*;
import net.retclient.gui.tabs.components.ModuleComponent;
import net.retclient.gui.tabs.components.StackPanelComponent;
import net.retclient.misc.RainbowColor;
import net.retclient.misc.RenderUtils;
import net.retclient.module.Module.Category;
import net.retclient.settings.SettingManager;
import net.retclient.settings.types.BooleanSetting;
import net.retclient.settings.types.ColorSetting;
import net.retclient.settings.types.FloatSetting;
import net.retclient.settings.types.KeybindSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

public class GuiManager implements LeftMouseDownListener, LeftMouseUpListener, KeyDownListener {
	protected MinecraftClient mc = MinecraftClient.getInstance();

	public KeybindSetting clickGuiButton = new KeybindSetting("key.clickgui", "ClickGUI Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_GRAVE_ACCENT, 0));
	private KeyBinding esc = new KeyBinding("key.esc", GLFW.GLFW_KEY_ESCAPE, "key.categories.aoba");

	private boolean clickGuiOpen = false;

	public static AbstractGui currentGrabbed = null;

	private HashMap<Object, AbstractHud> pinnedHuds = new HashMap<Object, AbstractHud>();

	// Navigation Bar and Pages
	public NavigationBar clickGuiNavBar;
	public Page modulesPane = new Page("Modules");
	public Page toolsPane = new Page("Tools");
	public Page hudPane = new Page("Hud");
	
	// Global HUD Settings
	public static ColorSetting foregroundColor;
	public static ColorSetting borderColor;
	public static ColorSetting backgroundColor;
	
	public FloatSetting effectSpeed = new FloatSetting("color_speed", "Effect Spd", 4f, 1f, 20f, 0.1f, null);
	public BooleanSetting rainbow = new BooleanSetting("rainbow_mode", "Rainbow", false, null);
	public BooleanSetting ah = new BooleanSetting("armorhud_toggle", "ArmorHUD", false, null);

	public ModuleSelectorHud moduleSelector;
	public ArmorHud armorHud;
	public RadarHud radarHud;
	public InfoHud infoHud;

	public GuiManager() {
		mc = MinecraftClient.getInstance();
		
		borderColor = new ColorSetting("hud_border_color", "Color of the borders.", new Color(0, 0, 0));
		backgroundColor = new ColorSetting("hud_background_color", "Color of the background.", new Color(0, 0, 0, 50));
		foregroundColor = new ColorSetting("hud_foreground_color", "The color of the HUD", new Color(1.0f, 1.0f, 1.0f));
		clickGuiNavBar = new NavigationBar();
		
		SettingManager.registerSetting(borderColor, Main.getInstance().settingManager.config_category);
		SettingManager.registerSetting(backgroundColor, Main.getInstance().settingManager.config_category);
		SettingManager.registerSetting(foregroundColor, Main.getInstance().settingManager.config_category);
		SettingManager.registerSetting(clickGuiButton, Main.getInstance().settingManager.modules_category);
		
		Main.getInstance().eventManager.AddListener(KeyDownListener.class, this);
	}

	public void Initialize() {
		toolsPane.AddHud(new AuthCrackerTab("Auth Cracker", 810, 500));
		
		moduleSelector = new ModuleSelectorHud();
		armorHud = new ArmorHud(790, 500, 200, 50);
		radarHud = new RadarHud(590, 500, 180, 180);
		infoHud = new InfoHud(100, 500);
		
		hudPane.AddHud(new HudsTab(new AbstractHud[] { moduleSelector, armorHud,radarHud, infoHud }));

		int xOffset = 50;
		for (Category category : Module.Category.values()) {
			ClickGuiTab tab = new ClickGuiTab(category.name(), xOffset, 75, true, category.name());
			
			StackPanelComponent stackPanel = new StackPanelComponent(tab);
			stackPanel.setTop(30);
			for (Module module : Main.getInstance().moduleManager.modules) {
				if (module.getCategory() == category) {
					ModuleComponent button = new ModuleComponent(module.getName(), stackPanel, module);
					stackPanel.addChild(button);
				}
			}
			tab.addChild(stackPanel);
			tab.setWidth(180);
			modulesPane.AddHud(tab);
			xOffset += tab.getWidth() + 10;
		}
		
		clickGuiNavBar.addPane(modulesPane);
		clickGuiNavBar.addPane(toolsPane);
		clickGuiNavBar.addPane(hudPane);
		//clickGuiNavBar.addPane(settingsPane);

		SettingManager.registerSetting(effectSpeed, Main.getInstance().settingManager.config_category);
		SettingManager.registerSetting(rainbow, Main.getInstance().settingManager.config_category);
		SettingManager.registerSetting(ah, Main.getInstance().settingManager.config_category);
		
		Main.getInstance().eventManager.AddListener(LeftMouseDownListener.class, this);
		Main.getInstance().eventManager.AddListener(LeftMouseUpListener.class, this);
		
		clickGuiNavBar.setSelectedIndex(0);
	}
	
	public void AddHud(AbstractGui hud, String pageName) {
		for(Page page : clickGuiNavBar.getPanes()) {
			if(page.getTitle().equals(pageName)) {
				page.tabs.add(hud);
				break;
			}
		}
	}
	
	public void RemoveHud(AbstractGui hud, String pageName) {
		for(Page page : clickGuiNavBar.getPanes()) {
			if(page.getTitle().equals(pageName)) {
				page.tabs.remove(hud);
				break;
			}
		}
	}
	
	@Override
	public void OnKeyDown(KeyDownEvent event) {
		if(clickGuiButton.getValue().getCode() == event.GetKey()) {
			this.clickGuiOpen = !this.clickGuiOpen;
			this.toggleMouse();
		}
	}
	
	public void SetHudActive(AbstractHud hud, boolean state) {
		if(state) {
			this.pinnedHuds.put(hud.getClass(), hud);
			hud.activated.silentSetValue(true);
		}
		else {
			this.pinnedHuds.remove(hud.getClass());
			hud.activated.silentSetValue(false);
		}
	}
	
	/**
	 * Getter for the current color used by the GUI for text rendering.
	 * @return Current Color
	 */
	public void update() {
		if(!Main.getInstance().isGhosted()){

			/**
			 * Moves the selected Tab to where the user moves their mouse.
			 */
			if (this.clickGuiOpen) {
				clickGuiNavBar.update();
			}

			/**
			 * Updates each of the Tab GUIs that are currently on the screen.
			 */
			for(AbstractGui hud : pinnedHuds.values()) {
				hud.update();
			}

			if (this.esc.isPressed() && this.clickGuiOpen) {
				this.clickGuiOpen = !this.clickGuiOpen;
				this.toggleMouse();
			}
		}
		
		/**
		 * Updates the Color. 
		 * TODO: Remove this and move to event-based.
		 */
		//if(this.rainbow.getValue()) {
		//	rainbowColor.update(this.effectSpeed.getValue().floatValue());
		//	this.currentColor = rainbowColor.getColor();
		//}else {
		//	this.currentColor = foregroundColor.getValue();
		//}
		
		//Main.getInstance().eventManager.Fire(new MouseScrollEvent(5.0f, 5.0f));
	}

	public void draw(DrawContext drawContext, float tickDelta) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		MatrixStack matrixStack = drawContext.getMatrices();
		matrixStack.push();
		
		int guiScale = mc.getWindow().calculateScaleFactor(mc.options.getGuiScale().getValue(), mc.forcesUnicodeFont());
		matrixStack.scale(1.0f / guiScale, 1.0f / guiScale, 1.0f);

		Window window = mc.getWindow();
		
		/**
		 * Render ClickGUI and Sidebar
		 */
		if (this.clickGuiOpen) {
			RenderUtils.drawBox(matrixStack, 0, 0, window.getWidth(), window.getHeight(), new Color(26, 26, 26, 100));
			clickGuiNavBar.draw(drawContext, tickDelta);
		}
		
		// Render HUDS
		if(!this.clickGuiOpen || this.clickGuiNavBar.getSelectedPage() == this.hudPane) {
			for(AbstractGui hud : pinnedHuds.values()) {
				if(hud.getVisible()) {
					hud.draw(drawContext, tickDelta);
				}
			}
		}
		
		
		matrixStack.pop();
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	/**
	 * Gets whether or not the Click GUI is currently open.
	 * @return State of the Click GUI.
	 */
	public boolean isClickGuiOpen() {
		return this.clickGuiOpen;
	}

	public void setClickGuiOpen(boolean state) {
		this.clickGuiOpen = state;	
		currentGrabbed = null;
	}
	
	/**
	 * Locks and unlocks the Mouse.
	 */
	public void toggleMouse() {
		if(this.mc.mouse.isCursorLocked()) {
			this.mc.mouse.unlockCursor();
		}else {
			this.mc.mouse.lockCursor();
		}
	}

	@Override
	public void OnLeftMouseDown(LeftMouseDownEvent event) {
		if (this.clickGuiOpen) {
			event.SetCancelled(true);
		}
	}

	@Override
	public void OnLeftMouseUp(LeftMouseUpEvent event) {
		currentGrabbed = null;
	}
}
