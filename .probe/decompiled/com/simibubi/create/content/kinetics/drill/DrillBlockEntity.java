package com.simibubi.create.content.kinetics.drill;

import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DrillBlockEntity extends BlockBreakingKineticBlockEntity {

    public DrillBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected BlockPos getBreakingPos() {
        return this.m_58899_().relative((Direction) this.m_58900_().m_61143_(DrillBlock.FACING));
    }
}