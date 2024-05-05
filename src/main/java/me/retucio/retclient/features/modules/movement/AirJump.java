package me.retucio.retclient.features.modules.movement;

import com.google.common.eventbus.Subscribe;

import me.retucio.retclient.event.events.KeyEvent;
import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;

public class AirJump extends Module {

    private int y;
    
    private final Setting<Boolean> maintainY = register(new Setting<>("Maintain Y", false));

    public AirJump() {
        super("AirJump", "Allows you to jump multiple times while on the air", Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onEnable() {
        y = mc.player.getBlockPos().getY();
    }
    
    @Override
    public void onTick() {
    	if (mc.player.isOnGround()) return;
    	
    	if (maintainY.getValue() && mc.player.getBlockPos().getY() == y && mc.options.jumpKey.isPressed()) {
    		mc.player.jump();
    	}
    }

    @Subscribe
    private void onKeyInput(KeyEvent event) {
        if (mc.currentScreen != null || mc.player.isOnGround()) return;

        if (mc.options.jumpKey.matchesKey(event.getKey(), 0)) {
            y = mc.player.getBlockPos().getY();
            mc.player.jump();
        }
        
        else if (mc.options.sneakKey.matchesKey(event.getKey(), 0)) {
            y--;
        }
    }
}