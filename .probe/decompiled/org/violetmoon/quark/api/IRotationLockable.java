package org.violetmoon.quark.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IRotationLockable {

    BlockState applyRotationLock(Level var1, BlockPos var2, BlockState var3, Direction var4, int var5);
}