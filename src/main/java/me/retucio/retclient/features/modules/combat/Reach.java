package me.retucio.retclient.features.modules.combat;

import com.google.common.eventbus.Subscribe;

import me.retucio.retclient.event.events.ReachEvent;
import me.retucio.retclient.features.modules.Module;
import me.retucio.retclient.features.settings.Setting;

public class Reach extends Module {
	
	private final Setting<Float> reach = register(new Setting<>("Blocks", 4f, 3f, 10f));

	public Reach() {
		super("Reach", "Turns you into long armed popbob", Category.COMBAT, true, false, false);
	}
	
	@Subscribe
	public void onReach(ReachEvent event) {
		event.setReach(event.getReach() + reach.getValue());
	}
}