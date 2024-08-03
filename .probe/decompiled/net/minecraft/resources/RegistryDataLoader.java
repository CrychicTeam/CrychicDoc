package net.minecraft.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ChatType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.slf4j.Logger;

public class RegistryDataLoader {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final List<RegistryDataLoader.RegistryData<?>> WORLDGEN_REGISTRIES = List.of(new RegistryDataLoader.RegistryData<>(Registries.DIMENSION_TYPE, DimensionType.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.BIOME, Biome.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.CHAT_TYPE, ChatType.CODEC), new RegistryDataLoader.RegistryData<>(Registries.CONFIGURED_CARVER, ConfiguredWorldCarver.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.CONFIGURED_FEATURE, ConfiguredFeature.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.PLACED_FEATURE, PlacedFeature.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.STRUCTURE, Structure.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.STRUCTURE_SET, StructureSet.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.PROCESSOR_LIST, StructureProcessorType.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.TEMPLATE_POOL, StructureTemplatePool.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.NOISE_SETTINGS, NoiseGeneratorSettings.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.NOISE, NormalNoise.NoiseParameters.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.DENSITY_FUNCTION, DensityFunction.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.WORLD_PRESET, WorldPreset.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.FLAT_LEVEL_GENERATOR_PRESET, FlatLevelGeneratorPreset.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.TRIM_PATTERN, TrimPattern.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.TRIM_MATERIAL, TrimMaterial.DIRECT_CODEC), new RegistryDataLoader.RegistryData<>(Registries.DAMAGE_TYPE, DamageType.CODEC), new RegistryDataLoader.RegistryData<>(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, MultiNoiseBiomeSourceParameterList.DIRECT_CODEC));

    public static final List<RegistryDataLoader.RegistryData<?>> DIMENSION_REGISTRIES = List.of(new RegistryDataLoader.RegistryData<>(Registries.LEVEL_STEM, LevelStem.CODEC));

    public static RegistryAccess.Frozen load(ResourceManager resourceManager0, RegistryAccess registryAccess1, List<RegistryDataLoader.RegistryData<?>> listRegistryDataLoaderRegistryData2) {
        Map<ResourceKey<?>, Exception> $$3 = new HashMap();
        List<Pair<WritableRegistry<?>, RegistryDataLoader.Loader>> $$4 = listRegistryDataLoaderRegistryData2.stream().map(p_250249_ -> p_250249_.create(Lifecycle.stable(), $$3)).toList();
        RegistryOps.RegistryInfoLookup $$5 = createContext(registryAccess1, $$4);
        $$4.forEach(p_255508_ -> ((RegistryDataLoader.Loader) p_255508_.getSecond()).load(resourceManager0, $$5));
        $$4.forEach(p_258223_ -> {
            Registry<?> $$2 = (Registry<?>) p_258223_.getFirst();
            try {
                $$2.freeze();
            } catch (Exception var4x) {
                $$3.put($$2.key(), var4x);
            }
        });
        if (!$$3.isEmpty()) {
            logErrors($$3);
            throw new IllegalStateException("Failed to load registries due to above errors");
        } else {
            return new RegistryAccess.ImmutableRegistryAccess($$4.stream().map(Pair::getFirst).toList()).m_203557_();
        }
    }

