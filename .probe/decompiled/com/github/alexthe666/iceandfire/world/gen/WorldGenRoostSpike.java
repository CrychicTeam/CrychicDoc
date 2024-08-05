package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;

public class WorldGenRoostSpike {

    private final Direction direction;

    public WorldGenRoostSpike(Direction direction) {
        this.direction = direction;
    }

    public boolean generate(LevelAccessor worldIn, RandomSource rand, BlockPos position) {
        int radius = 5;
        for (int i = 0; i < 5; i++) {
            int j = Math.max(0, radius - (int) ((float) i * 1.75F));
            int l = radius - i;
            int k = Math.max(0, radius - (int) ((float) i * 1.5F));
            float f = (float) (j + l) * 0.333F + 0.5F;
            BlockPos up = position.above().relative(this.direction, i);
            int xOrZero = this.direction.getAxis() == Direction.Axis.Z ? j : 0;
            int zOrZero = this.direction.getAxis() == Direction.Axis.Z ? 0 : k;
            for (BlockPos blockpos : (Set) BlockPos.betweenClosedStream(up.offset(-xOrZero, -l, -zOrZero), up.offset(xOrZero, l, zOrZero)).map(BlockPos::m_7949_).collect(Collectors.toSet())) {
                if (blockpos.m_123331_(position) <= (double) (f * f)) {
                    int height = Math.max(blockpos.m_123342_() - up.m_123342_(), 0);
                    if (i <= 0) {
                        if (rand.nextFloat() < (float) height * 0.3F) {
                            worldIn.m_7731_(blockpos, IafBlockRegistry.CRACKLED_STONE.get().defaultBlockState(), 2);
                        }
                    } else {
                        worldIn.m_7731_(blockpos, IafBlockRegistry.CRACKLED_STONE.get().defaultBlockState(), 2);
                    }
                }
            }
        }
        return true;
    }
}