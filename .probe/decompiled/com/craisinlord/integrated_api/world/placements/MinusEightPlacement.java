package com.craisinlord.integrated_api.world.placements;

import com.craisinlord.integrated_api.modinit.IAPlacements;
import com.mojang.serialization.Codec;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class MinusEightPlacement extends PlacementModifier {

    private static final MinusEightPlacement INSTANCE = new MinusEightPlacement();

    public static final Codec<MinusEightPlacement> CODEC = Codec.unit(() -> INSTANCE);

    public static MinusEightPlacement subtractedEight() {
        return INSTANCE;
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext, RandomSource random, BlockPos blockPos) {
        return Stream.of(new BlockPos(blockPos.m_123341_() - 8, blockPos.m_123342_(), blockPos.m_123343_() - 8));
    }

    @Override
    public PlacementModifierType<?> type() {
        return IAPlacements.MINUS_EIGHT_PLACEMENT.get();
    }
}