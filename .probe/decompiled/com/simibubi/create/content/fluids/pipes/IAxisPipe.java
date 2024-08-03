package com.simibubi.create.content.fluids.pipes;

import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface IAxisPipe {

    @Nullable
    static Direction.Axis getAxisOf(BlockState state) {
        return state.m_60734_() instanceof IAxisPipe ? ((IAxisPipe) state.m_60734_()).getAxis(state) : null;
    }

    Direction.Axis getAxis(BlockState var1);
}