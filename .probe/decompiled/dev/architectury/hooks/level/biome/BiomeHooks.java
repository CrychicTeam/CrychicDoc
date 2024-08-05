package dev.architectury.hooks.level.biome;

import dev.architectury.hooks.level.biome.forge.BiomeHooksImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Nullable;

public final class BiomeHooks {

    public static BiomeProperties getBiomeProperties(Biome biome) {
        return new BiomeHooks.BiomeWrapped(biome);
    }

    @ExpectPlatform
    @Transformed
    private static Biome.ClimateSettings extractClimateSettings(Biome biome) {
        return BiomeHooksImpl.extractClimateSettings(biome);
    }

    public static class BiomeWrapped implements BiomeProperties {

        protected final Biome biome;

        protected final ClimateProperties climateProperties;

        protected final EffectsProperties effectsProperties;

        protected final GenerationProperties generationProperties;

        protected final SpawnProperties spawnProperties;

        public BiomeWrapped(Biome biome) {
            this(biome, new BiomeHooks.ClimateWrapped(biome), new BiomeHooks.EffectsWrapped(biome), new BiomeHooks.GenerationSettingsWrapped(biome), new BiomeHooks.SpawnSettingsWrapped(biome));
        }

        public BiomeWrapped(Biome biome, ClimateProperties climateProperties, EffectsProperties effectsProperties, GenerationProperties generationProperties, SpawnProperties spawnProperties) {
            this.biome = biome;
            this.climateProperties = climateProperties;
            this.effectsProperties = effectsProperties;
            this.generationProperties = generationProperties;
            this.spawnProperties = spawnProperties;
        }

        @Override
        public ClimateProperties getClimateProperties() {
            return this.climateProperties;
        }

        @Override
        public EffectsProperties getEffectsProperties() {
            return this.effectsProperties;
        }

        @Override
        public GenerationProperties getGenerationProperties() {
            return this.generationProperties;
        }

        @Override
        public SpawnProperties getSpawnProperties() {
            return this.spawnProperties;
        }
    }

    public static class ClimateWrapped implements ClimateProperties.Mutable {

        protected final Biome.ClimateSettings climateSettings;

        public ClimateWrapped(Biome biome) {
            this(BiomeHooks.extractClimateSettings(biome));
        }

        public ClimateWrapped(Biome.ClimateSettings climateSettings) {
            this.climateSettings = climateSettings;
        }

        @Override
        public ClimateProperties.Mutable setHasPrecipitation(boolean hasPrecipitation) {
            this.climateSettings.hasPrecipitation = hasPrecipitation;
            return this;
        }

        @Override
        public ClimateProperties.Mutable setTemperature(float temperature) {
            this.climateSettings.temperature = temperature;
            return this;
        }

        @Override
        public ClimateProperties.Mutable setTemperatureModifier(Biome.TemperatureModifier temperatureModifier) {
            this.climateSettings.temperatureModifier = temperatureModifier;
            return this;
        }

        @Override
        public ClimateProperties.Mutable setDownfall(float downfall) {
            this.climateSettings.downfall = downfall;
            return this;
        }

        @Override
        public boolean hasPrecipitation() {
            return this.climateSettings.hasPrecipitation;
        }

        @Override
        public float getTemperature() {
            return this.climateSettings.temperature;
        }

        @Override
        public Biome.TemperatureModifier getTemperatureModifier() {
            return this.climateSettings.temperatureModifier;
        }

        @Override
        public float getDownfall() {
            return this.climateSettings.downfall;
        }
    }

    public static class EffectsWrapped implements EffectsProperties.Mutable {

        protected final BiomeSpecialEffects effects;

        public EffectsWrapped(Biome biome) {
            this(biome.getSpecialEffects());
        }

        public EffectsWrapped(BiomeSpecialEffects effects) {
            this.effects = effects;
        }

        @Override
        public EffectsProperties.Mutable setFogColor(int color) {
            this.effects.fogColor = color;
            return this;
        }

        @Override
        public EffectsProperties.Mutable setWaterColor(int color) {
            this.effects.waterColor = color;
            return this;
        }

        @Override
        public EffectsProperties.Mutable setWaterFogColor(int color) {
            this.effects.waterFogColor = color;
            return this;
        }

        @Override
        public EffectsProperties.Mutable setSkyColor(int color) {
            this.effects.skyColor = color;
            return this;
        }

