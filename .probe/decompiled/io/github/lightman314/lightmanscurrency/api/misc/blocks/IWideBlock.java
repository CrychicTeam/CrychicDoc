package io.github.lightman314.lightmanscurrency.api.misc.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public interface IWideBlock {

    BooleanProperty ISLEFT = BlockStateProperties.ATTACHED;

    default BlockPos getOtherSide(BlockPos pos, BlockState state, Direction facing) {
        return this.getIsLeft(state) ? IRotatableBlock.getRightPos(pos, facing) : IRotatableBlock.getLeftPos(pos, facing);
    }

    default boolean getIsLeft(BlockState state) {
        return (Boolean) state.m_61143_(ISLEFT);
    }

    default boolean getIsRight(BlockState state) {
        return !this.getIsLeft(state);
    }
}