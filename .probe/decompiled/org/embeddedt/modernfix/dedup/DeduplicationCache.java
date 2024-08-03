package org.embeddedt.modernfix.dedup;

import it.unimi.dsi.fastutil.Hash.Strategy;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import java.util.Objects;

public class DeduplicationCache<T> {

    private final ObjectOpenCustomHashSet<T> pool;

    private int attemptedInsertions = 0;

    private int deduplicated = 0;

    public DeduplicationCache(Strategy<T> strategy) {
        this.pool = new ObjectOpenCustomHashSet(strategy);
    }

    public DeduplicationCache() {
        this.pool = new ObjectOpenCustomHashSet(new Strategy<T>() {

            public int hashCode(T o) {
                return Objects.hashCode(o);
            }

            public boolean equals(T a, T b) {
                return Objects.equals(a, b);
            }
        });
    }

    public synchronized T deduplicate(T item) {
        this.attemptedInsertions++;
        T result = (T) this.pool.addOrGet(item);
        if (result != item) {
            this.deduplicated++;
        }
        return result;
    }

    public synchronized void clearCache() {
        this.attemptedInsertions = 0;
        this.deduplicated = 0;
        this.pool.clear();
    }

    public synchronized String toString() {
        return String.format("DeduplicationCache ( %d/%d de-duplicated, %d pooled )", this.deduplicated, this.attemptedInsertions, this.pool.size());
    }
}