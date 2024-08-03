package org.violetmoon.zeta.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public interface ICollateralMover {

    default boolean isCollateralMover(Level world, BlockPos source, Direction moveDirection, BlockPos pos) {
        return true;
    }

    ICollateralMover.MoveResult getCollateralMovement(Level var1, BlockPos var2, Direction var3, Direction var4, BlockPos var5);

    public static enum MoveResult {

        MOVE, BREAK, SKIP, PREVENT
    }
}