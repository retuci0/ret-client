package me.retucio.retclient.event.events;

import me.retucio.retclient.event.Event;
import me.retucio.retclient.features.Feature;
import me.retucio.retclient.features.settings.Setting;

public class ClientEvent extends Event {
	
    private Feature feature;
    private Setting<?> setting;
    private int stage;

    public ClientEvent(int stage, Feature feature) {
        this.stage = stage;
        this.feature = feature;
    }

    public ClientEvent(Setting<?> setting) {
        this.stage = 2;
        this.setting = setting;
    }

    public Feature getFeature() {
        return this.feature;
    }

    public Setting<?> getSetting() {
        return this.setting;
    }

    public int getStage() {
        return stage;
    }
}
