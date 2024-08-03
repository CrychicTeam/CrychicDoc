package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.GalenaSpireBlock;
import com.github.alexmodguy.alexscaves.server.block.TeslaBulbBlock;
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

public class TeslaBulbFeature extends Feature<NoneFeatureConfiguration> {

    public TeslaBulbFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        boolean ceiling = randomsource.nextBoolean();
        BlockPos.MutableBlockPos generateAt = new BlockPos.MutableBlockPos();
        generateAt.set(context.origin());
        if (!level.m_8055_(generateAt).m_60819_().is(Fluids.WATER) && !level.m_46859_(generateAt)) {
            return false;
        } else {
            if (ceiling) {
                while ((level.m_8055_(generateAt).m_60819_().is(Fluids.WATER) || !level.m_8055_(generateAt).m_60783_(level, generateAt, Direction.DOWN)) && generateAt.m_123342_() < level.m_151558_()) {
                    generateAt.move(0, 1, 0);
                }
            } else {
                while ((level.m_8055_(generateAt).m_60819_().is(Fluids.WATER) || !level.m_8055_(generateAt).m_60783_(level, generateAt, Direction.UP)) && generateAt.m_123342_() > level.m_141937_()) {
                    generateAt.move(0, -1, 0);
                }
            }
            if (!level.m_8055_(generateAt).m_204336_(ACTagRegistry.TESLA_BULB_BASE_BLOCKS)) {
                return false;
            } else {
                BlockPos below = generateAt.immutable();
                int centerHeight = 3 + randomsource.nextInt(3);
                generatePillar(level, below, randomsource, centerHeight, ceiling, randomsource.nextFloat() < 0.25F);
                for (int i = 0; i < 4 + randomsource.nextInt(4); i++) {
                    BlockPos offset = below.offset(randomsource.nextInt(8) - 4, ceiling ? -3 : 3, randomsource.nextInt(8) - 4);
                    if (ceiling) {
                        while (!level.m_8055_(offset).m_60783_(level, offset, Direction.DOWN) && offset.m_123342_() < level.m_151558_()) {
                            offset = offset.above();
                        }
                    } else {
                        while (!level.m_8055_(offset).m_60783_(level, offset, Direction.UP) && offset.m_123342_() > level.m_141937_()) {
                            offset = offset.below();
                        }
                    }
                    if (level.m_8055_(offset.relative(ceiling ? Direction.UP : Direction.DOWN)).m_204336_(ACTagRegistry.TESLA_BULB_BASE_BLOCKS) && (offset.m_123341_() != below.m_123341_() || offset.m_123343_() != below.m_123343_())) {
                        int dist = (int) Math.ceil((double) ((float) offset.m_123333_(below) * 0.2F));
                        generatePillar(level, offset, randomsource, Math.min(centerHeight - dist, 1) + randomsource.nextInt(2), ceiling, false);
                    }
                }
                return true;
            }
        }
    }

    private static boolean canReplace(BlockState state) {
        return (state.m_60795_() || state.m_247087_()) && !state.m_204336_(ACTagRegistry.UNMOVEABLE);
    }

    private static void generatePillar(WorldGenLevel level, BlockPos pos, RandomSource randomSource, int height, boolean ceiling, boolean tesla) {
        BlockPos begin = pos.relative(ceiling ? Direction.UP : Direction.DOWN, 3);
        BlockState spireState = (BlockState) ACBlockRegistry.GALENA_SPIRE.get().defaultBlockState().m_61124_(GalenaSpireBlock.DOWN, ceiling);
        int spireCount = 0;
        int j = 0;
        while (spireCount <= height && j < 25) {
            j++;
            int shape = 0;
            if (spireCount > height - 1) {
                shape = 3;
            } else if (spireCount > height - 2) {
                shape = 2;
            } else if (spireCount >= 1) {
                shape = 1;
            }
            begin = ceiling ? begin.below() : begin.above();
            BlockState prevState = level.m_8055_(begin);
            if (prevState.m_60713_(ACBlockRegistry.GALENA_SPIRE.get()) || !prevState.m_60819_().isEmpty() && !prevState.m_60819_().is(Fluids.WATER)) {
                break;
            }
            if (canReplace(prevState)) {
                if (shape == 3 && tesla) {
                    level.m_7731_(begin, (BlockState) ((BlockState) ACBlockRegistry.TESLA_BULB.get().defaultBlockState().m_61124_(TeslaBulbBlock.DOWN, ceiling)).m_61124_(TeslaBulbBlock.WATERLOGGED, level.m_6425_(begin).is(Fluids.WATER)), 3);
                    break;
                }
                level.m_7731_(begin, (BlockState) ((BlockState) spireState.m_61124_(GalenaSpireBlock.SHAPE, shape)).m_61124_(GalenaSpireBlock.WATERLOGGED, level.m_6425_(begin).is(Fluids.WATER)), 3);
                spireCount++;
            }
        }
    }
}