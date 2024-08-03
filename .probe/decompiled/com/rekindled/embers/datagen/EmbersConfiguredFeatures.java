package com.rekindled.embers.datagen;

import com.rekindled.embers.RegistryManager;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class EmbersConfiguredFeatures {

    public static final RuleTest STONE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);

    public static final RuleTest DEEPSLATE_ORE_REPLACEABLES = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

    public static final Supplier<List<OreConfiguration.TargetBlockState>> ORE_LEAD_TARGET_LIST = () -> List.of(OreConfiguration.target(STONE_ORE_REPLACEABLES, RegistryManager.LEAD_ORE.get().defaultBlockState()), OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, RegistryManager.DEEPSLATE_LEAD_ORE.get().defaultBlockState()));

    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LEAD_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("embers", "ore_lead"));

    public static final ConfiguredFeature<OreConfiguration, ?> ORE_LEAD = new ConfiguredFeature<>(Feature.ORE, new OreConfiguration((List<OreConfiguration.TargetBlockState>) ORE_LEAD_TARGET_LIST.get(), 8));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> ORE_SILVER_TARGET_LIST = () -> List.of(OreConfiguration.target(STONE_ORE_REPLACEABLES, RegistryManager.SILVER_ORE.get().defaultBlockState()), OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLES, RegistryManager.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));

    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_SILVER_KEY = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("embers", "ore_silver"));

    public static final ConfiguredFeature<OreConfiguration, ?> ORE_SILVER = new ConfiguredFeature<>(Feature.ORE, new OreConfiguration((List<OreConfiguration.TargetBlockState>) ORE_SILVER_TARGET_LIST.get(), 6, 0.25F));

    public static void generate(BootstapContext<ConfiguredFeature<?, ?>> bootstrap) {
        bootstrap.register(ORE_LEAD_KEY, ORE_LEAD);
        bootstrap.register(ORE_SILVER_KEY, ORE_SILVER);
    }
}