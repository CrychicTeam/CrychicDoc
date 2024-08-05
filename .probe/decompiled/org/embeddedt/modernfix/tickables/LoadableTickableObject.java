package org.embeddedt.modernfix.tickables;

import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public class LoadableTickableObject<T> implements TickableObject {

    private volatile int ticksInactive = 0;

    private final int timeout;

    private final Supplier<T> loader;

    private final Consumer<T> finalizer;

    private volatile T theObject = (T) null;

    public LoadableTickableObject(int timeout, Supplier<T> loader, Consumer<T> finalizer) {
        this(timeout, loader, finalizer, null);
    }

    public LoadableTickableObject(int timeout, Supplier<T> loader, Consumer<T> finalizer, @Nullable T initialValue) {
        this.timeout = timeout;
        this.loader = loader;
        this.finalizer = finalizer;
        this.theObject = initialValue;
    }

    public T get() {
        synchronized (this) {
            this.ticksInactive++;
            T obj = this.theObject;
            if (obj == null) {
                obj = (T) this.loader.get();
                this.theObject = obj;
            }
            return obj;
        }
    }

    @Override
    public final void tick() {
        synchronized (this) {
            this.ticksInactive++;
            if (this.ticksInactive >= this.timeout) {
                this.finalizer.accept(this.theObject);
                this.theObject = null;
            }
        }
    }
}