package net.mehvahdjukaar.moonlight.api.misc;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<T, U, V, R> {

    R apply(T var1, U var2, V var3);

    default <W> TriFunction<T, U, V, W> andThen(Function<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (t, u, v) -> (W) after.apply(this.apply(t, u, v));
    }
}