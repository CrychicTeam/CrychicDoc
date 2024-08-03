package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.AmbersolBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class AmbersolFeature extends Feature<NoneFeatureConfiguration> {

    public AmbersolFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos blockpos = context.origin();
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        blockpos = context.origin();
        while (blockpos.m_123342_() >= worldgenlevel.m_151558_() - 3 && worldgenlevel.m_46859_(blockpos.above())) {
            blockpos = blockpos.above();
        }
        if (blockpos.m_123342_() >= worldgenlevel.m_151558_() - 3) {
            return false;
        } else {
            for (int i = 0; i < 3; i++) {
                drawOrb(worldgenlevel, blockpos.offset(randomsource.nextInt(4) - 2, randomsource.nextInt(4) - 2, randomsource.nextInt(4) - 2), randomsource, ACBlockRegistry.AMBER.get().defaultBlockState(), 2 + randomsource.nextInt(2), 2 + randomsource.nextInt(2), 2 + randomsource.nextInt(2));
            }
            drawOrb(worldgenlevel, blockpos, randomsource, ACBlockRegistry.AMBER.get().defaultBlockState(), 2, 2, 2);
            worldgenlevel.m_7731_(blockpos, ACBlockRegistry.AMBERSOL.get().defaultBlockState(), 3);
            AmbersolBlock.fillWithLights(blockpos, worldgenlevel);
            return true;
        }
    }

    private static boolean canReplace(BlockState state) {
        return state.m_60795_() || state.m_247087_();
    }

    private static void drawOrb(WorldGenLevel level, BlockPos center, RandomSource random, BlockState blockState, int radiusX, int radiusY, int radiusZ) {
        double equalRadius = (double) (radiusX + radiusY + radiusZ) / 3.0;
        for (int x = -radiusX; x <= radiusX; x++) {
            for (int y = -radiusY; y <= radiusY; y++) {
                for (int z = -radiusZ; z <= radiusZ; z++) {
                    BlockPos fill = center.offset(x, y, z);
                    if (fill.m_203202_((double) center.m_123341_(), (double) center.m_123342_(), (double) center.m_123343_()) <= equalRadius * equalRadius - (double) (random.nextFloat() * 4.0F) && canReplace(level.m_8055_(fill))) {
                        level.m_7731_(fill, blockState, 2);
                    }
                }
            }
        }
    }
}