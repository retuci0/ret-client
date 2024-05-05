package me.retucio.retclient.event.events;

import me.retucio.retclient.event.Event;
import me.retucio.retclient.event.Stage;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

public class UpdateWalkingPlayerEvent extends Event {
	
    private final Stage stage;
	private MovementType type;
	private Vec3d vec;

    public UpdateWalkingPlayerEvent(Stage stage, MovementType type, Vec3d vec) {
        this.stage = stage;
        this.type = type;
        this.vec = vec;
    }

    public Stage getStage() {
        return stage;
    }
    
	public MovementType getType() {
		return type;
	}

	public void setType(MovementType type) {
		this.type = type;
	}

	public Vec3d getVec() {
		return vec;
	}

	public void setVec(Vec3d vec) {
		this.vec = vec;
	}
}
