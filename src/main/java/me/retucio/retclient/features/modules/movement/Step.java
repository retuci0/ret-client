package me.retucio.retclient.features.modules.movement;

import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;

public class Step extends Module {
	
    private final Setting<Float> height = register(new Setting<>("Height", 2f, 1f, 3f, v -> true));
    
    public Step() {
        super("Step", "Makes you able to step higher up", Category.MOVEMENT, true, false, false);
    }

    @Override 
    public void onDisable() {
        if (nullCheck()) return;
        mc.player.setStepHeight(0.6f);
    }

    @Override 
    public void onUpdate() {
        if (nullCheck()) return;
        mc.player.setStepHeight(height.getValue());
    }
}
