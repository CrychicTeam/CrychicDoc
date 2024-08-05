package me.lucko.spark.lib.adventure.examination;

import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Examiner<R> {

    @NotNull
    default R examine(@NotNull final Examinable examinable) {
        return this.examine(examinable.examinableName(), examinable.examinableProperties());
    }

    @NotNull
    R examine(@NotNull final String name, @NotNull final Stream<? extends ExaminableProperty> properties);

    @NotNull
    R examine(@Nullable final Object value);

    @NotNull
    R examine(final boolean value);

    @NotNull
    R examine(@Nullable final boolean[] values);

    @NotNull
    R examine(final byte value);

    @NotNull
    R examine(@Nullable final byte[] values);

    @NotNull
    R examine(final char value);

    @NotNull
    R examine(@Nullable final char[] values);

    @NotNull
    R examine(final double value);

    @NotNull
    R examine(@Nullable final double[] values);

    @NotNull
    R examine(final float value);

    @NotNull
    R examine(@Nullable final float[] values);

    @NotNull
    R examine(final int value);

    @NotNull
    R examine(@Nullable final int[] values);

    @NotNull
    R examine(final long value);

    @NotNull
    R examine(@Nullable final long[] values);

    @NotNull
    R examine(final short value);

    @NotNull
    R examine(@Nullable final short[] values);

    @NotNull
    R examine(@Nullable final String value);
}