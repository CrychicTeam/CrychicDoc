package com.rekindled.embers.api.block;

import com.rekindled.embers.blockentity.PipeBlockEntityBase;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface IPipeConnection {

    PipeBlockEntityBase.PipeConnection getPipeConnection(BlockState var1, Direction var2);
}