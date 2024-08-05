package me.lucko.spark.lib.adventure.pointer;

import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

public interface Pointered {

    @NotNull
    default <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
        return this.pointers().get(pointer);
    }

    @Contract("_, null -> _; _, !null -> !null")
    @Nullable
    default <T> T getOrDefault(@NotNull final Pointer<T> pointer, @Nullable final T defaultValue) {
        return this.pointers().getOrDefault(pointer, defaultValue);
    }

    @UnknownNullability
    default <T> T getOrDefaultFrom(@NotNull final Pointer<T> pointer, @NotNull final Supplier<? extends T> defaultValue) {
        return this.pointers().getOrDefaultFrom(pointer, defaultValue);
    }

    @NotNull
    default Pointers pointers() {
        return Pointers.empty();
    }
}