package net.minecraft.client.resources.language;

import java.util.IllegalFormatException;
import net.minecraft.locale.Language;

public class I18n {

    private static volatile Language language = Language.getInstance();

    private I18n() {
    }

    static void setLanguage(Language language0) {
        language = language0;
    }

    public static String get(String string0, Object... object1) {
        String $$2 = language.getOrDefault(string0);
        try {
            return String.format($$2, object1);
        } catch (IllegalFormatException var4) {
            return "Format error: " + $$2;
        }
    }

    public static boolean exists(String string0) {
        return language.has(string0);
    }
}