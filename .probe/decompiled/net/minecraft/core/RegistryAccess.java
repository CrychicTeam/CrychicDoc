package net.minecraft.core;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Lifecycle;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceKey;
import org.slf4j.Logger;

public interface RegistryAccess extends HolderLookup.Provider {

    Logger LOGGER = LogUtils.getLogger();

    RegistryAccess.Frozen EMPTY = new RegistryAccess.ImmutableRegistryAccess(Map.of()).m_203557_();

    <E> Optional<Registry<E>> registry(ResourceKey<? extends Registry<? extends E>> var1);

    @Override
    default <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> resourceKeyExtendsRegistryExtendsT0) {
        return this.registry(resourceKeyExtendsRegistryExtendsT0).map(Registry::m_255303_);
    }

    default <E> Registry<E> registryOrThrow(ResourceKey<? extends Registry<? extends E>> resourceKeyExtendsRegistryExtendsE0) {
        return (Registry<E>) this.registry(resourceKeyExtendsRegistryExtendsE0).orElseThrow(() -> new IllegalStateException("Missing registry: " + resourceKeyExtendsRegistryExtendsE0));
    }

    Stream<RegistryAccess.RegistryEntry<?>> registries();

    static RegistryAccess.Frozen fromRegistryOfRegistries(final Registry<? extends Registry<?>> registryExtendsRegistry0) {
        return new RegistryAccess.Frozen() {

            @Override
            public <T> Optional<Registry<T>> registry(ResourceKey<? extends Registry<? extends T>> p_206220_) {
                Registry<Registry<T>> $$1 = (Registry<Registry<T>>) registryExtendsRegistry0;
                return $$1.getOptional((ResourceKey<Registry<T>>) p_206220_);
            }

            @Override
            public Stream<RegistryAccess.RegistryEntry<?>> registries() {
                return registryExtendsRegistry0.entrySet().stream().map(RegistryAccess.RegistryEntry::m_206241_);
            }

            @Override
            public RegistryAccess.Frozen freeze() {
                return this;
            }
        };
    }

    default RegistryAccess.Frozen freeze() {
        class FrozenAccess extends RegistryAccess.ImmutableRegistryAccess implements RegistryAccess.Frozen {

            protected FrozenAccess(Stream<RegistryAccess.RegistryEntry<?>> streamRegistryAccessRegistryEntry0) {
                super(streamRegistryAccessRegistryEntry0);
            }
        }
        return new FrozenAccess(this.registries().map(RegistryAccess.RegistryEntry::m_206247_));
    }

    default Lifecycle allRegistriesLifecycle() {
        return (Lifecycle) this.registries().map(p_258181_ -> p_258181_.value.registryLifecycle()).reduce(Lifecycle.stable(), Lifecycle::add);
    }

    public interface Frozen extends RegistryAccess {
    }

    public static class ImmutableRegistryAccess implements RegistryAccess {

        private final Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>> registries;

        public ImmutableRegistryAccess(List<? extends Registry<?>> listExtendsRegistry0) {
            this.registries = (Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>>) listExtendsRegistry0.stream().collect(Collectors.toUnmodifiableMap(Registry::m_123023_, p_206232_ -> p_206232_));
        }

        public ImmutableRegistryAccess(Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>> mapExtendsResourceKeyExtendsRegistryExtendsRegistry0) {
            this.registries = Map.copyOf(mapExtendsResourceKeyExtendsRegistryExtendsRegistry0);
        }

        public ImmutableRegistryAccess(Stream<RegistryAccess.RegistryEntry<?>> streamRegistryAccessRegistryEntry0) {
            this.registries = (Map<? extends ResourceKey<? extends Registry<?>>, ? extends Registry<?>>) streamRegistryAccessRegistryEntry0.collect(ImmutableMap.toImmutableMap(RegistryAccess.RegistryEntry::f_206233_, RegistryAccess.RegistryEntry::f_206234_));
        }

        @Override
        public <E> Optional<Registry<E>> registry(ResourceKey<? extends Registry<? extends E>> resourceKeyExtendsRegistryExtendsE0) {
            return Optional.ofNullable((Registry) this.registries.get(resourceKeyExtendsRegistryExtendsE0)).map(p_247993_ -> p_247993_);
        }

        @Override
        public Stream<RegistryAccess.RegistryEntry<?>> registries() {
            return this.registries.entrySet().stream().map(RegistryAccess.RegistryEntry::m_206241_);
        }
    }

    public static record RegistryEntry<T>(ResourceKey<? extends Registry<T>> f_206233_, Registry<T> f_206234_) {

        private final ResourceKey<? extends Registry<T>> key;

        private final Registry<T> value;

        public RegistryEntry(ResourceKey<? extends Registry<T>> f_206233_, Registry<T> f_206234_) {
            this.key = f_206233_;
            this.value = f_206234_;
        }

        private static <T, R extends Registry<? extends T>> RegistryAccess.RegistryEntry<T> fromMapEntry(Entry<? extends ResourceKey<? extends Registry<?>>, R> p_206242_) {
            return fromUntyped((ResourceKey<? extends Registry<?>>) p_206242_.getKey(), (Registry<?>) p_206242_.getValue());
        }

        private static <T> RegistryAccess.RegistryEntry<T> fromUntyped(ResourceKey<? extends Registry<?>> p_206244_, Registry<?> p_206245_) {
            return new RegistryAccess.RegistryEntry<>((ResourceKey<? extends Registry<T>>) p_206244_, (Registry<T>) p_206245_);
        }

        private RegistryAccess.RegistryEntry<T> freeze() {
            return new RegistryAccess.RegistryEntry<>(this.key, this.value.freeze());
        }
    }
}