package net.minecraft.world.flag;

import it.unimi.dsi.fastutil.HashCommon;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Nullable;

public final class FeatureFlagSet {

    private static final FeatureFlagSet EMPTY = new FeatureFlagSet(null, 0L);

    public static final int MAX_CONTAINER_SIZE = 64;

    @Nullable
    private final FeatureFlagUniverse universe;

    private final long mask;

    private FeatureFlagSet(@Nullable FeatureFlagUniverse featureFlagUniverse0, long long1) {
        this.universe = featureFlagUniverse0;
        this.mask = long1;
    }

    static FeatureFlagSet create(FeatureFlagUniverse featureFlagUniverse0, Collection<FeatureFlag> collectionFeatureFlag1) {
        if (collectionFeatureFlag1.isEmpty()) {
            return EMPTY;
        } else {
            long $$2 = computeMask(featureFlagUniverse0, 0L, collectionFeatureFlag1);
            return new FeatureFlagSet(featureFlagUniverse0, $$2);
        }
    }

    public static FeatureFlagSet of() {
        return EMPTY;
    }

    public static FeatureFlagSet of(FeatureFlag featureFlag0) {
        return new FeatureFlagSet(featureFlag0.universe, featureFlag0.mask);
    }

    public static FeatureFlagSet of(FeatureFlag featureFlag0, FeatureFlag... featureFlag1) {
        long $$2 = featureFlag1.length == 0 ? featureFlag0.mask : computeMask(featureFlag0.universe, featureFlag0.mask, Arrays.asList(featureFlag1));
        return new FeatureFlagSet(featureFlag0.universe, $$2);
    }

    private static long computeMask(FeatureFlagUniverse featureFlagUniverse0, long long1, Iterable<FeatureFlag> iterableFeatureFlag2) {
        for (FeatureFlag $$3 : iterableFeatureFlag2) {
            if (featureFlagUniverse0 != $$3.universe) {
                throw new IllegalStateException("Mismatched feature universe, expected '" + featureFlagUniverse0 + "', but got '" + $$3.universe + "'");
            }
            long1 |= $$3.mask;
        }
        return long1;
    }

    public boolean contains(FeatureFlag featureFlag0) {
        return this.universe != featureFlag0.universe ? false : (this.mask & featureFlag0.mask) != 0L;
    }

    public boolean isSubsetOf(FeatureFlagSet featureFlagSet0) {
        if (this.universe == null) {
            return true;
        } else {
            return this.universe != featureFlagSet0.universe ? false : (this.mask & ~featureFlagSet0.mask) == 0L;
        }
    }

    public FeatureFlagSet join(FeatureFlagSet featureFlagSet0) {
        if (this.universe == null) {
            return featureFlagSet0;
        } else if (featureFlagSet0.universe == null) {
            return this;
        } else if (this.universe != featureFlagSet0.universe) {
            throw new IllegalArgumentException("Mismatched set elements: '" + this.universe + "' != '" + featureFlagSet0.universe + "'");
        } else {
            return new FeatureFlagSet(this.universe, this.mask | featureFlagSet0.mask);
        }
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            if (object0 instanceof FeatureFlagSet $$1 && this.universe == $$1.universe && this.mask == $$1.mask) {
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        return (int) HashCommon.mix(this.mask);
    }
}