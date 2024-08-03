package net.minecraft.world.level.levelgen.placement;

import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

public abstract class PlacementFilter extends PlacementModifier {

    @Override
    public final Stream<BlockPos> getPositions(PlacementContext placementContext0, RandomSource randomSource1, BlockPos blockPos2) {
        return this.shouldPlace(placementContext0, randomSource1, blockPos2) ? Stream.of(blockPos2) : Stream.of();
    }

    protected abstract boolean shouldPlace(PlacementContext var1, RandomSource var2, BlockPos var3);
}