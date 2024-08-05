package dev.architectury.registry.level.biome.forge;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import dev.architectury.hooks.level.biome.BiomeProperties;
import dev.architectury.hooks.level.biome.ClimateProperties;
import dev.architectury.hooks.level.biome.EffectsProperties;
import dev.architectury.hooks.level.biome.GenerationProperties;
import dev.architectury.hooks.level.biome.SpawnProperties;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.utils.GameInstance;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.BiomeSpecialEffectsBuilder;
import net.minecraftforge.common.world.ClimateSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

public class BiomeModificationsImpl {

    private static final List<Pair<Predicate<BiomeModifications.BiomeContext>, BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable>>> ADDITIONS = Lists.newArrayList();

    private static final List<Pair<Predicate<BiomeModifications.BiomeContext>, BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable>>> POST_PROCESSING = Lists.newArrayList();

    private static final List<Pair<Predicate<BiomeModifications.BiomeContext>, BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable>>> REMOVALS = Lists.newArrayList();

    private static final List<Pair<Predicate<BiomeModifications.BiomeContext>, BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable>>> REPLACEMENTS = Lists.newArrayList();

    @Nullable
    private static Codec<BiomeModificationsImpl.BiomeModifierImpl> noneBiomeModCodec = null;

    public static void init() {
        EventBuses.onRegistered("architectury", bus -> bus.addListener(event -> event.register(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, registry -> registry.register(new ResourceLocation("architectury", "none_biome_mod_codec"), noneBiomeModCodec = Codec.unit(BiomeModificationsImpl.BiomeModifierImpl.INSTANCE)))));
    }

    public static void addProperties(Predicate<BiomeModifications.BiomeContext> predicate, BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable> modifier) {
        ADDITIONS.add(Pair.of(predicate, modifier));
    }

    public static void postProcessProperties(Predicate<BiomeModifications.BiomeContext> predicate, BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable> modifier) {
        POST_PROCESSING.add(Pair.of(predicate, modifier));
    }

    public static void removeProperties(Predicate<BiomeModifications.BiomeContext> predicate, BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable> modifier) {
        REMOVALS.add(Pair.of(predicate, modifier));
    }

    public static void replaceProperties(Predicate<BiomeModifications.BiomeContext> predicate, BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable> modifier) {
        REPLACEMENTS.add(Pair.of(predicate, modifier));
    }

    private static BiomeModifications.BiomeContext wrapSelectionContext(Optional<ResourceKey<Biome>> biomeResourceKey, ModifiableBiomeInfo.BiomeInfo.Builder event) {
        return new BiomeModifications.BiomeContext() {

            BiomeProperties properties = new BiomeModificationsImpl.BiomeWrapped(event);

            @Override
            public Optional<ResourceLocation> getKey() {
                return biomeResourceKey.map(ResourceKey::m_135782_);
            }

            @Override
            public BiomeProperties getProperties() {
                return this.properties;
            }

            @Override
            public boolean hasTag(TagKey<Biome> tag) {
                MinecraftServer server = GameInstance.getServer();
                if (server != null) {
                    Optional<? extends Registry<Biome>> registry = server.registryAccess().m_6632_(Registries.BIOME);
                    if (registry.isPresent()) {
                        Optional<Holder.Reference<Biome>> holder = ((Registry) registry.get()).getHolder((ResourceKey) biomeResourceKey.get());
                        if (holder.isPresent()) {
                            return ((Holder.Reference) holder.get()).is(tag);
                        }
                    }
                }
                return false;
            }
        };
    }

    private static class BiomeModifierImpl implements BiomeModifier {

        private static final BiomeModificationsImpl.BiomeModifierImpl INSTANCE = new BiomeModificationsImpl.BiomeModifierImpl();

        @Override
        public void modify(Holder<Biome> arg, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            List<Pair<Predicate<BiomeModifications.BiomeContext>, BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable>>> list = switch(phase) {
                case ADD ->
                    BiomeModificationsImpl.ADDITIONS;
                case REMOVE ->
                    BiomeModificationsImpl.REMOVALS;
                case MODIFY ->
                    BiomeModificationsImpl.REPLACEMENTS;
                case AFTER_EVERYTHING ->
                    BiomeModificationsImpl.POST_PROCESSING;
                default ->
                    null;
            };
            if (list != null) {
                BiomeModifications.BiomeContext biomeContext = BiomeModificationsImpl.wrapSelectionContext(arg.unwrapKey(), builder);
                BiomeProperties.Mutable mutableBiome = new BiomeModificationsImpl.MutableBiomeWrapped(builder);
                for (Pair<Predicate<BiomeModifications.BiomeContext>, BiConsumer<BiomeModifications.BiomeContext, BiomeProperties.Mutable>> pair : list) {
                    if (((Predicate) pair.getLeft()).test(biomeContext)) {
                        ((BiConsumer) pair.getRight()).accept(biomeContext, mutableBiome);
                    }
                }
            }
        }

