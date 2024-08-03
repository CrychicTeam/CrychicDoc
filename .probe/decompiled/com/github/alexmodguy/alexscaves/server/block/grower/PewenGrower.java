package com.github.alexmodguy.alexscaves.server.block.grower;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class PewenGrower extends AbstractTreeGrower {

    public static final ResourceKey<ConfiguredFeature<?, ?>> PEWEN_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("alexscaves", "pewen_tree"));

    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean b) {
        return PEWEN_TREE;
    }
}