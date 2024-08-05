package com.almostreliable.morejs.features.teleport;

import dev.latvian.mods.kubejs.entity.EntityEventJS;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntityTeleportsEventJS extends EntityEventJS {

    private final Entity entity;

    private final TeleportType type;

    private final Level level;

    private double x;

    private double y;

    private double z;

    public EntityTeleportsEventJS(Entity entity, double x, double y, double z, TeleportType type) {
        this(entity, x, y, z, null, type);
    }

    public EntityTeleportsEventJS(Entity entity, double x, double y, double z, @Nullable Level level, TeleportType type) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.level = level;
    }

    public TeleportType getType() {
        return this.type;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}