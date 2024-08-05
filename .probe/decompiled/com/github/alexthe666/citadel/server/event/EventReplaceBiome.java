package com.github.alexthe666.citadel.server.event;

import com.github.alexthe666.citadel.server.world.ExpandedBiomeSource;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;

@HasResult
public class EventReplaceBiome extends Event {

    public Holder<Biome> biomeToGenerate;

    public ExpandedBiomeSource biomeSource;

    public float continentalness;

    public float erosion;

    public float temperature;

    public float humidity;

    public float weirdness;

    public float depth;

    private int x;

    private int y;

    private int z;

    private long worldSeed;

    private ResourceKey<Level> worldDimension;

    private Climate.Sampler climateSampler;

    public EventReplaceBiome(ExpandedBiomeSource biomeSource, Holder<Biome> biomeIn, int x, int y, int z, float continentalness, float erosion, float temperature, float humidity, float weirdness, float depth, long worldSeed, ResourceKey<Level> worldDimension, Climate.Sampler climateSampler) {
        this.biomeSource = biomeSource;
        this.biomeToGenerate = biomeIn;
        this.continentalness = continentalness;
        this.erosion = erosion;
        this.temperature = temperature;
        this.humidity = humidity;
        this.weirdness = weirdness;
        this.depth = depth;
        this.worldSeed = worldSeed;
        this.worldDimension = worldDimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.climateSampler = climateSampler;
    }

    public Holder<Biome> getBiomeToGenerate() {
        return this.biomeToGenerate;
    }

    public float getContinentalness() {
        return this.continentalness;
    }

    public float getErosion() {
        return this.erosion;
    }

    public float getTemperature() {
        return this.temperature;
    }

    public float getHumidity() {
        return this.humidity;
    }

    public float getWeirdness() {
        return this.weirdness;
    }

    public float getDepth() {
        return this.depth;
    }

    public boolean testContinentalness(float min, float max) {
        return this.continentalness >= min && this.continentalness <= max;
    }

    public boolean testErosion(float min, float max) {
        return this.erosion >= min && this.erosion <= max;
    }

    public boolean testTemperature(float min, float max) {
        return this.temperature >= min && this.temperature <= max;
    }

    public boolean testHumidity(float min, float max) {
        return this.humidity >= min && this.humidity <= max;
    }

    public boolean testWeirdness(float min, float max) {
        return this.weirdness >= min && this.weirdness <= max;
    }

    public boolean testDepth(float min, float max) {
        return this.depth >= min && this.depth <= max;
    }

    public ExpandedBiomeSource getBiomeSource() {
        return this.biomeSource;
    }

    public void setBiomeToGenerate(Holder<Biome> biome) {
        this.biomeToGenerate = biome;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public long getWorldSeed() {
        return this.worldSeed;
    }

    public ResourceKey<Level> getWorldDimension() {
        return this.worldDimension;
    }

    public Climate.Sampler getClimateSampler() {
        return this.climateSampler;
    }
}