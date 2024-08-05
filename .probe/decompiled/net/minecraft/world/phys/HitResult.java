package net.minecraft.world.phys;

import net.minecraft.world.entity.Entity;

public abstract class HitResult {

    protected final Vec3 location;

    protected HitResult(Vec3 vec0) {
        this.location = vec0;
    }

    public double distanceTo(Entity entity0) {
        double $$1 = this.location.x - entity0.getX();
        double $$2 = this.location.y - entity0.getY();
        double $$3 = this.location.z - entity0.getZ();
        return $$1 * $$1 + $$2 * $$2 + $$3 * $$3;
    }

    public abstract HitResult.Type getType();

    public Vec3 getLocation() {
        return this.location;
    }

    public static enum Type {

        MISS, BLOCK, ENTITY
    }
}