package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.configurations.UnderwaterMagmaConfiguration;
import net.minecraft.world.phys.AABB;

public class UnderwaterMagmaFeature extends Feature<UnderwaterMagmaConfiguration> {

    public UnderwaterMagmaFeature(Codec<UnderwaterMagmaConfiguration> codecUnderwaterMagmaConfiguration0) {
        super(codecUnderwaterMagmaConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<UnderwaterMagmaConfiguration> featurePlaceContextUnderwaterMagmaConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextUnderwaterMagmaConfiguration0.level();
        BlockPos $$2 = featurePlaceContextUnderwaterMagmaConfiguration0.origin();
        UnderwaterMagmaConfiguration $$3 = featurePlaceContextUnderwaterMagmaConfiguration0.config();
        RandomSource $$4 = featurePlaceContextUnderwaterMagmaConfiguration0.random();
        OptionalInt $$5 = getFloorY($$1, $$2, $$3);
        if (!$$5.isPresent()) {
            return false;
        } else {
            BlockPos $$6 = $$2.atY($$5.getAsInt());
            Vec3i $$7 = new Vec3i($$3.placementRadiusAroundFloor, $$3.placementRadiusAroundFloor, $$3.placementRadiusAroundFloor);
            AABB $$8 = new AABB($$6.subtract($$7), $$6.offset($$7));
            return BlockPos.betweenClosedStream($$8).filter(p_225310_ -> $$4.nextFloat() < $$3.placementProbabilityPerValidPosition).filter(p_160584_ -> this.isValidPlacement($$1, p_160584_)).mapToInt(p_160579_ -> {
                $$1.m_7731_(p_160579_, Blocks.MAGMA_BLOCK.defaultBlockState(), 2);
                return 1;
            }).sum() > 0;
        }
    }

    private static OptionalInt getFloorY(WorldGenLevel worldGenLevel0, BlockPos blockPos1, UnderwaterMagmaConfiguration underwaterMagmaConfiguration2) {
        Predicate<BlockState> $$3 = p_160586_ -> p_160586_.m_60713_(Blocks.WATER);
        Predicate<BlockState> $$4 = p_160581_ -> !p_160581_.m_60713_(Blocks.WATER);
        Optional<Column> $$5 = Column.scan(worldGenLevel0, blockPos1, underwaterMagmaConfiguration2.floorSearchRange, $$3, $$4);
        return (OptionalInt) $$5.map(Column::m_142009_).orElseGet(OptionalInt::empty);
    }

    private boolean isValidPlacement(WorldGenLevel worldGenLevel0, BlockPos blockPos1) {
        if (!this.isWaterOrAir(worldGenLevel0, blockPos1) && !this.isWaterOrAir(worldGenLevel0, blockPos1.below())) {
            for (Direction $$2 : Direction.Plane.HORIZONTAL) {
                if (this.isWaterOrAir(worldGenLevel0, blockPos1.relative($$2))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean isWaterOrAir(LevelAccessor levelAccessor0, BlockPos blockPos1) {
        BlockState $$2 = levelAccessor0.m_8055_(blockPos1);
        return $$2.m_60713_(Blocks.WATER) || $$2.m_60795_();
    }
}