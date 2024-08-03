package net.minecraft.world.level;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface CommonLevelAccessor extends EntityGetter, LevelReader, LevelSimulatedRW {

    @Override
    default <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos blockPos0, BlockEntityType<T> blockEntityTypeT1) {
        return LevelReader.super.m_141902_(blockPos0, blockEntityTypeT1);
    }

    @Override
    default List<VoxelShape> getEntityCollisions(@Nullable Entity entity0, AABB aABB1) {
        return EntityGetter.super.getEntityCollisions(entity0, aABB1);
    }

    @Override
    default boolean isUnobstructed(@Nullable Entity entity0, VoxelShape voxelShape1) {
        return EntityGetter.super.isUnobstructed(entity0, voxelShape1);
    }

    @Override
    default BlockPos getHeightmapPos(Heightmap.Types heightmapTypes0, BlockPos blockPos1) {
        return LevelReader.super.getHeightmapPos(heightmapTypes0, blockPos1);
    }
}