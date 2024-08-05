package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.level.feature.config.CoveredBlockBlobConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class CoveredBlockBlobFeature extends Feature<CoveredBlockBlobConfiguration> {

    public CoveredBlockBlobFeature(Codec<CoveredBlockBlobConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(FeaturePlaceContext<CoveredBlockBlobConfiguration> context) {
        BlockPos blockpos = context.origin();
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        CoveredBlockBlobConfiguration blockstateconfiguration;
        for (blockstateconfiguration = context.config(); blockpos.m_123342_() > worldgenlevel.m_141937_() + 3; blockpos = blockpos.below()) {
            if (!worldgenlevel.m_46859_(blockpos.below())) {
                BlockState blockstate = worldgenlevel.m_8055_(blockpos.below());
                if (m_159759_(blockstate) || m_159747_(blockstate)) {
                    break;
                }
            }
        }
        if (blockpos.m_123342_() <= worldgenlevel.m_141937_() + 3) {
            return false;
        } else {
            for (int l = 0; l < 3; l++) {
                int i = randomsource.nextInt(2);
                int j = randomsource.nextInt(2);
                int k = randomsource.nextInt(2);
                float f = (float) (i + j + k) * 0.333F + 0.5F;
                double radius = (double) (f * f);
                for (BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-i, -j, -k), blockpos.offset(i, j, k))) {
                    if (blockpos1.m_123331_(blockpos) <= radius) {
                        worldgenlevel.m_7731_(blockpos1, blockstateconfiguration.block.getState(randomsource, blockpos1), 3);
                        BlockPos blockpos2 = blockpos1.above();
                        if (blockpos2.m_123331_(blockpos) > radius && worldgenlevel.m_8055_(blockpos2).m_60795_()) {
                            worldgenlevel.m_7731_(blockpos2, blockstateconfiguration.coverBlock.getState(randomsource, blockpos2), 3);
                        }
                    }
                }
                blockpos = blockpos.offset(-1 + randomsource.nextInt(2), -randomsource.nextInt(2), -1 + randomsource.nextInt(2));
            }
            return true;
        }
    }
}