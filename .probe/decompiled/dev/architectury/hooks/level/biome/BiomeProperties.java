package dev.architectury.hooks.level.biome;

public interface BiomeProperties {

    ClimateProperties getClimateProperties();

    EffectsProperties getEffectsProperties();

    GenerationProperties getGenerationProperties();

    SpawnProperties getSpawnProperties();

    public interface Mutable extends BiomeProperties {

        ClimateProperties.Mutable getClimateProperties();

        EffectsProperties.Mutable getEffectsProperties();

        GenerationProperties.Mutable getGenerationProperties();

        SpawnProperties.Mutable getSpawnProperties();
    }
}