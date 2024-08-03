package net.minecraft.data.worldgen.placement;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public class PlacementUtils {

    public static final PlacementModifier HEIGHTMAP = HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING);

    public static final PlacementModifier HEIGHTMAP_TOP_SOLID = HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG);

    public static final PlacementModifier HEIGHTMAP_WORLD_SURFACE = HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG);

    public static final PlacementModifier HEIGHTMAP_OCEAN_FLOOR = HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR);

    public static final PlacementModifier FULL_RANGE = HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top());

    public static final PlacementModifier RANGE_10_10 = HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10));

    public static final PlacementModifier RANGE_8_8 = HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(8), VerticalAnchor.belowTop(8));

    public static final PlacementModifier RANGE_4_4 = HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(4), VerticalAnchor.belowTop(4));

    public static final PlacementModifier RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT = HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(256));

    public static void bootstrap(BootstapContext<PlacedFeature> bootstapContextPlacedFeature0) {
        AquaticPlacements.bootstrap(bootstapContextPlacedFeature0);
        CavePlacements.bootstrap(bootstapContextPlacedFeature0);
        EndPlacements.bootstrap(bootstapContextPlacedFeature0);
        MiscOverworldPlacements.bootstrap(bootstapContextPlacedFeature0);
        NetherPlacements.bootstrap(bootstapContextPlacedFeature0);
        OrePlacements.bootstrap(bootstapContextPlacedFeature0);
        TreePlacements.bootstrap(bootstapContextPlacedFeature0);
        VegetationPlacements.bootstrap(bootstapContextPlacedFeature0);
        VillagePlacements.bootstrap(bootstapContextPlacedFeature0);
    }

    public static ResourceKey<PlacedFeature> createKey(String string0) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(string0));
    }

    public static void register(BootstapContext<PlacedFeature> bootstapContextPlacedFeature0, ResourceKey<PlacedFeature> resourceKeyPlacedFeature1, Holder<ConfiguredFeature<?, ?>> holderConfiguredFeature2, List<PlacementModifier> listPlacementModifier3) {
        bootstapContextPlacedFeature0.register(resourceKeyPlacedFeature1, new PlacedFeature(holderConfiguredFeature2, List.copyOf(listPlacementModifier3)));
    }

    public static void register(BootstapContext<PlacedFeature> bootstapContextPlacedFeature0, ResourceKey<PlacedFeature> resourceKeyPlacedFeature1, Holder<ConfiguredFeature<?, ?>> holderConfiguredFeature2, PlacementModifier... placementModifier3) {
        register(bootstapContextPlacedFeature0, resourceKeyPlacedFeature1, holderConfiguredFeature2, List.of(placementModifier3));
    }

    public static PlacementModifier countExtra(int int0, float float1, int int2) {
        float $$3 = 1.0F / float1;
        if (Math.abs($$3 - (float) ((int) $$3)) > 1.0E-5F) {
            throw new IllegalStateException("Chance data cannot be represented as list weight");
        } else {
            SimpleWeightedRandomList<IntProvider> $$4 = SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(int0), (int) $$3 - 1).add(ConstantInt.of(int0 + int2), 1).build();
            return CountPlacement.of(new WeightedListInt($$4));
        }
    }

    public static PlacementFilter isEmpty() {
        return BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE);
    }

    public static BlockPredicateFilter filteredByBlockSurvival(Block block0) {
        return BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(block0.defaultBlockState(), BlockPos.ZERO));
    }

    public static Holder<PlacedFeature> inlinePlaced(Holder<ConfiguredFeature<?, ?>> holderConfiguredFeature0, PlacementModifier... placementModifier1) {
        return Holder.direct(new PlacedFeature(holderConfiguredFeature0, List.of(placementModifier1)));
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<PlacedFeature> inlinePlaced(F f0, FC fC1, PlacementModifier... placementModifier2) {
        return inlinePlaced(Holder.direct(new ConfiguredFeature(f0, fC1)), placementModifier2);
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<PlacedFeature> onlyWhenEmpty(F f0, FC fC1) {
        return filtered(f0, fC1, BlockPredicate.ONLY_IN_AIR_PREDICATE);
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<PlacedFeature> filtered(F f0, FC fC1, BlockPredicate blockPredicate2) {
        return inlinePlaced(f0, fC1, BlockPredicateFilter.forPredicate(blockPredicate2));
    }
}