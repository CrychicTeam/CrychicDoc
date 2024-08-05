package dev.kosmx.playerAnim.core.impl.event;

import java.util.ArrayList;

public class Event<T> {

    final ArrayList<T> listeners = new ArrayList();

    final Event.Invoker<T> _invoker;

    public Event(Class<T> clazz, Event.Invoker<T> invoker) {
        this._invoker = invoker;
    }

    public final T invoker() {
        return this._invoker.invoker(this.listeners);
    }

    public void register(T listener) {
        if (listener == null) {
            throw new NullPointerException("listener can not be null");
        } else {
            this.listeners.add(listener);
        }
    }

    public void unregister(T listener) {
        this.listeners.remove(listener);
    }

    @FunctionalInterface
    public interface Invoker<T> {

        T invoker(Iterable<T> var1);
    }
}