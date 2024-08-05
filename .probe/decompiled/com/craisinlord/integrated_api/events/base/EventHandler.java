package com.craisinlord.integrated_api.events.base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventHandler<T> {

    private final List<Consumer<T>> listeners = new ArrayList();

    public void addListener(Consumer<T> listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Consumer<T> listener) {
        this.listeners.remove(listener);
    }

    public void invoke(T event) {
        for (Consumer<T> listener : this.listeners) {
            listener.accept(event);
        }
    }
}