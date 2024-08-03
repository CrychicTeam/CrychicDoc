package org.violetmoon.zeta.registry;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

public class LateBoundHolder<T> implements Holder<T> {

    public final ResourceKey<T> key;

    @Nullable
    public T thing;

    public Registry<T> registry;

    public LateBoundHolder(ResourceKey<T> key) {
        this.key = key;
    }

    void bind(T thing, Registry<T> registry) {
        this.thing = thing;
        this.registry = registry;
    }

    @Override
    public T value() {
        if (this.thing == null) {
            throw new IllegalStateException("LateBoundHolder: too early");
        } else {
            return this.thing;
        }
    }

    @Override
    public boolean isBound() {
        return this.thing != null;
    }

    @Override
    public boolean is(ResourceLocation resourceLocation0) {
        return this.key.location().equals(resourceLocation0);
    }

    @Override
    public boolean is(ResourceKey<T> resourceKeyT0) {
        return this.key.equals(resourceKeyT0);
    }

    @Override
    public boolean is(Predicate<ResourceKey<T>> predicateResourceKeyT0) {
        return predicateResourceKeyT0.test(this.key);
    }

    @Override
    public boolean is(TagKey<T> tagKeyT0) {
        return false;
    }

    @Override
    public Stream<TagKey<T>> tags() {
        return Stream.of();
    }

    @Override
    public Either<ResourceKey<T>, T> unwrap() {
        return this.thing == null ? Either.left(this.key) : Either.right(this.thing);
    }

    @Override
    public Optional<ResourceKey<T>> unwrapKey() {
        return Optional.of(this.key);
    }

    @Override
    public Holder.Kind kind() {
        return Holder.Kind.REFERENCE;
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> what) {
        return this.registry == null ? false : this.registry.holderOwner().canSerializeIn(what);
    }
}