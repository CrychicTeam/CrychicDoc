package net.minecraft.resources;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;

public class HolderSetCodec<E> implements Codec<HolderSet<E>> {

    private final ResourceKey<? extends Registry<E>> registryKey;

    private final Codec<Holder<E>> elementCodec;

    private final Codec<List<Holder<E>>> homogenousListCodec;

    private final Codec<Either<TagKey<E>, List<Holder<E>>>> registryAwareCodec;

    private static <E> Codec<List<Holder<E>>> homogenousList(Codec<Holder<E>> codecHolderE0, boolean boolean1) {
        Codec<List<Holder<E>>> $$2 = ExtraCodecs.validate(codecHolderE0.listOf(), ExtraCodecs.ensureHomogenous(Holder::m_203376_));
        return boolean1 ? $$2 : Codec.either($$2, codecHolderE0).xmap(p_206664_ -> (List) p_206664_.map(p_206694_ -> p_206694_, List::of), p_206684_ -> p_206684_.size() == 1 ? Either.right((Holder) p_206684_.get(0)) : Either.left(p_206684_));
    }

    public static <E> Codec<HolderSet<E>> create(ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE0, Codec<Holder<E>> codecHolderE1, boolean boolean2) {
        return new HolderSetCodec<>(resourceKeyExtendsRegistryE0, codecHolderE1, boolean2);
    }

    private HolderSetCodec(ResourceKey<? extends Registry<E>> resourceKeyExtendsRegistryE0, Codec<Holder<E>> codecHolderE1, boolean boolean2) {
        this.registryKey = resourceKeyExtendsRegistryE0;
        this.elementCodec = codecHolderE1;
        this.homogenousListCodec = homogenousList(codecHolderE1, boolean2);
        this.registryAwareCodec = Codec.either(TagKey.hashedCodec(resourceKeyExtendsRegistryE0), this.homogenousListCodec);
    }

    public <T> DataResult<Pair<HolderSet<E>, T>> decode(DynamicOps<T> dynamicOpsT0, T t1) {
        if (dynamicOpsT0 instanceof RegistryOps<T> $$2) {
            Optional<HolderGetter<E>> $$3 = $$2.getter(this.registryKey);
            if ($$3.isPresent()) {
                HolderGetter<E> $$4 = (HolderGetter<E>) $$3.get();
                return this.registryAwareCodec.decode(dynamicOpsT0, t1).map(p_206682_ -> p_206682_.mapFirst(p_206679_ -> (HolderSet) p_206679_.map($$4::m_254956_, HolderSet::m_205800_)));
            }
        }
        return this.decodeWithoutRegistry(dynamicOpsT0, t1);
    }

    public <T> DataResult<T> encode(HolderSet<E> holderSetE0, DynamicOps<T> dynamicOpsT1, T t2) {
        if (dynamicOpsT1 instanceof RegistryOps<T> $$3) {
            Optional<HolderOwner<E>> $$4 = $$3.owner(this.registryKey);
            if ($$4.isPresent()) {
                if (!holderSetE0.canSerializeIn((HolderOwner<E>) $$4.get())) {
                    return DataResult.error(() -> "HolderSet " + holderSetE0 + " is not valid in current registry set");
                }
                return this.registryAwareCodec.encode(holderSetE0.unwrap().mapRight(List::copyOf), dynamicOpsT1, t2);
            }
        }
        return this.encodeWithoutRegistry(holderSetE0, dynamicOpsT1, t2);
    }

    private <T> DataResult<Pair<HolderSet<E>, T>> decodeWithoutRegistry(DynamicOps<T> dynamicOpsT0, T t1) {
        return this.elementCodec.listOf().decode(dynamicOpsT0, t1).flatMap(p_206666_ -> {
            List<Holder.Direct<E>> $$1 = new ArrayList();
            for (Holder<E> $$2 : (List) p_206666_.getFirst()) {
                if (!($$2 instanceof Holder.Direct<E> $$3)) {
                    return DataResult.error(() -> "Can't decode element " + $$2 + " without registry");
                }
                $$1.add($$3);
            }
            return DataResult.success(new Pair(HolderSet.direct($$1), p_206666_.getSecond()));
        });
    }

    private <T> DataResult<T> encodeWithoutRegistry(HolderSet<E> holderSetE0, DynamicOps<T> dynamicOpsT1, T t2) {
        return this.homogenousListCodec.encode(holderSetE0.stream().toList(), dynamicOpsT1, t2);
    }
}