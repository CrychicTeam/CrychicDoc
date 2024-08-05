package me.lucko.spark.lib.adventure.pointer;

import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.Examinable;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public interface Pointer<V> extends Examinable {

    @NotNull
    static <V> Pointer<V> pointer(@NotNull final Class<V> type, @NotNull final Key key) {
        return new PointerImpl<>(type, key);
    }

    @NotNull
    Class<V> type();

    @NotNull
    Key key();

    @NotNull
    @Override
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("type", this.type()), ExaminableProperty.of("key", this.key()));
    }
}