package net.minecraftforge.common.util;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Lazy<T> extends Supplier<T> {

    static <T> Lazy<T> of(@NotNull Supplier<T> supplier) {
        return new Lazy.Fast<>(supplier);
    }

    static <T> Lazy<T> concurrentOf(@NotNull Supplier<T> supplier) {
        return new Lazy.Concurrent<>(supplier);
    }

    public static final class Concurrent<T> implements Lazy<T> {

        private volatile Object lock = new Object();

        private volatile Supplier<T> supplier;

        private volatile T instance;

        private Concurrent(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Nullable
        public final T get() {
            Object localLock = this.lock;
            if (this.supplier != null) {
                synchronized (localLock) {
                    if (this.supplier != null) {
                        this.instance = (T) this.supplier.get();
                        this.supplier = null;
                        this.lock = null;
                    }
                }
            }
            return this.instance;
        }
    }

    public static final class Fast<T> implements Lazy<T> {

        private Supplier<T> supplier;

        private T instance;

        private Fast(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Nullable
        public final T get() {
            if (this.supplier != null) {
                this.instance = (T) this.supplier.get();
                this.supplier = null;
            }
            return this.instance;
        }
    }
}