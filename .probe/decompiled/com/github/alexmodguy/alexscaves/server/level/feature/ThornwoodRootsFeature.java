package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

public class ThornwoodRootsFeature extends Feature<NoneFeatureConfiguration> {

    public ThornwoodRootsFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        BlockPos.MutableBlockPos generateAt = new BlockPos.MutableBlockPos();
        generateAt.set(context.origin());
        if (!level.m_8055_(generateAt).m_60819_().is(Fluids.WATER) && !level.m_46859_(generateAt)) {
            return false;
        } else {
            while ((level.m_8055_(generateAt).m_60819_().is(Fluids.WATER) || !level.m_8055_(generateAt).m_60783_(level, generateAt, Direction.DOWN)) && generateAt.m_123342_() < level.m_151558_()) {
                generateAt.move(0, 1, 0);
            }
            if (level.m_8055_(generateAt).m_60795_()) {
                return false;
            } else {
                BlockPos rootsFrom = generateAt.immutable();
                level.m_7731_(rootsFrom, ACBlockRegistry.THORNWOOD_WOOD.get().defaultBlockState(), 3);
                ThornwoodTreeFeature.generateRoot(level, rootsFrom, 0.0F, randomsource, Direction.DOWN, 1 + randomsource.nextInt(2));
                for (Direction direction : ACMath.HORIZONTAL_DIRECTIONS) {
                    ThornwoodTreeFeature.generateRoot(level, rootsFrom, 0.4F, randomsource, direction, 2 + randomsource.nextInt(5));
                }
                return true;
            }
        }
    }
}