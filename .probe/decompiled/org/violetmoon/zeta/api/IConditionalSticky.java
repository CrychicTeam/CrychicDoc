package org.violetmoon.zeta.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IConditionalSticky {

    boolean canStickToBlock(Level var1, BlockPos var2, BlockPos var3, BlockPos var4, BlockState var5, BlockState var6, Direction var7);
}