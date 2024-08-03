package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

public class InSquarePlacement extends PlacementModifier {

    private static final InSquarePlacement INSTANCE = new InSquarePlacement();

    public static final Codec<InSquarePlacement> CODEC = Codec.unit(() -> INSTANCE);

    public static InSquarePlacement spread() {
        return INSTANCE;
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext0, RandomSource randomSource1, BlockPos blockPos2) {
        int $$3 = randomSource1.nextInt(16) + blockPos2.m_123341_();
        int $$4 = randomSource1.nextInt(16) + blockPos2.m_123343_();
        return Stream.of(new BlockPos($$3, blockPos2.m_123342_(), $$4));
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.IN_SQUARE;
    }
}