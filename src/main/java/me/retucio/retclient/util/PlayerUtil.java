package me.retucio.retclient.util;

import me.retucio.retclient.mixin.accessors.ClientPlayerEntityAccessor;
import me.retucio.retclient.util.interfaces.IVec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class PlayerUtil {

	public static MinecraftClient mc = MinecraftClient.getInstance();
	
	public static final double diagonal = 1 / Math.sqrt(2);
	public static final Vec3d horizontalVelocity = new Vec3d(0, 0, 0);
	public static boolean rotating = false;
	
	public static float[] getPlayerFacingRotations(double posX, double posY, double posZ) {
        double deltaX = posX - mc.player.getX();
        double deltaY = posY - mc.player.getY() - mc.player.getEyeHeight(mc.player.getPose());
        double deltaZ = posZ - mc.player.getZ();
        
        double deltaGround = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        float pitch = (float) - Math.toDegrees(Math.atan(deltaY/deltaGround));
        float yaw = (float) - Math.toDegrees(Math.atan(deltaX/deltaZ));

        if (deltaZ <= 0) {
            if (deltaX > 0) yaw = yaw - 180F;
            else yaw = yaw + 180F;
        }

        return new float[] {pitch, yaw};
    }

    public static double[] getPlayerMoveVec() {

        float yaw = mc.player.getYaw();
        float forward = mc.player.forwardSpeed;
        float sideways = mc.player.sidewaysSpeed;

        if (forward == 0 && sideways == 0) {
            return new double[] {0, 0};
        }

        if (forward > 0) {

            if (sideways > 0) yaw = yaw - 45;
            else if (sideways < 0) yaw = yaw + 45;
        } 
        
        else if (forward < 0) {

            yaw = yaw - 180;
            if (sideways > 0) yaw = yaw + 45;
            else if (sideways < 0) yaw = yaw - 45;
        } 
        
        else {
        	
            if (sideways > 0) yaw = yaw - 90;
            else if (sideways < 0) yaw = yaw + 90;
        }

        return new double[] {
        	  - Math.sin(Math.toRadians(yaw)), 
        		Math.cos(Math.toRadians(yaw))
        };
    }
    
    public static Vec3d getHorizontalVelocity(double bps) {
        float yaw = mc.player.getYaw();

        Vec3d forward = Vec3d.fromPolar(0, yaw);
        Vec3d right = Vec3d.fromPolar(0, yaw + 90);
        
        double velX = 0;
        double velZ = 0;

        boolean a = false;
        if (mc.player.input.pressingForward) {
            velX += forward.x / 20 * bps;
            velZ += forward.z / 20 * bps;
            a = true;
        }
        if (mc.player.input.pressingBack) {
            velX -= forward.x / 20 * bps;
            velZ -= forward.z / 20 * bps;
            a = true;
        }

        boolean b = false;
        if (mc.player.input.pressingRight) {
            velX += right.x / 20 * bps;
            velZ += right.z / 20 * bps;
            b = true;
        }
        if (mc.player.input.pressingLeft) {
            velX -= right.x / 20 * bps;
            velZ -= right.z / 20 * bps;
            b = true;
        }

        if (a && b) {
            velX *= diagonal;
            velZ *= diagonal;
        }

        ((IVec3d) horizontalVelocity).setXZ(velX, velZ);
        return horizontalVelocity;
    }
    
    public static void packetFacePitchAndYaw(float yaw, float pitch) {
        float preYaw = mc.player.getYaw();
        float prePitch = mc.player.getPitch();
        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
        rotating = true;
        ((ClientPlayerEntityAccessor) mc.player).invokeSync();
        mc.player.setYaw(preYaw);
        mc.player.setPitch(prePitch);
    }
}