package com.simibubi.create.infrastructure.worldgen;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class AllConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> ZINC_ORE = key("zinc_ore");

    public static final ResourceKey<ConfiguredFeature<?, ?>> STRIATED_ORES_OVERWORLD = key("striated_ores_overworld");

    public static final ResourceKey<ConfiguredFeature<?, ?>> STRIATED_ORES_NETHER = key("striated_ores_nether");

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Create.asResource(name));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
        RuleTest stoneOreReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        List<OreConfiguration.TargetBlockState> zincTargetStates = List.of(OreConfiguration.target(stoneOreReplaceables, ((Block) AllBlocks.ZINC_ORE.get()).defaultBlockState()), OreConfiguration.target(deepslateOreReplaceables, ((Block) AllBlocks.DEEPSLATE_ZINC_ORE.get()).defaultBlockState()));
        FeatureUtils.register(ctx, ZINC_ORE, Feature.ORE, new OreConfiguration(zincTargetStates, 12));
        List<LayerPattern> overworldLayerPatterns = List.of((LayerPattern) AllLayerPatterns.SCORIA.get(), (LayerPattern) AllLayerPatterns.CINNABAR.get(), (LayerPattern) AllLayerPatterns.MAGNETITE.get(), (LayerPattern) AllLayerPatterns.MALACHITE.get(), (LayerPattern) AllLayerPatterns.LIMESTONE.get(), (LayerPattern) AllLayerPatterns.OCHRESTONE.get());
        FeatureUtils.register(ctx, STRIATED_ORES_OVERWORLD, AllFeatures.LAYERED_ORE.get(), new LayeredOreConfiguration(overworldLayerPatterns, 32, 0.0F));
        List<LayerPattern> netherLayerPatterns = List.of((LayerPattern) AllLayerPatterns.SCORIA_NETHER.get(), (LayerPattern) AllLayerPatterns.SCORCHIA_NETHER.get());
        FeatureUtils.register(ctx, STRIATED_ORES_NETHER, AllFeatures.LAYERED_ORE.get(), new LayeredOreConfiguration(netherLayerPatterns, 32, 0.0F));
    }
}