package com.craisinlord.integrated_api.world.placements;

import com.craisinlord.integrated_api.modinit.IAPlacements;
import com.mojang.serialization.Codec;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class SnapToLowerNonAirPlacement extends PlacementModifier {

    private static final SnapToLowerNonAirPlacement INSTANCE = new SnapToLowerNonAirPlacement();

    public static final Codec<SnapToLowerNonAirPlacement> CODEC = Codec.unit(() -> INSTANCE);

    public static SnapToLowerNonAirPlacement snapToLowerNonAir() {
        return INSTANCE;
    }

    @Override
    public final Stream<BlockPos> getPositions(PlacementContext placementContext, RandomSource random, BlockPos blockPos) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(blockPos);
        while (placementContext.getBlockState(mutable).m_60795_() && mutable.m_123342_() > placementContext.m_142201_()) {
            mutable.move(Direction.DOWN);
        }
        return Stream.of(mutable.immutable());
    }

    @Override
    public PlacementModifierType<?> type() {
        return IAPlacements.SNAP_TO_LOWER_NON_AIR_PLACEMENT.get();
    }
}