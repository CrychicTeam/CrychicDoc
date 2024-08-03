package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;

public class MultifaceGrowthFeature extends Feature<MultifaceGrowthConfiguration> {

    public MultifaceGrowthFeature(Codec<MultifaceGrowthConfiguration> codecMultifaceGrowthConfiguration0) {
        super(codecMultifaceGrowthConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<MultifaceGrowthConfiguration> featurePlaceContextMultifaceGrowthConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextMultifaceGrowthConfiguration0.level();
        BlockPos $$2 = featurePlaceContextMultifaceGrowthConfiguration0.origin();
        RandomSource $$3 = featurePlaceContextMultifaceGrowthConfiguration0.random();
        MultifaceGrowthConfiguration $$4 = featurePlaceContextMultifaceGrowthConfiguration0.config();
        if (!isAirOrWater($$1.m_8055_($$2))) {
            return false;
        } else {
            List<Direction> $$5 = $$4.getShuffledDirections($$3);
            if (placeGrowthIfPossible($$1, $$2, $$1.m_8055_($$2), $$4, $$3, $$5)) {
                return true;
            } else {
                BlockPos.MutableBlockPos $$6 = $$2.mutable();
                for (Direction $$7 : $$5) {
                    $$6.set($$2);
                    List<Direction> $$8 = $$4.getShuffledDirectionsExcept($$3, $$7.getOpposite());
                    for (int $$9 = 0; $$9 < $$4.searchRange; $$9++) {
                        $$6.setWithOffset($$2, $$7);
                        BlockState $$10 = $$1.m_8055_($$6);
                        if (!isAirOrWater($$10) && !$$10.m_60713_($$4.placeBlock)) {
                            break;
                        }
                        if (placeGrowthIfPossible($$1, $$6, $$10, $$4, $$3, $$8)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
    }

    public static boolean placeGrowthIfPossible(WorldGenLevel worldGenLevel0, BlockPos blockPos1, BlockState blockState2, MultifaceGrowthConfiguration multifaceGrowthConfiguration3, RandomSource randomSource4, List<Direction> listDirection5) {
        BlockPos.MutableBlockPos $$6 = blockPos1.mutable();
        for (Direction $$7 : listDirection5) {
            BlockState $$8 = worldGenLevel0.m_8055_($$6.setWithOffset(blockPos1, $$7));
            if ($$8.m_204341_(multifaceGrowthConfiguration3.canBePlacedOn)) {
                BlockState $$9 = multifaceGrowthConfiguration3.placeBlock.getStateForPlacement(blockState2, worldGenLevel0, blockPos1, $$7);
                if ($$9 == null) {
                    return false;
                }
                worldGenLevel0.m_7731_(blockPos1, $$9, 3);
                worldGenLevel0.m_46865_(blockPos1).markPosForPostprocessing(blockPos1);
                if (randomSource4.nextFloat() < multifaceGrowthConfiguration3.chanceOfSpreading) {
                    multifaceGrowthConfiguration3.placeBlock.getSpreader().spreadFromFaceTowardRandomDirection($$9, worldGenLevel0, blockPos1, $$7, randomSource4, true);
                }
                return true;
            }
        }
        return false;
    }

    private static boolean isAirOrWater(BlockState blockState0) {
        return blockState0.m_60795_() || blockState0.m_60713_(Blocks.WATER);
    }
}