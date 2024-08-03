package net.minecraft.resources;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.util.ExtraCodecs;

public class RegistryOps<T> extends DelegatingOps<T> {

    private final RegistryOps.RegistryInfoLookup lookupProvider;

    private static RegistryOps.RegistryInfoLookup memoizeLookup(final RegistryOps.RegistryInfoLookup registryOpsRegistryInfoLookup0) {
        return new RegistryOps.RegistryInfoLookup() {

            private final Map<ResourceKey<? extends Registry<?>>, Optional<? extends RegistryOps.RegistryInfo<?>>> lookups = new HashMap();

            @Override
            public <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> p_256043_) {
                return (Optional<RegistryOps.RegistryInfo<T>>) this.lookups.computeIfAbsent(p_256043_, registryOpsRegistryInfoLookup0::m_254838_);
            }
        };
    }

    public static <T> RegistryOps<T> create(DynamicOps<T> dynamicOpsT0, final HolderLookup.Provider holderLookupProvider1) {
        return create(dynamicOpsT0, memoizeLookup(new RegistryOps.RegistryInfoLookup() {

            @Override
            public <E> Optional<RegistryOps.RegistryInfo<E>> lookup(ResourceKey<? extends Registry<? extends E>> p_256323_) {
                return holderLookupProvider1.lookup(p_256323_).map(p_258224_ -> new RegistryOps.RegistryInfo<>(p_258224_, p_258224_, p_258224_.registryLifecycle()));
            }
        }));
    }

    public static <T> RegistryOps<T> create(DynamicOps<T> dynamicOpsT0, RegistryOps.RegistryInfoLookup registryOpsRegistryInfoLookup1) {
        return new RegistryOps<>(dynamicOpsT0, registryOpsRegistryInfoLookup1);
    }

    private RegistryOps(DynamicOps<T> dynamicOpsT0, RegistryOps.RegistryInfoLookup registryOpsRegistryInfoLookup1) {
        super(dynamicOpsT0);
        this.lookupProvider = registryOpsRegistryInfoLookup1;
    }

    public <E> Optional<HolderOwner<E>> owner(ResourceKey<? extends Registry<? extends E>> resourceKeyExtendsRegistryExtendsE0) {
        return this.lookupProvider.lookup(resourceKeyExtendsRegistryExtendsE0).map(RegistryOps.RegistryInfo::f_254675_);
    }

    public <E> Optional<HolderGetter<E>> getter(ResourceKey<? extends Registry<? extends E>> resourceKeyExtendsRegistryExtendsE0) {
        return this.lookupProvider.lookup(resourceKeyExtendsRegistryExtendsE0).map(RegistryOps.RegistryInfo::f_254724_);
    }

    public static <E, O> RecordCodecBuilder<O, HolderGetter<E>> retrieveGetter(ResourceKey<? extends Registry<? extends E>> resourceKeyExtendsRegistryExtendsE0) {
        return ExtraCodecs.retrieveContext(p_274811_ -> p_274811_ instanceof RegistryOps<?> $$2 ? (DataResult) $$2.lookupProvider.lookup(resourceKeyExtendsRegistryExtendsE0).map(p_255527_ -> DataResult.success(p_255527_.getter(), p_255527_.elementsLifecycle())).orElseGet(() -> DataResult.error(() -> "Unknown registry: " + resourceKeyExtendsRegistryExtendsE0)) : DataResult.error(() -> "Not a registry ops")).forGetter(p_255526_ -> null);
    }

    public static <E, O> RecordCodecBuilder<O, Holder.Reference<E>> retrieveElement(ResourceKey<E> resourceKeyE0) {
        ResourceKey<? extends Registry<E>> $$1 = ResourceKey.createRegistryKey(resourceKeyE0.registry());
        return ExtraCodecs.retrieveContext(p_274808_ -> p_274808_ instanceof RegistryOps<?> $$3 ? (DataResult) $$3.lookupProvider.lookup($$1).flatMap(p_255518_ -> p_255518_.getter().get(resourceKeyE0)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Can't find value: " + resourceKeyE0)) : DataResult.error(() -> "Not a registry ops")).forGetter(p_255524_ -> null);
    }

    public static record RegistryInfo<T>(HolderOwner<T> f_254675_, HolderGetter<T> f_254724_, Lifecycle f_254751_) {

        private final HolderOwner<T> owner;

        private final HolderGetter<T> getter;

        private final Lifecycle elementsLifecycle;

        public RegistryInfo(HolderOwner<T> f_254675_, HolderGetter<T> f_254724_, Lifecycle f_254751_) {
            this.owner = f_254675_;
            this.getter = f_254724_;
            this.elementsLifecycle = f_254751_;
        }
    }

    public interface RegistryInfoLookup {

        <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> var1);
    }
}