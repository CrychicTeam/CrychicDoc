package net.minecraft.world.phys;

import net.minecraft.world.entity.Entity;

public class EntityHitResult extends HitResult {

    private final Entity entity;

    public EntityHitResult(Entity entity0) {
        this(entity0, entity0.position());
    }

    public EntityHitResult(Entity entity0, Vec3 vec1) {
        super(vec1);
        this.entity = entity0;
    }

    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public HitResult.Type getType() {
        return HitResult.Type.ENTITY;
    }
}