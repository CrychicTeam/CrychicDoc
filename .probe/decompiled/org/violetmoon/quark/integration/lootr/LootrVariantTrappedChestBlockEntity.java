package org.violetmoon.quark.integration.lootr;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.Quark;

public class LootrVariantTrappedChestBlockEntity extends LootrVariantChestBlockEntity {

    protected LootrVariantTrappedChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public LootrVariantTrappedChestBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        this(Quark.LOOTR_INTEGRATION.trappedChestTE(), pWorldPosition, pBlockState);
    }

    @Override
    protected void signalOpenCount(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, int prevOpenCount, int openCount) {
        super.m_142151_(world, pos, state, prevOpenCount, openCount);
        if (prevOpenCount != openCount) {
            Block block = state.m_60734_();
            world.updateNeighborsAt(pos, block);
            world.updateNeighborsAt(pos.below(), block);
        }
    }
}