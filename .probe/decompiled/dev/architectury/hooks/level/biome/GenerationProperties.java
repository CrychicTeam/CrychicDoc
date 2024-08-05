package dev.architectury.hooks.level.biome;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.ApiStatus.Experimental;

public interface GenerationProperties {

    Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving var1);

    Iterable<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration var1);

    List<Iterable<Holder<PlacedFeature>>> getFeatures();

    public interface Mutable extends GenerationProperties {

        GenerationProperties.Mutable addFeature(GenerationStep.Decoration var1, Holder<PlacedFeature> var2);

        @Experimental
        GenerationProperties.Mutable addFeature(GenerationStep.Decoration var1, ResourceKey<PlacedFeature> var2);

        GenerationProperties.Mutable addCarver(GenerationStep.Carving var1, Holder<ConfiguredWorldCarver<?>> var2);

        @Experimental
        GenerationProperties.Mutable addCarver(GenerationStep.Carving var1, ResourceKey<ConfiguredWorldCarver<?>> var2);

        GenerationProperties.Mutable removeFeature(GenerationStep.Decoration var1, ResourceKey<PlacedFeature> var2);

        GenerationProperties.Mutable removeCarver(GenerationStep.Carving var1, ResourceKey<ConfiguredWorldCarver<?>> var2);
    }
}