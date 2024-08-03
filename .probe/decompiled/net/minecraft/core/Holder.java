package net.minecraft.core;

import com.mojang.datafixers.util.Either;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public interface Holder<T> {

    T value();

    boolean isBound();

    boolean is(ResourceLocation var1);

    boolean is(ResourceKey<T> var1);

    boolean is(Predicate<ResourceKey<T>> var1);

    boolean is(TagKey<T> var1);

    Stream<TagKey<T>> tags();

    Either<ResourceKey<T>, T> unwrap();

    Optional<ResourceKey<T>> unwrapKey();

    Holder.Kind kind();

    boolean canSerializeIn(HolderOwner<T> var1);

    static <T> Holder<T> direct(T t0) {
        return new Holder.Direct<>(t0);
    }

    public static record Direct<T>(T f_205714_) implements Holder<T> {

        private final T value;

        public Direct(T f_205714_) {
            this.value = f_205714_;
        }

        @Override
        public boolean isBound() {
            return true;
        }

        @Override
        public boolean is(ResourceLocation p_205727_) {
            return false;
        }

        @Override
        public boolean is(ResourceKey<T> p_205725_) {
            return false;
        }

        @Override
        public boolean is(TagKey<T> p_205719_) {
            return false;
        }

        @Override
        public boolean is(Predicate<ResourceKey<T>> p_205723_) {
            return false;
        }

        @Override
        public Either<ResourceKey<T>, T> unwrap() {
            return Either.right(this.value);
        }

        @Override
        public Optional<ResourceKey<T>> unwrapKey() {
            return Optional.empty();
        }

        @Override
        public Holder.Kind kind() {
            return Holder.Kind.DIRECT;
        }

        public String toString() {
            return "Direct{" + this.value + "}";
        }

        @Override
        public boolean canSerializeIn(HolderOwner<T> p_256328_) {
            return true;
        }

        @Override
        public Stream<TagKey<T>> tags() {
            return Stream.of();
        }
    }

    public static enum Kind {

        REFERENCE, DIRECT
    }

    public static class Reference<T> implements Holder<T> {

        private final HolderOwner<T> owner;

        private Set<TagKey<T>> tags = Set.of();

        private final Holder.Reference.Type type;

        @Nullable
        private ResourceKey<T> key;

        @Nullable
        private T value;

        private Reference(Holder.Reference.Type holderReferenceType0, HolderOwner<T> holderOwnerT1, @Nullable ResourceKey<T> resourceKeyT2, @Nullable T t3) {
            this.owner = holderOwnerT1;
            this.type = holderReferenceType0;
            this.key = resourceKeyT2;
            this.value = t3;
        }

        public static <T> Holder.Reference<T> createStandAlone(HolderOwner<T> holderOwnerT0, ResourceKey<T> resourceKeyT1) {
            return new Holder.Reference<>(Holder.Reference.Type.STAND_ALONE, holderOwnerT0, resourceKeyT1, null);
        }

        @Deprecated
        public static <T> Holder.Reference<T> createIntrusive(HolderOwner<T> holderOwnerT0, @Nullable T t1) {
            return new Holder.Reference<>(Holder.Reference.Type.INTRUSIVE, holderOwnerT0, null, t1);
        }

        public ResourceKey<T> key() {
            if (this.key == null) {
                throw new IllegalStateException("Trying to access unbound value '" + this.value + "' from registry " + this.owner);
            } else {
                return this.key;
            }
        }

        @Override
        public T value() {
            if (this.value == null) {
                throw new IllegalStateException("Trying to access unbound value '" + this.key + "' from registry " + this.owner);
            } else {
                return this.value;
            }
        }

        @Override
        public boolean is(ResourceLocation resourceLocation0) {
            return this.key().location().equals(resourceLocation0);
        }

        @Override
        public boolean is(ResourceKey<T> resourceKeyT0) {
            return this.key() == resourceKeyT0;
        }

        @Override
        public boolean is(TagKey<T> tagKeyT0) {
            return this.tags.contains(tagKeyT0);
        }

        @Override
        public boolean is(Predicate<ResourceKey<T>> predicateResourceKeyT0) {
            return predicateResourceKeyT0.test(this.key());
        }

        @Override
        public boolean canSerializeIn(HolderOwner<T> holderOwnerT0) {
            return this.owner.canSerializeIn(holderOwnerT0);
        }

        @Override
        public Either<ResourceKey<T>, T> unwrap() {
            return Either.left(this.key());
        }

        @Override
        public Optional<ResourceKey<T>> unwrapKey() {
            return Optional.of(this.key());
        }

        @Override
        public Holder.Kind kind() {
            return Holder.Kind.REFERENCE;
        }

        @Override
        public boolean isBound() {
            return this.key != null && this.value != null;
        }

        void bindKey(ResourceKey<T> resourceKeyT0) {
            if (this.key != null && resourceKeyT0 != this.key) {
                throw new IllegalStateException("Can't change holder key: existing=" + this.key + ", new=" + resourceKeyT0);
            } else {
                this.key = resourceKeyT0;
            }
        }

        void bindValue(T t0) {
            if (this.type == Holder.Reference.Type.INTRUSIVE && this.value != t0) {
                throw new IllegalStateException("Can't change holder " + this.key + " value: existing=" + this.value + ", new=" + t0);
            } else {
                this.value = t0;
            }
        }

        void bindTags(Collection<TagKey<T>> collectionTagKeyT0) {
            this.tags = Set.copyOf(collectionTagKeyT0);
        }

        @Override
        public Stream<TagKey<T>> tags() {
            return this.tags.stream();
        }

        public String toString() {
            return "Reference{" + this.key + "=" + this.value + "}";
        }

        static enum Type {

            STAND_ALONE, INTRUSIVE
        }
    }
}