package net.mehvahdjukaar.moonlight.core.misc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.MoonlightRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class CaveFilter extends PlacementFilter {

    public static final Codec<CaveFilter> CODEC = RecordCodecBuilder.create(instance -> instance.group(Heightmap.Types.CODEC.listOf().fieldOf("heightmaps").forGetter(p -> p.belowHeightMaps), Codec.BOOL.fieldOf("below_sea_level").forGetter(p -> p.belowSeaLevel)).apply(instance, CaveFilter::new));

    private final List<Heightmap.Types> belowHeightMaps;

    private final Boolean belowSeaLevel;

    private CaveFilter(List<Heightmap.Types> types, Boolean belowSeaLevel) {
        this.belowHeightMaps = types;
        this.belowSeaLevel = belowSeaLevel;
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        if (context.getLevel().m_7726_() instanceof ServerChunkCache serverChunkCache) {
            int y = pos.m_123342_();
            if (this.belowSeaLevel) {
                int sea = serverChunkCache.getGenerator().getSeaLevel();
                if (y > sea) {
                    return false;
                }
            }
            for (Heightmap.Types h : this.belowHeightMaps) {
                int k = context.getHeight(h, pos.m_123341_(), pos.m_123343_());
                if (y > k) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public PlacementModifierType<?> type() {
        return (PlacementModifierType<?>) MoonlightRegistry.CAVE_MODIFIER.get();
    }

    public static class Type implements PlacementModifierType<CaveFilter> {

        @Override
        public Codec<CaveFilter> codec() {
            return CaveFilter.CODEC;
        }
    }
}