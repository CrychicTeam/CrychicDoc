package net.mehvahdjukaar.moonlight.api.block;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface IRotatable {

    Optional<BlockState> getRotatedState(BlockState var1, LevelAccessor var2, BlockPos var3, Rotation var4, Direction var5, @Nullable Vec3 var6);

    default Optional<Direction> rotateOverAxis(BlockState state, LevelAccessor world, BlockPos pos, Rotation rotation, Direction axis, @Nullable Vec3 hit) {
        Optional<BlockState> optional = this.getRotatedState(state, world, pos, rotation, axis, hit);
        if (optional.isPresent()) {
            BlockState rotated = (BlockState) optional.get();
            if (rotated.m_60710_(world, pos)) {
                rotated = Block.updateFromNeighbourShapes(rotated, world, pos);
                if (world instanceof ServerLevel) {
                    world.m_7731_(pos, rotated, 11);
                }
                this.onRotated(rotated, state, world, pos, rotation, axis, hit);
                return Optional.of(axis);
            }
        }
        return Optional.empty();
    }

    default void onRotated(BlockState newState, BlockState oldState, LevelAccessor world, BlockPos pos, Rotation rotation, Direction axis, @Nullable Vec3 hit) {
    }
}