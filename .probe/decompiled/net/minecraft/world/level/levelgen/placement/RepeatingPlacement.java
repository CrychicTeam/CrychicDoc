package net.minecraft.world.level.levelgen.placement;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

public abstract class RepeatingPlacement extends PlacementModifier {

    protected abstract int count(RandomSource var1, BlockPos var2);

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext0, RandomSource randomSource1, BlockPos blockPos2) {
        return IntStream.range(0, this.count(randomSource1, blockPos2)).mapToObj(p_191912_ -> blockPos2);
    }
}