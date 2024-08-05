package com.yungnickyoung.minecraft.yungsapi.world.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

public class SurfaceHelper {

    private SurfaceHelper() {
    }

    public static int getSurfaceHeight(ChunkAccess chunk, ColumnPos pos) {
        int maxY = chunk.m_151558_() - 1;
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(pos.x(), maxY, pos.z());
        if (chunk.m_8055_(blockPos) != Blocks.AIR.defaultBlockState()) {
            return maxY;
        } else {
            for (int y = maxY; y >= 0; y--) {
                BlockState blockState = chunk.m_8055_(blockPos);
                if (blockState != Blocks.AIR.defaultBlockState()) {
                    return y;
                }
                blockPos.move(Direction.DOWN);
            }
            return 1;
        }
    }
}