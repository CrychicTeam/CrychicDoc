package snownee.kiwi.util;

import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public class CachedSupplier<T> implements Supplier<T> {

    private final Object lock = new Object();

    private Supplier<T> delegate;

    @Nullable
    private T value;

    @Nullable
    private T fallback;

    public CachedSupplier(Supplier<T> delegate) {
        this.delegate = delegate;
    }

    public CachedSupplier(Supplier<T> delegate, @Nullable T fallback) {
        this.delegate = delegate;
        this.fallback = fallback;
    }

    public T get() {
        if (this.value != null) {
            return this.value;
        } else {
            synchronized (this.lock) {
                if (this.value == null) {
                    this.value = (T) this.delegate.get();
                }
                if (this.value != null) {
                    this.delegate = null;
                }
            }
            return this.value != null ? this.value : this.fallback;
        }
    }
}