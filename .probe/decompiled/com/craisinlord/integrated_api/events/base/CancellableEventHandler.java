package com.craisinlord.integrated_api.events.base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CancellableEventHandler<T> {

    private final List<CancellableEventHandler.CancellableFunction<T>> listeners = new ArrayList();

    public void addListener(CancellableEventHandler.CancellableFunction<T> listener) {
        this.listeners.add(listener);
    }

    public void addListener(CancellableEventHandler.CancellableFunctionNoReturn<T> listener) {
        this.addListener((CancellableEventHandler.CancellableFunction<T>) ((b, t) -> {
            listener.apply(b, t);
            return false;
        }));
    }

    public void addListener(CancellableEventHandler.CancellableFunctionOnlyReturn<T> listener) {
        this.addListener((CancellableEventHandler.CancellableFunction<T>) ((b, t) -> listener.apply(t)));
    }

    public void addListener(Consumer<T> listener) {
        this.addListener((CancellableEventHandler.CancellableFunctionOnlyReturn<T>) (t -> {
            listener.accept(t);
            return false;
        }));
    }

    public void removeListener(CancellableEventHandler.CancellableFunction<T> listener) {
        this.listeners.remove(listener);
    }

    public boolean invoke(T event, boolean isCancelled) {
        for (CancellableEventHandler.CancellableFunction<T> listener : this.listeners) {
            if (listener.apply(isCancelled, event)) {
                isCancelled = true;
            }
        }
        return isCancelled;
    }

    public boolean invoke(T event) {
        return this.invoke(event, false);
    }

    @FunctionalInterface
    public interface CancellableFunction<T> {

        boolean apply(boolean var1, T var2);
    }

    @FunctionalInterface
    public interface CancellableFunctionNoReturn<T> {

        void apply(boolean var1, T var2);
    }

    @FunctionalInterface
    public interface CancellableFunctionOnlyReturn<T> {

        boolean apply(T var1);
    }
}