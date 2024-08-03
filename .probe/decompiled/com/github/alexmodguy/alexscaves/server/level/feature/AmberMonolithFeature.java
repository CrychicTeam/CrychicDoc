package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class AmberMonolithFeature extends Feature<NoneFeatureConfiguration> {

    public AmberMonolithFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        BlockPos below = context.origin();
        if (!level.m_8055_(below.below()).m_280296_()) {
            return false;
        } else {
            BlockPos.MutableBlockPos pillar = new BlockPos.MutableBlockPos();
            pillar.set(below);
            for (int i = 0; i < 4 + randomsource.nextInt(2); i++) {
                level.m_7731_(pillar, ACBlockRegistry.LIMESTONE_PILLAR.get().defaultBlockState(), 3);
                pillar.move(0, 1, 0);
            }
            level.m_7731_(pillar, ACBlockRegistry.AMBER_MONOLITH.get().defaultBlockState(), 3);
            if (randomsource.nextBoolean()) {
                pillar.move(0, 1, 0);
                level.m_7731_(pillar, ACBlockRegistry.LIMESTONE_SLAB.get().defaultBlockState(), 3);
            }
            BlockPos pillarTop = pillar.immutable();
            for (int i = 0; i < 4 + randomsource.nextInt(6); i++) {
                BlockPos offset = pillarTop.offset(randomsource.nextInt(6) - 3, 1, randomsource.nextInt(6) - 3);
                while (level.m_46859_(offset) && offset.m_123342_() > level.m_141937_()) {
                    offset = offset.below();
                }
                if (level.m_8055_(offset).m_60783_(level, offset, Direction.UP) && level.m_46859_(offset.above())) {
                    float f = randomsource.nextFloat();
                    BlockState randomState;
                    if (f < 0.3F) {
                        randomState = ACBlockRegistry.LIMESTONE_SLAB.get().defaultBlockState();
                    } else if (f < 0.6F) {
                        randomState = (BlockState) ACBlockRegistry.LIMESTONE_PILLAR.get().defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, Direction.Axis.X);
                    } else if (f < 0.9F) {
                        randomState = (BlockState) ACBlockRegistry.LIMESTONE_PILLAR.get().defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, Direction.Axis.Z);
                    } else {
                        randomState = ACBlockRegistry.AMBER.get().defaultBlockState();
                    }
                    level.m_7731_(offset.above(), randomState, 3);
                }
            }
            return true;
        }
    }
}