        @Override
        public Codec<? extends BiomeModifier> codec() {
            return BiomeModificationsImpl.noneBiomeModCodec != null ? BiomeModificationsImpl.noneBiomeModCodec : Codec.unit(INSTANCE);
        }
    }

    public static class BiomeWrapped implements BiomeProperties {

        protected final ModifiableBiomeInfo.BiomeInfo.Builder event;

        protected final ClimateProperties climateProperties;

        protected final EffectsProperties effectsProperties;

        protected final GenerationProperties generationProperties;

        protected final SpawnProperties spawnProperties;

        public BiomeWrapped(ModifiableBiomeInfo.BiomeInfo.Builder event) {
            this(event, new BiomeModificationsImpl.MutableClimatePropertiesWrapped(event.getClimateSettings()), new BiomeModificationsImpl.MutableEffectsPropertiesWrapped(event.getSpecialEffects()), new BiomeModificationsImpl.GenerationSettingsBuilderWrapped(event.getGenerationSettings()), new BiomeModificationsImpl.SpawnSettingsBuilderWrapped(event.getMobSpawnSettings()));
        }

        public BiomeWrapped(ModifiableBiomeInfo.BiomeInfo.Builder event, ClimateProperties climateProperties, EffectsProperties effectsProperties, GenerationProperties generationProperties, SpawnProperties spawnProperties) {
            this.event = event;
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

    private static class GenerationSettingsBuilderWrapped implements GenerationProperties {

        protected final BiomeGenerationSettingsBuilder generation;

        public GenerationSettingsBuilderWrapped(BiomeGenerationSettingsBuilder generation) {
            this.generation = generation;
        }

        @Override
        public Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving carving) {
            return this.generation.getCarvers(carving);
        }

        @Override
        public Iterable<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration decoration) {
            return this.generation.getFeatures(decoration);
        }

        @Override
        public List<Iterable<Holder<PlacedFeature>>> getFeatures() {
            return this.generation.f_254648_;
        }
    }

    public static class MutableBiomeWrapped extends BiomeModificationsImpl.BiomeWrapped implements BiomeProperties.Mutable {

