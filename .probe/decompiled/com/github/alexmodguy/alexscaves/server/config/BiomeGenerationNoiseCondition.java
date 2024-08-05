package com.github.alexmodguy.alexscaves.server.config;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRarity;
import com.github.alexmodguy.alexscaves.server.misc.VoronoiGenerator;
import com.github.alexthe666.citadel.server.event.EventReplaceBiome;
import java.util.List;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.phys.Vec3;

public class BiomeGenerationNoiseCondition {

    private boolean disabledCompletely;

    private int distanceFromSpawn;

    private final int alexscavesRarityOffset;

    private final float[] continentalness;

    private final float[] erosion;

    private final float[] humidity;

    private final float[] temperature;

    private final float[] weirdness;

    private final float[] depth;

    private final List<String> dimensions;

    private BiomeGenerationNoiseCondition(boolean disabledCompletely, int distanceFromSpawn, int alexscavesRarityOffset, float[] continentalness, float[] erosion, float[] humidity, float[] temperature, float[] weirdness, float[] depth, String[] dimensions) {
        this.disabledCompletely = disabledCompletely;
        this.distanceFromSpawn = distanceFromSpawn;
        this.continentalness = continentalness;
        this.erosion = erosion;
        this.humidity = humidity;
        this.temperature = temperature;
        this.weirdness = weirdness;
        this.depth = depth;
        this.alexscavesRarityOffset = alexscavesRarityOffset;
        this.dimensions = List.of(dimensions);
    }

    public boolean test(EventReplaceBiome event, VoronoiGenerator.VoronoiInfo info) {
        if (this.disabledCompletely) {
            return false;
        } else if (!isFarEnoughFromSpawn(event, (double) this.distanceFromSpawn)) {
            return false;
        } else {
            Vec3 rareBiomeCenter = ACBiomeRarity.getRareBiomeCenter(info);
            if (rareBiomeCenter == null) {
                return false;
            } else {
                Climate.TargetPoint centerTargetPoint = event.getClimateSampler().sample((int) Math.floor(rareBiomeCenter.x), event.getY(), (int) Math.floor(rareBiomeCenter.z));
                float f = Climate.unquantizeCoord(centerTargetPoint.continentalness());
                float f1 = Climate.unquantizeCoord(centerTargetPoint.erosion());
                float f2 = Climate.unquantizeCoord(centerTargetPoint.temperature());
                float f3 = Climate.unquantizeCoord(centerTargetPoint.humidity());
                float f4 = Climate.unquantizeCoord(centerTargetPoint.weirdness());
                if (this.continentalness == null || this.continentalness.length < 2 || !(f < this.continentalness[0]) && !(f > this.continentalness[1])) {
                    if (this.erosion == null || this.erosion.length < 2 || !(f1 < this.erosion[0]) && !(f1 > this.erosion[1])) {
                        if (this.humidity == null || this.humidity.length < 2 || !(f2 < this.humidity[0]) && !(f2 > this.humidity[1])) {
                            if (this.temperature == null || this.temperature.length < 2 || !(f3 < this.temperature[0]) && !(f3 > this.temperature[1])) {
                                if (this.weirdness == null || this.weirdness.length < 2 || !(f4 < this.weirdness[0]) && !(f4 > this.weirdness[1])) {
                                    return this.depth != null && this.depth.length >= 2 && !event.testDepth(this.depth[0], this.depth[1]) ? false : event.getWorldDimension() == null || this.dimensions.contains(event.getWorldDimension().location().toString());
                                } else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    private static boolean isFarEnoughFromSpawn(EventReplaceBiome event, double dist) {
        int x = QuartPos.fromSection(event.getX());
        int z = QuartPos.toBlock(event.getZ());
        return (double) (x * x + z * z) >= dist * dist;
    }

    public boolean isDisabledCompletely() {
        return this.disabledCompletely;
    }

    public boolean isInvalid() {
        return this.dimensions == null && !this.disabledCompletely;
    }

    public int getRarityOffset() {
        return this.alexscavesRarityOffset;
    }

    public static final class Builder {

        private boolean disabledCompletely;

        private int distanceFromSpawn;

        private float[] alexBiomeRarity;

        private float[] continentalness;

        private float[] erosion;

        private float[] humidity;

        private float[] temperature;

        private float[] weirdness;

        private float[] depth;

        private String[] dimensions;

        private int rarityOffset;

        public BiomeGenerationNoiseCondition.Builder disabledCompletely(boolean disabledCompletely) {
            this.disabledCompletely = disabledCompletely;
            return this;
        }

        public BiomeGenerationNoiseCondition.Builder alexscavesRarityOffset(int rarityOffset) {
            this.rarityOffset = rarityOffset;
            return this;
        }

        public BiomeGenerationNoiseCondition.Builder distanceFromSpawn(int distanceFromSpawn) {
            this.distanceFromSpawn = distanceFromSpawn;
            return this;
        }

        public BiomeGenerationNoiseCondition.Builder continentalness(float... continentalness) {
            this.continentalness = continentalness;
            return this;
        }

        public BiomeGenerationNoiseCondition.Builder erosion(float... erosion) {
            this.erosion = erosion;
            return this;
        }

        public BiomeGenerationNoiseCondition.Builder humidity(float... humidity) {
            this.humidity = humidity;
            return this;
        }

        public BiomeGenerationNoiseCondition.Builder temperature(float... temperature) {
            this.temperature = temperature;
            return this;
        }

        public BiomeGenerationNoiseCondition.Builder weirdness(float... weirdness) {
            this.weirdness = weirdness;
            return this;
        }

        public BiomeGenerationNoiseCondition.Builder depth(float... depth) {
            this.depth = depth;
            return this;
        }

        public BiomeGenerationNoiseCondition.Builder dimensions(String... dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public BiomeGenerationNoiseCondition build() {
            return new BiomeGenerationNoiseCondition(this.disabledCompletely, this.distanceFromSpawn, this.rarityOffset, this.continentalness, this.erosion, this.humidity, this.temperature, this.weirdness, this.depth, this.dimensions);
        }
    }
}