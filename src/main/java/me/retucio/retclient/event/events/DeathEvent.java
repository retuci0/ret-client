package me.retucio.retclient.event.events;

import me.retucio.retclient.event.Event;
import net.minecraft.entity.LivingEntity;

public class DeathEvent extends Event {
	
    private final LivingEntity entity;

    public DeathEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }
}
