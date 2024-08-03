package com.simibubi.create.content.kinetics.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DirectionalShaftHalvesBlockEntity extends KineticBlockEntity {

    public DirectionalShaftHalvesBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public Direction getSourceFacing() {
        BlockPos localSource = this.source.subtract(this.m_58899_());
        return Direction.getNearest((float) localSource.m_123341_(), (float) localSource.m_123342_(), (float) localSource.m_123343_());
    }
}