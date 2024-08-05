package dev.architectury.hooks.level.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import org.jetbrains.annotations.Nullable;

public final class EntityHooks {

    private EntityHooks() {
    }

    @Nullable
    public static Entity fromCollision(CollisionContext ctx) {
        return ctx instanceof EntityCollisionContext ? ((EntityCollisionContext) ctx).getEntity() : null;
    }
}