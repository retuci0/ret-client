package me.retucio.retclient.features.modules.movement;

import java.util.Arrays;

import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import me.retucio.retclient.mixin.accessors.AbstractBlockAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.block.Block;


public class IceSpeed extends Module {
	
	private Setting<Float> speed = register(new Setting<>("Speed", 0.4f, 0.1f, 2f, v -> true));

    public IceSpeed() {
        super("IceSpeed", "Makes ice speedy", Category.MOVEMENT, true, false, false);
    }
    
    @Override
    public void onUpdate() {
        if (nullCheck()) return;
        setSlipperiness(speed.getValue());
    }
    
    @Override
    public void onDisable() {
        if (nullCheck()) return;
        setSlipperiness(0.98f);
        ((AbstractBlockAccessor) Blocks.BLUE_ICE).setSlipperiness(0.989f);
    }

    private void setSlipperiness(float speed) {
        for (Block block: Arrays.asList(Blocks.ICE, Blocks.PACKED_ICE, Blocks.FROSTED_ICE, Blocks.BLUE_ICE)) {
            ((AbstractBlockAccessor) block).setSlipperiness(speed);
        }
    }
}