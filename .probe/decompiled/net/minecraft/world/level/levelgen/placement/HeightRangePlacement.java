package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.heightproviders.TrapezoidHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;

public class HeightRangePlacement extends PlacementModifier {

    public static final Codec<HeightRangePlacement> CODEC = RecordCodecBuilder.create(p_191679_ -> p_191679_.group(HeightProvider.CODEC.fieldOf("height").forGetter(p_191686_ -> p_191686_.height)).apply(p_191679_, HeightRangePlacement::new));

    private final HeightProvider height;

    private HeightRangePlacement(HeightProvider heightProvider0) {
        this.height = heightProvider0;
    }

    public static HeightRangePlacement of(HeightProvider heightProvider0) {
        return new HeightRangePlacement(heightProvider0);
    }

    public static HeightRangePlacement uniform(VerticalAnchor verticalAnchor0, VerticalAnchor verticalAnchor1) {
        return of(UniformHeight.of(verticalAnchor0, verticalAnchor1));
    }

    public static HeightRangePlacement triangle(VerticalAnchor verticalAnchor0, VerticalAnchor verticalAnchor1) {
        return of(TrapezoidHeight.of(verticalAnchor0, verticalAnchor1));
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext0, RandomSource randomSource1, BlockPos blockPos2) {
        return Stream.of(blockPos2.atY(this.height.sample(randomSource1, placementContext0)));
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.HEIGHT_RANGE;
    }
}