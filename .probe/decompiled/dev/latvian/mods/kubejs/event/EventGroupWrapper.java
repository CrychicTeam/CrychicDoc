package dev.latvian.mods.kubejs.event;

import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.BaseFunction;
import java.util.HashMap;
import java.util.Set;

public class EventGroupWrapper extends HashMap<String, BaseFunction> {

    private final ScriptType scriptType;

    private final EventGroup group;

    public EventGroupWrapper(ScriptType scriptType, EventGroup group) {
        this.scriptType = scriptType;
        this.group = group;
    }

    public BaseFunction get(Object key) {
        String keyString = String.valueOf(key);
        EventHandler handler = (EventHandler) this.group.getHandlers().get(keyString);
        if (handler == null) {
            this.scriptType.console.error("Unknown event '%s.%s'!".formatted(this.group.name, keyString));
            return new BaseFunction();
        } else {
            return handler;
        }
    }

    public boolean containsKey(Object key) {
        return true;
    }

    public Set<String> keySet() {
        return this.group.getHandlers().keySet();
    }
}