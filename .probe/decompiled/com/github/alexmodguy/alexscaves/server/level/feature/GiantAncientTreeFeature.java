package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.TreeStarBlock;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class GiantAncientTreeFeature extends Feature<NoneFeatureConfiguration> {

    public GiantAncientTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        BlockPos treeBottom = context.origin();
        int maxHeight = 20 + randomsource.nextInt(10);
        int trunkThickness = 3;
        if (!this.checkCanTreePlace(level, treeBottom, maxHeight)) {
            return false;
        } else {
            BlockPos trunkRoot = treeBottom.below(2);
            int lastLeavesY = 0;
            for (int height = 0; height <= maxHeight; height++) {
                trunkRoot = trunkRoot.above();
                double radShrink = 1.0 - 0.8 * Math.pow((double) ((float) height / (float) maxHeight), 2.0);
                double leavesShrink = (double) (1.0F - 0.45F * ((float) height / (float) maxHeight));
                for (int width = -((int) Math.floor((double) ((float) trunkThickness / 2.0F))); width < (int) Math.ceil((double) ((float) trunkThickness / 2.0F)); width++) {
                    for (int length = -((int) Math.floor((double) ((float) trunkThickness / 2.0F))); length < (int) Math.ceil((double) ((float) trunkThickness / 2.0F)); length++) {
                        BlockPos logPos = trunkRoot.offset(width, 0, length);
                        if (trunkRoot.m_203202_((double) logPos.m_123341_(), (double) trunkRoot.m_123342_(), (double) logPos.m_123343_()) <= (double) (trunkThickness * trunkThickness) / 4.0 * radShrink && canReplace(level.m_8055_(logPos))) {
                            level.m_7731_(logPos, Blocks.JUNGLE_LOG.defaultBlockState(), 3);
                        }
                    }
                }
                if (height == maxHeight || height > 3 && (height - lastLeavesY > 4 + randomsource.nextInt(2) || randomsource.nextInt(5) == 0)) {
                    lastLeavesY = height;
                    drawLeafOrb(level, trunkRoot.offset((int) ((double) (randomsource.nextInt(2) - 1) * radShrink), 0, (int) ((double) (randomsource.nextInt(2) - 1) * radShrink)), randomsource, ACBlockRegistry.ANCIENT_LEAVES.get().defaultBlockState(), (int) Math.ceil(4.0 * leavesShrink) + randomsource.nextInt(2), 1 + randomsource.nextInt(2), (int) Math.ceil(4.0 * leavesShrink) + randomsource.nextInt(2));
                }
            }
            return true;
        }
    }

    private boolean checkCanTreePlace(WorldGenLevel level, BlockPos treeBottom, int height) {
        BlockState below = level.m_8055_(treeBottom.below());
        if (!below.m_204336_(BlockTags.DIRT)) {
            return false;
        } else {
            for (int i = 0; i < height; i++) {
                if (!canReplace(level.m_8055_(treeBottom.above(i)))) {
                    return false;
                }
            }
            BlockPos treeTop = treeBottom.above(height);
            for (BlockPos checkLeaf : BlockPos.betweenClosed(treeTop.offset(-3, -1, -3), treeTop.offset(3, 3, 3))) {
                if (!canReplace(level.m_8055_(checkLeaf))) {
                    return false;
                }
            }
            return true;
        }
    }

    private static void drawLeafOrb(WorldGenLevel level, BlockPos center, RandomSource random, BlockState blockState, int radiusX, int radiusY, int radiusZ) {
        double equalRadius = (double) (radiusX + radiusY + radiusZ) / 3.0;
        for (int x = -radiusX; x <= radiusX; x++) {
            for (int y = -radiusY / 3; y <= radiusY; y++) {
                for (int z = -radiusZ; z <= radiusZ; z++) {
                    BlockPos fill = center.offset(x, y, z);
                    if (fill.m_203202_((double) center.m_123341_(), (double) center.m_123342_(), (double) center.m_123343_()) <= equalRadius * equalRadius - (double) (random.nextFloat() * 2.0F) && canReplace(level.m_8055_(fill))) {
                        level.m_7731_(fill, blockState, 3);
                        if (random.nextInt(5) == 0) {
                            Direction dir = Direction.getRandom(random);
                            BlockPos starPos = fill.relative(dir);
                            if (level.m_8055_(starPos).m_60795_()) {
                                level.m_7731_(starPos, (BlockState) ACBlockRegistry.TREE_STAR.get().defaultBlockState().m_61124_(TreeStarBlock.FACING, dir), 3);
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean canReplace(BlockState state) {
        return (state.m_60795_() || state.m_247087_() || state.m_60713_(ACBlockRegistry.ANCIENT_LEAVES.get()) || state.m_60713_(ACBlockRegistry.TREE_STAR.get())) && state.m_60819_().isEmpty() && !state.m_204336_(ACTagRegistry.UNMOVEABLE);
    }
}