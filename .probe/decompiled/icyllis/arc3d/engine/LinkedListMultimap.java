package icyllis.arc3d.engine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class LinkedListMultimap<K, V> extends HashMap<K, LinkedList<V>> {

    private V mTmpValue;

    private final BiFunction<K, LinkedList<V>, LinkedList<V>> mPollFirstEntry = (k, list) -> {
        this.mTmpValue = (V) list.pollFirst();
        return list.isEmpty() ? null : list;
    };

    private final BiFunction<K, LinkedList<V>, LinkedList<V>> mPollLastEntry = (k, list) -> {
        this.mTmpValue = (V) list.pollLast();
        return list.isEmpty() ? null : list;
    };

    private final BiFunction<K, LinkedList<V>, LinkedList<V>> mRemoveFirstEntry = (k, list) -> list.removeFirstOccurrence(this.mTmpValue) && list.isEmpty() ? null : list;

    private final BiFunction<K, LinkedList<V>, LinkedList<V>> mRemoveLastEntry = (k, list) -> list.removeLastOccurrence(this.mTmpValue) && list.isEmpty() ? null : list;

    public LinkedListMultimap() {
    }

    public LinkedListMultimap(@Nonnull Map<? extends K, ? extends LinkedList<V>> other) {
        super(other);
    }

    public void addFirstEntry(@Nonnull K k, @Nonnull V v) {
        ((LinkedList) this.computeIfAbsent(k, __ -> new LinkedList())).addFirst(Objects.requireNonNull(v));
    }

    public void addLastEntry(@Nonnull K k, @Nonnull V v) {
        ((LinkedList) this.computeIfAbsent(k, __ -> new LinkedList())).addLast(Objects.requireNonNull(v));
    }

    @Nullable
    public V pollFirstEntry(@Nonnull K k) {
        assert this.mTmpValue == null;
        this.computeIfPresent(k, this.mPollFirstEntry);
        V v = this.mTmpValue;
        this.mTmpValue = null;
        return v;
    }

    @Nullable
    public V pollLastEntry(@Nonnull K k) {
        assert this.mTmpValue == null;
        this.computeIfPresent(k, this.mPollLastEntry);
        V v = this.mTmpValue;
        this.mTmpValue = null;
        return v;
    }

    public void removeFirstEntry(@Nonnull K k, @Nonnull V v) {
        assert this.mTmpValue == null;
        this.mTmpValue = v;
        this.computeIfPresent(k, this.mRemoveFirstEntry);
        assert this.mTmpValue == v;
        this.mTmpValue = null;
    }

    public void removeLastEntry(@Nonnull K k, @Nonnull V v) {
        assert this.mTmpValue == null;
        this.mTmpValue = v;
        this.computeIfPresent(k, this.mRemoveLastEntry);
        assert this.mTmpValue == v;
        this.mTmpValue = null;
    }
}