package me.lucko.spark.lib.adventure.translation;

import java.util.Locale;
import java.util.function.Supplier;
import me.lucko.spark.lib.adventure.internal.properties.AdventureProperties;

final class TranslationLocales {

    private static final Supplier<Locale> GLOBAL;

    private TranslationLocales() {
    }

    static Locale global() {
        return (Locale) GLOBAL.get();
    }

    static {
        String property = AdventureProperties.DEFAULT_TRANSLATION_LOCALE.value();
        if (property == null || property.isEmpty()) {
            GLOBAL = () -> Locale.US;
        } else if (property.equals("system")) {
            GLOBAL = Locale::getDefault;
        } else {
            Locale locale = Translator.parseLocale(property);
            GLOBAL = () -> locale;
        }
    }
}