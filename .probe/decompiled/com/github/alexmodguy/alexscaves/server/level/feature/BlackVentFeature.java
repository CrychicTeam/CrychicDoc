package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.GeothermalVentBlock;
import com.github.alexmodguy.alexscaves.server.block.ThinGeothermalVentBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BlackVentFeature extends Feature<NoneFeatureConfiguration> {

    public BlackVentFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        BlockPos.MutableBlockPos ventBottom = new BlockPos.MutableBlockPos();
        ventBottom.set(context.origin());
        while (!level.m_8055_(ventBottom).m_60819_().isEmpty() && ventBottom.m_123342_() > level.m_141937_()) {
            ventBottom.move(0, -1, 0);
        }
        if (level.m_8055_(ventBottom.m_7495_()).equals(Blocks.TUFF.defaultBlockState())) {
            drawVent(level, ventBottom.m_7494_().immutable(), randomsource);
            return true;
        } else {
            return false;
        }
    }

    private static void drawVent(WorldGenLevel level, BlockPos ventBottom, RandomSource randomsource) {
        int height = randomsource.nextInt(6) + 2;
        ventBottom = ventBottom.below();
        level.m_7731_(ventBottom.north(), Blocks.TUFF.defaultBlockState(), 3);
        level.m_7731_(ventBottom.south(), Blocks.TUFF.defaultBlockState(), 3);
        level.m_7731_(ventBottom.east(), Blocks.TUFF.defaultBlockState(), 3);
        level.m_7731_(ventBottom.west(), Blocks.TUFF.defaultBlockState(), 3);
        level.m_7731_(ventBottom.below(), Blocks.TUFF.defaultBlockState(), 3);
        level.m_7731_(ventBottom, Blocks.LAVA.defaultBlockState(), 3);
        int middleStart = Math.max(1, height / 3);
        int middleTop = middleStart * 2;
        for (int i = 1; i <= height; i++) {
            BlockState vent;
            if (i <= middleStart) {
                vent = (BlockState) ((BlockState) ACBlockRegistry.GEOTHERMAL_VENT.get().defaultBlockState().m_61124_(GeothermalVentBlock.SMOKE_TYPE, 2)).m_61124_(GeothermalVentBlock.SPAWNING_PARTICLES, i == height);
            } else if (i > middleTop) {
                vent = (BlockState) ((BlockState) ((BlockState) ACBlockRegistry.GEOTHERMAL_VENT_THIN.get().defaultBlockState().m_61124_(GeothermalVentBlock.SMOKE_TYPE, 2)).m_61124_(GeothermalVentBlock.SPAWNING_PARTICLES, i == height)).m_61124_(ThinGeothermalVentBlock.WATERLOGGED, true);
            } else {
                vent = (BlockState) ((BlockState) ((BlockState) ACBlockRegistry.GEOTHERMAL_VENT_MEDIUM.get().defaultBlockState().m_61124_(GeothermalVentBlock.SMOKE_TYPE, 2)).m_61124_(GeothermalVentBlock.SPAWNING_PARTICLES, i == height)).m_61124_(ThinGeothermalVentBlock.WATERLOGGED, true);
            }
            level.m_7731_(ventBottom.above(i), vent, 3);
        }
    }
}