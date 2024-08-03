package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.ThornwoodBranchBlock;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

public class ThornwoodTreeFeature extends Feature<NoneFeatureConfiguration> {

    public ThornwoodTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        BlockPos treeGround = context.origin();
        int centerAboveGround = randomsource.nextInt(5);
        int height = centerAboveGround + 4 + randomsource.nextInt(5);
        if (!this.checkCanTreePlace(level, treeGround, height)) {
            return false;
        } else {
            BlockPos centerPos = treeGround.above(centerAboveGround);
            if (centerAboveGround > 0) {
                int rootCount = 0;
                for (Direction direction : ACMath.HORIZONTAL_DIRECTIONS) {
                    if (rootCount <= 3 + randomsource.nextInt(1)) {
                        generateRoot(level, centerPos.relative(direction), 0.25F, randomsource, direction, centerAboveGround + 1 + randomsource.nextInt(6));
                        rootCount++;
                    }
                }
            }
            BlockPos.MutableBlockPos trunkPos = new BlockPos.MutableBlockPos();
            int i = 0;
            trunkPos.set(centerPos);
            trunkPos.move(0, -1, 0);
            for (int tallPart = height - centerAboveGround; i < tallPart; decorateLog(level, trunkPos, randomsource, true)) {
                i++;
                trunkPos.move(0, 1, 0);
                if (randomsource.nextInt(5) == 0) {
                    level.m_7731_(trunkPos, ACBlockRegistry.THORNWOOD_WOOD.get().defaultBlockState(), 3);
                    trunkPos.move(Util.getRandom(ACMath.HORIZONTAL_DIRECTIONS, randomsource));
                    level.m_7731_(trunkPos, ACBlockRegistry.THORNWOOD_WOOD.get().defaultBlockState(), 3);
                } else {
                    level.m_7731_(trunkPos, i == tallPart ? ACBlockRegistry.THORNWOOD_WOOD.get().defaultBlockState() : ACBlockRegistry.THORNWOOD_LOG.get().defaultBlockState(), 3);
                }
            }
            BlockPos canopy = trunkPos.immutable();
            BlockPos.MutableBlockPos canopyLogPos = new BlockPos.MutableBlockPos();
            for (Direction directionx : ACMath.HORIZONTAL_DIRECTIONS) {
                canopyLogPos.set(canopy);
                int canopyLength = 1 + randomsource.nextInt(3);
                for (int j = 1; j <= canopyLength; j++) {
                    boolean upFlag = false;
                    canopyLogPos.move(directionx);
                    if (randomsource.nextInt(2) != 0) {
                        upFlag = true;
                        level.m_7731_(canopyLogPos, (BlockState) ACBlockRegistry.THORNWOOD_WOOD.get().defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, directionx.getAxis()), 3);
                        canopyLogPos.move(0, 1, 0);
                    }
                    if (j == canopyLength && level.m_8055_(canopyLogPos).m_247087_()) {
                        level.m_7731_(canopyLogPos, (BlockState) ((BlockState) ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().m_61124_(ThornwoodBranchBlock.FACING, upFlag ? Direction.UP : directionx)).m_61124_(ThornwoodBranchBlock.WATERLOGGED, level.m_6425_(canopyLogPos).is(Fluids.WATER)), 3);
                    } else {
                        Block wood = j != canopyLength - 1 && !upFlag ? ACBlockRegistry.THORNWOOD_LOG.get() : ACBlockRegistry.THORNWOOD_WOOD.get();
                        level.m_7731_(canopyLogPos, (BlockState) wood.defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, directionx.getAxis()), 3);
                        decorateLog(level, trunkPos, randomsource, true);
                    }
                }
            }
            return true;
        }
    }

    private boolean checkCanTreePlace(WorldGenLevel level, BlockPos treeBottom, int height) {
        BlockState below = level.m_8055_(treeBottom.below());
        if (!below.m_204336_(BlockTags.DIRT) && !below.m_60713_(ACBlockRegistry.GUANOSTONE.get()) && !below.m_60713_(ACBlockRegistry.COPROLITH.get()) && !below.m_60713_(Blocks.PACKED_MUD)) {
            return false;
        } else {
            for (int i = 0; i < height; i++) {
                if (!canReplace(level.m_8055_(treeBottom.above(i)))) {
                    return false;
                }
            }
            BlockPos treeTop = treeBottom.above(height).immutable();
            for (BlockPos checkLeaf : BlockPos.betweenClosed(treeTop.offset(-2, -1, -2), treeTop.offset(2, 1, 2))) {
                if (!canReplace(level.m_8055_(checkLeaf))) {
                    return false;
                }
            }
            return true;
        }
    }

    protected static void decorateLog(WorldGenLevel level, BlockPos from, RandomSource random, boolean logBranches) {
        if (random.nextFloat() < 0.65F) {
            Direction ranDir = Util.getRandom(Direction.values(), random);
            BlockPos branchPos = from.immutable().relative(ranDir);
            if (level.m_8055_(branchPos).m_247087_()) {
                if (logBranches && random.nextFloat() < 0.4F) {
                    int bigBranchLength = 1 + random.nextInt(1);
                    for (int i = 0; i < bigBranchLength; i++) {
                        Block wood = i == bigBranchLength - 1 ? ACBlockRegistry.THORNWOOD_WOOD.get() : ACBlockRegistry.THORNWOOD_LOG.get();
                        level.m_7731_(branchPos, (BlockState) wood.defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, ranDir.getAxis()), 3);
                        branchPos = branchPos.relative(ranDir);
                    }
                }
                level.m_7731_(branchPos, (BlockState) ((BlockState) ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().m_61124_(ThornwoodBranchBlock.FACING, ranDir)).m_61124_(ThornwoodBranchBlock.WATERLOGGED, level.m_6425_(branchPos).is(Fluids.WATER)), 3);
            }
        }
    }

    public static void generateRoot(WorldGenLevel level, BlockPos from, float bendChance, RandomSource random, Direction direction, int length) {
        BlockPos.MutableBlockPos at = new BlockPos.MutableBlockPos();
        at.set(from);
        for (int i = 0; i < length; i++) {
            if (level.m_8055_(at).m_204336_(ACTagRegistry.UNMOVEABLE)) {
                return;
            }
            if (random.nextFloat() < bendChance) {
                if (!level.m_8055_(at).m_60713_(ACBlockRegistry.THORNWOOD_WOOD.get())) {
                    level.m_7731_(at, (BlockState) ACBlockRegistry.THORNWOOD_WOOD.get().defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, direction.getAxis()), 3);
                }
                at.move(0, -1, 0);
                at.move(direction);
                level.m_7731_(at, ACBlockRegistry.THORNWOOD_WOOD.get().defaultBlockState(), 3);
            } else {
                at.move(0, -1, 0);
                level.m_7731_(at, i == 0 ? ACBlockRegistry.THORNWOOD_WOOD.get().defaultBlockState() : ACBlockRegistry.THORNWOOD_LOG.get().defaultBlockState(), 3);
            }
            decorateLog(level, at, random, false);
        }
        BlockPos rootPos = at.immutable().below();
        if (level.m_8055_(rootPos).m_247087_()) {
            level.m_7731_(rootPos, (BlockState) ((BlockState) ACBlockRegistry.THORNWOOD_BRANCH.get().defaultBlockState().m_61124_(ThornwoodBranchBlock.FACING, Direction.DOWN)).m_61124_(ThornwoodBranchBlock.WATERLOGGED, level.m_6425_(rootPos).is(Fluids.WATER)), 3);
        }
    }

    private static boolean canReplace(BlockState state) {
        return (state.m_60795_() || state.m_247087_() || state.m_60713_(ACBlockRegistry.THORNWOOD_BRANCH.get()) || state.m_204336_(BlockTags.DIRT) || state.m_60713_(Blocks.PACKED_MUD)) && !state.m_204336_(ACTagRegistry.UNMOVEABLE);
    }
}