package net.minecraft.data.worldgen;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class NoiseData {

    @Deprecated
    public static final NormalNoise.NoiseParameters DEFAULT_SHIFT = new NormalNoise.NoiseParameters(-3, 1.0, 1.0, 1.0, 0.0);

    public static void bootstrap(BootstapContext<NormalNoise.NoiseParameters> bootstapContextNormalNoiseNoiseParameters0) {
        registerBiomeNoises(bootstapContextNormalNoiseNoiseParameters0, 0, Noises.TEMPERATURE, Noises.VEGETATION, Noises.CONTINENTALNESS, Noises.EROSION);
        registerBiomeNoises(bootstapContextNormalNoiseNoiseParameters0, -2, Noises.TEMPERATURE_LARGE, Noises.VEGETATION_LARGE, Noises.CONTINENTALNESS_LARGE, Noises.EROSION_LARGE);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.RIDGE, -7, 1.0, 2.0, 1.0, 0.0, 0.0, 0.0);
        bootstapContextNormalNoiseNoiseParameters0.register(Noises.SHIFT, DEFAULT_SHIFT);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.AQUIFER_BARRIER, -3, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS, -7, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.AQUIFER_LAVA, -1, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.AQUIFER_FLUID_LEVEL_SPREAD, -5, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.PILLAR, -7, 1.0, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.PILLAR_RARENESS, -8, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.PILLAR_THICKNESS, -8, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.SPAGHETTI_2D, -7, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.SPAGHETTI_2D_ELEVATION, -8, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.SPAGHETTI_2D_MODULATOR, -11, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.SPAGHETTI_2D_THICKNESS, -11, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.SPAGHETTI_3D_1, -7, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.SPAGHETTI_3D_2, -7, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.SPAGHETTI_3D_RARITY, -11, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.SPAGHETTI_3D_THICKNESS, -8, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.SPAGHETTI_ROUGHNESS, -5, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.SPAGHETTI_ROUGHNESS_MODULATOR, -8, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.CAVE_ENTRANCE, -7, 0.4, 0.5, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.CAVE_LAYER, -8, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.CAVE_CHEESE, -8, 0.5, 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 2.0, 0.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.ORE_VEININESS, -8, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.ORE_VEIN_A, -7, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.ORE_VEIN_B, -7, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.ORE_GAP, -5, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.NOODLE, -8, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.NOODLE_THICKNESS, -8, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.NOODLE_RIDGE_A, -7, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.NOODLE_RIDGE_B, -7, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.JAGGED, -16, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.SURFACE, -6, 1.0, 1.0, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.SURFACE_SECONDARY, -6, 1.0, 1.0, 0.0, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.CLAY_BANDS_OFFSET, -8, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.BADLANDS_PILLAR, -2, 1.0, 1.0, 1.0, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.BADLANDS_PILLAR_ROOF, -8, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.BADLANDS_SURFACE, -6, 1.0, 1.0, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.ICEBERG_PILLAR, -6, 1.0, 1.0, 1.0, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.ICEBERG_PILLAR_ROOF, -3, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.ICEBERG_SURFACE, -6, 1.0, 1.0, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.SWAMP, -2, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.CALCITE, -9, 1.0, 1.0, 1.0, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.GRAVEL, -8, 1.0, 1.0, 1.0, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.POWDER_SNOW, -6, 1.0, 1.0, 1.0, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.PACKED_ICE, -7, 1.0, 1.0, 1.0, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.ICE, -4, 1.0, 1.0, 1.0, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.SOUL_SAND_LAYER, -8, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.013333333333333334);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.GRAVEL_LAYER, -8, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.013333333333333334);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.PATCH, -5, 1.0, 0.0, 0.0, 0.0, 0.0, 0.013333333333333334);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.NETHERRACK, -3, 1.0, 0.0, 0.0, 0.35);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.NETHER_WART, -3, 1.0, 0.0, 0.0, 0.9);
        register(bootstapContextNormalNoiseNoiseParameters0, Noises.NETHER_STATE_SELECTOR, -4, 1.0);
    }

    private static void registerBiomeNoises(BootstapContext<NormalNoise.NoiseParameters> bootstapContextNormalNoiseNoiseParameters0, int int1, ResourceKey<NormalNoise.NoiseParameters> resourceKeyNormalNoiseNoiseParameters2, ResourceKey<NormalNoise.NoiseParameters> resourceKeyNormalNoiseNoiseParameters3, ResourceKey<NormalNoise.NoiseParameters> resourceKeyNormalNoiseNoiseParameters4, ResourceKey<NormalNoise.NoiseParameters> resourceKeyNormalNoiseNoiseParameters5) {
        register(bootstapContextNormalNoiseNoiseParameters0, resourceKeyNormalNoiseNoiseParameters2, -10 + int1, 1.5, 0.0, 1.0, 0.0, 0.0, 0.0);
        register(bootstapContextNormalNoiseNoiseParameters0, resourceKeyNormalNoiseNoiseParameters3, -8 + int1, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0);
        register(bootstapContextNormalNoiseNoiseParameters0, resourceKeyNormalNoiseNoiseParameters4, -9 + int1, 1.0, 1.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0, 1.0);
        register(bootstapContextNormalNoiseNoiseParameters0, resourceKeyNormalNoiseNoiseParameters5, -9 + int1, 1.0, 1.0, 0.0, 1.0, 1.0);
    }

    private static void register(BootstapContext<NormalNoise.NoiseParameters> bootstapContextNormalNoiseNoiseParameters0, ResourceKey<NormalNoise.NoiseParameters> resourceKeyNormalNoiseNoiseParameters1, int int2, double double3, double... double4) {
        bootstapContextNormalNoiseNoiseParameters0.register(resourceKeyNormalNoiseNoiseParameters1, new NormalNoise.NoiseParameters(int2, double3, double4));
    }
}