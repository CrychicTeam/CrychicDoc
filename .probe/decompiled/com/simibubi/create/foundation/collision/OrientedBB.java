package com.simibubi.create.foundation.collision;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class OrientedBB {

    Vec3 center;

    Vec3 extents;

    Matrix3d rotation;

    public OrientedBB(AABB bb) {
        this(bb.getCenter(), extentsFromBB(bb), new Matrix3d().asIdentity());
    }

    public OrientedBB() {
        this(Vec3.ZERO, Vec3.ZERO, new Matrix3d().asIdentity());
    }

    public OrientedBB(Vec3 center, Vec3 extents, Matrix3d rotation) {
        this.setCenter(center);
        this.extents = extents;
        this.setRotation(rotation);
    }

    public OrientedBB copy() {
        return new OrientedBB(this.center, this.extents, this.rotation);
    }

    public Vec3 intersect(AABB bb) {
        Vec3 extentsA = extentsFromBB(bb);
        return OBBCollider.separateBBs(bb.getCenter(), this.center, extentsA, this.extents, this.rotation);
    }

    public ContinuousOBBCollider.ContinuousSeparationManifold intersect(AABB bb, Vec3 motion) {
        Vec3 extentsA = extentsFromBB(bb);
        return ContinuousOBBCollider.separateBBs(bb.getCenter(), this.center, extentsA, this.extents, this.rotation, motion);
    }

    private static Vec3 extentsFromBB(AABB bb) {
        return new Vec3(bb.getXsize() / 2.0, bb.getYsize() / 2.0, bb.getZsize() / 2.0);
    }

    public Matrix3d getRotation() {
        return this.rotation;
    }

    public void setRotation(Matrix3d rotation) {
        this.rotation = rotation;
    }

    public Vec3 getCenter() {
        return this.center;
    }

    public void setCenter(Vec3 center) {
        this.center = center;
    }

    public void move(Vec3 offset) {
        this.setCenter(this.getCenter().add(offset));
    }

    public AABB getAsAABB() {
        return new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0).move(this.center).inflate(this.extents.x, this.extents.y, this.extents.z);
    }
}