        @Override
        public EffectsProperties.Mutable setFoliageColorOverride(@Nullable Integer colorOverride) {
            this.effects.foliageColorOverride = Optional.ofNullable(colorOverride);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setGrassColorOverride(@Nullable Integer colorOverride) {
            this.effects.grassColorOverride = Optional.ofNullable(colorOverride);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setGrassColorModifier(BiomeSpecialEffects.GrassColorModifier modifier) {
            this.effects.grassColorModifier = modifier;
            return this;
        }

        @Override
        public EffectsProperties.Mutable setAmbientParticle(@Nullable AmbientParticleSettings settings) {
            this.effects.ambientParticleSettings = Optional.ofNullable(settings);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setAmbientLoopSound(@Nullable Holder<SoundEvent> sound) {
            this.effects.ambientLoopSoundEvent = Optional.ofNullable(sound);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setAmbientMoodSound(@Nullable AmbientMoodSettings settings) {
            this.effects.ambientMoodSettings = Optional.ofNullable(settings);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setAmbientAdditionsSound(@Nullable AmbientAdditionsSettings settings) {
            this.effects.ambientAdditionsSettings = Optional.ofNullable(settings);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setBackgroundMusic(@Nullable Music music) {
            this.effects.backgroundMusic = Optional.ofNullable(music);
            return this;
        }

        @Override
        public int getFogColor() {
            return this.effects.fogColor;
        }

        @Override
        public int getWaterColor() {
            return this.effects.waterColor;
        }

        @Override
        public int getWaterFogColor() {
            return this.effects.waterFogColor;
        }

        @Override
        public int getSkyColor() {
            return this.effects.skyColor;
        }

        @Override
        public OptionalInt getFoliageColorOverride() {
            return (OptionalInt) this.effects.foliageColorOverride.map(OptionalInt::of).orElseGet(OptionalInt::empty);
        }

        @Override
        public OptionalInt getGrassColorOverride() {
            return (OptionalInt) this.effects.grassColorOverride.map(OptionalInt::of).orElseGet(OptionalInt::empty);
        }

        @Override
        public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier() {
            return this.effects.grassColorModifier;
        }

        @Override
        public Optional<AmbientParticleSettings> getAmbientParticle() {
            return this.effects.ambientParticleSettings;
        }

        @Override
        public Optional<Holder<SoundEvent>> getAmbientLoopSound() {
            return this.effects.ambientLoopSoundEvent;
        }

        @Override
        public Optional<AmbientMoodSettings> getAmbientMoodSound() {
            return this.effects.ambientMoodSettings;
        }

        @Override
        public Optional<AmbientAdditionsSettings> getAmbientAdditionsSound() {
            return this.effects.ambientAdditionsSettings;
        }

        @Override
        public Optional<Music> getBackgroundMusic() {
            return this.effects.backgroundMusic;
        }
    }

    public static class GenerationSettingsWrapped implements GenerationProperties {

        protected final BiomeGenerationSettings settings;

        public GenerationSettingsWrapped(Biome biome) {
            this(biome.getGenerationSettings());
        }

        public GenerationSettingsWrapped(BiomeGenerationSettings settings) {
            this.settings = settings;
        }

        @Override
        public Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving carving) {
            return this.settings.getCarvers(carving);
        }

        @Override
        public Iterable<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration decoration) {
            return (Iterable<Holder<PlacedFeature>>) (decoration.ordinal() >= this.settings.features().size() ? Collections.emptyList() : (Iterable) this.settings.features().get(decoration.ordinal()));
        }

        @Override
        public List<Iterable<Holder<PlacedFeature>>> getFeatures() {
            return this.settings.features();
        }
    }

    public static class MutableBiomeWrapped extends BiomeHooks.BiomeWrapped implements BiomeProperties.Mutable {

        public MutableBiomeWrapped(Biome biome, GenerationProperties.Mutable generationProperties, SpawnProperties.Mutable spawnProperties) {
            this(biome, new BiomeHooks.ClimateWrapped(BiomeHooks.extractClimateSettings(biome)), new BiomeHooks.EffectsWrapped(biome.getSpecialEffects()), generationProperties, spawnProperties);
        }

        public MutableBiomeWrapped(Biome biome, ClimateProperties.Mutable climateProperties, EffectsProperties.Mutable effectsProperties, GenerationProperties.Mutable generationProperties, SpawnProperties.Mutable spawnProperties) {
            super(biome, climateProperties, effectsProperties, generationProperties, spawnProperties);
        }

        @Override
        public ClimateProperties.Mutable getClimateProperties() {
            return (ClimateProperties.Mutable) super.getClimateProperties();
        }

        @Override
        public EffectsProperties.Mutable getEffectsProperties() {
            return (EffectsProperties.Mutable) super.getEffectsProperties();
        }

        @Override
        public GenerationProperties.Mutable getGenerationProperties() {
            return (GenerationProperties.Mutable) super.getGenerationProperties();
        }

        @Override
        public SpawnProperties.Mutable getSpawnProperties() {
            return (SpawnProperties.Mutable) super.getSpawnProperties();
        }
    }

    public static class SpawnSettingsWrapped implements SpawnProperties {

        protected final MobSpawnSettings settings;

        public SpawnSettingsWrapped(Biome biome) {
            this(biome.getMobSettings());
        }

        public SpawnSettingsWrapped(MobSpawnSettings settings) {
            this.settings = settings;
        }

        @Override
        public float getCreatureProbability() {
            return this.settings.getCreatureProbability();
        }

        @Override
        public Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners() {
            return null;
        }

        @Override
        public Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts() {
            return null;
        }
    }
}