    private static RegistryOps.RegistryInfoLookup createContext(RegistryAccess registryAccess0, List<Pair<WritableRegistry<?>, RegistryDataLoader.Loader>> listPairWritableRegistryRegistryDataLoaderLoader1) {
        final Map<ResourceKey<? extends Registry<?>>, RegistryOps.RegistryInfo<?>> $$2 = new HashMap();
        registryAccess0.registries().forEach(p_255505_ -> $$2.put(p_255505_.key(), createInfoForContextRegistry(p_255505_.value())));
        listPairWritableRegistryRegistryDataLoaderLoader1.forEach(p_258221_ -> $$2.put(((WritableRegistry) p_258221_.getFirst()).m_123023_(), createInfoForNewRegistry((WritableRegistry) p_258221_.getFirst())));
        return new RegistryOps.RegistryInfoLookup() {

            @Override
            public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> p_256014_) {
                return Optional.ofNullable((RegistryOps.RegistryInfo) $$2.get(p_256014_));
            }
        };
    }

    private static <T> RegistryOps.RegistryInfo<T> createInfoForNewRegistry(WritableRegistry<T> writableRegistryT0) {
        return new RegistryOps.RegistryInfo<>(writableRegistryT0.m_255303_(), writableRegistryT0.createRegistrationLookup(), writableRegistryT0.m_203658_());
    }

    private static <T> RegistryOps.RegistryInfo<T> createInfoForContextRegistry(Registry<T> registryT0) {
        return new RegistryOps.RegistryInfo<>(registryT0.asLookup(), registryT0.asTagAddingLookup(), registryT0.registryLifecycle());
    }

    private static void logErrors(Map<ResourceKey<?>, Exception> mapResourceKeyException0) {
        StringWriter $$1 = new StringWriter();
        PrintWriter $$2 = new PrintWriter($$1);
        Map<ResourceLocation, Map<ResourceLocation, Exception>> $$3 = (Map<ResourceLocation, Map<ResourceLocation, Exception>>) mapResourceKeyException0.entrySet().stream().collect(Collectors.groupingBy(p_249353_ -> ((ResourceKey) p_249353_.getKey()).registry(), Collectors.toMap(p_251444_ -> ((ResourceKey) p_251444_.getKey()).location(), Entry::getValue)));
        $$3.entrySet().stream().sorted(Entry.comparingByKey()).forEach(p_249838_ -> {
            $$2.printf("> Errors in registry %s:%n", p_249838_.getKey());
            ((Map) p_249838_.getValue()).entrySet().stream().sorted(Entry.comparingByKey()).forEach(p_250688_ -> {
                $$2.printf(">> Errors in element %s:%n", p_250688_.getKey());
                ((Exception) p_250688_.getValue()).printStackTrace($$2);
            });
        });
        $$2.flush();
        LOGGER.error("Registry loading errors:\n{}", $$1);
    }

    private static String registryDirPath(ResourceLocation resourceLocation0) {
        return resourceLocation0.getPath();
    }

    static <E> void loadRegistryContents(RegistryOps.RegistryInfoLookup registryOpsRegistryInfoLookup0, ResourceManager resourceManager1, ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE2, WritableRegistry<E> writableRegistryE3, Decoder<E> decoderE4, Map<ResourceKey<?>, Exception> mapResourceKeyException5) {
        String $$6 = registryDirPath(resourceKeyExtendsRegistryE2.location());
        FileToIdConverter $$7 = FileToIdConverter.json($$6);
        RegistryOps<JsonElement> $$8 = RegistryOps.create(JsonOps.INSTANCE, registryOpsRegistryInfoLookup0);
        for (Entry<ResourceLocation, Resource> $$9 : $$7.listMatchingResources(resourceManager1).entrySet()) {
            ResourceLocation $$10 = (ResourceLocation) $$9.getKey();
            ResourceKey<E> $$11 = ResourceKey.create(resourceKeyExtendsRegistryE2, $$7.fileToId($$10));
            Resource $$12 = (Resource) $$9.getValue();
            try {
                Reader $$13 = $$12.openAsReader();
                try {
                    JsonElement $$14 = JsonParser.parseReader($$13);
                    DataResult<E> $$15 = decoderE4.parse($$8, $$14);
                    E $$16 = (E) $$15.getOrThrow(false, p_248715_ -> {
                    });
                    writableRegistryE3.register($$11, $$16, $$12.isBuiltin() ? Lifecycle.stable() : $$15.lifecycle());
                } catch (Throwable var19) {
                    if ($$13 != null) {
                        try {
                            $$13.close();
                        } catch (Throwable var18) {
                            var19.addSuppressed(var18);
                        }
                    }
                    throw var19;
                }
                if ($$13 != null) {
                    $$13.close();
                }
            } catch (Exception var20) {
                mapResourceKeyException5.put($$11, new IllegalStateException(String.format(Locale.ROOT, "Failed to parse %s from pack %s", $$10, $$12.sourcePackId()), var20));
            }
        }
    }

    interface Loader {

        void load(ResourceManager var1, RegistryOps.RegistryInfoLookup var2);
    }

    public static record RegistryData<T>(ResourceKey<? extends Registry<T>> f_243794_, Codec<T> f_244580_) {

        private final ResourceKey<? extends Registry<T>> key;

        private final Codec<T> elementCodec;

        public RegistryData(ResourceKey<? extends Registry<T>> f_243794_, Codec<T> f_244580_) {
            this.key = f_243794_;
            this.elementCodec = f_244580_;
        }

        Pair<WritableRegistry<?>, RegistryDataLoader.Loader> create(Lifecycle p_251662_, Map<ResourceKey<?>, Exception> p_251565_) {
            WritableRegistry<T> $$2 = new MappedRegistry<>(this.key, p_251662_);
            RegistryDataLoader.Loader $$3 = (p_255511_, p_255512_) -> RegistryDataLoader.loadRegistryContents(p_255512_, p_255511_, this.key, $$2, this.elementCodec, p_251565_);
            return Pair.of($$2, $$3);
        }
    }
}