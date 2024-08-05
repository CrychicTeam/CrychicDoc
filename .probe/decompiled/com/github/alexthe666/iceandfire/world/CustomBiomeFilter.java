package com.github.alexthe666.iceandfire.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

public class CustomBiomeFilter extends PlacementFilter {

    private static final CustomBiomeFilter INSTANCE = new CustomBiomeFilter();

    public static Codec<CustomBiomeFilter> CODEC = Codec.unit(() -> INSTANCE);

    private CustomBiomeFilter() {
    }

    public static CustomBiomeFilter biome() {
        return INSTANCE;
    }

    @Override
    protected boolean shouldPlace(PlacementContext context, @NotNull RandomSource random, @NotNull BlockPos position) {
        PlacedFeature placedfeature = (PlacedFeature) context.topFeature().orElseThrow(() -> new IllegalStateException("Tried to biome check an unregistered feature, or a feature that should not restrict the biome"));
        boolean hasFeature = context.generator().getBiomeGenerationSettings(context.getLevel().m_204166_(position)).hasFeature(placedfeature);
        if (!hasFeature) {
            hasFeature = context.generator().getBiomeGenerationSettings(context.getLevel().m_204166_(context.getLevel().m_5452_(Heightmap.Types.WORLD_SURFACE_WG, position))).hasFeature(placedfeature);
        }
        return hasFeature;
    }

    @NotNull
    @Override
    public PlacementModifierType<?> type() {
        return IafPlacementFilterRegistry.CUSTOM_BIOME_FILTER.get();
    }
}