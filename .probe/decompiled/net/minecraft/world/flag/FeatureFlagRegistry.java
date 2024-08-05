package net.minecraft.world.flag;

import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class FeatureFlagRegistry {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final FeatureFlagUniverse universe;

    private final Map<ResourceLocation, FeatureFlag> names;

    private final FeatureFlagSet allFlags;

    FeatureFlagRegistry(FeatureFlagUniverse featureFlagUniverse0, FeatureFlagSet featureFlagSet1, Map<ResourceLocation, FeatureFlag> mapResourceLocationFeatureFlag2) {
        this.universe = featureFlagUniverse0;
        this.names = mapResourceLocationFeatureFlag2;
        this.allFlags = featureFlagSet1;
    }

    public boolean isSubset(FeatureFlagSet featureFlagSet0) {
        return featureFlagSet0.isSubsetOf(this.allFlags);
    }

    public FeatureFlagSet allFlags() {
        return this.allFlags;
    }

    public FeatureFlagSet fromNames(Iterable<ResourceLocation> iterableResourceLocation0) {
        return this.fromNames(iterableResourceLocation0, p_251224_ -> LOGGER.warn("Unknown feature flag: {}", p_251224_));
    }

    public FeatureFlagSet subset(FeatureFlag... featureFlag0) {
        return FeatureFlagSet.create(this.universe, Arrays.asList(featureFlag0));
    }

    public FeatureFlagSet fromNames(Iterable<ResourceLocation> iterableResourceLocation0, Consumer<ResourceLocation> consumerResourceLocation1) {
        Set<FeatureFlag> $$2 = Sets.newIdentityHashSet();
        for (ResourceLocation $$3 : iterableResourceLocation0) {
            FeatureFlag $$4 = (FeatureFlag) this.names.get($$3);
            if ($$4 == null) {
                consumerResourceLocation1.accept($$3);
            } else {
                $$2.add($$4);
            }
        }
        return FeatureFlagSet.create(this.universe, $$2);
    }

    public Set<ResourceLocation> toNames(FeatureFlagSet featureFlagSet0) {
        Set<ResourceLocation> $$1 = new HashSet();
        this.names.forEach((p_252018_, p_250772_) -> {
            if (featureFlagSet0.contains(p_250772_)) {
                $$1.add(p_252018_);
            }
        });
        return $$1;
    }

    public Codec<FeatureFlagSet> codec() {
        return ResourceLocation.CODEC.listOf().comapFlatMap(p_275144_ -> {
            Set<ResourceLocation> $$1 = new HashSet();
            FeatureFlagSet $$2 = this.fromNames(p_275144_, $$1::add);
            return !$$1.isEmpty() ? DataResult.error(() -> "Unknown feature ids: " + $$1, $$2) : DataResult.success($$2);
        }, p_249796_ -> List.copyOf(this.toNames(p_249796_)));
    }

    public static class Builder {

        private final FeatureFlagUniverse universe;

        private int id;

        private final Map<ResourceLocation, FeatureFlag> flags = new LinkedHashMap();

        public Builder(String string0) {
            this.universe = new FeatureFlagUniverse(string0);
        }

        public FeatureFlag createVanilla(String string0) {
            return this.create(new ResourceLocation("minecraft", string0));
        }

        public FeatureFlag create(ResourceLocation resourceLocation0) {
            if (this.id >= 64) {
                throw new IllegalStateException("Too many feature flags");
            } else {
                FeatureFlag $$1 = new FeatureFlag(this.universe, this.id++);
                FeatureFlag $$2 = (FeatureFlag) this.flags.put(resourceLocation0, $$1);
                if ($$2 != null) {
                    throw new IllegalStateException("Duplicate feature flag " + resourceLocation0);
                } else {
                    return $$1;
                }
            }
        }

        public FeatureFlagRegistry build() {
            FeatureFlagSet $$0 = FeatureFlagSet.create(this.universe, this.flags.values());
            return new FeatureFlagRegistry(this.universe, $$0, Map.copyOf(this.flags));
        }
    }
}