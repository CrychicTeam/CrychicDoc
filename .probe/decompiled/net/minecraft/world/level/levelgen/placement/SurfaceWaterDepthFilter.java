package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;

public class SurfaceWaterDepthFilter extends PlacementFilter {

    public static final Codec<SurfaceWaterDepthFilter> CODEC = RecordCodecBuilder.create(p_191953_ -> p_191953_.group(Codec.INT.fieldOf("max_water_depth").forGetter(p_191959_ -> p_191959_.maxWaterDepth)).apply(p_191953_, SurfaceWaterDepthFilter::new));

    private final int maxWaterDepth;

    private SurfaceWaterDepthFilter(int int0) {
        this.maxWaterDepth = int0;
    }

    public static SurfaceWaterDepthFilter forMaxDepth(int int0) {
        return new SurfaceWaterDepthFilter(int0);
    }

    @Override
    protected boolean shouldPlace(PlacementContext placementContext0, RandomSource randomSource1, BlockPos blockPos2) {
        int $$3 = placementContext0.getHeight(Heightmap.Types.OCEAN_FLOOR, blockPos2.m_123341_(), blockPos2.m_123343_());
        int $$4 = placementContext0.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos2.m_123341_(), blockPos2.m_123343_());
        return $$4 - $$3 <= this.maxWaterDepth;
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.SURFACE_WATER_DEPTH_FILTER;
    }
}