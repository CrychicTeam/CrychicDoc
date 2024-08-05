package org.violetmoon.zeta.world;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class PassthroughTreeGrower extends AbstractTreeGrower {

    protected final ResourceKey<ConfiguredFeature<?, ?>> key;

    public PassthroughTreeGrower(ResourceKey<ConfiguredFeature<?, ?>> key) {
        this.key = key;
    }

    @Nullable
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean flowers) {
        return this.key;
    }

    public static class Mega extends AbstractMegaTreeGrower {

        protected final ResourceKey<ConfiguredFeature<?, ?>> key;

        protected final ResourceKey<ConfiguredFeature<?, ?>> megaKey;

        public Mega(ResourceKey<ConfiguredFeature<?, ?>> key, ResourceKey<ConfiguredFeature<?, ?>> megaKey) {
            this.key = key;
            this.megaKey = megaKey;
        }

        @Nullable
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource0, boolean boolean1) {
            return this.key;
        }

        @Nullable
        @Override
        protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource randomSource0) {
            return this.megaKey;
        }
    }
}