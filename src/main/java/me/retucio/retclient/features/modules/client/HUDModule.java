package me.retucio.retclient.features.modules.client;

import me.retucio.retclient.RetClient;
import me.retucio.retclient.event.events.Render2DEvent;
import me.retucio.retclient.features.modules.Module;

public class HUDModule extends Module {
	
    public HUDModule() {
        super("HUD", "HUD", Category.CLIENT, true, false, false);
    }

    @Override 
    public void onRender2D(Render2DEvent event) {
        event.getContext().drawTextWithShadow(
                mc.textRenderer,
                RetClient.NAME + " " + RetClient.VERSION,
                2, 2,
                -1
        );
    }
}
