package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.level.feature.config.FloatingOrbFeatureConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class FloatingOrbFeature extends Feature<FloatingOrbFeatureConfig> {

    public FloatingOrbFeature(Codec<FloatingOrbFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<FloatingOrbFeatureConfig> context) {
        BlockPos pos = context.origin();
        WorldGenLevel level = context.level();
        RandomSource randomSource = context.random();
        if (!canReplace(level.m_8055_(pos))) {
            return false;
        } else {
            int minRadius = context.config().minRadius;
            int radAdd = Math.max(1, context.config().maxRadius - context.config().minRadius);
            int radius = minRadius + randomSource.nextInt(radAdd);
            drawOrb(level, pos, randomSource, context.config().orbBlock, radius + randomSource.nextInt(2) - 1, radius + randomSource.nextInt(2) - 1, radius + randomSource.nextInt(2) - 1);
            return true;
        }
    }

    private static boolean canReplace(BlockState state) {
        return state.m_60795_() || state.m_247087_();
    }

    private static void drawOrb(WorldGenLevel level, BlockPos center, RandomSource random, BlockStateProvider blockState, int radiusX, int radiusY, int radiusZ) {
        double equalRadius = (double) (radiusX + radiusY + radiusZ) / 3.0;
        for (int x = -radiusX; x <= radiusX; x++) {
            for (int y = -radiusY; y <= radiusY; y++) {
                for (int z = -radiusZ; z <= radiusZ; z++) {
                    BlockPos fill = center.offset(x, y, z);
                    if (fill.m_203202_((double) center.m_123341_(), (double) center.m_123342_(), (double) center.m_123343_()) <= equalRadius * equalRadius + (double) (random.nextFloat() * 2.0F) && canReplace(level.m_8055_(fill))) {
                        level.m_7731_(fill, blockState.getState(random, fill), 3);
                    }
                }
            }
        }
    }
}