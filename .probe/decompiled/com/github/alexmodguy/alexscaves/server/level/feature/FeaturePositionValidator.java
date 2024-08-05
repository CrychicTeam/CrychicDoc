package com.github.alexmodguy.alexscaves.server.level.feature;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class FeaturePositionValidator {

    public static boolean isBiome(FeaturePlaceContext context, ResourceKey<Biome> biomeResourceKey) {
        int j = context.level().m_6924_(Heightmap.Types.OCEAN_FLOOR, context.origin().m_123341_(), context.origin().m_123343_());
        return context.level().m_204166_(context.origin().atY(Math.min(context.level().m_141937_(), j - 30))).is(biomeResourceKey);
    }
}