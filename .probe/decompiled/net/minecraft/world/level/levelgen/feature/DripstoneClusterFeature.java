package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ClampedNormalFloat;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.configurations.DripstoneClusterConfiguration;

public class DripstoneClusterFeature extends Feature<DripstoneClusterConfiguration> {

    public DripstoneClusterFeature(Codec<DripstoneClusterConfiguration> codecDripstoneClusterConfiguration0) {
        super(codecDripstoneClusterConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<DripstoneClusterConfiguration> featurePlaceContextDripstoneClusterConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextDripstoneClusterConfiguration0.level();
        BlockPos $$2 = featurePlaceContextDripstoneClusterConfiguration0.origin();
        DripstoneClusterConfiguration $$3 = featurePlaceContextDripstoneClusterConfiguration0.config();
        RandomSource $$4 = featurePlaceContextDripstoneClusterConfiguration0.random();
        if (!DripstoneUtils.isEmptyOrWater($$1, $$2)) {
            return false;
        } else {
            int $$5 = $$3.height.sample($$4);
            float $$6 = $$3.wetness.m_214084_($$4);
            float $$7 = $$3.density.m_214084_($$4);
            int $$8 = $$3.radius.sample($$4);
            int $$9 = $$3.radius.sample($$4);
            for (int $$10 = -$$8; $$10 <= $$8; $$10++) {
                for (int $$11 = -$$9; $$11 <= $$9; $$11++) {
                    double $$12 = this.getChanceOfStalagmiteOrStalactite($$8, $$9, $$10, $$11, $$3);
                    BlockPos $$13 = $$2.offset($$10, 0, $$11);
                    this.placeColumn($$1, $$4, $$13, $$10, $$11, $$6, $$12, $$5, $$7, $$3);
                }
            }
            return true;
        }
    }

    private void placeColumn(WorldGenLevel worldGenLevel0, RandomSource randomSource1, BlockPos blockPos2, int int3, int int4, float float5, double double6, int int7, float float8, DripstoneClusterConfiguration dripstoneClusterConfiguration9) {
        Optional<Column> $$10 = Column.scan(worldGenLevel0, blockPos2, dripstoneClusterConfiguration9.floorToCeilingSearchRange, DripstoneUtils::m_159664_, DripstoneUtils::m_203130_);
        if ($$10.isPresent()) {
            OptionalInt $$11 = ((Column) $$10.get()).getCeiling();
            OptionalInt $$12 = ((Column) $$10.get()).getFloor();
            if ($$11.isPresent() || $$12.isPresent()) {
                boolean $$13 = randomSource1.nextFloat() < float5;
                Column $$15;
                if ($$13 && $$12.isPresent() && this.canPlacePool(worldGenLevel0, blockPos2.atY($$12.getAsInt()))) {
                    int $$14 = $$12.getAsInt();
                    $$15 = ((Column) $$10.get()).withFloor(OptionalInt.of($$14 - 1));
                    worldGenLevel0.m_7731_(blockPos2.atY($$14), Blocks.WATER.defaultBlockState(), 2);
                } else {
                    $$15 = (Column) $$10.get();
                }
                OptionalInt $$17 = $$15.getFloor();
                boolean $$18 = randomSource1.nextDouble() < double6;
                int $$22;
                if ($$11.isPresent() && $$18 && !this.isLava(worldGenLevel0, blockPos2.atY($$11.getAsInt()))) {
                    int $$19 = dripstoneClusterConfiguration9.dripstoneBlockLayerThickness.sample(randomSource1);
                    this.replaceBlocksWithDripstoneBlocks(worldGenLevel0, blockPos2.atY($$11.getAsInt()), $$19, Direction.UP);
                    int $$20;
                    if ($$17.isPresent()) {
                        $$20 = Math.min(int7, $$11.getAsInt() - $$17.getAsInt());
                    } else {
                        $$20 = int7;
                    }
                    $$22 = this.getDripstoneHeight(randomSource1, int3, int4, float8, $$20, dripstoneClusterConfiguration9);
                } else {
                    $$22 = 0;
                }
                boolean $$24 = randomSource1.nextDouble() < double6;
                int $$26;
                if ($$17.isPresent() && $$24 && !this.isLava(worldGenLevel0, blockPos2.atY($$17.getAsInt()))) {
                    int $$25 = dripstoneClusterConfiguration9.dripstoneBlockLayerThickness.sample(randomSource1);
                    this.replaceBlocksWithDripstoneBlocks(worldGenLevel0, blockPos2.atY($$17.getAsInt()), $$25, Direction.DOWN);
                    if ($$11.isPresent()) {
                        $$26 = Math.max(0, $$22 + Mth.randomBetweenInclusive(randomSource1, -dripstoneClusterConfiguration9.maxStalagmiteStalactiteHeightDiff, dripstoneClusterConfiguration9.maxStalagmiteStalactiteHeightDiff));
                    } else {
                        $$26 = this.getDripstoneHeight(randomSource1, int3, int4, float8, int7, dripstoneClusterConfiguration9);
                    }
                } else {
                    $$26 = 0;
                }
                int $$36;
                int $$35;
                if ($$11.isPresent() && $$17.isPresent() && $$11.getAsInt() - $$22 <= $$17.getAsInt() + $$26) {
                    int $$29 = $$17.getAsInt();
                    int $$30 = $$11.getAsInt();
                    int $$31 = Math.max($$30 - $$22, $$29 + 1);
                    int $$32 = Math.min($$29 + $$26, $$30 - 1);
                    int $$33 = Mth.randomBetweenInclusive(randomSource1, $$31, $$32 + 1);
                    int $$34 = $$33 - 1;
                    $$35 = $$30 - $$33;
                    $$36 = $$34 - $$29;
                } else {
                    $$35 = $$22;
                    $$36 = $$26;
                }
                boolean $$39 = randomSource1.nextBoolean() && $$35 > 0 && $$36 > 0 && $$15.getHeight().isPresent() && $$35 + $$36 == $$15.getHeight().getAsInt();
                if ($$11.isPresent()) {
                    DripstoneUtils.growPointedDripstone(worldGenLevel0, blockPos2.atY($$11.getAsInt() - 1), Direction.DOWN, $$35, $$39);
                }
                if ($$17.isPresent()) {
                    DripstoneUtils.growPointedDripstone(worldGenLevel0, blockPos2.atY($$17.getAsInt() + 1), Direction.UP, $$36, $$39);
                }
            }
        }
    }

    private boolean isLava(LevelReader levelReader0, BlockPos blockPos1) {
        return levelReader0.m_8055_(blockPos1).m_60713_(Blocks.LAVA);
    }

    private int getDripstoneHeight(RandomSource randomSource0, int int1, int int2, float float3, int int4, DripstoneClusterConfiguration dripstoneClusterConfiguration5) {
        if (randomSource0.nextFloat() > float3) {
            return 0;
        } else {
            int $$6 = Math.abs(int1) + Math.abs(int2);
            float $$7 = (float) Mth.clampedMap((double) $$6, 0.0, (double) dripstoneClusterConfiguration5.maxDistanceFromCenterAffectingHeightBias, (double) int4 / 2.0, 0.0);
            return (int) randomBetweenBiased(randomSource0, 0.0F, (float) int4, $$7, (float) dripstoneClusterConfiguration5.heightDeviation);
        }
    }

    private boolean canPlacePool(WorldGenLevel worldGenLevel0, BlockPos blockPos1) {
        BlockState $$2 = worldGenLevel0.m_8055_(blockPos1);
        if (!$$2.m_60713_(Blocks.WATER) && !$$2.m_60713_(Blocks.DRIPSTONE_BLOCK) && !$$2.m_60713_(Blocks.POINTED_DRIPSTONE)) {
            if (worldGenLevel0.m_8055_(blockPos1.above()).m_60819_().is(FluidTags.WATER)) {
                return false;
            } else {
                for (Direction $$3 : Direction.Plane.HORIZONTAL) {
                    if (!this.canBeAdjacentToWater(worldGenLevel0, blockPos1.relative($$3))) {
                        return false;
                    }
                }
                return this.canBeAdjacentToWater(worldGenLevel0, blockPos1.below());
            }
        } else {
            return false;
        }
    }

    private boolean canBeAdjacentToWater(LevelAccessor levelAccessor0, BlockPos blockPos1) {
        BlockState $$2 = levelAccessor0.m_8055_(blockPos1);
        return $$2.m_204336_(BlockTags.BASE_STONE_OVERWORLD) || $$2.m_60819_().is(FluidTags.WATER);
    }

    private void replaceBlocksWithDripstoneBlocks(WorldGenLevel worldGenLevel0, BlockPos blockPos1, int int2, Direction direction3) {
        BlockPos.MutableBlockPos $$4 = blockPos1.mutable();
        for (int $$5 = 0; $$5 < int2; $$5++) {
            if (!DripstoneUtils.placeDripstoneBlockIfPossible(worldGenLevel0, $$4)) {
                return;
            }
            $$4.move(direction3);
        }
    }

    private double getChanceOfStalagmiteOrStalactite(int int0, int int1, int int2, int int3, DripstoneClusterConfiguration dripstoneClusterConfiguration4) {
        int $$5 = int0 - Math.abs(int2);
        int $$6 = int1 - Math.abs(int3);
        int $$7 = Math.min($$5, $$6);
        return (double) Mth.clampedMap((float) $$7, 0.0F, (float) dripstoneClusterConfiguration4.maxDistanceFromEdgeAffectingChanceOfDripstoneColumn, dripstoneClusterConfiguration4.chanceOfDripstoneColumnAtMaxDistanceFromCenter, 1.0F);
    }

    private static float randomBetweenBiased(RandomSource randomSource0, float float1, float float2, float float3, float float4) {
        return ClampedNormalFloat.sample(randomSource0, float3, float4, float1, float2);
    }
}