package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;

public class BiomeFilter extends PlacementFilter {

    private static final BiomeFilter INSTANCE = new BiomeFilter();

    public static Codec<BiomeFilter> CODEC = Codec.unit(() -> INSTANCE);

    private BiomeFilter() {
    }

    public static BiomeFilter biome() {
        return INSTANCE;
    }

    @Override
    protected boolean shouldPlace(PlacementContext placementContext0, RandomSource randomSource1, BlockPos blockPos2) {
        PlacedFeature $$3 = (PlacedFeature) placementContext0.topFeature().orElseThrow(() -> new IllegalStateException("Tried to biome check an unregistered feature, or a feature that should not restrict the biome"));
        Holder<Biome> $$4 = placementContext0.getLevel().m_204166_(blockPos2);
        return placementContext0.generator().getBiomeGenerationSettings($$4).hasFeature($$3);
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.BIOME_FILTER;
    }
}