package de.keksuccino.fancymenu.customization.element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementRegistry {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<String, ElementBuilder<?, ?>> BUILDERS = new LinkedHashMap();

    public static void register(@NotNull ElementBuilder<?, ?> builder) {
        if (BUILDERS.containsKey(Objects.requireNonNull(builder.getIdentifier()))) {
            LOGGER.warn("[FANCYMENU] ElementBuilder with identifier '" + builder.getIdentifier() + "' already registered! Overriding builder!");
        }
        BUILDERS.put(builder.getIdentifier(), builder);
    }

    @NotNull
    public static List<ElementBuilder<?, ?>> getBuilders() {
        return new ArrayList(BUILDERS.values());
    }

    @Nullable
    public static ElementBuilder<?, ?> getBuilder(@NotNull String identifier) {
        return (ElementBuilder<?, ?>) BUILDERS.get(identifier);
    }

    public static boolean hasBuilder(@NotNull String identifier) {
        return BUILDERS.containsKey(identifier);
    }
}