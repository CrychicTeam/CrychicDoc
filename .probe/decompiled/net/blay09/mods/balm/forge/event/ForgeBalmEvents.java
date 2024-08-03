package net.blay09.mods.balm.forge.event;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.EventPriority;
import net.blay09.mods.balm.api.event.TickPhase;
import net.blay09.mods.balm.api.event.TickType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

public class ForgeBalmEvents implements BalmEvents {

    private final Table<Class<?>, EventPriority, Consumer<EventPriority>> eventInitializers = HashBasedTable.create();

    private final Map<Class<?>, Consumer<?>> eventDispatchers = new HashMap();

    private final Table<Class<?>, EventPriority, List<Consumer<?>>> eventHandlers = HashBasedTable.create();

    private final Table<TickType<?>, TickPhase, Consumer<?>> tickEventInitializers = HashBasedTable.create();

    public void registerEvent(Class<?> eventClass, Consumer<EventPriority> initializer) {
        this.registerEvent(eventClass, initializer, null);
    }

    public void registerEvent(Class<?> eventClass, Consumer<EventPriority> initializer, @Nullable Consumer<?> dispatcher) {
        for (EventPriority priority : EventPriority.values()) {
            this.eventInitializers.put(eventClass, priority, initializer);
        }
        if (dispatcher != null) {
            this.eventDispatchers.put(eventClass, dispatcher);
        }
    }

    public <T> void fireEventHandlers(EventPriority priority, T event) {
        List<Consumer<?>> handlers = (List<Consumer<?>>) this.eventHandlers.get(event.getClass(), priority);
        if (handlers != null) {
            handlers.forEach(handler -> this.fireEventHandler(handler, event));
        }
    }

    private <T> void fireEventHandler(Consumer<T> handler, Object event) {
        handler.accept(event);
    }

    @Override
    public <T> void onEvent(Class<T> eventClass, Consumer<T> handler, EventPriority priority) {
        Consumer<EventPriority> initializer = (Consumer<EventPriority>) this.eventInitializers.remove(eventClass, priority);
        if (initializer != null) {
            initializer.accept(priority);
        }
        List<Consumer<?>> consumers = (List<Consumer<?>>) this.eventHandlers.get(eventClass, priority);
        if (consumers == null) {
            consumers = new ArrayList();
            this.eventHandlers.put(eventClass, priority, consumers);
        }
        consumers.add(handler);
    }

    @Override
    public <T> void fireEvent(T event) {
        Consumer<T> handler = (Consumer<T>) this.eventDispatchers.get(event.getClass());
        if (handler != null) {
            handler.accept(event);
        } else {
            for (EventPriority priority : EventPriority.values) {
                this.fireEventHandlers(priority, event);
            }
        }
        if (event instanceof Event forgeEvent) {
            MinecraftForge.EVENT_BUS.post(forgeEvent);
        }
    }

    @Override
    public <T> void onTickEvent(TickType<T> type, TickPhase phase, T handler) {
        Consumer<T> initializer = (Consumer<T>) this.tickEventInitializers.get(type, phase);
        initializer.accept(handler);
    }

    public <T> void registerTickEvent(TickType<?> type, TickPhase phase, Consumer<T> initializer) {
        this.tickEventInitializers.put(type, phase, initializer);
    }

    public static net.minecraftforge.eventbus.api.EventPriority toForge(EventPriority priority) {
        return switch(priority) {
            case Lowest ->
                net.minecraftforge.eventbus.api.EventPriority.LOWEST;
            case Low ->
                net.minecraftforge.eventbus.api.EventPriority.LOW;
            case Normal ->
                net.minecraftforge.eventbus.api.EventPriority.NORMAL;
            case High ->
                net.minecraftforge.eventbus.api.EventPriority.HIGH;
            case Highest ->
                net.minecraftforge.eventbus.api.EventPriority.HIGHEST;
        };
    }
}