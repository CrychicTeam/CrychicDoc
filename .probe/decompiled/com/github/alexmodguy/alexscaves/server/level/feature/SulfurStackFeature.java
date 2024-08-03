package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.SulfurBudBlock;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

public class SulfurStackFeature extends Feature<NoneFeatureConfiguration> {

    public SulfurStackFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        BlockPos below = context.origin();
        if (!level.m_8055_(below.below()).m_60713_(ACBlockRegistry.RADROCK.get())) {
            return false;
        } else {
            int centerHeight = 3 + randomsource.nextInt(3);
            generatePillar(level, below, randomsource, centerHeight);
            for (int i = 0; i < 2 + randomsource.nextInt(2); i++) {
                BlockPos offset = below.offset(randomsource.nextInt(4) - 2, -randomsource.nextInt(2), randomsource.nextInt(4) - 2);
                int dist = (int) Math.ceil((double) offset.m_123333_(below));
                generatePillar(level, offset, randomsource, centerHeight - dist + randomsource.nextInt(2));
            }
            for (int i = 0; i < 4 + randomsource.nextInt(6); i++) {
                BlockPos offset = below.offset(randomsource.nextInt(6) - 3, 4, randomsource.nextInt(6) - 3);
                while (level.m_46859_(offset) && offset.m_123342_() > level.m_141937_()) {
                    offset = offset.below();
                }
                if (level.m_8055_(offset).m_60783_(level, offset, Direction.UP) && level.m_46859_(offset.above())) {
                    placeRandomCrystal(level, offset.above(), randomsource);
                }
            }
            return true;
        }
    }

    private static boolean canReplace(BlockState state) {
        return (state.m_60795_() || state.m_247087_()) && !state.m_204336_(ACTagRegistry.UNMOVEABLE);
    }

    private static void generatePillar(WorldGenLevel level, BlockPos pos, RandomSource randomSource, int height) {
        BlockPos begin = pos.relative(Direction.DOWN, 3);
        BlockPos stopPillarAt = pos.relative(Direction.UP, height);
        while (!begin.equals(stopPillarAt)) {
            begin = begin.relative(Direction.UP);
            if (canReplace(level.m_8055_(begin))) {
                level.m_7731_(begin, ACBlockRegistry.SULFUR.get().defaultBlockState(), 3);
            }
        }
        if (canReplace(level.m_8055_(stopPillarAt.above())) && !(level.m_8055_(stopPillarAt).m_60734_() instanceof SulfurBudBlock)) {
            placeRandomCrystal(level, stopPillarAt.above(), randomSource);
        }
    }

    private static void placeRandomCrystal(WorldGenLevel level, BlockPos placeAt, RandomSource randomSource) {
        BlockState crystal = ACBlockRegistry.SULFUR_CLUSTER.get().defaultBlockState();
        switch(randomSource.nextInt(3)) {
            case 0:
                crystal = ACBlockRegistry.SULFUR_BUD_SMALL.get().defaultBlockState();
                break;
            case 1:
                crystal = ACBlockRegistry.SULFUR_BUD_MEDIUM.get().defaultBlockState();
                break;
            case 2:
                crystal = ACBlockRegistry.SULFUR_BUD_LARGE.get().defaultBlockState();
        }
        if (crystal.m_60734_() instanceof SulfurBudBlock) {
            if (level.m_6425_(placeAt).is(Fluids.WATER)) {
                crystal = (BlockState) crystal.m_61124_(SulfurBudBlock.LIQUID_LOGGED, 1);
            } else if (level.m_6425_(placeAt).getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get()) {
                crystal = (BlockState) crystal.m_61124_(SulfurBudBlock.LIQUID_LOGGED, 2);
            }
        }
        level.m_7731_(placeAt, crystal, 3);
        BlockPos drip = placeAt.above();
        while (level.m_46859_(drip) && drip.m_123342_() < level.m_151558_()) {
            drip = drip.above();
        }
        if (level.m_6425_(drip).isEmpty()) {
            level.m_7731_(drip, ACBlockRegistry.ACIDIC_RADROCK.get().defaultBlockState(), 3);
        }
    }
}