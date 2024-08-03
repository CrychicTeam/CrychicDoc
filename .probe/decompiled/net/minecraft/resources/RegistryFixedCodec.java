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

public final class RegistryFixedCodec<E> implements Codec<Holder<E>> {

    private final ResourceKey<? extends Registry<E>> registryKey;

    public static <E> RegistryFixedCodec<E> create(ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE0) {
        return new RegistryFixedCodec<>(resourceKeyExtendsRegistryE0);
    }

    private RegistryFixedCodec(ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE0) {
        this.registryKey = resourceKeyExtendsRegistryE0;
    }

    public <T> DataResult<T> encode(Holder<E> holderE0, DynamicOps<T> dynamicOpsT1, T t2) {
        if (dynamicOpsT1 instanceof RegistryOps<?> $$3) {
            Optional<HolderOwner<E>> $$4 = $$3.owner(this.registryKey);
            if ($$4.isPresent()) {
                if (!holderE0.canSerializeIn((HolderOwner<E>) $$4.get())) {
                    return DataResult.error(() -> "Element " + holderE0 + " is not valid in current registry set");
                }
                return (DataResult<T>) holderE0.unwrap().map(p_206727_ -> ResourceLocation.CODEC.encode(p_206727_.location(), dynamicOpsT1, t2), p_274804_ -> DataResult.error(() -> "Elements from registry " + this.registryKey + " can't be serialized to a value"));
            }
        }
        return DataResult.error(() -> "Can't access registry " + this.registryKey);
    }

    public <T> DataResult<Pair<Holder<E>, T>> decode(DynamicOps<T> dynamicOpsT0, T t1) {
        if (dynamicOpsT0 instanceof RegistryOps<?> $$2) {
            Optional<HolderGetter<E>> $$3 = $$2.getter(this.registryKey);
            if ($$3.isPresent()) {
                return ResourceLocation.CODEC.decode(dynamicOpsT0, t1).flatMap(p_255515_ -> {
                    ResourceLocation $$2x = (ResourceLocation) p_255515_.getFirst();
                    return ((DataResult) ((HolderGetter) $$3.get()).get(ResourceKey.create(this.registryKey, $$2x)).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Failed to get element " + $$2x))).map(p_256041_ -> Pair.of(p_256041_, p_255515_.getSecond())).setLifecycle(Lifecycle.stable());
                });
            }
        }
        return DataResult.error(() -> "Can't access registry " + this.registryKey);
    }

    public String toString() {
        return "RegistryFixedCodec[" + this.registryKey + "]";
    }
}