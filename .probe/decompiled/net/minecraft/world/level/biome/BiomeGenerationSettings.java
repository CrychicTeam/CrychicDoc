package net.minecraft.world.level.biome;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.slf4j.Logger;

public class BiomeGenerationSettings {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final BiomeGenerationSettings EMPTY = new BiomeGenerationSettings(ImmutableMap.of(), ImmutableList.of());

    public static final MapCodec<BiomeGenerationSettings> CODEC = RecordCodecBuilder.mapCodec(p_186655_ -> p_186655_.group(Codec.simpleMap(GenerationStep.Carving.CODEC, ConfiguredWorldCarver.LIST_CODEC.promotePartial(Util.prefix("Carver: ", LOGGER::error)), StringRepresentable.keys(GenerationStep.Carving.values())).fieldOf("carvers").forGetter(p_186661_ -> p_186661_.carvers), PlacedFeature.LIST_OF_LISTS_CODEC.promotePartial(Util.prefix("Features: ", LOGGER::error)).fieldOf("features").forGetter(p_186653_ -> p_186653_.features)).apply(p_186655_, BiomeGenerationSettings::new));

    private final Map<GenerationStep.Carving, HolderSet<ConfiguredWorldCarver<?>>> carvers;

    private final List<HolderSet<PlacedFeature>> features;

    private final Supplier<List<ConfiguredFeature<?, ?>>> flowerFeatures;

    private final Supplier<Set<PlacedFeature>> featureSet;

    BiomeGenerationSettings(Map<GenerationStep.Carving, HolderSet<ConfiguredWorldCarver<?>>> mapGenerationStepCarvingHolderSetConfiguredWorldCarver0, List<HolderSet<PlacedFeature>> listHolderSetPlacedFeature1) {
        this.carvers = mapGenerationStepCarvingHolderSetConfiguredWorldCarver0;
        this.features = listHolderSetPlacedFeature1;
        this.flowerFeatures = Suppliers.memoize(() -> (List) listHolderSetPlacedFeature1.stream().flatMap(HolderSet::m_203614_).map(Holder::m_203334_).flatMap(PlacedFeature::m_191781_).filter(p_186657_ -> p_186657_.feature() == Feature.FLOWER).collect(ImmutableList.toImmutableList()));
        this.featureSet = Suppliers.memoize(() -> (Set) listHolderSetPlacedFeature1.stream().flatMap(HolderSet::m_203614_).map(Holder::m_203334_).collect(Collectors.toSet()));
    }

    public Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving generationStepCarving0) {
        return (Iterable<Holder<ConfiguredWorldCarver<?>>>) Objects.requireNonNullElseGet((Iterable) this.carvers.get(generationStepCarving0), List::of);
    }

    public List<ConfiguredFeature<?, ?>> getFlowerFeatures() {
        return (List<ConfiguredFeature<?, ?>>) this.flowerFeatures.get();
    }

    public List<HolderSet<PlacedFeature>> features() {
        return this.features;
    }

    public boolean hasFeature(PlacedFeature placedFeature0) {
        return ((Set) this.featureSet.get()).contains(placedFeature0);
    }

    public static class Builder extends BiomeGenerationSettings.PlainBuilder {

        private final HolderGetter<PlacedFeature> placedFeatures;

        private final HolderGetter<ConfiguredWorldCarver<?>> worldCarvers;

        public Builder(HolderGetter<PlacedFeature> holderGetterPlacedFeature0, HolderGetter<ConfiguredWorldCarver<?>> holderGetterConfiguredWorldCarver1) {
            this.placedFeatures = holderGetterPlacedFeature0;
            this.worldCarvers = holderGetterConfiguredWorldCarver1;
        }

        public BiomeGenerationSettings.Builder addFeature(GenerationStep.Decoration generationStepDecoration0, ResourceKey<PlacedFeature> resourceKeyPlacedFeature1) {
            this.m_254982_(generationStepDecoration0.ordinal(), this.placedFeatures.getOrThrow(resourceKeyPlacedFeature1));
            return this;
        }

        public BiomeGenerationSettings.Builder addCarver(GenerationStep.Carving generationStepCarving0, ResourceKey<ConfiguredWorldCarver<?>> resourceKeyConfiguredWorldCarver1) {
            this.m_254863_(generationStepCarving0, this.worldCarvers.getOrThrow(resourceKeyConfiguredWorldCarver1));
            return this;
        }
    }

    public static class PlainBuilder {

        private final Map<GenerationStep.Carving, List<Holder<ConfiguredWorldCarver<?>>>> carvers = Maps.newLinkedHashMap();

        private final List<List<Holder<PlacedFeature>>> features = Lists.newArrayList();

        public BiomeGenerationSettings.PlainBuilder addFeature(GenerationStep.Decoration generationStepDecoration0, Holder<PlacedFeature> holderPlacedFeature1) {
            return this.addFeature(generationStepDecoration0.ordinal(), holderPlacedFeature1);
        }

        public BiomeGenerationSettings.PlainBuilder addFeature(int int0, Holder<PlacedFeature> holderPlacedFeature1) {
            this.addFeatureStepsUpTo(int0);
            ((List) this.features.get(int0)).add(holderPlacedFeature1);
            return this;
        }

        public BiomeGenerationSettings.PlainBuilder addCarver(GenerationStep.Carving generationStepCarving0, Holder<ConfiguredWorldCarver<?>> holderConfiguredWorldCarver1) {
            ((List) this.carvers.computeIfAbsent(generationStepCarving0, p_256199_ -> Lists.newArrayList())).add(holderConfiguredWorldCarver1);
            return this;
        }

        private void addFeatureStepsUpTo(int int0) {
            while (this.features.size() <= int0) {
                this.features.add(Lists.newArrayList());
            }
        }

        public BiomeGenerationSettings build() {
            return new BiomeGenerationSettings((Map<GenerationStep.Carving, HolderSet<ConfiguredWorldCarver<?>>>) this.carvers.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, p_255831_ -> HolderSet.direct((List) p_255831_.getValue()))), (List<HolderSet<PlacedFeature>>) this.features.stream().map(HolderSet::m_205800_).collect(ImmutableList.toImmutableList()));
        }
    }
}