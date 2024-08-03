package net.minecraft.data.worldgen.placement;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public class NetherPlacements {

    public static final ResourceKey<PlacedFeature> DELTA = PlacementUtils.createKey("delta");

    public static final ResourceKey<PlacedFeature> SMALL_BASALT_COLUMNS = PlacementUtils.createKey("small_basalt_columns");

    public static final ResourceKey<PlacedFeature> LARGE_BASALT_COLUMNS = PlacementUtils.createKey("large_basalt_columns");

    public static final ResourceKey<PlacedFeature> BASALT_BLOBS = PlacementUtils.createKey("basalt_blobs");

    public static final ResourceKey<PlacedFeature> BLACKSTONE_BLOBS = PlacementUtils.createKey("blackstone_blobs");

    public static final ResourceKey<PlacedFeature> GLOWSTONE_EXTRA = PlacementUtils.createKey("glowstone_extra");

    public static final ResourceKey<PlacedFeature> GLOWSTONE = PlacementUtils.createKey("glowstone");

    public static final ResourceKey<PlacedFeature> CRIMSON_FOREST_VEGETATION = PlacementUtils.createKey("crimson_forest_vegetation");

    public static final ResourceKey<PlacedFeature> WARPED_FOREST_VEGETATION = PlacementUtils.createKey("warped_forest_vegetation");

    public static final ResourceKey<PlacedFeature> NETHER_SPROUTS = PlacementUtils.createKey("nether_sprouts");

    public static final ResourceKey<PlacedFeature> TWISTING_VINES = PlacementUtils.createKey("twisting_vines");

    public static final ResourceKey<PlacedFeature> WEEPING_VINES = PlacementUtils.createKey("weeping_vines");

    public static final ResourceKey<PlacedFeature> PATCH_CRIMSON_ROOTS = PlacementUtils.createKey("patch_crimson_roots");

    public static final ResourceKey<PlacedFeature> BASALT_PILLAR = PlacementUtils.createKey("basalt_pillar");

    public static final ResourceKey<PlacedFeature> SPRING_DELTA = PlacementUtils.createKey("spring_delta");

    public static final ResourceKey<PlacedFeature> SPRING_CLOSED = PlacementUtils.createKey("spring_closed");

    public static final ResourceKey<PlacedFeature> SPRING_CLOSED_DOUBLE = PlacementUtils.createKey("spring_closed_double");

    public static final ResourceKey<PlacedFeature> SPRING_OPEN = PlacementUtils.createKey("spring_open");

    public static final ResourceKey<PlacedFeature> PATCH_SOUL_FIRE = PlacementUtils.createKey("patch_soul_fire");

    public static final ResourceKey<PlacedFeature> PATCH_FIRE = PlacementUtils.createKey("patch_fire");

    public static void bootstrap(BootstapContext<PlacedFeature> bootstapContextPlacedFeature0) {
        HolderGetter<ConfiguredFeature<?, ?>> $$1 = bootstapContextPlacedFeature0.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> $$2 = $$1.getOrThrow(NetherFeatures.DELTA);
        Holder<ConfiguredFeature<?, ?>> $$3 = $$1.getOrThrow(NetherFeatures.SMALL_BASALT_COLUMNS);
        Holder<ConfiguredFeature<?, ?>> $$4 = $$1.getOrThrow(NetherFeatures.LARGE_BASALT_COLUMNS);
        Holder<ConfiguredFeature<?, ?>> $$5 = $$1.getOrThrow(NetherFeatures.BASALT_BLOBS);
        Holder<ConfiguredFeature<?, ?>> $$6 = $$1.getOrThrow(NetherFeatures.BLACKSTONE_BLOBS);
        Holder<ConfiguredFeature<?, ?>> $$7 = $$1.getOrThrow(NetherFeatures.GLOWSTONE_EXTRA);
        Holder<ConfiguredFeature<?, ?>> $$8 = $$1.getOrThrow(NetherFeatures.CRIMSON_FOREST_VEGETATION);
        Holder<ConfiguredFeature<?, ?>> $$9 = $$1.getOrThrow(NetherFeatures.WARPED_FOREST_VEGETION);
        Holder<ConfiguredFeature<?, ?>> $$10 = $$1.getOrThrow(NetherFeatures.NETHER_SPROUTS);
        Holder<ConfiguredFeature<?, ?>> $$11 = $$1.getOrThrow(NetherFeatures.TWISTING_VINES);
        Holder<ConfiguredFeature<?, ?>> $$12 = $$1.getOrThrow(NetherFeatures.WEEPING_VINES);
        Holder<ConfiguredFeature<?, ?>> $$13 = $$1.getOrThrow(NetherFeatures.PATCH_CRIMSON_ROOTS);
        Holder<ConfiguredFeature<?, ?>> $$14 = $$1.getOrThrow(NetherFeatures.BASALT_PILLAR);
        Holder<ConfiguredFeature<?, ?>> $$15 = $$1.getOrThrow(NetherFeatures.SPRING_LAVA_NETHER);
        Holder<ConfiguredFeature<?, ?>> $$16 = $$1.getOrThrow(NetherFeatures.SPRING_NETHER_CLOSED);
        Holder<ConfiguredFeature<?, ?>> $$17 = $$1.getOrThrow(NetherFeatures.SPRING_NETHER_OPEN);
        Holder<ConfiguredFeature<?, ?>> $$18 = $$1.getOrThrow(NetherFeatures.PATCH_SOUL_FIRE);
        Holder<ConfiguredFeature<?, ?>> $$19 = $$1.getOrThrow(NetherFeatures.PATCH_FIRE);
        PlacementUtils.register(bootstapContextPlacedFeature0, DELTA, $$2, CountOnEveryLayerPlacement.of(40), BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, SMALL_BASALT_COLUMNS, $$3, CountOnEveryLayerPlacement.of(4), BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, LARGE_BASALT_COLUMNS, $$4, CountOnEveryLayerPlacement.of(2), BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, BASALT_BLOBS, $$5, CountPlacement.of(75), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, BLACKSTONE_BLOBS, $$6, CountPlacement.of(25), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, GLOWSTONE_EXTRA, $$7, CountPlacement.of(BiasedToBottomInt.of(0, 9)), InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, GLOWSTONE, $$7, CountPlacement.of(10), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, CRIMSON_FOREST_VEGETATION, $$8, CountOnEveryLayerPlacement.of(6), BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, WARPED_FOREST_VEGETATION, $$9, CountOnEveryLayerPlacement.of(5), BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, NETHER_SPROUTS, $$10, CountOnEveryLayerPlacement.of(4), BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, TWISTING_VINES, $$11, CountPlacement.of(10), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, WEEPING_VINES, $$12, CountPlacement.of(10), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, PATCH_CRIMSON_ROOTS, $$13, PlacementUtils.FULL_RANGE, BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, BASALT_PILLAR, $$14, CountPlacement.of(10), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, SPRING_DELTA, $$15, CountPlacement.of(16), InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, SPRING_CLOSED, $$16, CountPlacement.of(16), InSquarePlacement.spread(), PlacementUtils.RANGE_10_10, BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, SPRING_CLOSED_DOUBLE, $$16, CountPlacement.of(32), InSquarePlacement.spread(), PlacementUtils.RANGE_10_10, BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, SPRING_OPEN, $$17, CountPlacement.of(8), InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, BiomeFilter.biome());
        List<PlacementModifier> $$20 = List.of(CountPlacement.of(UniformInt.of(0, 5)), InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, PATCH_SOUL_FIRE, $$18, $$20);
        PlacementUtils.register(bootstapContextPlacedFeature0, PATCH_FIRE, $$19, $$20);
    }
}