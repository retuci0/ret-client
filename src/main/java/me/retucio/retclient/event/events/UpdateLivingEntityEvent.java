package me.retucio.retclient.event.events;

import me.retucio.retclient.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class UpdateLivingEntityEvent extends Event {
	
    private final Entity entity;
    private final Vec3d movement;

    public UpdateLivingEntityEvent(Entity entity, Vec3d movement) {
        this.entity = entity;
        this.movement = movement;
    }

    public Entity getEntity() {
        return entity;
    }
    
    public Vec3d getMovement() {
    	return movement;
    }

    public static class Pre extends UpdateLivingEntityEvent {
        public Pre(Entity entity, Vec3d movement) {
            super(entity, movement);
        }
    }

    public static class Post extends UpdateLivingEntityEvent {
        public Post(Entity entity, Vec3d movement) {
            super(entity, movement);
        }
    }
}