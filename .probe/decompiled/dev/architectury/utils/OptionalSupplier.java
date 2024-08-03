package dev.architectury.utils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

public interface OptionalSupplier<T> extends Supplier<T> {

    boolean isPresent();

    @Nullable
    default T getOrNull() {
        return (T) (this.isPresent() ? this.get() : null);
    }

    default Optional<T> toOptional() {
        return Optional.ofNullable(this.getOrNull());
    }

    default void ifPresent(Consumer<? super T> action) {
        if (this.isPresent()) {
            action.accept(this.get());
        }
    }

    default void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction) {
        if (this.isPresent()) {
            action.accept(this.get());
        } else {
            emptyAction.run();
        }
    }

    default Stream<T> stream() {
        return !this.isPresent() ? Stream.empty() : Stream.of(this.get());
    }

    default T orElse(T other) {
        return (T) (this.isPresent() ? this.get() : other);
    }

    default T orElseGet(Supplier<? extends T> supplier) {
        return (T) (this.isPresent() ? this.get() : supplier.get());
    }
}