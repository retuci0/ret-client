package me.retucio.retclient.features.modules.player;

import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import me.retucio.retclient.mixin.accessors.MinecraftClientAccessor;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.Item;

public class FastPlace extends Module {
	
    private final Setting<Whitelist> mode = register(new Setting<>("Whitelist", Whitelist.ALL));
    
    public FastPlace() {
    	super("FastPlace", "Like place but faster", Category.PLAYER, true, false, false);
    }

    @Override
    public void onTick() {
        Item item = mc.player.getInventory().getMainHandStack().getItem();

        boolean isExp = item instanceof ExperienceBottleItem;
        boolean isCrystal = item instanceof EndCrystalItem;

        switch(mode.getValue()) {
        
            case ALL:
                setClickDelay();
                break;
                
            case EXP:
                if(isExp) setClickDelay();
                break;
                
            case CRYSTAL:
                if(isCrystal) setClickDelay();
                break;
                
            case EXP_CRYSTAL:
                if(isCrystal || isExp) setClickDelay();
                break;
        }
    }

    private void setClickDelay() {
        ((MinecraftClientAccessor) mc).setItemUseCooldown(0);
    }

    @Override
    public String getInfo() {
        return mode.getValue().name();
    }

    enum Whitelist {
        ALL(),
        EXP(),
        CRYSTAL(),
        EXP_CRYSTAL()
    }
}