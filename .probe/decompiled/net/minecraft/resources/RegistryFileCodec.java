package net.minecraft.resources;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;

public final class RegistryFileCodec<E> implements Codec<Holder<E>> {

    private final ResourceKey<? extends Registry<E>> registryKey;

    private final Codec<E> elementCodec;

    private final boolean allowInline;

    public static <E> RegistryFileCodec<E> create(ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE0, Codec<E> codecE1) {
        return create(resourceKeyExtendsRegistryE0, codecE1, true);
    }

    public static <E> RegistryFileCodec<E> create(ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE0, Codec<E> codecE1, boolean boolean2) {
        return new RegistryFileCodec<>(resourceKeyExtendsRegistryE0, codecE1, boolean2);
    }

    private RegistryFileCodec(ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE0, Codec<E> codecE1, boolean boolean2) {
        this.registryKey = resourceKeyExtendsRegistryE0;
        this.elementCodec = codecE1;
        this.allowInline = boolean2;
    }

    public <T> DataResult<T> encode(Holder<E> holderE0, DynamicOps<T> dynamicOpsT1, T t2) {
        if (dynamicOpsT1 instanceof RegistryOps<?> $$3) {
            Optional<HolderOwner<E>> $$4 = $$3.owner(this.registryKey);
            if ($$4.isPresent()) {
                if (!holderE0.canSerializeIn((HolderOwner<E>) $$4.get())) {
                    return DataResult.error(() -> "Element " + holderE0 + " is not valid in current registry set");
                }
                return (DataResult<T>) holderE0.unwrap().map(p_206714_ -> ResourceLocation.CODEC.encode(p_206714_.location(), dynamicOpsT1, t2), p_206710_ -> this.elementCodec.encode(p_206710_, dynamicOpsT1, t2));
            }
        }
        return this.elementCodec.encode(holderE0.value(), dynamicOpsT1, t2);
    }

    public <T> DataResult<Pair<Holder<E>, T>> decode(DynamicOps<T> dynamicOpsT0, T t1) {
        if (dynamicOpsT0 instanceof RegistryOps<?> $$2) {
            Optional<HolderGetter<E>> $$3 = $$2.getter(this.registryKey);
            if ($$3.isEmpty()) {
                return DataResult.error(() -> "Registry does not exist: " + this.registryKey);
            } else {
                HolderGetter<E> $$4 = (HolderGetter<E>) $$3.get();
                DataResult<Pair<ResourceLocation, T>> $$5 = ResourceLocation.CODEC.decode(dynamicOpsT0, t1);
                if ($$5.result().isEmpty()) {
                    return !this.allowInline ? DataResult.error(() -> "Inline definitions not allowed here") : this.elementCodec.decode(dynamicOpsT0, t1).map(p_206720_ -> p_206720_.mapFirst(Holder::m_205709_));
                } else {
                    Pair<ResourceLocation, T> $$6 = (Pair<ResourceLocation, T>) $$5.result().get();
                    ResourceKey<E> $$7 = ResourceKey.create(this.registryKey, (ResourceLocation) $$6.getFirst());
                    return ((DataResult) $$4.get($$7).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Failed to get element " + $$7))).map(p_255658_ -> Pair.of(p_255658_, $$6.getSecond())).setLifecycle(Lifecycle.stable());
                }
            }
        } else {
            return this.elementCodec.decode(dynamicOpsT0, t1).map(p_214212_ -> p_214212_.mapFirst(Holder::m_205709_));
        }
    }

    public String toString() {
        return "RegistryFileCodec[" + this.registryKey + " " + this.elementCodec + "]";
    }
}