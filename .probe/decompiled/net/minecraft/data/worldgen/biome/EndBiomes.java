package net.minecraft.data.worldgen.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class EndBiomes {

    private static Biome baseEndBiome(BiomeGenerationSettings.Builder biomeGenerationSettingsBuilder0) {
        MobSpawnSettings.Builder $$1 = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.endSpawns($$1);
        return new Biome.BiomeBuilder().hasPrecipitation(false).temperature(0.5F).downfall(0.5F).specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(10518688).skyColor(0).ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS).build()).mobSpawnSettings($$1.build()).generationSettings(biomeGenerationSettingsBuilder0.m_255380_()).build();
    }

    public static Biome endBarrens(HolderGetter<PlacedFeature> holderGetterPlacedFeature0, HolderGetter<ConfiguredWorldCarver<?>> holderGetterConfiguredWorldCarver1) {
        BiomeGenerationSettings.Builder $$2 = new BiomeGenerationSettings.Builder(holderGetterPlacedFeature0, holderGetterConfiguredWorldCarver1);
        return baseEndBiome($$2);
    }

    public static Biome theEnd(HolderGetter<PlacedFeature> holderGetterPlacedFeature0, HolderGetter<ConfiguredWorldCarver<?>> holderGetterConfiguredWorldCarver1) {
        BiomeGenerationSettings.Builder $$2 = new BiomeGenerationSettings.Builder(holderGetterPlacedFeature0, holderGetterConfiguredWorldCarver1).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, EndPlacements.END_SPIKE);
        return baseEndBiome($$2);
    }

    public static Biome endMidlands(HolderGetter<PlacedFeature> holderGetterPlacedFeature0, HolderGetter<ConfiguredWorldCarver<?>> holderGetterConfiguredWorldCarver1) {
        BiomeGenerationSettings.Builder $$2 = new BiomeGenerationSettings.Builder(holderGetterPlacedFeature0, holderGetterConfiguredWorldCarver1);
        return baseEndBiome($$2);
    }

    public static Biome endHighlands(HolderGetter<PlacedFeature> holderGetterPlacedFeature0, HolderGetter<ConfiguredWorldCarver<?>> holderGetterConfiguredWorldCarver1) {
        BiomeGenerationSettings.Builder $$2 = new BiomeGenerationSettings.Builder(holderGetterPlacedFeature0, holderGetterConfiguredWorldCarver1).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EndPlacements.CHORUS_PLANT);
        return baseEndBiome($$2);
    }

    public static Biome smallEndIslands(HolderGetter<PlacedFeature> holderGetterPlacedFeature0, HolderGetter<ConfiguredWorldCarver<?>> holderGetterConfiguredWorldCarver1) {
        BiomeGenerationSettings.Builder $$2 = new BiomeGenerationSettings.Builder(holderGetterPlacedFeature0, holderGetterConfiguredWorldCarver1).addFeature(GenerationStep.Decoration.RAW_GENERATION, EndPlacements.END_ISLAND_DECORATED);
        return baseEndBiome($$2);
    }
}