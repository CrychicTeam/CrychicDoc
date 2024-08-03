package com.github.alexthe666.iceandfire.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

public interface IPhasesThroughBlock {

    boolean canPhaseThroughBlock(LevelAccessor var1, BlockPos var2);
}