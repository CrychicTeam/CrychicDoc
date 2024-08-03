package me.jellysquid.mods.lithium.common.entity;

import com.google.common.collect.AbstractIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import me.jellysquid.mods.lithium.common.entity.movement.ChunkAwareBlockCollisionSweeper;
import me.jellysquid.mods.lithium.common.util.Pos;
import me.jellysquid.mods.lithium.common.world.WorldHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LithiumEntityCollisions {

    public static final double EPSILON = 1.0E-7;

    public static List<VoxelShape> getBlockCollisions(Level world, Entity entity, AABB box) {
        return new ChunkAwareBlockCollisionSweeper(world, entity, box).collectAll();
    }

    public static boolean doesBoxCollideWithBlocks(Level world, Entity entity, AABB box) {
        ChunkAwareBlockCollisionSweeper sweeper = new ChunkAwareBlockCollisionSweeper(world, entity, box);
        VoxelShape shape = sweeper.computeNext();
        return shape != null && !shape.isEmpty();
    }

    public static boolean doesBoxCollideWithHardEntities(EntityGetter view, Entity entity, AABB box) {
        return isBoxEmpty(box) ? false : getEntityWorldBorderCollisionIterable(view, entity, box.inflate(1.0E-7), false).iterator().hasNext();
    }

    public static List<VoxelShape> getEntityWorldBorderCollisions(Level world, Entity entity, AABB box, boolean includeWorldBorder) {
        if (isBoxEmpty(box)) {
            return Collections.emptyList();
        } else {
            ArrayList<VoxelShape> shapes = new ArrayList();
            for (VoxelShape shape : getEntityWorldBorderCollisionIterable(world, entity, box.inflate(1.0E-7), includeWorldBorder)) {
                shapes.add(shape);
            }
            return shapes;
        }
    }

    public static Iterable<VoxelShape> getEntityWorldBorderCollisionIterable(EntityGetter view, Entity entity, AABB box, boolean includeWorldBorder) {
        assert !includeWorldBorder || entity != null;
        return new Iterable<VoxelShape>() {

            private List<Entity> entityList;

            private int nextFilterIndex;

            @NotNull
            public Iterator<VoxelShape> iterator() {
                return new AbstractIterator<VoxelShape>() {

                    int index = 0;

                    boolean consumedWorldBorder = false;

                    protected VoxelShape computeNext() {
                        if (entityList == null) {
                            entityList = WorldHelper.getEntitiesForCollision(view, box, entity);
                            nextFilterIndex = 0;
                        }
                        List<Entity> list = entityList;
                        while (this.index < list.size()) {
                            Entity otherEntity = (Entity) list.get(this.index);
                            if (this.index >= nextFilterIndex) {
                                if (entity == null) {
                                    if (!otherEntity.canBeCollidedWith()) {
                                        otherEntity = null;
                                    }
                                } else if (!entity.canCollideWith(otherEntity)) {
                                    otherEntity = null;
                                }
                                nextFilterIndex++;
                            }
                            this.index++;
                            if (otherEntity != null) {
                                return Shapes.create(otherEntity.getBoundingBox());
                            }
                        }
                        if (includeWorldBorder && !this.consumedWorldBorder) {
                            this.consumedWorldBorder = true;
                            WorldBorder worldBorder = entity.level().getWorldBorder();
                            if (!LithiumEntityCollisions.isWithinWorldBorder(worldBorder, box) && LithiumEntityCollisions.isWithinWorldBorder(worldBorder, entity.getBoundingBox())) {
                                return worldBorder.getCollisionShape();
                            }
                        }
                        return (VoxelShape) this.endOfData();
                    }
                };
            }
        };
    }

    public static boolean isWithinWorldBorder(WorldBorder border, AABB box) {
        double wboxMinX = Math.floor(border.getMinX());
        double wboxMinZ = Math.floor(border.getMinZ());
        double wboxMaxX = Math.ceil(border.getMaxX());
        double wboxMaxZ = Math.ceil(border.getMaxZ());
        return box.minX >= wboxMinX && box.minX <= wboxMaxX && box.minZ >= wboxMinZ && box.minZ <= wboxMaxZ && box.maxX >= wboxMinX && box.maxX <= wboxMaxX && box.maxZ >= wboxMinZ && box.maxZ <= wboxMaxZ;
    }

    private static boolean isBoxEmpty(AABB box) {
        return box.getSize() <= 1.0E-7;
    }

    public static boolean doesEntityCollideWithWorldBorder(CollisionGetter collisionView, Entity entity) {
        if (isWithinWorldBorder(collisionView.getWorldBorder(), entity.getBoundingBox())) {
            return false;
        } else {
            VoxelShape worldBorderShape = getWorldBorderCollision(collisionView, entity);
            return worldBorderShape != null && Shapes.joinIsNotEmpty(worldBorderShape, Shapes.create(entity.getBoundingBox()), BooleanOp.AND);
        }
    }

    public static VoxelShape getWorldBorderCollision(CollisionGetter collisionView, Entity entity) {
        AABB box = entity.getBoundingBox();
        WorldBorder worldBorder = collisionView.getWorldBorder();
        return worldBorder.isInsideCloseToBorder(entity, box) ? worldBorder.getCollisionShape() : null;
    }

    public static VoxelShape getCollisionShapeBelowEntity(Level world, @Nullable Entity entity, AABB entityBoundingBox) {
        int x = Mth.floor(entityBoundingBox.minX + (entityBoundingBox.maxX - entityBoundingBox.minX) / 2.0);
        int y = Mth.floor(entityBoundingBox.minY);
        int z = Mth.floor(entityBoundingBox.minZ + (entityBoundingBox.maxZ - entityBoundingBox.minZ) / 2.0);
        if (world.m_151562_(y)) {
            return null;
        } else {
            ChunkAccess chunk = world.getChunk(Pos.ChunkCoord.fromBlockCoord(x), Pos.ChunkCoord.fromBlockCoord(z), ChunkStatus.FULL, false);
            if (chunk != null) {
                LevelChunkSection cachedChunkSection = chunk.getSections()[Pos.SectionYIndex.fromBlockCoord(world, y)];
                return cachedChunkSection.getBlockState(x & 15, y & 15, z & 15).m_60742_(world, new BlockPos(x, y, z), entity == null ? CollisionContext.empty() : CollisionContext.of(entity));
            } else {
                return null;
            }
        }
    }
}