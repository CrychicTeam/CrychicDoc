package net.minecraft.world.level.levelgen.feature.featuresize;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class FeatureSizeType<P extends FeatureSize> {

    public static final FeatureSizeType<TwoLayersFeatureSize> TWO_LAYERS_FEATURE_SIZE = register("two_layers_feature_size", TwoLayersFeatureSize.CODEC);

    public static final FeatureSizeType<ThreeLayersFeatureSize> THREE_LAYERS_FEATURE_SIZE = register("three_layers_feature_size", ThreeLayersFeatureSize.CODEC);

    private final Codec<P> codec;

    private static <P extends FeatureSize> FeatureSizeType<P> register(String string0, Codec<P> codecP1) {
        return Registry.register(BuiltInRegistries.FEATURE_SIZE_TYPE, string0, new FeatureSizeType<>(codecP1));
    }

    private FeatureSizeType(Codec<P> codecP0) {
        this.codec = codecP0;
    }

    public Codec<P> codec() {
        return this.codec;
    }
}