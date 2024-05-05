package me.retucio.retclient.features.modules.movement;

import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;

public class TridentBoost extends Module {

    private final Setting<Float> multiplier = register(new Setting<>("Boost", 2f, 0.1f, 2f));

    private final Setting<Boolean> allowOutOfWater = register(new Setting<>("OutOfWater", true));

    public TridentBoost() {
        super("TridentBoost", "Exploits the trident's riptime enchantment to make you travel very fast", Category.MOVEMENT, true, true, false);
    }

    public double getMultiplier() {
        return isEnabled() ? multiplier.getValue() : 1;
    }

    public boolean allowOutOfWater() {
        return isEnabled() ? allowOutOfWater.getValue() : false;
    }
}