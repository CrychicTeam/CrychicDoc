package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.TubeWormBlock;
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

public class TubeWormFeature extends Feature<NoneFeatureConfiguration> {

    public TubeWormFeature(Codec<NoneFeatureConfiguration> codec) {
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
        if (ventBottom.m_123342_() < level.m_5736_() - 30 && level.m_8055_(ventBottom.m_7495_()).equals(Blocks.TUFF.defaultBlockState())) {
            for (int i = 0; i < 4 + randomsource.nextInt(4); i++) {
                BlockPos wormAt = ventBottom.immutable().offset(randomsource.nextInt(10) - 5, randomsource.nextInt(4), randomsource.nextInt(10) - 5);
                Direction wormAttachDirection = Direction.DOWN;
                Direction randomDirection = Direction.from2DDataValue(2 + randomsource.nextInt(3));
                BlockPos randomPos = wormAt.relative(randomDirection);
                BlockState randomState = level.m_8055_(randomPos);
                BlockPos wormAttachedToPos;
                if (randomState.m_60783_(level, randomPos, randomDirection.getOpposite())) {
                    wormAttachDirection = randomDirection;
                    wormAttachedToPos = randomPos;
                } else {
                    while (level.m_8055_(wormAt).m_60783_(level, wormAt.above(), Direction.DOWN) && wormAt.m_123342_() < level.m_151558_()) {
                        wormAt = wormAt.above();
                    }
                    while (!level.m_8055_(wormAt).m_60819_().isEmpty() && wormAt.m_123342_() > level.m_141937_()) {
                        wormAt = wormAt.below();
                    }
                    wormAttachedToPos = wormAt.below();
                }
                if (!level.m_8055_(wormAt).m_204336_(ACTagRegistry.TUBE_WORM_AVOIDS) && !level.m_8055_(wormAt).m_60713_(ACBlockRegistry.TUBE_WORM.get())) {
                    int maxSegments = 4 + randomsource.nextInt(12);
                    this.growWorm(level, wormAttachedToPos, wormAttachDirection, randomsource, maxSegments);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private void growWorm(WorldGenLevel level, BlockPos wormAttachedToPos, Direction wormAttachDirection, RandomSource randomsource, int maxSegments) {
        if (wormAttachedToPos.m_123342_() <= level.m_5736_() - 30) {
            int placedWorms = 0;
            BlockPos.MutableBlockPos prevWorm = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos worm = new BlockPos.MutableBlockPos();
            prevWorm.set(wormAttachedToPos);
            worm.set(wormAttachedToPos.relative(wormAttachDirection.getOpposite()));
            BlockState defaultWormState = (BlockState) ACBlockRegistry.TUBE_WORM.get().defaultBlockState().m_61124_(TubeWormBlock.WATERLOGGED, true);
            for (boolean canBranch = false; placedWorms < maxSegments; placedWorms++) {
                BlockState wormState = defaultWormState;
                prevWorm.set(worm);
                if (worm.m_123342_() > level.m_5736_() - 30) {
                    return;
                }
                if (canBranch) {
                    if (randomsource.nextBoolean()) {
                        Direction randomDirection = Direction.from2DDataValue(2 + randomsource.nextInt(3));
                        worm.move(randomDirection.getStepX(), randomDirection.getStepY(), randomDirection.getStepZ());
                        if (!level.m_6425_(worm).isEmpty()) {
                            if (!level.m_8055_(worm).m_60713_(Blocks.WATER) || !level.m_8055_(worm).m_247087_()) {
                                return;
                            }
                            if (level.m_8055_(prevWorm).m_247087_() || level.m_8055_(prevWorm).m_60713_(ACBlockRegistry.TUBE_WORM.get())) {
                                level.m_7731_(prevWorm, (BlockState) ((BlockState) defaultWormState.m_61124_(TubeWormBlock.TUBE_TYPE, TubeWormBlock.TubeShape.TURN)).m_61124_(TubeWormBlock.FACING, randomDirection), 3);
                            }
                            wormState = (BlockState) ((BlockState) defaultWormState.m_61124_(TubeWormBlock.TUBE_TYPE, TubeWormBlock.TubeShape.ELBOW)).m_61124_(TubeWormBlock.FACING, randomDirection.getOpposite());
                        } else {
                            worm.set(prevWorm);
                            worm.move(0, 1, 0);
                        }
                    }
                    canBranch = false;
                } else {
                    worm.move(0, 1, 0);
                    canBranch = placedWorms > 1;
                }
                if (level.m_6425_(worm).isEmpty()) {
                    break;
                }
                if (level.m_8055_(worm).m_247087_()) {
                    level.m_7731_(worm, wormState, 3);
                }
            }
            BlockPos fixAttachementPos = wormAttachedToPos.relative(wormAttachDirection.getOpposite());
            if (wormAttachDirection.getAxis().isHorizontal() && (level.m_8055_(fixAttachementPos).m_247087_() || level.m_8055_(fixAttachementPos).m_60713_(ACBlockRegistry.TUBE_WORM.get()))) {
                level.m_7731_(fixAttachementPos, (BlockState) ((BlockState) defaultWormState.m_61124_(TubeWormBlock.TUBE_TYPE, TubeWormBlock.TubeShape.ELBOW)).m_61124_(TubeWormBlock.FACING, wormAttachDirection), 3);
            }
        }
    }
}