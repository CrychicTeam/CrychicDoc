package me.lucko.spark.lib.adventure.translation;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import me.lucko.spark.lib.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TranslationRegistry extends Translator {

    Pattern SINGLE_QUOTE_PATTERN = Pattern.compile("'");

    @NotNull
    static TranslationRegistry create(final Key name) {
        return new TranslationRegistryImpl((Key) Objects.requireNonNull(name, "name"));
    }

    boolean contains(@NotNull final String key);

    @Nullable
    @Override
    MessageFormat translate(@NotNull final String key, @NotNull final Locale locale);

    void defaultLocale(@NotNull final Locale locale);

    void register(@NotNull final String key, @NotNull final Locale locale, @NotNull final MessageFormat format);

    default void registerAll(@NotNull final Locale locale, @NotNull final Map<String, MessageFormat> formats) {
        this.registerAll(locale, formats.keySet(), formats::get);
    }

    default void registerAll(@NotNull final Locale locale, @NotNull final Path path, final boolean escapeSingleQuotes) {
        try {
            BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            try {
                this.registerAll(locale, new PropertyResourceBundle(reader), escapeSingleQuotes);
            } catch (Throwable var8) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            if (reader != null) {
                reader.close();
            }
        } catch (IOException var9) {
        }
    }

    default void registerAll(@NotNull final Locale locale, @NotNull final ResourceBundle bundle, final boolean escapeSingleQuotes) {
        this.registerAll(locale, bundle.keySet(), key -> {
            String format = bundle.getString(key);
            return new MessageFormat(escapeSingleQuotes ? SINGLE_QUOTE_PATTERN.matcher(format).replaceAll("''") : format, locale);
        });
    }

    default void registerAll(@NotNull final Locale locale, @NotNull final Set<String> keys, final Function<String, MessageFormat> function) {
        IllegalArgumentException firstError = null;
        int errorCount = 0;
        for (String key : keys) {
            try {
                this.register(key, locale, (MessageFormat) function.apply(key));
            } catch (IllegalArgumentException var9) {
                if (firstError == null) {
                    firstError = var9;
                }
                errorCount++;
            }
        }
        if (firstError != null) {
            if (errorCount == 1) {
                throw firstError;
            }
            if (errorCount > 1) {
                throw new IllegalArgumentException(String.format("Invalid key (and %d more)", errorCount - 1), firstError);
            }
        }
    }

    void unregister(@NotNull final String key);
}