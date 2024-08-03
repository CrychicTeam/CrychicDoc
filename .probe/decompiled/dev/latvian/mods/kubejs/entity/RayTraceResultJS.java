package dev.latvian.mods.kubejs.entity;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class RayTraceResultJS {

    public final Entity fromEntity;

    public final HitResult.Type type;

    public final double distance;

    public Vec3 hit = null;

    public BlockContainerJS block = null;

    public Direction facing = null;

    public Entity entity = null;

    public RayTraceResultJS(Entity from, @Nullable HitResult result, double d) {
        this.fromEntity = from;
        this.distance = d;
        this.type = result == null ? HitResult.Type.MISS : result.getType();
        if (result instanceof BlockHitResult b && result.getType() == HitResult.Type.BLOCK) {
            this.hit = result.getLocation();
            this.block = new BlockContainerJS(from.level(), b.getBlockPos());
            this.facing = b.getDirection();
            return;
        }
        if (result instanceof EntityHitResult e && result.getType() == HitResult.Type.ENTITY) {
            this.hit = result.getLocation();
            this.entity = e.getEntity();
        }
    }

    public double getHitX() {
        return this.hit == null ? Double.NaN : this.hit.x;
    }

    public double getHitY() {
        return this.hit == null ? Double.NaN : this.hit.y;
    }

    public double getHitZ() {
        return this.hit == null ? Double.NaN : this.hit.z;
    }
}