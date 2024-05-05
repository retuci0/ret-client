package me.retucio.retclient.util.interfaces;

import org.joml.Vector3d;

import net.minecraft.util.math.Vec3i;

public interface IVec3d {
	
    void set(double x, double y, double z);

    default void set(Vec3i vec) {
        set(vec.getX(), vec.getY(), vec.getZ());
    }

    default void set(Vector3d vec) {
        set(vec.x, vec.y, vec.z);
    }

    void setXZ(double x, double z);

    void setY(double y);
}