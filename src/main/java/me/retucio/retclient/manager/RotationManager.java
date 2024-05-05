package me.retucio.retclient.manager;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import me.retucio.retclient.util.MathUtil;
import me.retucio.retclient.util.traits.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class RotationManager implements Util {
	
    private float yaw;
    private float pitch;

    public void updateRotations() {
        this.yaw = mc.player.getYaw();
        this.pitch = mc.player.getPitch();
    }

    public void restoreRotations() {
        mc.player.setYaw(yaw);
        mc.player.headYaw = yaw;
        mc.player.setPitch(pitch);
    }

    public void setPlayerRotations(float yaw, float pitch) {
        mc.player.setYaw(yaw);
        mc.player.headYaw = yaw;
        mc.player.setPitch(pitch);
    }

    public void setPlayerYaw(float yaw) {
        mc.player.setYaw(yaw);
        mc.player.headYaw = yaw;
    }

    public void lookAtPos(BlockPos pos) {
        float[] angle = MathUtil.calcAngle(mc.player.getEyePos(), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f));
        this.setPlayerRotations(angle[0], angle[1]);
    }

    public void lookAtVec3d(Vec3d vec3d) {
        float[] angle = MathUtil.calcAngle(mc.player.getEyePos(), new Vec3d(vec3d.x, vec3d.y, vec3d.z));
        this.setPlayerRotations(angle[0], angle[1]);
    }

    public void lookAtVec3d(double x, double y, double z) {
        Vec3d vec3d = new Vec3d(x, y, z);
        this.lookAtVec3d(vec3d);
    }

    public void lookAtEntity(Entity entity) {
        float[] angle = MathUtil.calcAngle(mc.player.getEyePos(), entity.getEyePos());
        this.setPlayerRotations(angle[0], angle[1]);
    }

    public void setPlayerPitch(float pitch) {
        mc.player.setPitch(pitch);
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
    
    public static float[] getSmoothRotations(LivingEntity entity) {
        double deltaX = entity.getPos().getX() + (entity.getPos().getX() - entity.lastRenderX) - mc.player.getPos().getX();
        double deltaY = entity.getPos().getY() - 3.5 + entity.getEyeHeight(entity.getPose()) - mc.player.getPos().getY() + mc.player.getEyeHeight(mc.player.getPose());
        double deltaZ = entity.getPos().getZ() + (entity.getPos().getZ() - entity.lastRenderZ) - mc.player.getPos().getZ();
        
        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));

        float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ));
        float pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

        if (deltaX < 0 && deltaZ < 0) {
            yaw = (float) (90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        } 
        
        else if (deltaX > 0 && deltaZ < 0) {
            yaw = (float) (-90 + Math.toDegrees(Math.atan(deltaZ / deltaX)));
        }

        return new float[] {yaw, pitch};
    }

}
