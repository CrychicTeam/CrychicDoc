package fuzs.puzzleslib.api.biome.v1;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public interface GenerationSettingsContext {

    boolean removeFeature(GenerationStep.Decoration var1, ResourceKey<PlacedFeature> var2);

    default boolean removeFeature(ResourceKey<PlacedFeature> featureKey) {
        boolean anyFound = false;
        for (GenerationStep.Decoration step : GenerationStep.Decoration.values()) {
            if (this.removeFeature(step, featureKey)) {
                anyFound = true;
            }
        }
        return anyFound;
    }

    void addFeature(GenerationStep.Decoration var1, ResourceKey<PlacedFeature> var2);

    void addCarver(GenerationStep.Carving var1, ResourceKey<ConfiguredWorldCarver<?>> var2);

    boolean removeCarver(GenerationStep.Carving var1, ResourceKey<ConfiguredWorldCarver<?>> var2);

    default boolean removeCarver(ResourceKey<ConfiguredWorldCarver<?>> carverKey) {
        boolean anyFound = false;
        for (GenerationStep.Carving step : GenerationStep.Carving.values()) {
            if (this.removeCarver(step, carverKey)) {
                anyFound = true;
            }
        }
        return anyFound;
    }

    Iterable<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration var1);

    Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving var1);
}