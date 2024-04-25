package me.retucio.retclient.features.modules.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import me.retucio.retclient.RetClient;
import me.retucio.retclient.features.command.Command;
import me.retucio.retclient.features.modules.Module;

public class MCF extends Module {
	
    private boolean pressed;
    
    public MCF() {
        super("MCF", "Friends", Category.MISC, true, false, false);
    }

    @Override 
    public void onTick() {
        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 2) == 1) {
        	
            if (!pressed) {
                Entity targetedEntity = mc.targetedEntity;
                
                if (!(targetedEntity instanceof PlayerEntity)) return;
                
                String name = ((PlayerEntity) targetedEntity).getGameProfile().getName();

                if (RetClient.friendManager.isFriend(name)) {
                    RetClient.friendManager.removeFriend(name);
                    Command.sendMessage(Formatting.RED + name + Formatting.RED + " has been unfriended.");
                    
                } 
                
                else {
                    RetClient.friendManager.addFriend(name);
                    Command.sendMessage(Formatting.AQUA + name + Formatting.AQUA + " has been friended.");
                }

                pressed = true;
            }
            
        } 
        
        else {
            pressed = false;
        }
    }
}
