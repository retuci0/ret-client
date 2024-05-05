package me.retucio.retclient.features.modules.misc;

import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;

public class Timer extends Module {
	
	// see rendertickcountermixin for full code
	
	public final Setting<Float> tps = register(new Setting<>("TPS", 1f, 0.01f, 20f));

	public Timer() {
		super("Timer", "Changes how fast the game updates", Category.MISC, true, false, false);
	}
}