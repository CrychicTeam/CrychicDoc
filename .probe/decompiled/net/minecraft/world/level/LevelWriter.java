package net.minecraft.world.level;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public interface LevelWriter {

    boolean setBlock(BlockPos var1, BlockState var2, int var3, int var4);

    default boolean setBlock(BlockPos blockPos0, BlockState blockState1, int int2) {
        return this.setBlock(blockPos0, blockState1, int2, 512);
    }

    boolean removeBlock(BlockPos var1, boolean var2);

    default boolean destroyBlock(BlockPos blockPos0, boolean boolean1) {
        return this.destroyBlock(blockPos0, boolean1, null);
    }

    default boolean destroyBlock(BlockPos blockPos0, boolean boolean1, @Nullable Entity entity2) {
        return this.destroyBlock(blockPos0, boolean1, entity2, 512);
    }

    boolean destroyBlock(BlockPos var1, boolean var2, @Nullable Entity var3, int var4);

    default boolean addFreshEntity(Entity entity0) {
        return false;
    }
}