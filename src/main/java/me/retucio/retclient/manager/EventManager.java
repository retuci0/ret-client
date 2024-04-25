package me.retucio.retclient.manager;

import com.google.common.eventbus.Subscribe;

import me.retucio.retclient.RetClient;
import me.retucio.retclient.event.Stage;
import me.retucio.retclient.event.events.*;
import me.retucio.retclient.features.Feature;
import me.retucio.retclient.features.command.Command;
import me.retucio.retclient.util.models.Timer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.util.Formatting;

public class EventManager extends Feature {
	
    @SuppressWarnings("unused")
	private final Timer logoutTimer = new Timer();

    public void init() {
        EVENT_BUS.register(this);
    }

    public void onUnload() {
        EVENT_BUS.unregister(this);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        mc.getWindow().setTitle("Ret Client v" + RetClient.VERSION);
        
        if (!fullNullCheck()) {
            RetClient.moduleManager.onUpdate();
            RetClient.moduleManager.sortModules(true);
            onTick();
        }
    }

    public void onTick() {
        if (fullNullCheck()) return;
        
        RetClient.moduleManager.onTick();
        
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == null || player.getHealth() > 0.0F)
                continue;
            
            EVENT_BUS.post(new DeathEvent(player));
        }
    }

    @Subscribe
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (fullNullCheck())
            return;
        
        if (event.getStage() == Stage.PRE) {
            RetClient.speedManager.updateValues();
            RetClient.rotationManager.updateRotations();
            RetClient.positionManager.updatePosition();
        }
        
        if (event.getStage() == Stage.POST) {
            RetClient.rotationManager.restoreRotations();
            RetClient.positionManager.restorePosition();
        }
    }

    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        RetClient.serverManager.onPacketReceived();
        
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket)
            RetClient.serverManager.update();
    }

    @Subscribe
    public void onWorldRender(Render3DEvent event) {
        RetClient.moduleManager.onRender3D(event);
    }

    @Subscribe 
    public void onRenderGameOverlayEvent(Render2DEvent event) {
        RetClient.moduleManager.onRender2D(event);
    }

    @Subscribe 
    public void onKeyInput(KeyEvent event) {
        RetClient.moduleManager.onKeyPressed(event.getKey());
    }

    @Subscribe 
    public void onChatSent(ChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.cancel();
            
            try {
            	
                if (event.getMessage().length() > 1) {
                    RetClient.commandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                } 
                
                else {
                    Command.sendMessage("Please enter a command.");
                }
            } 
            
            catch (Exception e) {
                e.printStackTrace();
                Command.sendMessage(Formatting.RED + "An error occurred while running this command. Check the log!");
            }
        }
    }
}