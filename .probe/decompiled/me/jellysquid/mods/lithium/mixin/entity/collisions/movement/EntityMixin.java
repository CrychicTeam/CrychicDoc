package me.jellysquid.mods.lithium.mixin.entity.collisions.movement;

import java.util.List;
import me.jellysquid.mods.lithium.common.entity.LithiumEntityCollisions;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ Entity.class })
public class EntityMixin {

    @Redirect(method = { "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntityCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;"))
    private List<VoxelShape> getEntitiesLater(Level world, Entity entity, AABB box) {
        return List.of();
    }

    @Redirect(method = { "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;"), require = 5)
    private Vec3 adjustMovementForCollisionsGetEntitiesLater(@Nullable Entity entity, Vec3 movement, AABB entityBoundingBox, Level world, List<VoxelShape> collisions) {
        return lithiumCollideMultiAxisMovement(entity, movement, entityBoundingBox, world, true, collisions);
    }

    @Overwrite
    public static Vec3 collideBoundingBox(@Nullable Entity entity, Vec3 movement, AABB entityBoundingBox, Level world, List<VoxelShape> collisions) {
        return lithiumCollideMultiAxisMovement(entity, movement, entityBoundingBox, world, false, collisions);
    }

    private static Vec3 lithiumCollideMultiAxisMovement(@Nullable Entity entity, Vec3 movement, AABB entityBoundingBox, Level world, boolean getEntityCollisions, List<VoxelShape> otherCollisions) {
        double velX = movement.x;
        double velY = movement.y;
        double velZ = movement.z;
        boolean isVerticalOnly = velX == 0.0 && velZ == 0.0;
        AABB movementSpace;
        if (isVerticalOnly) {
            if (velY < 0.0) {
                VoxelShape voxelShape = LithiumEntityCollisions.getCollisionShapeBelowEntity(world, entity, entityBoundingBox);
                if (voxelShape != null) {
                    double v = voxelShape.collide(Direction.Axis.Y, entityBoundingBox, velY);
                    if (v == 0.0) {
                        return Vec3.ZERO;
                    }
                }
                movementSpace = new AABB(entityBoundingBox.minX, entityBoundingBox.minY + velY, entityBoundingBox.minZ, entityBoundingBox.maxX, entityBoundingBox.minY, entityBoundingBox.maxZ);
            } else {
                movementSpace = new AABB(entityBoundingBox.minX, entityBoundingBox.maxY, entityBoundingBox.minZ, entityBoundingBox.maxX, entityBoundingBox.maxY + velY, entityBoundingBox.maxZ);
            }
        } else {
            movementSpace = entityBoundingBox.expandTowards(movement);
        }
        List<VoxelShape> blockCollisions = LithiumEntityCollisions.getBlockCollisions(world, entity, movementSpace);
        List<VoxelShape> entityWorldBorderCollisions = null;
        if (velY != 0.0) {
            velY = Shapes.collide(Direction.Axis.Y, entityBoundingBox, blockCollisions, velY);
            if (velY != 0.0) {
                if (!otherCollisions.isEmpty()) {
                    velY = Shapes.collide(Direction.Axis.Y, entityBoundingBox, otherCollisions, velY);
                }
                if (velY != 0.0 && getEntityCollisions) {
                    entityWorldBorderCollisions = LithiumEntityCollisions.getEntityWorldBorderCollisions(world, entity, movementSpace, entity != null);
                    velY = Shapes.collide(Direction.Axis.Y, entityBoundingBox, entityWorldBorderCollisions, velY);
                }
                if (velY != 0.0) {
                    entityBoundingBox = entityBoundingBox.move(0.0, velY, 0.0);
                }
            }
        }
        boolean velXSmallerVelZ = Math.abs(velX) < Math.abs(velZ);
        if (velXSmallerVelZ) {
            velZ = Shapes.collide(Direction.Axis.Z, entityBoundingBox, blockCollisions, velZ);
            if (velZ != 0.0) {
                if (!otherCollisions.isEmpty()) {
                    velZ = Shapes.collide(Direction.Axis.Z, entityBoundingBox, otherCollisions, velZ);
                }
                if (velZ != 0.0 && getEntityCollisions) {
                    if (entityWorldBorderCollisions == null) {
                        entityWorldBorderCollisions = LithiumEntityCollisions.getEntityWorldBorderCollisions(world, entity, movementSpace, entity != null);
                    }
                    velZ = Shapes.collide(Direction.Axis.Z, entityBoundingBox, entityWorldBorderCollisions, velZ);
                }
                if (velZ != 0.0) {
                    entityBoundingBox = entityBoundingBox.move(0.0, 0.0, velZ);
                }
            }
        }
        if (velX != 0.0) {
            velX = Shapes.collide(Direction.Axis.X, entityBoundingBox, blockCollisions, velX);
            if (velX != 0.0) {
                if (!otherCollisions.isEmpty()) {
                    velX = Shapes.collide(Direction.Axis.X, entityBoundingBox, otherCollisions, velX);
                }
                if (velX != 0.0 && getEntityCollisions) {
                    if (entityWorldBorderCollisions == null) {
                        entityWorldBorderCollisions = LithiumEntityCollisions.getEntityWorldBorderCollisions(world, entity, movementSpace, entity != null);
                    }
                    velX = Shapes.collide(Direction.Axis.X, entityBoundingBox, entityWorldBorderCollisions, velX);
                }
                if (velX != 0.0) {
                    entityBoundingBox = entityBoundingBox.move(velX, 0.0, 0.0);
                }
            }
        }
        if (!velXSmallerVelZ && velZ != 0.0) {
            velZ = Shapes.collide(Direction.Axis.Z, entityBoundingBox, blockCollisions, velZ);
            if (velZ != 0.0) {
                if (!otherCollisions.isEmpty()) {
                    velZ = Shapes.collide(Direction.Axis.Z, entityBoundingBox, otherCollisions, velZ);
                }
                if (velZ != 0.0 && getEntityCollisions) {
                    if (entityWorldBorderCollisions == null) {
                        entityWorldBorderCollisions = LithiumEntityCollisions.getEntityWorldBorderCollisions(world, entity, movementSpace, entity != null);
                    }
                    velZ = Shapes.collide(Direction.Axis.Z, entityBoundingBox, entityWorldBorderCollisions, velZ);
                }
            }
        }
        return new Vec3(velX, velY, velZ);
    }
}