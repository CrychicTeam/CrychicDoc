package com.github.alexthe666.citadel.server.entity.collision;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface ICustomCollisions {

    static Vec3 getAllowedMovementForEntity(Entity entity, Vec3 vecIN) {
        AABB aabb = entity.getBoundingBox();
        List<VoxelShape> list = entity.level().m_183134_(entity, aabb.expandTowards(vecIN));
        Vec3 vec3 = vecIN.lengthSqr() == 0.0 ? vecIN : collideBoundingBox2(entity, vecIN, aabb, entity.level(), list);
        boolean flag = vecIN.x != vec3.x;
        boolean flag1 = vecIN.y != vec3.y;
        boolean flag2 = vecIN.z != vec3.z;
        boolean flag3 = entity.onGround() || flag1 && vecIN.y < 0.0;
        if (entity.getStepHeight() > 0.0F && flag3 && (flag || flag2)) {
            Vec3 vec31 = collideBoundingBox2(entity, new Vec3(vecIN.x, (double) entity.getStepHeight(), vecIN.z), aabb, entity.level(), list);
            Vec3 vec32 = collideBoundingBox2(entity, new Vec3(0.0, (double) entity.getStepHeight(), 0.0), aabb.expandTowards(vecIN.x, 0.0, vecIN.z), entity.level(), list);
            if (vec32.y < (double) entity.getStepHeight()) {
                Vec3 vec33 = collideBoundingBox2(entity, new Vec3(vecIN.x, 0.0, vecIN.z), aabb.move(vec32), entity.level(), list).add(vec32);
                if (vec33.horizontalDistanceSqr() > vec31.horizontalDistanceSqr()) {
                    vec31 = vec33;
                }
            }
            if (vec31.horizontalDistanceSqr() > vec3.horizontalDistanceSqr()) {
                return vec31.add(collideBoundingBox2(entity, new Vec3(0.0, -vec31.y + vecIN.y, 0.0), aabb.move(vec31), entity.level(), list));
            }
        }
        return vec3;
    }

    boolean canPassThrough(BlockPos var1, BlockState var2, VoxelShape var3);

    private static Vec3 collideBoundingBox2(@Nullable Entity entity0, Vec3 vec1, AABB aABB2, Level level3, List<VoxelShape> listVoxelShape4) {
        Builder<VoxelShape> builder = ImmutableList.builderWithExpectedSize(listVoxelShape4.size() + 1);
        if (!listVoxelShape4.isEmpty()) {
            builder.addAll(listVoxelShape4);
        }
        WorldBorder worldborder = level3.getWorldBorder();
        boolean flag = entity0 != null && worldborder.isInsideCloseToBorder(entity0, aABB2.expandTowards(vec1));
        if (flag) {
            builder.add(worldborder.getCollisionShape());
        }
        builder.addAll(new CustomCollisionsBlockCollisions(level3, entity0, aABB2.expandTowards(vec1)));
        return collideWithShapes2(vec1, aABB2, builder.build());
    }

    private static Vec3 collideWithShapes2(Vec3 vec0, AABB aABB1, List<VoxelShape> listVoxelShape2) {
        if (listVoxelShape2.isEmpty()) {
            return vec0;
        } else {
            double d0 = vec0.x;
            double d1 = vec0.y;
            double d2 = vec0.z;
            if (d1 != 0.0) {
                d1 = Shapes.collide(Direction.Axis.Y, aABB1, listVoxelShape2, d1);
                if (d1 != 0.0) {
                    aABB1 = aABB1.move(0.0, d1, 0.0);
                }
            }
            boolean flag = Math.abs(d0) < Math.abs(d2);
            if (flag && d2 != 0.0) {
                d2 = Shapes.collide(Direction.Axis.Z, aABB1, listVoxelShape2, d2);
                if (d2 != 0.0) {
                    aABB1 = aABB1.move(0.0, 0.0, d2);
                }
            }
            if (d0 != 0.0) {
                d0 = Shapes.collide(Direction.Axis.X, aABB1, listVoxelShape2, d0);
                if (!flag && d0 != 0.0) {
                    aABB1 = aABB1.move(d0, 0.0, 0.0);
                }
            }
            if (!flag && d2 != 0.0) {
                d2 = Shapes.collide(Direction.Axis.Z, aABB1, listVoxelShape2, d2);
            }
            return new Vec3(d0, d1, d2);
        }
    }
}