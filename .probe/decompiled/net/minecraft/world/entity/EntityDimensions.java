package net.minecraft.world.entity;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityDimensions {

    public final float width;

    public final float height;

    public final boolean fixed;

    public EntityDimensions(float float0, float float1, boolean boolean2) {
        this.width = float0;
        this.height = float1;
        this.fixed = boolean2;
    }

    public AABB makeBoundingBox(Vec3 vec0) {
        return this.makeBoundingBox(vec0.x, vec0.y, vec0.z);
    }

    public AABB makeBoundingBox(double double0, double double1, double double2) {
        float $$3 = this.width / 2.0F;
        float $$4 = this.height;
        return new AABB(double0 - (double) $$3, double1, double2 - (double) $$3, double0 + (double) $$3, double1 + (double) $$4, double2 + (double) $$3);
    }

    public EntityDimensions scale(float float0) {
        return this.scale(float0, float0);
    }

    public EntityDimensions scale(float float0, float float1) {
        return !this.fixed && (float0 != 1.0F || float1 != 1.0F) ? scalable(this.width * float0, this.height * float1) : this;
    }

    public static EntityDimensions scalable(float float0, float float1) {
        return new EntityDimensions(float0, float1, false);
    }

    public static EntityDimensions fixed(float float0, float float1) {
        return new EntityDimensions(float0, float1, true);
    }

    public String toString() {
        return "EntityDimensions w=" + this.width + ", h=" + this.height + ", fixed=" + this.fixed;
    }
}