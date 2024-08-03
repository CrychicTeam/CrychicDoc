package com.github.alexthe666.iceandfire.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

public class WorldGenRoostArch {

    private static final Direction[] HORIZONTALS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    private final Block block;

    public WorldGenRoostArch(Block block) {
        this.block = block;
    }

    public boolean generate(LevelAccessor worldIn, RandomSource rand, BlockPos position) {
        int height = 3 + rand.nextInt(3);
        int width = Math.min(3, height - 2);
        Direction direction = HORIZONTALS[rand.nextInt(HORIZONTALS.length - 1)];
        boolean diagonal = rand.nextBoolean();
        for (int i = 0; i < height; i++) {
            worldIn.m_7731_(position.above(i), this.block.defaultBlockState(), 2);
        }
        BlockPos offsetPos = position;
        int placedWidths = 0;
        for (int i = 0; i < width; i++) {
            offsetPos = position.above(height).relative(direction, i);
            if (diagonal) {
                offsetPos = position.above(height).relative(direction, i).relative(direction.getClockWise(), i);
            }
            if (placedWidths < width - 1 || rand.nextBoolean()) {
                worldIn.m_7731_(offsetPos, this.block.defaultBlockState(), 2);
            }
            placedWidths++;
        }
        while (worldIn.m_46859_(offsetPos.below()) && offsetPos.m_123342_() > 0) {
            worldIn.m_7731_(offsetPos.below(), this.block.defaultBlockState(), 2);
            offsetPos = offsetPos.below();
        }
        return true;
    }
}