        public MutableBiomeWrapped(ModifiableBiomeInfo.BiomeInfo.Builder event) {
            super(event, new BiomeModificationsImpl.MutableClimatePropertiesWrapped(event.getClimateSettings()), new BiomeModificationsImpl.MutableEffectsPropertiesWrapped(event.getSpecialEffects()), new BiomeModificationsImpl.MutableGenerationSettingsBuilderWrapped(event.getGenerationSettings()), new BiomeModificationsImpl.MutableSpawnSettingsBuilderWrapped(event.getMobSpawnSettings()));
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

    public static class MutableClimatePropertiesWrapped implements ClimateProperties.Mutable {

        public ClimateSettingsBuilder builder;

        public MutableClimatePropertiesWrapped(ClimateSettingsBuilder builder) {
            this.builder = builder;
        }

        @Override
        public boolean hasPrecipitation() {
            return this.builder.hasPrecipitation();
        }

        @Override
        public float getTemperature() {
            return this.builder.getTemperature();
        }

        @Override
        public Biome.TemperatureModifier getTemperatureModifier() {
            return this.builder.getTemperatureModifier();
        }

        @Override
        public float getDownfall() {
            return this.builder.getDownfall();
        }

        @Override
        public ClimateProperties.Mutable setHasPrecipitation(boolean hasPrecipitation) {
            this.builder.setHasPrecipitation(hasPrecipitation);
            return this;
        }

        @Override
        public ClimateProperties.Mutable setTemperature(float temperature) {
            this.builder.setTemperature(temperature);
            return this;
        }

        @Override
        public ClimateProperties.Mutable setTemperatureModifier(Biome.TemperatureModifier temperatureModifier) {
            this.builder.setTemperatureModifier(temperatureModifier);
            return this;
        }

        @Override
        public ClimateProperties.Mutable setDownfall(float downfall) {
            this.builder.setDownfall(downfall);
            return this;
        }
    }

    public static class MutableEffectsPropertiesWrapped implements EffectsProperties.Mutable {

        public BiomeSpecialEffects.Builder builder;

        public MutableEffectsPropertiesWrapped(BiomeSpecialEffects.Builder builder) {
            this.builder = builder;
        }

        @Override
        public int getFogColor() {
            return this.builder instanceof BiomeSpecialEffectsBuilder b ? b.getFogColor() : this.builder.fogColor.orElse(-1);
        }

        @Override
        public int getWaterColor() {
            return this.builder instanceof BiomeSpecialEffectsBuilder b ? b.getWaterFogColor() : this.builder.waterColor.orElse(-1);
        }

        @Override
        public int getWaterFogColor() {
            return this.builder instanceof BiomeSpecialEffectsBuilder b ? b.getWaterFogColor() : this.builder.waterFogColor.orElse(-1);
        }

        @Override
        public int getSkyColor() {
            return this.builder instanceof BiomeSpecialEffectsBuilder b ? b.getSkyColor() : this.builder.skyColor.orElse(-1);
        }

        @Override
        public OptionalInt getFoliageColorOverride() {
            return (OptionalInt) this.builder.foliageColorOverride.map(OptionalInt::of).orElseGet(OptionalInt::empty);
        }

        @Override
        public OptionalInt getGrassColorOverride() {
            return (OptionalInt) this.builder.grassColorOverride.map(OptionalInt::of).orElseGet(OptionalInt::empty);
        }

        @Override
        public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier() {
            return this.builder.grassColorModifier;
        }

        @Override
        public Optional<AmbientParticleSettings> getAmbientParticle() {
            return this.builder.ambientParticle;
        }

        @Override
        public Optional<Holder<SoundEvent>> getAmbientLoopSound() {
            return this.builder.ambientLoopSoundEvent;
        }

        @Override
        public Optional<AmbientMoodSettings> getAmbientMoodSound() {
            return this.builder.ambientMoodSettings;
        }

        @Override
        public Optional<AmbientAdditionsSettings> getAmbientAdditionsSound() {
            return this.builder.ambientAdditionsSettings;
        }

        @Override
        public Optional<Music> getBackgroundMusic() {
            return this.builder.backgroundMusic;
        }

        @Override
        public EffectsProperties.Mutable setFogColor(int color) {
            this.builder.fogColor(color);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setWaterColor(int color) {
            this.builder.waterColor(color);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setWaterFogColor(int color) {
            this.builder.waterFogColor(color);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setSkyColor(int color) {
            this.builder.skyColor(color);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setFoliageColorOverride(@Nullable Integer colorOverride) {
            this.builder.foliageColorOverride = Optional.ofNullable(colorOverride);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setGrassColorOverride(@Nullable Integer colorOverride) {
            this.builder.foliageColorOverride = Optional.ofNullable(colorOverride);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setGrassColorModifier(BiomeSpecialEffects.GrassColorModifier modifier) {
            this.builder.grassColorModifier(modifier);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setAmbientParticle(@Nullable AmbientParticleSettings settings) {
            this.builder.ambientParticle = Optional.ofNullable(settings);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setAmbientLoopSound(@Nullable Holder<SoundEvent> sound) {
            this.builder.ambientLoopSoundEvent = Optional.ofNullable(sound);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setAmbientMoodSound(@Nullable AmbientMoodSettings settings) {
            this.builder.ambientMoodSettings = Optional.ofNullable(settings);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setAmbientAdditionsSound(@Nullable AmbientAdditionsSettings settings) {
            this.builder.ambientAdditionsSettings = Optional.ofNullable(settings);
            return this;
        }

        @Override
        public EffectsProperties.Mutable setBackgroundMusic(@Nullable Music music) {
            this.builder.backgroundMusic = Optional.ofNullable(music);
            return this;
        }
    }

    private static class MutableGenerationSettingsBuilderWrapped extends BiomeModificationsImpl.GenerationSettingsBuilderWrapped implements GenerationProperties.Mutable {

        public MutableGenerationSettingsBuilderWrapped(BiomeGenerationSettingsBuilder generation) {
            super(generation);
        }

        @Override
        public GenerationProperties.Mutable addFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature) {
            this.generation.m_255419_(decoration, feature);
            return this;
        }

        @Override
        public GenerationProperties.Mutable addFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature) {
            MinecraftServer server = GameInstance.getServer();
            if (server != null) {
                Optional<? extends Registry<PlacedFeature>> registry = server.registryAccess().m_6632_(Registries.PLACED_FEATURE);
                if (registry.isPresent()) {
                    Optional<Holder.Reference<PlacedFeature>> holder = ((Registry) registry.get()).getHolder(feature);
                    if (holder.isPresent()) {
                        return this.addFeature(decoration, (Holder<PlacedFeature>) holder.get());
                    }
                    throw new IllegalArgumentException("Unknown feature: " + feature);
                }
            }
            return this;
        }

        @Override
        public GenerationProperties.Mutable addCarver(GenerationStep.Carving carving, Holder<ConfiguredWorldCarver<?>> feature) {
            this.generation.m_254863_(carving, feature);
            return this;
        }

        @Override
        public GenerationProperties.Mutable addCarver(GenerationStep.Carving carving, ResourceKey<ConfiguredWorldCarver<?>> feature) {
            MinecraftServer server = GameInstance.getServer();
            if (server != null) {
                Optional<? extends Registry<ConfiguredWorldCarver<?>>> registry = server.registryAccess().m_6632_(Registries.CONFIGURED_CARVER);
                if (registry.isPresent()) {
                    Optional<Holder.Reference<ConfiguredWorldCarver<?>>> holder = ((Registry) registry.get()).getHolder(feature);
                    if (holder.isPresent()) {
                        return this.addCarver(carving, (Holder<ConfiguredWorldCarver<?>>) holder.get());
                    }
                    throw new IllegalArgumentException("Unknown carver: " + feature);
                }
            }
            return this;
        }

        @Override
        public GenerationProperties.Mutable removeFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature) {
            this.generation.getFeatures(decoration).removeIf(supplier -> supplier.is(feature));
            return this;
        }

        @Override
        public GenerationProperties.Mutable removeCarver(GenerationStep.Carving carving, ResourceKey<ConfiguredWorldCarver<?>> feature) {
            this.generation.getCarvers(carving).removeIf(supplier -> supplier.is(feature));
            return this;
        }
    }

    private static class MutableSpawnSettingsBuilderWrapped extends BiomeModificationsImpl.SpawnSettingsBuilderWrapped implements SpawnProperties.Mutable {

        public MutableSpawnSettingsBuilderWrapped(MobSpawnSettingsBuilder builder) {
            super(builder);
        }

        @Override
        public SpawnProperties.Mutable setCreatureProbability(float probability) {
            this.builder.m_48368_(probability);
            return this;
        }

        @Override
        public SpawnProperties.Mutable addSpawn(MobCategory category, MobSpawnSettings.SpawnerData data) {
            this.builder.m_48376_(category, data);
            return this;
        }

        @Override
        public boolean removeSpawns(BiPredicate<MobCategory, MobSpawnSettings.SpawnerData> predicate) {
            boolean removed = false;
            for (MobCategory type : this.builder.getSpawnerTypes()) {
                if (this.builder.getSpawner(type).removeIf(data -> predicate.test(type, data))) {
                    removed = true;
                }
            }
            return removed;
        }

        @Override
        public SpawnProperties.Mutable setSpawnCost(EntityType<?> entityType, MobSpawnSettings.MobSpawnCost cost) {
            this.builder.m_48370_(entityType, cost.charge(), cost.energyBudget());
            return this;
        }

        @Override
        public SpawnProperties.Mutable setSpawnCost(EntityType<?> entityType, double charge, double energyBudget) {
            this.builder.m_48370_(entityType, charge, energyBudget);
            return this;
        }

        @Override
        public SpawnProperties.Mutable clearSpawnCost(EntityType<?> entityType) {
            this.getMobSpawnCosts().remove(entityType);
            return this;
        }
    }

    private static class SpawnSettingsBuilderWrapped implements SpawnProperties {

        protected final MobSpawnSettingsBuilder builder;

        public SpawnSettingsBuilderWrapped(MobSpawnSettingsBuilder builder) {
            this.builder = builder;
        }

        @Override
        public float getCreatureProbability() {
            return this.builder.getProbability();
        }

        @Override
        public Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners() {
            return this.builder.f_48362_;
        }

        @Override
        public Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts() {
            return this.builder.f_48363_;
        }
    }
}