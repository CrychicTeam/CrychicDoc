package com.github.alexmodguy.alexscaves.server.level.biome;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.config.BiomeGenerationConfig;
import com.github.alexmodguy.alexscaves.server.misc.VoronoiGenerator;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.phys.Vec3;

public class ACBiomeRarity {

    private static long lastTestedSeed = 0L;

    private static final List<Integer> BIOME_OCTAVES = ImmutableList.of(0);

    private static final PerlinSimplexNoise NOISE_X = new PerlinSimplexNoise(new XoroshiroRandomSource(1234L), BIOME_OCTAVES);

    private static final PerlinSimplexNoise NOISE_Z = new PerlinSimplexNoise(new XoroshiroRandomSource(4321L), BIOME_OCTAVES);

    private static final VoronoiGenerator VORONOI_GENERATOR = new VoronoiGenerator(42L);

    private static double biomeSize;

    private static double seperationDistance;

    public static void init() {
        VORONOI_GENERATOR.setOffsetAmount(AlexsCaves.COMMON_CONFIG.caveBiomeSpacingRandomness.get());
        biomeSize = AlexsCaves.COMMON_CONFIG.caveBiomeMeanWidth.get() * 0.25;
        seperationDistance = biomeSize + (double) AlexsCaves.COMMON_CONFIG.caveBiomeMeanSeparation.get().intValue() * 0.25;
    }

    @Nullable
    public static VoronoiGenerator.VoronoiInfo getRareBiomeInfoForQuad(long worldSeed, int x, int z) {
        VORONOI_GENERATOR.setSeed(worldSeed);
        double sampleX = (double) x / seperationDistance;
        double sampleZ = (double) z / seperationDistance;
        double positionOffsetX = AlexsCaves.COMMON_CONFIG.caveBiomeWidthRandomness.get() * NOISE_X.getValue(sampleX, sampleZ, false);
        double positionOffsetZ = AlexsCaves.COMMON_CONFIG.caveBiomeWidthRandomness.get() * NOISE_Z.getValue(sampleX, sampleZ, false);
        VoronoiGenerator.VoronoiInfo info = VORONOI_GENERATOR.get2(sampleX + positionOffsetX, sampleZ + positionOffsetZ);
        return info.distance() < biomeSize / seperationDistance ? info : null;
    }

    @Nullable
    public static Vec3 getRareBiomeCenter(VoronoiGenerator.VoronoiInfo voronoiInfo) {
        return voronoiInfo.cellPos().scale(seperationDistance);
    }

    @Nullable
    public static int getRareBiomeOffsetId(VoronoiGenerator.VoronoiInfo voronoiInfo) {
        return (int) ((voronoiInfo.hash() + 1.0) * 0.5 * (double) BiomeGenerationConfig.getBiomeCount());
    }

    public static boolean isQuartInRareBiome(long worldSeed, int x, int z) {
        return getRareBiomeInfoForQuad(worldSeed, x, z) != null;
    }
}