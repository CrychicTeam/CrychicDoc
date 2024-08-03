package com.almostreliable.morejs.util;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class ResourceOrTag<T> {

    private final Either<ResourceKey<T>, TagKey<T>> either;

    private ResourceOrTag(Either<ResourceKey<T>, TagKey<T>> either) {
        this.either = either;
    }

    public static <E> ResourceOrTag<E> get(String s, ResourceKey<Registry<E>> registry) {
        if (s.startsWith("#")) {
            ResourceLocation rl = new ResourceLocation(s.substring(1));
            return new ResourceOrTag(Either.right(TagKey.create(registry, rl)));
        } else {
            ResourceLocation rl = new ResourceLocation(s);
            return new ResourceOrTag(Either.left(ResourceKey.create(registry, rl)));
        }
    }

    public Optional<? extends HolderSet<T>> asHolderSet(Registry<T> registry) {
        return (Optional<? extends HolderSet<T>>) this.either.map(id -> registry.getHolder(id).map(xva$0 -> HolderSet.direct(xva$0)), registry::m_203431_);
    }

    public Predicate<Holder<T>> asHolderPredicate() {
        return (Predicate<Holder<T>>) this.either.map(id -> holder -> holder.is(id), tag -> holder -> holder.is(tag));
    }

    public Component getName() {
        String name = (String) this.either.map(id -> id.location().toString(), tag -> "#" + tag.location().toString());
        return Component.literal("Map for: " + Utils.format(name));
    }
}