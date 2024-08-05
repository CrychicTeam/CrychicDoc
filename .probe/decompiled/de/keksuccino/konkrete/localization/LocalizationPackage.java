package de.keksuccino.konkrete.localization;

import java.util.HashMap;
import java.util.Map;

public class LocalizationPackage {

    private final String language;

    private final Map<String, String> locals = new HashMap();

    protected LocalizationPackage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return this.language;
    }

    public boolean isEmpty() {
        return this.locals.isEmpty();
    }

    public boolean containsKey(String key) {
        return this.locals.containsKey(key);
    }

    public void addLocalizedString(String key, String value) {
        this.locals.put(key, value);
    }

    public void removeLocalizedString(String key) {
        if (this.locals.containsKey(key)) {
            this.locals.remove(key);
        }
    }

    public String getLocalizedString(String key) {
        return (String) this.locals.get(key);
    }
}