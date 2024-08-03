package dev.architectury.hooks.level.biome.forge;

import net.minecraft.world.level.biome.Biome;

public class BiomeHooksImpl {

    public static Biome.ClimateSettings extractClimateSettings(Biome biome) {
        return biome.getModifiedClimateSettings();
    }
}