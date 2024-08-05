package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.configurations.PointedDripstoneConfiguration;

public class PointedDripstoneFeature extends Feature<PointedDripstoneConfiguration> {

    public PointedDripstoneFeature(Codec<PointedDripstoneConfiguration> codecPointedDripstoneConfiguration0) {
        super(codecPointedDripstoneConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<PointedDripstoneConfiguration> featurePlaceContextPointedDripstoneConfiguration0) {
        LevelAccessor $$1 = featurePlaceContextPointedDripstoneConfiguration0.level();
        BlockPos $$2 = featurePlaceContextPointedDripstoneConfiguration0.origin();
        RandomSource $$3 = featurePlaceContextPointedDripstoneConfiguration0.random();
        PointedDripstoneConfiguration $$4 = featurePlaceContextPointedDripstoneConfiguration0.config();
        Optional<Direction> $$5 = getTipDirection($$1, $$2, $$3);
        if ($$5.isEmpty()) {
            return false;
        } else {
            BlockPos $$6 = $$2.relative(((Direction) $$5.get()).getOpposite());
            createPatchOfDripstoneBlocks($$1, $$3, $$6, $$4);
            int $$7 = $$3.nextFloat() < $$4.chanceOfTallerDripstone && DripstoneUtils.isEmptyOrWater($$1.m_8055_($$2.relative((Direction) $$5.get()))) ? 2 : 1;
            DripstoneUtils.growPointedDripstone($$1, $$2, (Direction) $$5.get(), $$7, false);
            return true;
        }
    }

    private static Optional<Direction> getTipDirection(LevelAccessor levelAccessor0, BlockPos blockPos1, RandomSource randomSource2) {
        boolean $$3 = DripstoneUtils.isDripstoneBase(levelAccessor0.m_8055_(blockPos1.above()));
        boolean $$4 = DripstoneUtils.isDripstoneBase(levelAccessor0.m_8055_(blockPos1.below()));
        if ($$3 && $$4) {
            return Optional.of(randomSource2.nextBoolean() ? Direction.DOWN : Direction.UP);
        } else if ($$3) {
            return Optional.of(Direction.DOWN);
        } else {
            return $$4 ? Optional.of(Direction.UP) : Optional.empty();
        }
    }

    private static void createPatchOfDripstoneBlocks(LevelAccessor levelAccessor0, RandomSource randomSource1, BlockPos blockPos2, PointedDripstoneConfiguration pointedDripstoneConfiguration3) {
        DripstoneUtils.placeDripstoneBlockIfPossible(levelAccessor0, blockPos2);
        for (Direction $$4 : Direction.Plane.HORIZONTAL) {
            if (!(randomSource1.nextFloat() > pointedDripstoneConfiguration3.chanceOfDirectionalSpread)) {
                BlockPos $$5 = blockPos2.relative($$4);
                DripstoneUtils.placeDripstoneBlockIfPossible(levelAccessor0, $$5);
                if (!(randomSource1.nextFloat() > pointedDripstoneConfiguration3.chanceOfSpreadRadius2)) {
                    BlockPos $$6 = $$5.relative(Direction.getRandom(randomSource1));
                    DripstoneUtils.placeDripstoneBlockIfPossible(levelAccessor0, $$6);
                    if (!(randomSource1.nextFloat() > pointedDripstoneConfiguration3.chanceOfSpreadRadius3)) {
                        BlockPos $$7 = $$6.relative(Direction.getRandom(randomSource1));
                        DripstoneUtils.placeDripstoneBlockIfPossible(levelAccessor0, $$7);
                    }
                }
            }
        }
    }
}