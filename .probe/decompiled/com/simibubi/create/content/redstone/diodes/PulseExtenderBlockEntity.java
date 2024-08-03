package com.simibubi.create.content.redstone.diodes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PulseExtenderBlockEntity extends BrassDiodeBlockEntity {

    public PulseExtenderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void updateState(boolean powered, boolean powering, boolean atMax, boolean atMin) {
        if (!atMin || powered) {
            if (atMin || powered) {
                this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) this.m_58900_().m_61124_(BrassDiodeBlock.POWERING, true));
                this.state = this.maxState.getValue();
            } else if (this.state == 1) {
                if (powering && !this.f_58857_.isClientSide) {
                    this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) this.m_58900_().m_61124_(BrassDiodeBlock.POWERING, false));
                }
                if (!powered) {
                    this.state = 0;
                }
            } else {
                if (!powered) {
                    this.state--;
                }
            }
        }
    }
}