package net.minecraft.world.flag;

import com.mojang.serialization.Codec;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;

public class FeatureFlags {

    public static final FeatureFlag VANILLA;

    public static final FeatureFlag BUNDLE;

    public static final FeatureFlagRegistry REGISTRY;

    public static final Codec<FeatureFlagSet> CODEC;

    public static final FeatureFlagSet VANILLA_SET;

    public static final FeatureFlagSet DEFAULT_FLAGS;

    public static String printMissingFlags(FeatureFlagSet featureFlagSet0, FeatureFlagSet featureFlagSet1) {
        return printMissingFlags(REGISTRY, featureFlagSet0, featureFlagSet1);
    }

    public static String printMissingFlags(FeatureFlagRegistry featureFlagRegistry0, FeatureFlagSet featureFlagSet1, FeatureFlagSet featureFlagSet2) {
        Set<ResourceLocation> $$3 = featureFlagRegistry0.toNames(featureFlagSet2);
        Set<ResourceLocation> $$4 = featureFlagRegistry0.toNames(featureFlagSet1);
        return (String) $$3.stream().filter(p_251831_ -> !$$4.contains(p_251831_)).map(ResourceLocation::toString).collect(Collectors.joining(", "));
    }

    public static boolean isExperimental(FeatureFlagSet featureFlagSet0) {
        return !featureFlagSet0.isSubsetOf(VANILLA_SET);
    }

    static {
        FeatureFlagRegistry.Builder $$0 = new FeatureFlagRegistry.Builder("main");
        VANILLA = $$0.createVanilla("vanilla");
        BUNDLE = $$0.createVanilla("bundle");
        REGISTRY = $$0.build();
        CODEC = REGISTRY.codec();
        VANILLA_SET = FeatureFlagSet.of(VANILLA);
        DEFAULT_FLAGS = VANILLA_SET;
    }
}