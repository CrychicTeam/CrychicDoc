package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.RootSystemConfiguration;

public class RootSystemFeature extends Feature<RootSystemConfiguration> {

    public RootSystemFeature(Codec<RootSystemConfiguration> codecRootSystemConfiguration0) {
        super(codecRootSystemConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<RootSystemConfiguration> featurePlaceContextRootSystemConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextRootSystemConfiguration0.level();
        BlockPos $$2 = featurePlaceContextRootSystemConfiguration0.origin();
        if (!$$1.m_8055_($$2).m_60795_()) {
            return false;
        } else {
            RandomSource $$3 = featurePlaceContextRootSystemConfiguration0.random();
            BlockPos $$4 = featurePlaceContextRootSystemConfiguration0.origin();
            RootSystemConfiguration $$5 = featurePlaceContextRootSystemConfiguration0.config();
            BlockPos.MutableBlockPos $$6 = $$4.mutable();
            if (placeDirtAndTree($$1, featurePlaceContextRootSystemConfiguration0.chunkGenerator(), $$5, $$3, $$6, $$4)) {
                placeRoots($$1, $$5, $$3, $$4, $$6);
            }
            return true;
        }
    }

    private static boolean spaceForTree(WorldGenLevel worldGenLevel0, RootSystemConfiguration rootSystemConfiguration1, BlockPos blockPos2) {
        BlockPos.MutableBlockPos $$3 = blockPos2.mutable();
        for (int $$4 = 1; $$4 <= rootSystemConfiguration1.requiredVerticalSpaceForTree; $$4++) {
            $$3.move(Direction.UP);
            BlockState $$5 = worldGenLevel0.m_8055_($$3);
            if (!isAllowedTreeSpace($$5, $$4, rootSystemConfiguration1.allowedVerticalWaterForTree)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAllowedTreeSpace(BlockState blockState0, int int1, int int2) {
        if (blockState0.m_60795_()) {
            return true;
        } else {
            int $$3 = int1 + 1;
            return $$3 <= int2 && blockState0.m_60819_().is(FluidTags.WATER);
        }
    }

    private static boolean placeDirtAndTree(WorldGenLevel worldGenLevel0, ChunkGenerator chunkGenerator1, RootSystemConfiguration rootSystemConfiguration2, RandomSource randomSource3, BlockPos.MutableBlockPos blockPosMutableBlockPos4, BlockPos blockPos5) {
        for (int $$6 = 0; $$6 < rootSystemConfiguration2.rootColumnMaxHeight; $$6++) {
            blockPosMutableBlockPos4.move(Direction.UP);
            if (rootSystemConfiguration2.allowedTreePosition.test(worldGenLevel0, blockPosMutableBlockPos4) && spaceForTree(worldGenLevel0, rootSystemConfiguration2, blockPosMutableBlockPos4)) {
                BlockPos $$7 = blockPosMutableBlockPos4.m_7495_();
                if (worldGenLevel0.m_6425_($$7).is(FluidTags.LAVA) || !worldGenLevel0.m_8055_($$7).m_280296_()) {
                    return false;
                }
                if (rootSystemConfiguration2.treeFeature.value().place(worldGenLevel0, chunkGenerator1, randomSource3, blockPosMutableBlockPos4)) {
                    placeDirt(blockPos5, blockPos5.m_123342_() + $$6, worldGenLevel0, rootSystemConfiguration2, randomSource3);
                    return true;
                }
            }
        }
        return false;
    }

    private static void placeDirt(BlockPos blockPos0, int int1, WorldGenLevel worldGenLevel2, RootSystemConfiguration rootSystemConfiguration3, RandomSource randomSource4) {
        int $$5 = blockPos0.m_123341_();
        int $$6 = blockPos0.m_123343_();
        BlockPos.MutableBlockPos $$7 = blockPos0.mutable();
        for (int $$8 = blockPos0.m_123342_(); $$8 < int1; $$8++) {
            placeRootedDirt(worldGenLevel2, rootSystemConfiguration3, randomSource4, $$5, $$6, $$7.set($$5, $$8, $$6));
        }
    }

    private static void placeRootedDirt(WorldGenLevel worldGenLevel0, RootSystemConfiguration rootSystemConfiguration1, RandomSource randomSource2, int int3, int int4, BlockPos.MutableBlockPos blockPosMutableBlockPos5) {
        int $$6 = rootSystemConfiguration1.rootRadius;
        Predicate<BlockState> $$7 = p_204762_ -> p_204762_.m_204336_(rootSystemConfiguration1.rootReplaceable);
        for (int $$8 = 0; $$8 < rootSystemConfiguration1.rootPlacementAttempts; $$8++) {
            blockPosMutableBlockPos5.setWithOffset(blockPosMutableBlockPos5, randomSource2.nextInt($$6) - randomSource2.nextInt($$6), 0, randomSource2.nextInt($$6) - randomSource2.nextInt($$6));
            if ($$7.test(worldGenLevel0.m_8055_(blockPosMutableBlockPos5))) {
                worldGenLevel0.m_7731_(blockPosMutableBlockPos5, rootSystemConfiguration1.rootStateProvider.getState(randomSource2, blockPosMutableBlockPos5), 2);
            }
            blockPosMutableBlockPos5.setX(int3);
            blockPosMutableBlockPos5.setZ(int4);
        }
    }

    private static void placeRoots(WorldGenLevel worldGenLevel0, RootSystemConfiguration rootSystemConfiguration1, RandomSource randomSource2, BlockPos blockPos3, BlockPos.MutableBlockPos blockPosMutableBlockPos4) {
        int $$5 = rootSystemConfiguration1.hangingRootRadius;
        int $$6 = rootSystemConfiguration1.hangingRootsVerticalSpan;
        for (int $$7 = 0; $$7 < rootSystemConfiguration1.hangingRootPlacementAttempts; $$7++) {
            blockPosMutableBlockPos4.setWithOffset(blockPos3, randomSource2.nextInt($$5) - randomSource2.nextInt($$5), randomSource2.nextInt($$6) - randomSource2.nextInt($$6), randomSource2.nextInt($$5) - randomSource2.nextInt($$5));
            if (worldGenLevel0.m_46859_(blockPosMutableBlockPos4)) {
                BlockState $$8 = rootSystemConfiguration1.hangingRootStateProvider.getState(randomSource2, blockPosMutableBlockPos4);
                if ($$8.m_60710_(worldGenLevel0, blockPosMutableBlockPos4) && worldGenLevel0.m_8055_(blockPosMutableBlockPos4.m_7494_()).m_60783_(worldGenLevel0, blockPosMutableBlockPos4, Direction.DOWN)) {
                    worldGenLevel0.m_7731_(blockPosMutableBlockPos4, $$8, 2);
                }
            }
        }
    }
}