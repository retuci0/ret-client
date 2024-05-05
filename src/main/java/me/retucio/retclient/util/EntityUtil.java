package me.retucio.retclient.util;

import net.minecraft.entity.player.PlayerEntity;

public class EntityUtil {

	public static double[] calculateLookAt(double x, double y, double z, PlayerEntity player) {
	    double dirx = player.getX() - x;
	    double diry = player.getY() - y;
	    double dirz = player.getZ() - z;
	    
	    double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
	    
	    dirx /= len;
	    diry /= len;
	    dirz /= len;
	    
	    double pitch = Math.asin(diry);
	    double yaw = Math.atan2(dirz, dirx);

	    pitch = pitch * 180.0d / Math.PI;
	    yaw = yaw * 180.0d / Math.PI;
	    yaw += 90f;
	    
	    return new double[]{ yaw, pitch };
	}
}