package me.lucko.spark.lib.adventure.pointer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

final class PointersImpl implements Pointers {

    static final Pointers EMPTY = new Pointers() {

        @NotNull
        @Override
        public <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
            return Optional.empty();
        }

        @Override
        public <T> boolean supports(@NotNull final Pointer<T> pointer) {
            return false;
        }

        @NotNull
        public Pointers.Builder toBuilder() {
            return new PointersImpl.BuilderImpl();
        }

        public String toString() {
            return "EmptyPointers";
        }
    };

    private final Map<Pointer<?>, Supplier<?>> pointers;

    PointersImpl(@NotNull final PointersImpl.BuilderImpl builder) {
        this.pointers = new HashMap(builder.pointers);
    }

    @NotNull
    @Override
    public <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
        Objects.requireNonNull(pointer, "pointer");
        Supplier<?> supplier = (Supplier<?>) this.pointers.get(pointer);
        return supplier == null ? Optional.empty() : Optional.ofNullable(supplier.get());
    }

    @Override
    public <T> boolean supports(@NotNull final Pointer<T> pointer) {
        Objects.requireNonNull(pointer, "pointer");
        return this.pointers.containsKey(pointer);
    }

    @NotNull
    public Pointers.Builder toBuilder() {
        return new PointersImpl.BuilderImpl(this);
    }

    static final class BuilderImpl implements Pointers.Builder {

        private final Map<Pointer<?>, Supplier<?>> pointers;

        BuilderImpl() {
            this.pointers = new HashMap();
        }

        BuilderImpl(@NotNull final PointersImpl pointers) {
            this.pointers = new HashMap(pointers.pointers);
        }

        @NotNull
        @Override
        public <T> Pointers.Builder withDynamic(@NotNull final Pointer<T> pointer, @NotNull final Supplier<T> value) {
            this.pointers.put((Pointer) Objects.requireNonNull(pointer, "pointer"), (Supplier) Objects.requireNonNull(value, "value"));
            return this;
        }

        @NotNull
        public Pointers build() {
            return new PointersImpl(this);
        }
    }
}