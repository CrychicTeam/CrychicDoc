package mezz.jei.common.util;

import java.util.Locale;
import java.util.function.Supplier;
import net.minecraft.client.resources.language.I18n;

public final class Translator {

    private static Supplier<Locale> localeSupplier = () -> Locale.ROOT;

    private Translator() {
    }

    public static String translateToLocal(String key) {
        return I18n.get(key);
    }

    public static String translateToLocalFormatted(String key, Object... format) {
        return I18n.get(key, format);
    }

    public static String toLowercaseWithLocale(String string) {
        Locale locale = (Locale) localeSupplier.get();
        return string.toLowerCase(locale);
    }

    public static void setLocaleSupplier(Supplier<Locale> localeSupplier) {
        Translator.localeSupplier = localeSupplier;
    }
}