package fr.lucreeper74.createmetallurgy.worldgen;

import fr.lucreeper74.createmetallurgy.CreateMetallurgy;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class PlacedFeatures {

    public static final ResourceKey<PlacedFeature> WOLFRAMIE_ORE_PLACED_KEY = registerKey("wolframite_ore_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        register(context, WOLFRAMIE_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ConfiguredFeatures.WOLFRAMIE_ORE_KEY), commonOrePlacement(7, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(60))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, CreateMetallurgy.genRL(name));
    }

    public static List<PlacementModifier> orePlacement(PlacementModifier placementModifier0, PlacementModifier placementModifier1) {
        return List.of(placementModifier0, InSquarePlacement.spread(), placementModifier1, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int int0, PlacementModifier placementModifier1) {
        return orePlacement(CountPlacement.of(int0), placementModifier1);
    }

    public static List<PlacementModifier> rareOrePlacement(int int0, PlacementModifier placementModifier1) {
        return orePlacement(RarityFilter.onAverageOnceEvery(int0), placementModifier1);
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}