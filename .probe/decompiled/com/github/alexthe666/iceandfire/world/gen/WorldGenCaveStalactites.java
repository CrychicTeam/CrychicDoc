package com.github.alexthe666.iceandfire.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

public class WorldGenCaveStalactites {

    private final Block block;

    private int maxHeight = 3;

    public WorldGenCaveStalactites(Block block, int maxHeight) {
        this.block = block;
        this.maxHeight = maxHeight;
    }

    public boolean generate(LevelAccessor worldIn, RandomSource rand, BlockPos position) {
        int height = this.maxHeight + rand.nextInt(3);
        for (int i = 0; i < height; i++) {
            if (i < height / 2) {
                worldIn.m_7731_(position.below(i).north(), this.block.defaultBlockState(), 2);
                worldIn.m_7731_(position.below(i).east(), this.block.defaultBlockState(), 2);
                worldIn.m_7731_(position.below(i).south(), this.block.defaultBlockState(), 2);
                worldIn.m_7731_(position.below(i).west(), this.block.defaultBlockState(), 2);
            }
            worldIn.m_7731_(position.below(i), this.block.defaultBlockState(), 2);
        }
        return true;
    }
}