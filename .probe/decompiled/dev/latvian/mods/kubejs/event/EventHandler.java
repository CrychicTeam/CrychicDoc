package dev.latvian.mods.kubejs.event;

import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.script.ScriptTypeHolder;
import dev.latvian.mods.kubejs.script.ScriptTypePredicate;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.RhinoException;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.Wrapper;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public final class EventHandler extends BaseFunction {

    public final EventGroup group;

    public final String name;

    public final ScriptTypePredicate scriptTypePredicate;

    public final Supplier<Class<? extends EventJS>> eventType;

    private boolean hasResult;

    public transient Extra extra;

    private EventHandlerContainer[] eventContainers;

    private Map<Object, EventHandlerContainer[]> extraEventContainers;

    EventHandler(EventGroup g, String n, ScriptTypePredicate st, Supplier<Class<? extends EventJS>> e) {
        this.group = g;
        this.name = n;
        this.scriptTypePredicate = st;
        this.eventType = e;
        this.hasResult = false;
        this.extra = null;
        this.eventContainers = null;
        this.extraEventContainers = null;
    }

    public EventHandler hasResult() {
        this.hasResult = true;
        return this;
    }

    public boolean getHasResult() {
        return this.hasResult;
    }

    @HideFromJS
    public EventHandler extra(Extra extra) {
        this.extra = extra;
        return this;
    }

    @HideFromJS
    public void clear(ScriptType type) {
        if (this.eventContainers != null) {
            this.eventContainers[type.ordinal()] = null;
            if (EventHandlerContainer.isEmpty(this.eventContainers)) {
                this.eventContainers = null;
            }
        }
        if (this.extraEventContainers != null) {
            Iterator<Entry<Object, EventHandlerContainer[]>> entries = this.extraEventContainers.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<Object, EventHandlerContainer[]> entry = (Entry<Object, EventHandlerContainer[]>) entries.next();
                ((EventHandlerContainer[]) entry.getValue())[type.ordinal()] = null;
                if (EventHandlerContainer.isEmpty((EventHandlerContainer[]) entry.getValue())) {
                    entries.remove();
                }
            }
            if (this.extraEventContainers.isEmpty()) {
                this.extraEventContainers = null;
            }
        }
    }

    public boolean hasListeners() {
        return this.eventContainers != null || this.extraEventContainers != null;
    }

    public boolean hasListeners(Object extraId) {
        return this.eventContainers != null || this.extraEventContainers != null && this.extraEventContainers.containsKey(extraId);
    }

    public void listen(ScriptType type, @Nullable Object extraId, IEventHandler handler) {
        if (!((ScriptManager) type.manager.get()).canListenEvents) {
            throw new IllegalStateException("Event handler '" + this + "' can only be registered during script loading!");
        } else if (!this.scriptTypePredicate.test(type)) {
            throw new UnsupportedOperationException("Tried to register event handler '" + this + "' for invalid script type " + type + "! Valid script types: " + this.scriptTypePredicate.getValidTypes());
        } else {
            if (extraId != null && this.extra != null) {
                extraId = Wrapper.unwrapped(extraId);
                extraId = this.extra.transformer.transform(extraId);
            }
            if (this.extra != null && this.extra.required && extraId == null) {
                throw new IllegalArgumentException("Event handler '" + this + "' requires extra id!");
            } else if (this.extra == null && extraId != null) {
                throw new IllegalArgumentException("Event handler '" + this + "' doesn't support extra id!");
            } else if (this.extra != null && extraId != null && !this.extra.validator.test(extraId)) {
                throw new IllegalArgumentException("Event handler '" + this + "' doesn't accept id '" + this.extra.toString.transform(extraId) + "'!");
            } else {
                int[] line = new int[1];
                String source = Context.getSourcePositionFromStack(((ScriptManager) type.manager.get()).context, line);
                EventHandlerContainer[] map;
                if (extraId == null) {
                    if (this.eventContainers == null) {
                        this.eventContainers = new EventHandlerContainer[ScriptType.VALUES.length];
                    }
                    map = this.eventContainers;
                } else {
                    if (this.extraEventContainers == null) {
                        this.extraEventContainers = (Map<Object, EventHandlerContainer[]>) (this.extra.identity ? new LinkedHashMap() : new HashMap());
                    }
                    map = (EventHandlerContainer[]) this.extraEventContainers.get(extraId);
                    if (map == null) {
                        map = new EventHandlerContainer[ScriptType.VALUES.length];
                        this.extraEventContainers.put(extraId, map);
                    }
                }
                int index = type.ordinal();
                if (map[index] == null) {
                    map[index] = new EventHandlerContainer(extraId, handler, source, line[0]);
                } else {
                    map[index].add(extraId, handler, source, line[0]);
                }
            }
        }
    }

    @HideFromJS
    public void listenJava(ScriptType type, @Nullable Object extraId, IEventHandler handler) {
        boolean b = ((ScriptManager) type.manager.get()).canListenEvents;
        ((ScriptManager) type.manager.get()).canListenEvents = true;
        this.listen(type, extraId, handler);
        ((ScriptManager) type.manager.get()).canListenEvents = b;
    }

    public EventResult post(EventJS event) {
        if (this.scriptTypePredicate instanceof ScriptTypeHolder type) {
            return this.post(type, null, event);
        } else {
            throw new IllegalStateException("You must specify which script type to post event to");
        }
    }

    public EventResult post(ScriptTypeHolder scriptType, EventJS event) {
        return this.post(scriptType, null, event);
    }

    public EventResult post(ScriptTypeHolder scriptType, EventJS event, EventExceptionHandler exh) {
        return this.post(scriptType, null, event, exh);
    }

    public EventResult post(EventJS event, @Nullable Object extraId) {
        if (this.scriptTypePredicate instanceof ScriptTypeHolder type) {
            return this.post(type, extraId, event);
        } else {
            throw new IllegalStateException("You must specify which script type to post event to");
        }
    }

    public EventResult post(EventJS event, @Nullable Object extraId, EventExceptionHandler exh) {
        if (this.scriptTypePredicate instanceof ScriptTypeHolder type) {
            return this.post(type, extraId, event, exh);
        } else {
            throw new IllegalStateException("You must specify which script type to post event to");
        }
    }

    public EventResult post(ScriptTypeHolder type, @Nullable Object extraId, EventJS event) {
        return this.post(type, extraId, event, null);
    }

    public EventResult post(ScriptTypeHolder type, @Nullable Object extraId, EventJS event, EventExceptionHandler exh) {
        if (!this.hasListeners()) {
            return EventResult.PASS;
        } else {
            ScriptType scriptType = type.kjs$getScriptType();
            if (extraId != null && this.extra != null) {
                extraId = Wrapper.unwrapped(extraId);
                extraId = this.extra.transformer.transform(extraId);
            }
            if (this.extra != null && this.extra.required && extraId == null) {
                throw new IllegalArgumentException("Event handler '" + this + "' requires extra id!");
            } else if (this.extra == null && extraId != null) {
                throw new IllegalArgumentException("Event handler '" + this + "' doesn't support extra id " + extraId + "!");
            } else {
                EventResult eventResult = EventResult.PASS;
                try {
                    EventHandlerContainer[] extraContainers = this.extraEventContainers == null ? null : (EventHandlerContainer[]) this.extraEventContainers.get(extraId);
                    if (extraContainers != null) {
                        this.postToHandlers(scriptType, extraContainers, event, exh);
                        if (!scriptType.isStartup()) {
                            this.postToHandlers(ScriptType.STARTUP, extraContainers, event, exh);
                        }
                    }
                    if (this.eventContainers != null) {
                        this.postToHandlers(scriptType, this.eventContainers, event, exh);
                        if (!scriptType.isStartup()) {
                            this.postToHandlers(ScriptType.STARTUP, this.eventContainers, event, exh);
                        }
                    }
                } catch (EventExit var8) {
                    if (var8.result.type() == EventResult.Type.ERROR) {
                        if (DevProperties.get().debugInfo) {
                            ((Throwable) var8.result.value()).printStackTrace();
                        }
                        scriptType.console.error("Error in '" + this + "'", (Throwable) var8.result.value());
                    } else {
                        eventResult = var8.result;
                        if (!this.getHasResult()) {
                            scriptType.console.error("Error in '" + this + "'", new IllegalStateException("Event returned result when it's not cancellable"));
                        }
                    }
                } catch (RhinoException var9) {
                    scriptType.console.error("Error in '" + this + "'", var9);
                }
                event.afterPosted(eventResult);
                return eventResult;
            }
        }
    }

    private void postToHandlers(ScriptType type, EventHandlerContainer[] containers, EventJS event, @Nullable EventExceptionHandler exh) throws EventExit {
        EventHandlerContainer handler = containers[type.ordinal()];
        if (handler != null) {
            handler.handle(event, exh);
        }
    }

    @Override
    public String toString() {
        return this.group + "." + this.name;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        ScriptType type = cx.getProperty("Type", null);
        if (type == null) {
            throw new IllegalStateException("Unknown script type!");
        } else {
            try {
                if (args.length == 1) {
                    this.listen(type, null, (IEventHandler) Context.jsToJava(cx, args[0], IEventHandler.class));
                } else if (args.length == 2) {
                    IEventHandler handler = (IEventHandler) Context.jsToJava(cx, args[1], IEventHandler.class);
                    for (Object o : ListJS.orSelf(args[0])) {
                        this.listen(type, o, handler);
                    }
                }
            } catch (Exception var9) {
                type.console.error(var9.getLocalizedMessage());
            }
            return null;
        }
    }

    public void forEachListener(ScriptType type, Consumer<EventHandlerContainer> callback) {
        if (this.eventContainers != null) {
            for (EventHandlerContainer c = this.eventContainers[type.ordinal()]; c != null; c = c.child) {
                callback.accept(c);
            }
        }
        if (this.extraEventContainers != null) {
            for (Entry<Object, EventHandlerContainer[]> entry : this.extraEventContainers.entrySet()) {
                for (EventHandlerContainer c = ((EventHandlerContainer[]) entry.getValue())[type.ordinal()]; c != null; c = c.child) {
                    callback.accept(c);
                }
            }
        }
    }

    public Set<Object> findUniqueExtraIds(ScriptType type) {
        if (!this.hasListeners()) {
            return Set.of();
        } else {
            LinkedHashSet<Object> set = new LinkedHashSet();
            this.forEachListener(type, c -> {
                if (c.extraId != null) {
                    set.add(c.extraId);
                }
            });
            return set;
        }
    }
}