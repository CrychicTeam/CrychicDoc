package de.keksuccino.fancymenu.customization.screen.dummyscreen;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DummyScreenRegistry {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<String, DummyScreenBuilder> BUILDERS = new LinkedHashMap();

    public static void register(@NotNull DummyScreenBuilder builder) {
        Objects.requireNonNull(builder);
        if (BUILDERS.containsKey(builder.screenIdentifier)) {
            LOGGER.warn("[FANCYMENU] DummyScreenBuilder for screen identifier '" + builder.screenIdentifier + "' already exists! Replacing builder..");
        }
        BUILDERS.put(builder.screenIdentifier, builder);
    }

    @Nullable
    public static DummyScreenBuilder getBuilderFor(@NotNull String screenIdentifier) {
        return (DummyScreenBuilder) BUILDERS.get(screenIdentifier);
    }

    @NotNull
    public static List<DummyScreenBuilder> getBuilders() {
        return new ArrayList(BUILDERS.values());
    }
}