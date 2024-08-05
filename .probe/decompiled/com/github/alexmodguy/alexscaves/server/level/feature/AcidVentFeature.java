package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.GeothermalVentBlock;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class AcidVentFeature extends Feature<NoneFeatureConfiguration> {

    public AcidVentFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        BlockPos ventBottom = context.origin();
        if (level.m_8055_(ventBottom.below()).equals(Blocks.MUD.defaultBlockState())) {
            drawVent(level, ventBottom, randomsource);
            for (int i = 0; i < 1 + randomsource.nextInt(2); i++) {
                drawVent(level, ventBottom.offset(randomsource.nextInt(8) - 4, 0, randomsource.nextInt(8) - 4), randomsource);
            }
        }
        return false;
    }

    private static void drawVent(WorldGenLevel level, BlockPos ventBottom, RandomSource randomsource) {
        int height = randomsource.nextInt(4) + 2;
        int acidCount = 0;
        while ((!level.m_6425_(ventBottom).isEmpty() || !level.m_8055_(ventBottom).m_60795_()) && ventBottom.m_123342_() < level.m_151558_() - height) {
            ventBottom = ventBottom.above();
            if (++acidCount >= 3) {
                return;
            }
        }
        if (hasClearance(level, ventBottom, height)) {
            ventBottom = ventBottom.below();
            drawOrb(level, ventBottom, randomsource, 1 + randomsource.nextInt(1), 2 + randomsource.nextInt(3), 1 + randomsource.nextInt(1));
            level.m_7731_(ventBottom.north(), Blocks.TUFF.defaultBlockState(), 2);
            level.m_7731_(ventBottom.south(), Blocks.TUFF.defaultBlockState(), 2);
            level.m_7731_(ventBottom.east(), Blocks.TUFF.defaultBlockState(), 2);
            level.m_7731_(ventBottom.west(), Blocks.TUFF.defaultBlockState(), 2);
            level.m_7731_(ventBottom.below(), Blocks.TUFF.defaultBlockState(), 2);
            level.m_7731_(ventBottom, ACBlockRegistry.ACID.get().m_49966_(), 2);
            int middleStart = Math.max(1, height / 3);
            int middleTop = middleStart * 2;
            for (int i = 1; i <= height; i++) {
                BlockState vent;
                if (i <= middleStart) {
                    vent = (BlockState) ((BlockState) ACBlockRegistry.GEOTHERMAL_VENT.get().defaultBlockState().m_61124_(GeothermalVentBlock.SMOKE_TYPE, 3)).m_61124_(GeothermalVentBlock.SPAWNING_PARTICLES, i == height);
                } else if (i > middleTop) {
                    vent = (BlockState) ((BlockState) ACBlockRegistry.GEOTHERMAL_VENT_THIN.get().defaultBlockState().m_61124_(GeothermalVentBlock.SMOKE_TYPE, 3)).m_61124_(GeothermalVentBlock.SPAWNING_PARTICLES, i == height);
                } else {
                    vent = (BlockState) ((BlockState) ACBlockRegistry.GEOTHERMAL_VENT_MEDIUM.get().defaultBlockState().m_61124_(GeothermalVentBlock.SMOKE_TYPE, 3)).m_61124_(GeothermalVentBlock.SPAWNING_PARTICLES, i == height);
                }
                level.m_7731_(ventBottom.above(i), vent, 2);
            }
            level.m_7731_(ventBottom.above(height + 1), Blocks.CAVE_AIR.defaultBlockState(), 2);
        }
    }

    private static boolean hasClearance(WorldGenLevel level, BlockPos ventBottom, int height) {
        for (int i = 0; i < height; i++) {
            if (!level.m_46859_(ventBottom.above(1 + i))) {
                return false;
            }
        }
        return true;
    }

    private static void drawOrb(WorldGenLevel level, BlockPos center, RandomSource random, int radiusX, int radiusY, int radiusZ) {
        double equalRadius = (double) (radiusX + radiusY + radiusZ) / 3.0;
        for (int x = -radiusX; x <= radiusX; x++) {
            for (int y = -radiusY; y <= radiusY; y++) {
                for (int z = -radiusZ; z <= radiusZ; z++) {
                    BlockPos fill = center.offset(x, y, z);
                    if (fill.m_203202_((double) center.m_123341_(), (double) center.m_123342_(), (double) center.m_123343_()) <= equalRadius * equalRadius + (double) (random.nextFloat() * 2.0F) && canReplace(level.m_8055_(fill)) && fill.m_123342_() <= center.m_123342_()) {
                        level.m_7731_(fill, Blocks.TUFF.defaultBlockState(), 2);
                    }
                }
            }
        }
    }

    private static boolean canReplace(BlockState state) {
        return !state.m_204336_(ACTagRegistry.UNMOVEABLE) && !state.m_60795_();
    }
}