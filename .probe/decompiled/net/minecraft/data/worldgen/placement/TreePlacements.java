package net.minecraft.data.worldgen.placement;

import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public class TreePlacements {

    public static final ResourceKey<PlacedFeature> CRIMSON_FUNGI = PlacementUtils.createKey("crimson_fungi");

    public static final ResourceKey<PlacedFeature> WARPED_FUNGI = PlacementUtils.createKey("warped_fungi");

    public static final ResourceKey<PlacedFeature> OAK_CHECKED = PlacementUtils.createKey("oak_checked");

    public static final ResourceKey<PlacedFeature> DARK_OAK_CHECKED = PlacementUtils.createKey("dark_oak_checked");

    public static final ResourceKey<PlacedFeature> BIRCH_CHECKED = PlacementUtils.createKey("birch_checked");

    public static final ResourceKey<PlacedFeature> ACACIA_CHECKED = PlacementUtils.createKey("acacia_checked");

    public static final ResourceKey<PlacedFeature> SPRUCE_CHECKED = PlacementUtils.createKey("spruce_checked");

    public static final ResourceKey<PlacedFeature> MANGROVE_CHECKED = PlacementUtils.createKey("mangrove_checked");

    public static final ResourceKey<PlacedFeature> CHERRY_CHECKED = PlacementUtils.createKey("cherry_checked");

    public static final ResourceKey<PlacedFeature> PINE_ON_SNOW = PlacementUtils.createKey("pine_on_snow");

    public static final ResourceKey<PlacedFeature> SPRUCE_ON_SNOW = PlacementUtils.createKey("spruce_on_snow");

    public static final ResourceKey<PlacedFeature> PINE_CHECKED = PlacementUtils.createKey("pine_checked");

    public static final ResourceKey<PlacedFeature> JUNGLE_TREE_CHECKED = PlacementUtils.createKey("jungle_tree");

    public static final ResourceKey<PlacedFeature> FANCY_OAK_CHECKED = PlacementUtils.createKey("fancy_oak_checked");

    public static final ResourceKey<PlacedFeature> MEGA_JUNGLE_TREE_CHECKED = PlacementUtils.createKey("mega_jungle_tree_checked");

    public static final ResourceKey<PlacedFeature> MEGA_SPRUCE_CHECKED = PlacementUtils.createKey("mega_spruce_checked");

    public static final ResourceKey<PlacedFeature> MEGA_PINE_CHECKED = PlacementUtils.createKey("mega_pine_checked");

    public static final ResourceKey<PlacedFeature> TALL_MANGROVE_CHECKED = PlacementUtils.createKey("tall_mangrove_checked");

    public static final ResourceKey<PlacedFeature> JUNGLE_BUSH = PlacementUtils.createKey("jungle_bush");

    public static final ResourceKey<PlacedFeature> SUPER_BIRCH_BEES_0002 = PlacementUtils.createKey("super_birch_bees_0002");

    public static final ResourceKey<PlacedFeature> SUPER_BIRCH_BEES = PlacementUtils.createKey("super_birch_bees");

    public static final ResourceKey<PlacedFeature> OAK_BEES_0002 = PlacementUtils.createKey("oak_bees_0002");

    public static final ResourceKey<PlacedFeature> OAK_BEES_002 = PlacementUtils.createKey("oak_bees_002");

    public static final ResourceKey<PlacedFeature> BIRCH_BEES_0002_PLACED = PlacementUtils.createKey("birch_bees_0002");

    public static final ResourceKey<PlacedFeature> BIRCH_BEES_002 = PlacementUtils.createKey("birch_bees_002");

    public static final ResourceKey<PlacedFeature> FANCY_OAK_BEES_0002 = PlacementUtils.createKey("fancy_oak_bees_0002");

    public static final ResourceKey<PlacedFeature> FANCY_OAK_BEES_002 = PlacementUtils.createKey("fancy_oak_bees_002");

    public static final ResourceKey<PlacedFeature> FANCY_OAK_BEES = PlacementUtils.createKey("fancy_oak_bees");

    public static final ResourceKey<PlacedFeature> CHERRY_BEES_005 = PlacementUtils.createKey("cherry_bees_005");

    public static void bootstrap(BootstapContext<PlacedFeature> bootstapContextPlacedFeature0) {
        HolderGetter<ConfiguredFeature<?, ?>> $$1 = bootstapContextPlacedFeature0.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> $$2 = $$1.getOrThrow(TreeFeatures.CRIMSON_FUNGUS);
        Holder<ConfiguredFeature<?, ?>> $$3 = $$1.getOrThrow(TreeFeatures.WARPED_FUNGUS);
        Holder<ConfiguredFeature<?, ?>> $$4 = $$1.getOrThrow(TreeFeatures.OAK);
        Holder<ConfiguredFeature<?, ?>> $$5 = $$1.getOrThrow(TreeFeatures.DARK_OAK);
        Holder<ConfiguredFeature<?, ?>> $$6 = $$1.getOrThrow(TreeFeatures.BIRCH);
        Holder<ConfiguredFeature<?, ?>> $$7 = $$1.getOrThrow(TreeFeatures.ACACIA);
        Holder<ConfiguredFeature<?, ?>> $$8 = $$1.getOrThrow(TreeFeatures.SPRUCE);
        Holder<ConfiguredFeature<?, ?>> $$9 = $$1.getOrThrow(TreeFeatures.MANGROVE);
        Holder<ConfiguredFeature<?, ?>> $$10 = $$1.getOrThrow(TreeFeatures.CHERRY);
        Holder<ConfiguredFeature<?, ?>> $$11 = $$1.getOrThrow(TreeFeatures.PINE);
        Holder<ConfiguredFeature<?, ?>> $$12 = $$1.getOrThrow(TreeFeatures.JUNGLE_TREE);
        Holder<ConfiguredFeature<?, ?>> $$13 = $$1.getOrThrow(TreeFeatures.FANCY_OAK);
        Holder<ConfiguredFeature<?, ?>> $$14 = $$1.getOrThrow(TreeFeatures.MEGA_JUNGLE_TREE);
        Holder<ConfiguredFeature<?, ?>> $$15 = $$1.getOrThrow(TreeFeatures.MEGA_SPRUCE);
        Holder<ConfiguredFeature<?, ?>> $$16 = $$1.getOrThrow(TreeFeatures.MEGA_PINE);
        Holder<ConfiguredFeature<?, ?>> $$17 = $$1.getOrThrow(TreeFeatures.TALL_MANGROVE);
        Holder<ConfiguredFeature<?, ?>> $$18 = $$1.getOrThrow(TreeFeatures.JUNGLE_BUSH);
        Holder<ConfiguredFeature<?, ?>> $$19 = $$1.getOrThrow(TreeFeatures.SUPER_BIRCH_BEES_0002);
        Holder<ConfiguredFeature<?, ?>> $$20 = $$1.getOrThrow(TreeFeatures.SUPER_BIRCH_BEES);
        Holder<ConfiguredFeature<?, ?>> $$21 = $$1.getOrThrow(TreeFeatures.OAK_BEES_0002);
        Holder<ConfiguredFeature<?, ?>> $$22 = $$1.getOrThrow(TreeFeatures.OAK_BEES_002);
        Holder<ConfiguredFeature<?, ?>> $$23 = $$1.getOrThrow(TreeFeatures.BIRCH_BEES_0002);
        Holder<ConfiguredFeature<?, ?>> $$24 = $$1.getOrThrow(TreeFeatures.BIRCH_BEES_002);
        Holder<ConfiguredFeature<?, ?>> $$25 = $$1.getOrThrow(TreeFeatures.FANCY_OAK_BEES_0002);
        Holder<ConfiguredFeature<?, ?>> $$26 = $$1.getOrThrow(TreeFeatures.FANCY_OAK_BEES_002);
        Holder<ConfiguredFeature<?, ?>> $$27 = $$1.getOrThrow(TreeFeatures.FANCY_OAK_BEES);
        Holder<ConfiguredFeature<?, ?>> $$28 = $$1.getOrThrow(TreeFeatures.CHERRY_BEES_005);
        PlacementUtils.register(bootstapContextPlacedFeature0, CRIMSON_FUNGI, $$2, CountOnEveryLayerPlacement.of(8), BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, WARPED_FUNGI, $$3, CountOnEveryLayerPlacement.of(8), BiomeFilter.biome());
        PlacementUtils.register(bootstapContextPlacedFeature0, OAK_CHECKED, $$4, PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, DARK_OAK_CHECKED, $$5, PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, BIRCH_CHECKED, $$6, PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, ACACIA_CHECKED, $$7, PlacementUtils.filteredByBlockSurvival(Blocks.ACACIA_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, SPRUCE_CHECKED, $$8, PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, MANGROVE_CHECKED, $$9, PlacementUtils.filteredByBlockSurvival(Blocks.MANGROVE_PROPAGULE));
        PlacementUtils.register(bootstapContextPlacedFeature0, CHERRY_CHECKED, $$10, PlacementUtils.filteredByBlockSurvival(Blocks.CHERRY_SAPLING));
        BlockPredicate $$29 = BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW);
        List<PlacementModifier> $$30 = List.of(EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.not(BlockPredicate.matchesBlocks(Blocks.POWDER_SNOW)), 8), BlockPredicateFilter.forPredicate($$29));
        PlacementUtils.register(bootstapContextPlacedFeature0, PINE_ON_SNOW, $$11, $$30);
        PlacementUtils.register(bootstapContextPlacedFeature0, SPRUCE_ON_SNOW, $$8, $$30);
        PlacementUtils.register(bootstapContextPlacedFeature0, PINE_CHECKED, $$11, PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, JUNGLE_TREE_CHECKED, $$12, PlacementUtils.filteredByBlockSurvival(Blocks.JUNGLE_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, FANCY_OAK_CHECKED, $$13, PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, MEGA_JUNGLE_TREE_CHECKED, $$14, PlacementUtils.filteredByBlockSurvival(Blocks.JUNGLE_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, MEGA_SPRUCE_CHECKED, $$15, PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, MEGA_PINE_CHECKED, $$16, PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, TALL_MANGROVE_CHECKED, $$17, PlacementUtils.filteredByBlockSurvival(Blocks.MANGROVE_PROPAGULE));
        PlacementUtils.register(bootstapContextPlacedFeature0, JUNGLE_BUSH, $$18, PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, SUPER_BIRCH_BEES_0002, $$19, PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, SUPER_BIRCH_BEES, $$20, PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, OAK_BEES_0002, $$21, PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, OAK_BEES_002, $$22, PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, BIRCH_BEES_0002_PLACED, $$23, PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, BIRCH_BEES_002, $$24, PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, FANCY_OAK_BEES_0002, $$25, PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, FANCY_OAK_BEES_002, $$26, PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, FANCY_OAK_BEES, $$27, PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
        PlacementUtils.register(bootstapContextPlacedFeature0, CHERRY_BEES_005, $$28, PlacementUtils.filteredByBlockSurvival(Blocks.CHERRY_SAPLING));
    }
}