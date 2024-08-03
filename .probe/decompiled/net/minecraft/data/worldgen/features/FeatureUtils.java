package net.minecraft.data.worldgen.features;

import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class FeatureUtils {

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> bootstapContextConfiguredFeature0) {
        AquaticFeatures.bootstrap(bootstapContextConfiguredFeature0);
        CaveFeatures.bootstrap(bootstapContextConfiguredFeature0);
        EndFeatures.bootstrap(bootstapContextConfiguredFeature0);
        MiscOverworldFeatures.bootstrap(bootstapContextConfiguredFeature0);
        NetherFeatures.bootstrap(bootstapContextConfiguredFeature0);
        OreFeatures.bootstrap(bootstapContextConfiguredFeature0);
        PileFeatures.bootstrap(bootstapContextConfiguredFeature0);
        TreeFeatures.bootstrap(bootstapContextConfiguredFeature0);
        VegetationFeatures.bootstrap(bootstapContextConfiguredFeature0);
    }

    private static BlockPredicate simplePatchPredicate(List<Block> listBlock0) {
        BlockPredicate $$1;
        if (!listBlock0.isEmpty()) {
            $$1 = BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), listBlock0));
        } else {
            $$1 = BlockPredicate.ONLY_IN_AIR_PREDICATE;
        }
        return $$1;
    }

    public static RandomPatchConfiguration simpleRandomPatchConfiguration(int int0, Holder<PlacedFeature> holderPlacedFeature1) {
        return new RandomPatchConfiguration(int0, 7, 3, holderPlacedFeature1);
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F f0, FC fC1, List<Block> listBlock2, int int3) {
        return simpleRandomPatchConfiguration(int3, PlacementUtils.filtered(f0, fC1, simplePatchPredicate(listBlock2)));
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F f0, FC fC1, List<Block> listBlock2) {
        return simplePatchConfiguration(f0, fC1, listBlock2, 96);
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> RandomPatchConfiguration simplePatchConfiguration(F f0, FC fC1) {
        return simplePatchConfiguration(f0, fC1, List.of(), 96);
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String string0) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(string0));
    }

    public static void register(BootstapContext<ConfiguredFeature<?, ?>> bootstapContextConfiguredFeature0, ResourceKey<ConfiguredFeature<?, ?>> resourceKeyConfiguredFeature1, Feature<NoneFeatureConfiguration> featureNoneFeatureConfiguration2) {
        register(bootstapContextConfiguredFeature0, resourceKeyConfiguredFeature1, featureNoneFeatureConfiguration2, FeatureConfiguration.NONE);
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> bootstapContextConfiguredFeature0, ResourceKey<ConfiguredFeature<?, ?>> resourceKeyConfiguredFeature1, F f2, FC fC3) {
        bootstapContextConfiguredFeature0.register(resourceKeyConfiguredFeature1, new ConfiguredFeature(f2, fC3));
    }
}