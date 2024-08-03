package net.minecraft.world.flag;

public class FeatureFlag {

    final FeatureFlagUniverse universe;

    final long mask;

    FeatureFlag(FeatureFlagUniverse featureFlagUniverse0, int int1) {
        this.universe = featureFlagUniverse0;
        this.mask = 1L << int1;
    }
}