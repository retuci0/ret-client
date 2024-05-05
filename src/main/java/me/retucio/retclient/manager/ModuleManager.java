package me.retucio.retclient.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.retucio.retclient.event.events.Render2DEvent;
import me.retucio.retclient.event.events.Render3DEvent;
import me.retucio.retclient.features.Feature;
import me.retucio.retclient.features.modules.Module;
//import me.retucio.retclient.features.modules.chat.*;
import me.retucio.retclient.features.modules.client.*;
import me.retucio.retclient.features.modules.combat.*;
import me.retucio.retclient.features.modules.exploit.*;
import me.retucio.retclient.features.modules.misc.*;
import me.retucio.retclient.features.modules.movement.*;
import me.retucio.retclient.features.modules.player.*;
import me.retucio.retclient.features.modules.render.*;
import me.retucio.retclient.util.traits.Jsonable;
import me.retucio.retclient.util.traits.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager implements Jsonable, Util {
	
    public List<Module> modules = new ArrayList<>();
    public List<Module> sortedModules = new ArrayList<>();
    public List<String> sortedModulesABC = new ArrayList<>();
    
    // and there it goes
    
    // client
    public static ClickGUI clickGUI = new ClickGUI();
    public static HUDModule hudModule = new HUDModule();
    public static MCF mcf = new MCF();
    
    // combat
    public static AimBot aimBot = new AimBot();
    public static AutoTotem autoTotem = new AutoTotem();
    public static Criticals criticals = new Criticals();
    public static TriggerBot triggerBot = new TriggerBot();
    
    // exploit
    public static CoordLogger coordLogger = new CoordLogger();
    public static Crasher crasher = new Crasher();
    public static HitboxDesync hitboxDesync = new HitboxDesync();
    public static InstantBowKill instantBowKill = new InstantBowKill();
    public static DeathScreenInvincibility invincibility = new DeathScreenInvincibility();
    public static PortalInvincibility portalGodMode = new PortalInvincibility();
    
    // misc
    public static ColoredSigns coloredSigns = new ColoredSigns();
    public static PacketCanceller packetCanceller = new PacketCanceller();
    public static Timer timer = new Timer();
    
    // movement
    public static AirJump airJump = new AirJump();
    public static BoatFly boatFly = new BoatFly();
    public static ElytraFly elytraFly = new ElytraFly();
    public static EntitySpeed entitySpeed = new EntitySpeed();
    public static Jesus jesus = new Jesus();
    public static FastJump fastJump = new FastJump();
    public static FastSwim fastSwim = new FastSwim();
    public static Flight flight = new Flight();
    public static IceSpeed iceSpeed = new IceSpeed();
    public static InventoryMove invMove = new InventoryMove();
    public static ReverseStep reverseStep = new ReverseStep();
    public static Spider spider = new Spider();
    public static Step step = new Step();
    public static TridentBoost tridentBoost = new TridentBoost();
    
    // player
    public static AntiHunger antiHunger = new AntiHunger();
    public static AutoMount autoMount = new AutoMount();
    public static FakePlayer fakePlayer = new FakePlayer();
    public static FastBreak fastBreak = new FastBreak();
    public static FastPlace fastPlace = new FastPlace();
    public static NoFall noFall = new NoFall();
    public static Velocity velocity = new Velocity();
    
    // render
    public static FullBright fullBright = new FullBright();
    public static Zoom zoom = new Zoom();

    public void init() {
    	initChat();
    	initClient();
    	initCombat();
    	initExploit();
    	initMisc();
    	initMovement();
    	initPlayer();
    	initRender();
    }
    
    public void initChat() {
//    	modules.add(new Greeter());
    }
    
    public void initClient() {
    	modules.add(clickGUI);
    	modules.add(hudModule);
    	modules.add(mcf);
    }
    
    public void initCombat() {
    	modules.add(aimBot);
    	modules.add(autoTotem);
    	modules.add(criticals);
    	modules.add(triggerBot);
    }
    
    public void initExploit() {
    	modules.add(coordLogger);
    	modules.add(crasher);
    	modules.add(hitboxDesync);
    	modules.add(instantBowKill);
    	modules.add(invincibility);
    	modules.add(portalGodMode);
    }

    public void initMisc() {
    	modules.add(coloredSigns);
    	modules.add(packetCanceller);
    	modules.add(timer);
    }
    
    public void initMovement() {
    	modules.add(airJump);
    	modules.add(boatFly);
    	modules.add(elytraFly);
    	modules.add(entitySpeed);
    	modules.add(jesus);
    	modules.add(fastJump);
    	modules.add(fastSwim);
    	modules.add(flight);
    	modules.add(iceSpeed);
    	modules.add(invMove);
    	modules.add(reverseStep);
    	modules.add(spider);
    	modules.add(step);
    	modules.add(tridentBoost);
    }
    
    public void initPlayer() {
    	modules.add(antiHunger);
    	modules.add(autoMount);
    	modules.add(fakePlayer);
    	modules.add(fastPlace);
    	modules.add(fastBreak);
    	modules.add(noFall);
    	modules.add(velocity);
    }
    
    public void initRender() {
    	modules.add(fullBright);
    	modules.add(zoom);
    }
    
    public Module getModuleByName(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        
        return null;
    }

    @SuppressWarnings("unchecked")
	public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        
        return null;
    }

    public void enableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<>();
        for (Module module : this.modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        
        return enabledModules;
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<>();
        for (Module module : this.modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add(module);
            }
        });
        
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(EVENT_BUS::register);
        this.modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn)
                .sorted(Comparator.comparing(module -> mc.textRenderer.getWidth(module.getFullArrayString()) * (reverse ? -1 : 1)))
                .collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onUnload() {
        this.modules.forEach(EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : this.modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey <= 0) return;
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) module.toggle();
        });
    }

    @Override 
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        
        for (Module module : modules) {
            object.add(module.getName(), module.toJson());
        }
        
        return object;
    }

    @Override 
    public void fromJson(JsonElement element) {
        for (Module module : modules) {
            module.fromJson(element.getAsJsonObject().get(module.getName()));
        }
    }

    @Override 
    public String getFileName() {
        return "modules.json";
    }
}
