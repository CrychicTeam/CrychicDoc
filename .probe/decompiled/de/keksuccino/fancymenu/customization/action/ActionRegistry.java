package de.keksuccino.fancymenu.customization.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActionRegistry {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<String, Action> ACTIONS = new LinkedHashMap();

    public static void register(@NotNull Action action) {
        if (ACTIONS.containsKey(Objects.requireNonNull(action.getIdentifier()))) {
            LOGGER.warn("[FANCYMENU] An action with the identifier '" + action.getIdentifier() + "' is already registered! Overriding action!");
        }
        ACTIONS.put(action.getIdentifier(), action);
    }

    @NotNull
    public static List<Action> getActions() {
        List<Action> l = new ArrayList();
        ACTIONS.forEach((key, value) -> l.add(value));
        return l;
    }

    @Nullable
    public static Action getAction(@NotNull String identifier) {
        return (Action) ACTIONS.get(identifier);
    }
}