package de.keksuccino.konkrete.localization;

import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.locale.Language;

public class LocaleUtils {

    public static String getKeyForString(String s) {
        try {
            Language l = Language.getInstance();
            Map<String, String> properties = l.getLanguageData();
            for (Entry<String, String> m : properties.entrySet()) {
                if (((String) m.getValue()).equals(s)) {
                    return (String) m.getKey();
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }
        return null;
    }
}