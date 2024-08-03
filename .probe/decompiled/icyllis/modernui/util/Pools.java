package icyllis.modernui.util;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.util.Objects;

public final class Pools {

    private Pools() {
    }

    @NonNull
    public static <T> Pools.Pool<T> newSimplePool(int maxPoolSize) {
        return new Pools.SimplePool<>(maxPoolSize);
    }

    @NonNull
    public static <T> Pools.Pool<T> newSynchronizedPool(int maxPoolSize) {
        return new Pools.SynchronizedPool<>(maxPoolSize);
    }

    @NonNull
    public static <T> Pools.Pool<T> newSynchronizedPool(int maxPoolSize, @NonNull Object lock) {
        return new Pools.SynchronizedPool<>(maxPoolSize, lock);
    }

    public interface Pool<T> {

        @Nullable
        T acquire();

        boolean release(@NonNull T var1);
    }

    public static class SimplePool<T> implements Pools.Pool<T> {

        private final T[] mPool;

        private int mPoolSize;

        public SimplePool(int maxPoolSize) {
            if (maxPoolSize <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            } else {
                this.mPool = (T[]) (new Object[maxPoolSize]);
            }
        }

        @Nullable
        @Override
        public T acquire() {
            if (this.mPoolSize == 0) {
                return null;
            } else {
                int i = --this.mPoolSize;
                T instance = this.mPool[i];
                this.mPool[i] = null;
                return instance;
            }
        }

        @Override
        public boolean release(@NonNull T instance) {
            if (this.mPoolSize == this.mPool.length) {
                return false;
            } else {
                for (int i = this.mPoolSize - 1; i >= 0; i--) {
                    if (this.mPool[i] == instance) {
                        throw new IllegalStateException("Already in the pool!");
                    }
                }
                this.mPool[this.mPoolSize++] = instance;
                return true;
            }
        }
    }

    public static class SynchronizedPool<T> extends Pools.SimplePool<T> {

        private final Object mLock;

        public SynchronizedPool(int maxPoolSize) {
            super(maxPoolSize);
            this.mLock = this;
        }

        public SynchronizedPool(int maxPoolSize, @NonNull Object lock) {
            super(maxPoolSize);
            this.mLock = Objects.requireNonNull(lock);
        }

        @Nullable
        @Override
        public T acquire() {
            synchronized (this.mLock) {
                return super.acquire();
            }
        }

        @Override
        public boolean release(@NonNull T element) {
            synchronized (this.mLock) {
                return super.release(element);
            }
        }
    }
}