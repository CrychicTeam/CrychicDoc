package me.lucko.spark.lib.adventure.internal;

import me.lucko.spark.lib.adventure.examination.Examinable;
import me.lucko.spark.lib.adventure.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class Internals {

    private Internals() {
    }

    @NotNull
    public static String toString(@NotNull final Examinable examinable) {
        return examinable.examine(StringExaminer.simpleEscaping());
    }
}