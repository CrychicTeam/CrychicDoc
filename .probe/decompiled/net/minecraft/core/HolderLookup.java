package net.minecraft.core;

import com.mojang.serialization.Lifecycle;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlagSet;

public interface HolderLookup<T> extends HolderGetter<T> {

    Stream<Holder.Reference<T>> listElements();

    default Stream<ResourceKey<T>> listElementIds() {
        return this.listElements().map(Holder.Reference::m_205785_);
    }

    Stream<HolderSet.Named<T>> listTags();

    default Stream<TagKey<T>> listTagIds() {
        return this.listTags().map(HolderSet.Named::m_205839_);
    }

    default HolderLookup<T> filterElements(final Predicate<T> predicateT0) {
        return new HolderLookup.Delegate<T>(this) {

            @Override
            public Optional<Holder.Reference<T>> get(ResourceKey<T> p_255836_) {
                return this.f_254653_.m_254902_(p_255836_).filter(p_256496_ -> predicateT0.test(p_256496_.value()));
            }

            @Override
            public Stream<Holder.Reference<T>> listElements() {
                return this.f_254653_.listElements().filter(p_255794_ -> predicateT0.test(p_255794_.value()));
            }
        };
    }

    public static class Delegate<T> implements HolderLookup<T> {

        protected final HolderLookup<T> parent;

        public Delegate(HolderLookup<T> holderLookupT0) {
            this.parent = holderLookupT0;
        }

        @Override
        public Optional<Holder.Reference<T>> get(ResourceKey<T> resourceKeyT0) {
            return this.parent.m_254902_(resourceKeyT0);
        }

        @Override
        public Stream<Holder.Reference<T>> listElements() {
            return this.parent.listElements();
        }

        @Override
        public Optional<HolderSet.Named<T>> get(TagKey<T> tagKeyT0) {
            return this.parent.m_254901_(tagKeyT0);
        }

        @Override
        public Stream<HolderSet.Named<T>> listTags() {
            return this.parent.listTags();
        }
    }

    public interface Provider {

        <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> var1);

        default <T> HolderLookup.RegistryLookup<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> resourceKeyExtendsRegistryExtendsT0) {
            return (HolderLookup.RegistryLookup<T>) this.lookup(resourceKeyExtendsRegistryExtendsT0).orElseThrow(() -> new IllegalStateException("Registry " + resourceKeyExtendsRegistryExtendsT0.location() + " not found"));
        }

        default HolderGetter.Provider asGetterLookup() {
            return new HolderGetter.Provider() {

                @Override
                public <T> Optional<HolderGetter<T>> lookup(ResourceKey<? extends Registry<? extends T>> p_256379_) {
                    return Provider.this.lookup(p_256379_).map(p_255952_ -> p_255952_);
                }
            };
        }

        static HolderLookup.Provider create(Stream<HolderLookup.RegistryLookup<?>> streamHolderLookupRegistryLookup0) {
            final Map<ResourceKey<? extends Registry<?>>, HolderLookup.RegistryLookup<?>> $$1 = (Map<ResourceKey<? extends Registry<?>>, HolderLookup.RegistryLookup<?>>) streamHolderLookupRegistryLookup0.collect(Collectors.toUnmodifiableMap(HolderLookup.RegistryLookup::m_254879_, p_256335_ -> p_256335_));
            return new HolderLookup.Provider() {

                @Override
                public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> p_255663_) {
                    return Optional.ofNullable((HolderLookup.RegistryLookup) $$1.get(p_255663_));
                }
            };
        }
    }

    public interface RegistryLookup<T> extends HolderLookup<T>, HolderOwner<T> {

        ResourceKey<? extends Registry<? extends T>> key();

        Lifecycle registryLifecycle();

        default HolderLookup<T> filterFeatures(FeatureFlagSet featureFlagSet0) {
            return (HolderLookup<T>) (FeatureElement.FILTERED_REGISTRIES.contains(this.key()) ? this.m_255348_(p_250240_ -> ((FeatureElement) p_250240_).isEnabled(featureFlagSet0)) : this);
        }

        public abstract static class Delegate<T> implements HolderLookup.RegistryLookup<T> {

            protected abstract HolderLookup.RegistryLookup<T> parent();

            @Override
            public ResourceKey<? extends Registry<? extends T>> key() {
                return this.parent().key();
            }

            @Override
            public Lifecycle registryLifecycle() {
                return this.parent().registryLifecycle();
            }

            @Override
            public Optional<Holder.Reference<T>> get(ResourceKey<T> resourceKeyT0) {
                return this.parent().m_254902_(resourceKeyT0);
            }

            @Override
            public Stream<Holder.Reference<T>> listElements() {
                return this.parent().m_214062_();
            }

            @Override
            public Optional<HolderSet.Named<T>> get(TagKey<T> tagKeyT0) {
                return this.parent().m_254901_(tagKeyT0);
            }

            @Override
            public Stream<HolderSet.Named<T>> listTags() {
                return this.parent().m_214063_();
            }
        }
    }
}