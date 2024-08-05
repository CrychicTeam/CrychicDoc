package me.jellysquid.mods.lithium.common.util.math;

import net.minecraft.world.phys.Vec3;

public class MutableVec3d {

    private double x;

    private double y;

    private double z;

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public void add(Vec3 vec) {
        this.x = this.x + vec.x;
        this.y = this.y + vec.y;
        this.z = this.z + vec.z;
    }

    public Vec3 toImmutable() {
        return new Vec3(this.x, this.y, this.z);
    }
}