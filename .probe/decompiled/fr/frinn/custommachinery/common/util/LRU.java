package fr.frinn.custommachinery.common.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public class LRU<T> extends AbstractCollection<T> {

    private final Map<T, LRU.LRUEntry<T>> lookupMap = new Object2ObjectOpenHashMap();

    private final LRU.LRUEntry<T> head = new LRU.LRUEntry<>(null);

    private final LRU.LRUEntry<T> tail = new LRU.LRUEntry<>(null);

    private int size;

    public LRU() {
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    private void removeEntry(LRU.LRUEntry<T> entry) {
        entry.next.prev = entry.prev;
        entry.prev.next = entry.next;
        this.size--;
        this.lookupMap.remove(entry.value);
    }

    private void addFirst(LRU.LRUEntry<T> entry) {
        entry.prev = this.head;
        entry.next = this.head.next;
        entry.next.prev = entry;
        this.head.next = entry;
        this.size++;
        this.lookupMap.put(entry.value, entry);
    }

    public boolean add(T element) {
        this.addFirst(new LRU.LRUEntry<>(element));
        return true;
    }

    public void moveUp(T element) {
        LRU.LRUEntry<T> entry = (LRU.LRUEntry<T>) this.lookupMap.get(element);
        if (entry != null) {
            this.removeEntry(entry);
            this.addFirst(entry);
        }
    }

    public boolean remove(Object element) {
        LRU.LRUEntry<T> entry = (LRU.LRUEntry<T>) this.lookupMap.get(element);
        if (entry == null) {
            return false;
        } else {
            this.removeEntry(entry);
            return true;
        }
    }

    public boolean contains(Object element) {
        return this.lookupMap.containsKey(element);
    }

    public int size() {
        return this.size;
    }

    public void reverseIterate(Consumer<T> callback) {
        for (LRU.LRUEntry<T> ptr = this.tail.prev; ptr != this.head; ptr = ptr.prev) {
            callback.accept(ptr.value);
        }
    }

    @NotNull
    public LRU<T>.LRUIterator iterator() {
        return new LRU.LRUIterator();
    }

    public LRU<T>.LRUIterator descendingIterator() {
        return new LRU.LRUIterator().reverse();
    }

    private static class LRUEntry<T> {

        private final T value;

        private LRU.LRUEntry<T> prev;

        private LRU.LRUEntry<T> next;

        private LRUEntry(T value) {
            this.value = value;
        }
    }

    public class LRUIterator implements Iterator<T> {

        boolean reverse = false;

        LRU.LRUEntry<T> curEntry = LRU.this.head;

        public boolean hasNext() {
            return this.reverse ? this.curEntry.prev != LRU.this.head : this.curEntry.next != LRU.this.tail;
        }

        public T next() {
            if (this.reverse) {
                this.curEntry = this.curEntry.prev;
                if (this.curEntry == LRU.this.head) {
                    throw new NoSuchElementException("Reached beginning of LRU");
                }
            } else {
                this.curEntry = this.curEntry.next;
                if (this.curEntry == LRU.this.tail) {
                    throw new NoSuchElementException("Reached end of LRU");
                }
            }
            return this.curEntry == null ? null : this.curEntry.value;
        }

        public LRU<T>.LRUIterator reverse() {
            this.reverse = true;
            this.curEntry = LRU.this.tail;
            return this;
        }
    }
}