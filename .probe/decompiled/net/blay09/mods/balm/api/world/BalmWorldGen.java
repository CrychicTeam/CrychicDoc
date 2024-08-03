package net.blay09.mods.balm.api.world;

import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public interface BalmWorldGen {

    <T extends Feature<?>> DeferredObject<T> registerFeature(ResourceLocation var1, Supplier<T> var2);

    <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(ResourceLocation var1, Supplier<T> var2);

    void addFeatureToBiomes(BiomePredicate var1, GenerationStep.Decoration var2, ResourceLocation var3);
}