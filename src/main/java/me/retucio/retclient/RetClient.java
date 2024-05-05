package me.retucio.retclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.retucio.retclient.manager.*;

// hello big guy what you doing here

public class RetClient implements ModInitializer, ClientModInitializer {
	
    public static final String NAME = "Ret Client";
    public static final String VERSION = "0.2 [1.20.4]";

    public static float TIMER = 1f;
    public static final Logger LOGGER = LogManager.getLogger("Ret");
    
    public static ServerManager serverManager;
    public static ColorManager colorManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static HoleManager holeManager;
    public static EventManager eventManager;
    public static SpeedManager speedManager;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;

    
    @Override 
    public void onInitialize() {
        eventManager = new EventManager();
        serverManager = new ServerManager();
        rotationManager = new RotationManager();
        positionManager = new PositionManager();
        friendManager = new FriendManager();
        colorManager = new ColorManager();
        commandManager = new CommandManager();
        moduleManager = new ModuleManager();
        speedManager = new SpeedManager();
        holeManager = new HoleManager();
    }


    @Override 
    public void onInitializeClient() {
        eventManager.init();
        moduleManager.init();

        configManager = new ConfigManager();
        configManager.load();
        colorManager.init();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> configManager.save()));
    }
}
