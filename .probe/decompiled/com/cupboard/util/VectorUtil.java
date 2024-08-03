package com.cupboard.util;

import net.minecraft.world.phys.Vec3;

public class VectorUtil {

    public static Vec3 rotateLeft(Vec3 vec) {
        return Math.abs(vec.x) > Math.abs(vec.z) ? new Vec3(-vec.z, vec.y, vec.x) : new Vec3(vec.z, vec.y, -vec.x);
    }

    public static Vec3 rotateRight(Vec3 vec) {
        return Math.abs(vec.x) > Math.abs(vec.z) ? new Vec3(vec.z, vec.y, -vec.x) : new Vec3(-vec.z, vec.y, vec.x);
    }
}