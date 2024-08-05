package net.minecraft.world.level.block.grower;

import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class SpruceTreeGrower extends AbstractMegaTreeGrower {

    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource0, boolean boolean1) {
        return TreeFeatures.SPRUCE;
    }

    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource randomSource0) {
        return randomSource0.nextBoolean() ? TreeFeatures.MEGA_SPRUCE : TreeFeatures.MEGA_PINE;
    }
}