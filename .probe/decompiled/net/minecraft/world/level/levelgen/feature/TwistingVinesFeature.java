package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TwistingVinesConfig;

public class TwistingVinesFeature extends Feature<TwistingVinesConfig> {

    public TwistingVinesFeature(Codec<TwistingVinesConfig> codecTwistingVinesConfig0) {
        super(codecTwistingVinesConfig0);
    }

    @Override
    public boolean place(FeaturePlaceContext<TwistingVinesConfig> featurePlaceContextTwistingVinesConfig0) {
        WorldGenLevel $$1 = featurePlaceContextTwistingVinesConfig0.level();
        BlockPos $$2 = featurePlaceContextTwistingVinesConfig0.origin();
        if (isInvalidPlacementLocation($$1, $$2)) {
            return false;
        } else {
            RandomSource $$3 = featurePlaceContextTwistingVinesConfig0.random();
            TwistingVinesConfig $$4 = featurePlaceContextTwistingVinesConfig0.config();
            int $$5 = $$4.spreadWidth();
            int $$6 = $$4.spreadHeight();
            int $$7 = $$4.maxHeight();
            BlockPos.MutableBlockPos $$8 = new BlockPos.MutableBlockPos();
            for (int $$9 = 0; $$9 < $$5 * $$5; $$9++) {
                $$8.set($$2).move(Mth.nextInt($$3, -$$5, $$5), Mth.nextInt($$3, -$$6, $$6), Mth.nextInt($$3, -$$5, $$5));
                if (findFirstAirBlockAboveGround($$1, $$8) && !isInvalidPlacementLocation($$1, $$8)) {
                    int $$10 = Mth.nextInt($$3, 1, $$7);
                    if ($$3.nextInt(6) == 0) {
                        $$10 *= 2;
                    }
                    if ($$3.nextInt(5) == 0) {
                        $$10 = 1;
                    }
                    int $$11 = 17;
                    int $$12 = 25;
                    placeWeepingVinesColumn($$1, $$3, $$8, $$10, 17, 25);
                }
            }
            return true;
        }
    }

    private static boolean findFirstAirBlockAboveGround(LevelAccessor levelAccessor0, BlockPos.MutableBlockPos blockPosMutableBlockPos1) {
        do {
            blockPosMutableBlockPos1.move(0, -1, 0);
            if (levelAccessor0.m_151570_(blockPosMutableBlockPos1)) {
                return false;
            }
        } while (levelAccessor0.m_8055_(blockPosMutableBlockPos1).m_60795_());
        blockPosMutableBlockPos1.move(0, 1, 0);
        return true;
    }

    public static void placeWeepingVinesColumn(LevelAccessor levelAccessor0, RandomSource randomSource1, BlockPos.MutableBlockPos blockPosMutableBlockPos2, int int3, int int4, int int5) {
        for (int $$6 = 1; $$6 <= int3; $$6++) {
            if (levelAccessor0.m_46859_(blockPosMutableBlockPos2)) {
                if ($$6 == int3 || !levelAccessor0.m_46859_(blockPosMutableBlockPos2.m_7494_())) {
                    levelAccessor0.m_7731_(blockPosMutableBlockPos2, (BlockState) Blocks.TWISTING_VINES.defaultBlockState().m_61124_(GrowingPlantHeadBlock.AGE, Mth.nextInt(randomSource1, int4, int5)), 2);
                    break;
                }
                levelAccessor0.m_7731_(blockPosMutableBlockPos2, Blocks.TWISTING_VINES_PLANT.defaultBlockState(), 2);
            }
            blockPosMutableBlockPos2.move(Direction.UP);
        }
    }

    private static boolean isInvalidPlacementLocation(LevelAccessor levelAccessor0, BlockPos blockPos1) {
        if (!levelAccessor0.m_46859_(blockPos1)) {
            return true;
        } else {
            BlockState $$2 = levelAccessor0.m_8055_(blockPos1.below());
            return !$$2.m_60713_(Blocks.NETHERRACK) && !$$2.m_60713_(Blocks.WARPED_NYLIUM) && !$$2.m_60713_(Blocks.WARPED_WART_BLOCK);
        }
    }
}