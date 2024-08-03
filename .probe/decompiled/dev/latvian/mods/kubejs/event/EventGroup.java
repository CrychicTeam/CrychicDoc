package dev.latvian.mods.kubejs.event;

import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.script.ScriptTypePredicate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class EventGroup {

    private static final Map<String, EventGroup> MAP = new HashMap();

    public final String name;

    private final Map<String, EventHandler> handlers;

    public static Map<String, EventGroup> getGroups() {
        return Collections.unmodifiableMap(MAP);
    }

    public static EventGroup of(String name) {
        return new EventGroup(name);
    }

    private EventGroup(String n) {
        this.name = n;
        this.handlers = new HashMap();
    }

    public void register() {
        MAP.put(this.name, this);
    }

    public String toString() {
        return this.name;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            if (obj instanceof EventGroup g && this.name.equals(g.name)) {
                return true;
            }
            return false;
        }
    }

    public EventHandler add(String name, ScriptTypePredicate scriptType, Supplier<Class<? extends EventJS>> eventType) {
        EventHandler handler = new EventHandler(this, name, scriptType, eventType);
        this.handlers.put(name, handler);
        return handler;
    }

    public EventHandler startup(String name, Supplier<Class<? extends EventJS>> eventType) {
        return this.add(name, ScriptType.STARTUP, eventType);
    }

    public EventHandler server(String name, Supplier<Class<? extends EventJS>> eventType) {
        return this.add(name, ScriptType.SERVER, eventType);
    }

    public EventHandler client(String name, Supplier<Class<? extends EventJS>> eventType) {
        return this.add(name, ScriptType.CLIENT, eventType);
    }

    public EventHandler common(String name, Supplier<Class<? extends EventJS>> eventType) {
        return this.add(name, ScriptTypePredicate.COMMON, eventType);
    }

    public Map<String, EventHandler> getHandlers() {
        return this.handlers;
    }
}