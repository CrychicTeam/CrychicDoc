package net.minecraftforge.common;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ToolAction {

    private static final Map<String, ToolAction> actions = new ConcurrentHashMap();

    private final String name;

    public static Collection<ToolAction> getActions() {
        return Collections.unmodifiableCollection(actions.values());
    }

    public static ToolAction get(String name) {
        return (ToolAction) actions.computeIfAbsent(name, ToolAction::new);
    }

    public String name() {
        return this.name;
    }

    public String toString() {
        return "ToolAction[" + this.name + "]";
    }

    private ToolAction(String name) {
        this.name = name;
    }
}