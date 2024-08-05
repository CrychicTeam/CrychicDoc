package net.minecraft.data.worldgen.features;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.DualNoiseProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseThresholdProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.material.Fluids;

public class VegetationFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> BAMBOO_NO_PODZOL = FeatureUtils.createKey("bamboo_no_podzol");

    public static final ResourceKey<ConfiguredFeature<?, ?>> BAMBOO_SOME_PODZOL = FeatureUtils.createKey("bamboo_some_podzol");

    public static final ResourceKey<ConfiguredFeature<?, ?>> VINES = FeatureUtils.createKey("vines");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BROWN_MUSHROOM = FeatureUtils.createKey("patch_brown_mushroom");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_RED_MUSHROOM = FeatureUtils.createKey("patch_red_mushroom");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SUNFLOWER = FeatureUtils.createKey("patch_sunflower");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_PUMPKIN = FeatureUtils.createKey("patch_pumpkin");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BERRY_BUSH = FeatureUtils.createKey("patch_berry_bush");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_TAIGA_GRASS = FeatureUtils.createKey("patch_taiga_grass");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GRASS = FeatureUtils.createKey("patch_grass");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GRASS_JUNGLE = FeatureUtils.createKey("patch_grass_jungle");

    public static final ResourceKey<ConfiguredFeature<?, ?>> SINGLE_PIECE_OF_GRASS = FeatureUtils.createKey("single_piece_of_grass");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DEAD_BUSH = FeatureUtils.createKey("patch_dead_bush");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_MELON = FeatureUtils.createKey("patch_melon");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_WATERLILY = FeatureUtils.createKey("patch_waterlily");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_TALL_GRASS = FeatureUtils.createKey("patch_tall_grass");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_LARGE_FERN = FeatureUtils.createKey("patch_large_fern");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CACTUS = FeatureUtils.createKey("patch_cactus");

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SUGAR_CANE = FeatureUtils.createKey("patch_sugar_cane");

    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_DEFAULT = FeatureUtils.createKey("flower_default");

    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_FLOWER_FOREST = FeatureUtils.createKey("flower_flower_forest");

    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_SWAMP = FeatureUtils.createKey("flower_swamp");

    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PLAIN = FeatureUtils.createKey("flower_plain");

    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_MEADOW = FeatureUtils.createKey("flower_meadow");

    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_CHERRY = FeatureUtils.createKey("flower_cherry");

    public static final ResourceKey<ConfiguredFeature<?, ?>> FOREST_FLOWERS = FeatureUtils.createKey("forest_flowers");

    public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_FOREST_VEGETATION = FeatureUtils.createKey("dark_forest_vegetation");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_FLOWER_FOREST = FeatureUtils.createKey("trees_flower_forest");

    public static final ResourceKey<ConfiguredFeature<?, ?>> MEADOW_TREES = FeatureUtils.createKey("meadow_trees");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_TAIGA = FeatureUtils.createKey("trees_taiga");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_GROVE = FeatureUtils.createKey("trees_grove");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_SAVANNA = FeatureUtils.createKey("trees_savanna");

    public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_TALL = FeatureUtils.createKey("birch_tall");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_WINDSWEPT_HILLS = FeatureUtils.createKey("trees_windswept_hills");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_WATER = FeatureUtils.createKey("trees_water");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BIRCH_AND_OAK = FeatureUtils.createKey("trees_birch_and_oak");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PLAINS = FeatureUtils.createKey("trees_plains");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_SPARSE_JUNGLE = FeatureUtils.createKey("trees_sparse_jungle");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_OLD_GROWTH_SPRUCE_TAIGA = FeatureUtils.createKey("trees_old_growth_spruce_taiga");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_OLD_GROWTH_PINE_TAIGA = FeatureUtils.createKey("trees_old_growth_pine_taiga");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_JUNGLE = FeatureUtils.createKey("trees_jungle");

    public static final ResourceKey<ConfiguredFeature<?, ?>> BAMBOO_VEGETATION = FeatureUtils.createKey("bamboo_vegetation");

    public static final ResourceKey<ConfiguredFeature<?, ?>> MUSHROOM_ISLAND_VEGETATION = FeatureUtils.createKey("mushroom_island_vegetation");

    public static final ResourceKey<ConfiguredFeature<?, ?>> MANGROVE_VEGETATION = FeatureUtils.createKey("mangrove_vegetation");

    private static RandomPatchConfiguration grassPatch(BlockStateProvider blockStateProvider0, int int1) {
        return FeatureUtils.simpleRandomPatchConfiguration(int1, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(blockStateProvider0)));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> bootstapContextConfiguredFeature0) {
        HolderGetter<ConfiguredFeature<?, ?>> $$1 = bootstapContextConfiguredFeature0.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> $$2 = $$1.getOrThrow(TreeFeatures.HUGE_BROWN_MUSHROOM);
        Holder<ConfiguredFeature<?, ?>> $$3 = $$1.getOrThrow(TreeFeatures.HUGE_RED_MUSHROOM);
        Holder<ConfiguredFeature<?, ?>> $$4 = $$1.getOrThrow(TreeFeatures.FANCY_OAK_BEES_005);
        Holder<ConfiguredFeature<?, ?>> $$5 = $$1.getOrThrow(TreeFeatures.OAK_BEES_005);
        Holder<ConfiguredFeature<?, ?>> $$6 = $$1.getOrThrow(PATCH_GRASS_JUNGLE);
        HolderGetter<PlacedFeature> $$7 = bootstapContextConfiguredFeature0.lookup(Registries.PLACED_FEATURE);
        Holder<PlacedFeature> $$8 = $$7.getOrThrow(TreePlacements.DARK_OAK_CHECKED);
        Holder<PlacedFeature> $$9 = $$7.getOrThrow(TreePlacements.BIRCH_CHECKED);
        Holder<PlacedFeature> $$10 = $$7.getOrThrow(TreePlacements.FANCY_OAK_CHECKED);
        Holder<PlacedFeature> $$11 = $$7.getOrThrow(TreePlacements.BIRCH_BEES_002);
        Holder<PlacedFeature> $$12 = $$7.getOrThrow(TreePlacements.FANCY_OAK_BEES_002);
        Holder<PlacedFeature> $$13 = $$7.getOrThrow(TreePlacements.FANCY_OAK_BEES);
        Holder<PlacedFeature> $$14 = $$7.getOrThrow(TreePlacements.PINE_CHECKED);
        Holder<PlacedFeature> $$15 = $$7.getOrThrow(TreePlacements.SPRUCE_CHECKED);
        Holder<PlacedFeature> $$16 = $$7.getOrThrow(TreePlacements.PINE_ON_SNOW);
        Holder<PlacedFeature> $$17 = $$7.getOrThrow(TreePlacements.ACACIA_CHECKED);
        Holder<PlacedFeature> $$18 = $$7.getOrThrow(TreePlacements.SUPER_BIRCH_BEES_0002);
        Holder<PlacedFeature> $$19 = $$7.getOrThrow(TreePlacements.BIRCH_BEES_0002_PLACED);
        Holder<PlacedFeature> $$20 = $$7.getOrThrow(TreePlacements.FANCY_OAK_BEES_0002);
        Holder<PlacedFeature> $$21 = $$7.getOrThrow(TreePlacements.JUNGLE_BUSH);
        Holder<PlacedFeature> $$22 = $$7.getOrThrow(TreePlacements.MEGA_SPRUCE_CHECKED);
        Holder<PlacedFeature> $$23 = $$7.getOrThrow(TreePlacements.MEGA_PINE_CHECKED);
        Holder<PlacedFeature> $$24 = $$7.getOrThrow(TreePlacements.MEGA_JUNGLE_TREE_CHECKED);
        Holder<PlacedFeature> $$25 = $$7.getOrThrow(TreePlacements.TALL_MANGROVE_CHECKED);
        Holder<PlacedFeature> $$26 = $$7.getOrThrow(TreePlacements.OAK_CHECKED);
        Holder<PlacedFeature> $$27 = $$7.getOrThrow(TreePlacements.OAK_BEES_002);
        Holder<PlacedFeature> $$28 = $$7.getOrThrow(TreePlacements.SUPER_BIRCH_BEES);
        Holder<PlacedFeature> $$29 = $$7.getOrThrow(TreePlacements.SPRUCE_ON_SNOW);
        Holder<PlacedFeature> $$30 = $$7.getOrThrow(TreePlacements.OAK_BEES_0002);
        Holder<PlacedFeature> $$31 = $$7.getOrThrow(TreePlacements.JUNGLE_TREE_CHECKED);
        Holder<PlacedFeature> $$32 = $$7.getOrThrow(TreePlacements.MANGROVE_CHECKED);
        FeatureUtils.register(bootstapContextConfiguredFeature0, BAMBOO_NO_PODZOL, Feature.BAMBOO, new ProbabilityFeatureConfiguration(0.0F));
        FeatureUtils.register(bootstapContextConfiguredFeature0, BAMBOO_SOME_PODZOL, Feature.BAMBOO, new ProbabilityFeatureConfiguration(0.2F));
        FeatureUtils.register(bootstapContextConfiguredFeature0, VINES, Feature.VINES);
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_BROWN_MUSHROOM, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.BROWN_MUSHROOM))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_RED_MUSHROOM, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.RED_MUSHROOM))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_SUNFLOWER, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SUNFLOWER))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_PUMPKIN, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.PUMPKIN)), List.of(Blocks.GRASS_BLOCK)));
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_BERRY_BUSH, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple((BlockState) Blocks.SWEET_BERRY_BUSH.defaultBlockState().m_61124_(SweetBerryBushBlock.AGE, 3))), List.of(Blocks.GRASS_BLOCK)));
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_TAIGA_GRASS, Feature.RANDOM_PATCH, grassPatch(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(Blocks.GRASS.defaultBlockState(), 1).add(Blocks.FERN.defaultBlockState(), 4)), 32));
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_GRASS, Feature.RANDOM_PATCH, grassPatch(BlockStateProvider.simple(Blocks.GRASS), 32));
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_GRASS_JUNGLE, Feature.RANDOM_PATCH, new RandomPatchConfiguration(32, 7, 3, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(Blocks.GRASS.defaultBlockState(), 3).add(Blocks.FERN.defaultBlockState(), 1))), BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.not(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.PODZOL))))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, SINGLE_PIECE_OF_GRASS, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.GRASS.defaultBlockState())));
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_DEAD_BUSH, Feature.RANDOM_PATCH, grassPatch(BlockStateProvider.simple(Blocks.DEAD_BUSH), 4));
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_MELON, Feature.RANDOM_PATCH, new RandomPatchConfiguration(64, 7, 3, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.MELON)), BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.noFluid(), BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK)))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_WATERLILY, Feature.RANDOM_PATCH, new RandomPatchConfiguration(10, 7, 3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.LILY_PAD)))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_TALL_GRASS, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.TALL_GRASS))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_LARGE_FERN, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.LARGE_FERN))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_CACTUS, Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(10, PlacementUtils.inlinePlaced(Feature.BLOCK_COLUMN, BlockColumnConfiguration.simple(BiasedToBottomInt.of(1, 3), BlockStateProvider.simple(Blocks.CACTUS)), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.wouldSurvive(Blocks.CACTUS.defaultBlockState(), BlockPos.ZERO))))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, PATCH_SUGAR_CANE, Feature.RANDOM_PATCH, new RandomPatchConfiguration(20, 4, 0, PlacementUtils.inlinePlaced(Feature.BLOCK_COLUMN, BlockColumnConfiguration.simple(BiasedToBottomInt.of(2, 4), BlockStateProvider.simple(Blocks.SUGAR_CANE)), BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.wouldSurvive(Blocks.SUGAR_CANE.defaultBlockState(), BlockPos.ZERO), BlockPredicate.anyOf(BlockPredicate.matchesFluids(new BlockPos(1, -1, 0), Fluids.WATER, Fluids.FLOWING_WATER), BlockPredicate.matchesFluids(new BlockPos(-1, -1, 0), Fluids.WATER, Fluids.FLOWING_WATER), BlockPredicate.matchesFluids(new BlockPos(0, -1, 1), Fluids.WATER, Fluids.FLOWING_WATER), BlockPredicate.matchesFluids(new BlockPos(0, -1, -1), Fluids.WATER, Fluids.FLOWING_WATER)))))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, FLOWER_DEFAULT, Feature.FLOWER, grassPatch(new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(Blocks.POPPY.defaultBlockState(), 2).add(Blocks.DANDELION.defaultBlockState(), 1)), 64));
        FeatureUtils.register(bootstapContextConfiguredFeature0, FLOWER_FLOWER_FOREST, Feature.FLOWER, new RandomPatchConfiguration(96, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new NoiseProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0), 0.020833334F, List.of(Blocks.DANDELION.defaultBlockState(), Blocks.POPPY.defaultBlockState(), Blocks.ALLIUM.defaultBlockState(), Blocks.AZURE_BLUET.defaultBlockState(), Blocks.RED_TULIP.defaultBlockState(), Blocks.ORANGE_TULIP.defaultBlockState(), Blocks.WHITE_TULIP.defaultBlockState(), Blocks.PINK_TULIP.defaultBlockState(), Blocks.OXEYE_DAISY.defaultBlockState(), Blocks.CORNFLOWER.defaultBlockState(), Blocks.LILY_OF_THE_VALLEY.defaultBlockState()))))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, FLOWER_SWAMP, Feature.FLOWER, new RandomPatchConfiguration(64, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.BLUE_ORCHID)))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, FLOWER_PLAIN, Feature.FLOWER, new RandomPatchConfiguration(64, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0), 0.005F, -0.8F, 0.33333334F, Blocks.DANDELION.defaultBlockState(), List.of(Blocks.ORANGE_TULIP.defaultBlockState(), Blocks.RED_TULIP.defaultBlockState(), Blocks.PINK_TULIP.defaultBlockState(), Blocks.WHITE_TULIP.defaultBlockState()), List.of(Blocks.POPPY.defaultBlockState(), Blocks.AZURE_BLUET.defaultBlockState(), Blocks.OXEYE_DAISY.defaultBlockState(), Blocks.CORNFLOWER.defaultBlockState()))))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, FLOWER_MEADOW, Feature.FLOWER, new RandomPatchConfiguration(96, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new DualNoiseProvider(new InclusiveRange(1, 3), new NormalNoise.NoiseParameters(-10, 1.0), 1.0F, 2345L, new NormalNoise.NoiseParameters(-3, 1.0), 1.0F, List.of(Blocks.TALL_GRASS.defaultBlockState(), Blocks.ALLIUM.defaultBlockState(), Blocks.POPPY.defaultBlockState(), Blocks.AZURE_BLUET.defaultBlockState(), Blocks.DANDELION.defaultBlockState(), Blocks.CORNFLOWER.defaultBlockState(), Blocks.OXEYE_DAISY.defaultBlockState(), Blocks.GRASS.defaultBlockState()))))));
        SimpleWeightedRandomList.Builder<BlockState> $$33 = SimpleWeightedRandomList.builder();
        for (int $$34 = 1; $$34 <= 4; $$34++) {
            for (Direction $$35 : Direction.Plane.HORIZONTAL) {
                $$33.add((BlockState) ((BlockState) Blocks.PINK_PETALS.defaultBlockState().m_61124_(PinkPetalsBlock.AMOUNT, $$34)).m_61124_(PinkPetalsBlock.FACING, $$35), 1);
            }
        }
        FeatureUtils.register(bootstapContextConfiguredFeature0, FLOWER_CHERRY, Feature.FLOWER, new RandomPatchConfiguration(96, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider($$33)))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, FOREST_FLOWERS, Feature.SIMPLE_RANDOM_SELECTOR, new SimpleRandomFeatureConfiguration(HolderSet.direct(PlacementUtils.inlinePlaced(Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.LILAC)))), PlacementUtils.inlinePlaced(Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.ROSE_BUSH)))), PlacementUtils.inlinePlaced(Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.PEONY)))), PlacementUtils.inlinePlaced(Feature.NO_BONEMEAL_FLOWER, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.LILY_OF_THE_VALLEY)))))));
        FeatureUtils.register(bootstapContextConfiguredFeature0, DARK_FOREST_VEGETATION, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced($$2), 0.025F), new WeightedPlacedFeature(PlacementUtils.inlinePlaced($$3), 0.05F), new WeightedPlacedFeature($$8, 0.6666667F), new WeightedPlacedFeature($$9, 0.2F), new WeightedPlacedFeature($$10, 0.1F)), $$26));
        FeatureUtils.register(bootstapContextConfiguredFeature0, TREES_FLOWER_FOREST, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$11, 0.2F), new WeightedPlacedFeature($$12, 0.1F)), $$27));
        FeatureUtils.register(bootstapContextConfiguredFeature0, MEADOW_TREES, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$13, 0.5F)), $$28));
        FeatureUtils.register(bootstapContextConfiguredFeature0, TREES_TAIGA, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$14, 0.33333334F)), $$15));
        FeatureUtils.register(bootstapContextConfiguredFeature0, TREES_GROVE, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$16, 0.33333334F)), $$29));
        FeatureUtils.register(bootstapContextConfiguredFeature0, TREES_SAVANNA, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$17, 0.8F)), $$26));
        FeatureUtils.register(bootstapContextConfiguredFeature0, BIRCH_TALL, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$18, 0.5F)), $$19));
        FeatureUtils.register(bootstapContextConfiguredFeature0, TREES_WINDSWEPT_HILLS, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$15, 0.666F), new WeightedPlacedFeature($$10, 0.1F)), $$26));
        FeatureUtils.register(bootstapContextConfiguredFeature0, TREES_WATER, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$10, 0.1F)), $$26));
        FeatureUtils.register(bootstapContextConfiguredFeature0, TREES_BIRCH_AND_OAK, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$19, 0.2F), new WeightedPlacedFeature($$20, 0.1F)), $$30));
        FeatureUtils.register(bootstapContextConfiguredFeature0, TREES_PLAINS, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(PlacementUtils.inlinePlaced($$4), 0.33333334F)), PlacementUtils.inlinePlaced($$5)));
        FeatureUtils.register(bootstapContextConfiguredFeature0, TREES_SPARSE_JUNGLE, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$10, 0.1F), new WeightedPlacedFeature($$21, 0.5F)), $$31));
        FeatureUtils.register(bootstapContextConfiguredFeature0, TREES_OLD_GROWTH_SPRUCE_TAIGA, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$22, 0.33333334F), new WeightedPlacedFeature($$14, 0.33333334F)), $$15));
        FeatureUtils.register(bootstapContextConfiguredFeature0, TREES_OLD_GROWTH_PINE_TAIGA, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$22, 0.025641026F), new WeightedPlacedFeature($$23, 0.30769232F), new WeightedPlacedFeature($$14, 0.33333334F)), $$15));
        FeatureUtils.register(bootstapContextConfiguredFeature0, TREES_JUNGLE, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$10, 0.1F), new WeightedPlacedFeature($$21, 0.5F), new WeightedPlacedFeature($$24, 0.33333334F)), $$31));
        FeatureUtils.register(bootstapContextConfiguredFeature0, BAMBOO_VEGETATION, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$10, 0.05F), new WeightedPlacedFeature($$21, 0.15F), new WeightedPlacedFeature($$24, 0.7F)), PlacementUtils.inlinePlaced($$6)));
        FeatureUtils.register(bootstapContextConfiguredFeature0, MUSHROOM_ISLAND_VEGETATION, Feature.RANDOM_BOOLEAN_SELECTOR, new RandomBooleanFeatureConfiguration(PlacementUtils.inlinePlaced($$3), PlacementUtils.inlinePlaced($$2)));
        FeatureUtils.register(bootstapContextConfiguredFeature0, MANGROVE_VEGETATION, Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature($$25, 0.85F)), $$32));
    }
}