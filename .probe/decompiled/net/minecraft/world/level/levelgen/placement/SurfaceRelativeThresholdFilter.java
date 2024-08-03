package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;

public class SurfaceRelativeThresholdFilter extends PlacementFilter {

    public static final Codec<SurfaceRelativeThresholdFilter> CODEC = RecordCodecBuilder.create(p_191929_ -> p_191929_.group(Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(p_191944_ -> p_191944_.heightmap), Codec.INT.optionalFieldOf("min_inclusive", Integer.MIN_VALUE).forGetter(p_191942_ -> p_191942_.minInclusive), Codec.INT.optionalFieldOf("max_inclusive", Integer.MAX_VALUE).forGetter(p_191939_ -> p_191939_.maxInclusive)).apply(p_191929_, SurfaceRelativeThresholdFilter::new));

    private final Heightmap.Types heightmap;

    private final int minInclusive;

    private final int maxInclusive;

    private SurfaceRelativeThresholdFilter(Heightmap.Types heightmapTypes0, int int1, int int2) {
        this.heightmap = heightmapTypes0;
        this.minInclusive = int1;
        this.maxInclusive = int2;
    }

    public static SurfaceRelativeThresholdFilter of(Heightmap.Types heightmapTypes0, int int1, int int2) {
        return new SurfaceRelativeThresholdFilter(heightmapTypes0, int1, int2);
    }

    @Override
    protected boolean shouldPlace(PlacementContext placementContext0, RandomSource randomSource1, BlockPos blockPos2) {
        long $$3 = (long) placementContext0.getHeight(this.heightmap, blockPos2.m_123341_(), blockPos2.m_123343_());
        long $$4 = $$3 + (long) this.minInclusive;
        long $$5 = $$3 + (long) this.maxInclusive;
        return $$4 <= (long) blockPos2.m_123342_() && (long) blockPos2.m_123342_() <= $$5;
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.SURFACE_RELATIVE_THRESHOLD_FILTER;
    }
}