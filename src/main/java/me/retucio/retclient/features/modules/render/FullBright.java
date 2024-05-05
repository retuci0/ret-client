package me.retucio.retclient.features.modules.render;

import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class FullBright extends Module {
	
	private double lastGamma;
	
	private final Setting<Mode> mode = register(new Setting<>("Mode", Mode.GAMMA));

	public FullBright() {
		super("FullBright", "Makes everything brighter", Category.RENDER, true, true, false);
	}
	
	@Override
	public void onEnable() {
		lastGamma = mc.options.getGamma().getValue();
	}
	
	@Override
	public void onDisable() {
		if (mode.getValue() == Mode.GAMMA) mc.options.getGamma().setValue(lastGamma);
		mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
	}
	
	@Override
	public void onUpdate() {
		switch(mode.getValue()) {
			case Mode.GAMMA: 
				mc.options.getGamma().setValue(1000D);
				
			case Mode.POTION:
				mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 500, 0));
		}
	}
	
	private enum Mode {
		GAMMA,
		POTION;
	}
}