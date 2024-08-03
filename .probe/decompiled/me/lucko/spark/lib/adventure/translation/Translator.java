package me.lucko.spark.lib.adventure.translation;

import java.text.MessageFormat;
import java.util.Locale;
import me.lucko.spark.lib.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Translator {

    @Nullable
    static Locale parseLocale(@NotNull final String string) {
        String[] segments = string.split("_", 3);
        int length = segments.length;
        if (length == 1) {
            return new Locale(string);
        } else if (length == 2) {
            return new Locale(segments[0], segments[1]);
        } else {
            return length == 3 ? new Locale(segments[0], segments[1], segments[2]) : null;
        }
    }

    @NotNull
    Key name();

    @Nullable
    MessageFormat translate(@NotNull final String key, @NotNull final Locale locale);
}