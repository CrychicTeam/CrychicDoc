package de.keksuccino.fancymenu.customization.placeholder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderRegistry {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<String, Placeholder> PLACEHOLDERS = new LinkedHashMap();

    public static void register(@NotNull Placeholder placeholder) {
        if (PLACEHOLDERS.containsKey(Objects.requireNonNull(placeholder.getIdentifier()))) {
            LOGGER.warn("[FANCYMENU] A placeholder with the identifier '" + placeholder.getIdentifier() + "' is already registered! Overriding placeholder!");
        }
        PLACEHOLDERS.put(placeholder.getIdentifier(), placeholder);
    }

    @NotNull
    public static List<Placeholder> getPlaceholders() {
        return new ArrayList(PLACEHOLDERS.values());
    }

    @Nullable
    public static Placeholder getPlaceholder(String identifier) {
        Placeholder ph = (Placeholder) PLACEHOLDERS.get(identifier);
        if (ph != null) {
            return ph;
        } else {
            for (Placeholder p : PLACEHOLDERS.values()) {
                List<String> alt = p.getAlternativeIdentifiers();
                if (alt != null && alt.contains(identifier)) {
                    return p;
                }
            }
            return null;
        }
    }
}