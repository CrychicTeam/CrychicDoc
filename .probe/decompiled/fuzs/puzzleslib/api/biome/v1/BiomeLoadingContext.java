package fuzs.puzzleslib.api.biome.v1;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;

public interface BiomeLoadingContext {

    ResourceKey<Biome> getResourceKey();

    Biome getBiome();

    Holder<Biome> holder();

    default boolean hasFeature(ResourceKey<ConfiguredFeature<?, ?>> key) {
        for (HolderSet<PlacedFeature> featureSuppliers : this.getBiome().getGenerationSettings().features()) {
            for (Holder<PlacedFeature> featureSupplier : featureSuppliers) {
                if (featureSupplier.value().getFeatures().anyMatch(cf -> this.getFeatureKey(cf).orElse(null) == key)) {
                    return true;
                }
            }
        }
        return false;
    }

    default boolean hasPlacedFeature(ResourceKey<PlacedFeature> key) {
        for (HolderSet<PlacedFeature> featureSuppliers : this.getBiome().getGenerationSettings().features()) {
            for (Holder<PlacedFeature> featureSupplier : featureSuppliers) {
                if (this.getPlacedFeatureKey(featureSupplier.value()).orElse(null) == key) {
                    return true;
                }
            }
        }
        return false;
    }

    Optional<ResourceKey<ConfiguredFeature<?, ?>>> getFeatureKey(ConfiguredFeature<?, ?> var1);

    Optional<ResourceKey<PlacedFeature>> getPlacedFeatureKey(PlacedFeature var1);

    boolean validForStructure(ResourceKey<Structure> var1);

    Optional<ResourceKey<Structure>> getStructureKey(Structure var1);

    boolean canGenerateIn(ResourceKey<LevelStem> var1);

    boolean is(TagKey<Biome> var1);

    default boolean is(Biome biome) {
        return this.getBiome() == biome;
    }

    default boolean is(Holder<Biome> holder) {
        return this.holder() == holder;
    }

    default boolean is(ResourceKey<Biome> resourceKey) {
        return this.getResourceKey() == resourceKey;
    }
}