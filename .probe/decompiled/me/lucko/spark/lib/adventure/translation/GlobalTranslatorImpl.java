package me.lucko.spark.lib.adventure.translation;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.key.Key;
import me.lucko.spark.lib.adventure.text.renderer.TranslatableComponentRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class GlobalTranslatorImpl implements GlobalTranslator {

    private static final Key NAME = Key.key("adventure", "global");

    static final GlobalTranslatorImpl INSTANCE = new GlobalTranslatorImpl();

    final TranslatableComponentRenderer<Locale> renderer = TranslatableComponentRenderer.usingTranslationSource(this);

    private final Set<Translator> sources = Collections.newSetFromMap(new ConcurrentHashMap());

    private GlobalTranslatorImpl() {
    }

    @NotNull
    @Override
    public Key name() {
        return NAME;
    }

    @NotNull
    @Override
    public Iterable<? extends Translator> sources() {
        return Collections.unmodifiableSet(this.sources);
    }

    @Override
    public boolean addSource(@NotNull final Translator source) {
        Objects.requireNonNull(source, "source");
        if (source == this) {
            throw new IllegalArgumentException("GlobalTranslationSource");
        } else {
            return this.sources.add(source);
        }
    }

    @Override
    public boolean removeSource(@NotNull final Translator source) {
        Objects.requireNonNull(source, "source");
        return this.sources.remove(source);
    }

    @Nullable
    @Override
    public MessageFormat translate(@NotNull final String key, @NotNull final Locale locale) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(locale, "locale");
        for (Translator source : this.sources) {
            MessageFormat translation = source.translate(key, locale);
            if (translation != null) {
                return translation;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("sources", this.sources));
    }
}