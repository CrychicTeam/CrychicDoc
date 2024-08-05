package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.MusselBlock;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class AbyssalBoulderFeature extends Feature<NoneFeatureConfiguration> {

    public AbyssalBoulderFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos blockpos = context.origin();
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource;
        for (randomsource = context.random(); blockpos.m_123342_() > worldgenlevel.m_141937_() + 1; blockpos = blockpos.below()) {
            if (!worldgenlevel.m_46859_(blockpos.below())) {
                BlockState blockstate = worldgenlevel.m_8055_(blockpos.below());
                if (blockstate.m_60713_(ACBlockRegistry.MUCK.get())) {
                    break;
                }
            }
        }
        if (blockpos.m_123342_() <= worldgenlevel.m_141937_() + 1) {
            return false;
        } else {
            blockpos = blockpos.above(1 + randomsource.nextInt(1));
            for (int l = 0; l < 3; l++) {
                int i = randomsource.nextInt(3);
                int j = randomsource.nextInt(3);
                int k = randomsource.nextInt(3);
                float f = (float) (i + j + k) * 0.333F + 0.5F;
                double radius = (double) (f * f);
                for (BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-i, -j, -k), blockpos.offset(i, j, k))) {
                    if (blockpos1.m_123331_(blockpos) <= radius && !worldgenlevel.m_8055_(blockpos1).m_204336_(ACTagRegistry.UNMOVEABLE)) {
                        worldgenlevel.m_7731_(blockpos1, Blocks.DEEPSLATE.defaultBlockState(), 2);
                        if (randomsource.nextInt(2) == 0) {
                            Direction dir = Direction.getRandom(randomsource);
                            BlockPos blockpos2 = blockpos1.relative(dir);
                            if (worldgenlevel.m_8055_(blockpos2).m_60713_(Blocks.WATER)) {
                                worldgenlevel.m_7731_(blockpos2, (BlockState) ((BlockState) ((BlockState) ACBlockRegistry.MUSSEL.get().defaultBlockState().m_61124_(MusselBlock.FACING, dir)).m_61124_(MusselBlock.WATERLOGGED, true)).m_61124_(MusselBlock.MUSSELS, 1 + randomsource.nextInt(4)), 3);
                            }
                        }
                    }
                }
                blockpos = blockpos.offset(-2 + randomsource.nextInt(4), -randomsource.nextInt(2), -2 + randomsource.nextInt(4));
            }
            return true;
        }
    }
}