package org.violetmoon.zeta.world;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

public class DeferredFeature extends Feature<NoneFeatureConfiguration> {

    private final GenerationStep.Decoration stage;

    public DeferredFeature(GenerationStep.Decoration stage) {
        super(NoneFeatureConfiguration.CODEC);
        this.stage = stage;
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenHandler.generateChunk(context, this.stage);
        return false;
    }
}