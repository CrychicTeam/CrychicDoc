package me.jellysquid.mods.lithium.common.util.collections;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.AbstractList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;

public class MaskedList<E> extends AbstractList<E> {

    private final ObjectArrayList<E> allElements = new ObjectArrayList();

    private final BitSet visibleMask = new BitSet();

    private final Object2IntOpenHashMap<E> element2Index;

    private final boolean defaultVisibility;

    private int numCleared;

    public MaskedList(ObjectArrayList<E> allElements, boolean defaultVisibility) {
        this.defaultVisibility = defaultVisibility;
        this.element2Index = new Object2IntOpenHashMap();
        this.element2Index.defaultReturnValue(-1);
        this.addAll(allElements);
    }

    public MaskedList() {
        this(new ObjectArrayList(), true);
    }

    public int totalSize() {
        return this.allElements.size();
    }

    public void addOrSet(E element, boolean visible) {
        int index = this.element2Index.getInt(element);
        if (index != -1) {
            this.visibleMask.set(index, visible);
        } else {
            this.add(element);
            this.setVisible(element, visible);
        }
    }

    public void setVisible(E element, boolean visible) {
        int index = this.element2Index.getInt(element);
        if (index != -1) {
            this.visibleMask.set(index, visible);
        }
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {

            int nextIndex = 0;

            int cachedNext = -1;

            public boolean hasNext() {
                return (this.cachedNext = MaskedList.this.visibleMask.nextSetBit(this.nextIndex)) != -1;
            }

            public E next() {
                int index = this.cachedNext;
                this.cachedNext = -1;
                this.nextIndex = index + 1;
                return (E) MaskedList.this.allElements.get(index);
            }
        };
    }

    public Spliterator<E> spliterator() {
        return new AbstractSpliterator<E>(Long.MAX_VALUE, 272) {

            int nextIndex = 0;

            public boolean tryAdvance(Consumer<? super E> action) {
                int index = MaskedList.this.visibleMask.nextSetBit(this.nextIndex);
                if (index == -1) {
                    return false;
                } else {
                    this.nextIndex = index + 1;
                    action.accept(MaskedList.this.allElements.get(index));
                    return true;
                }
            }
        };
    }

    public boolean add(E e) {
        int oldIndex = this.element2Index.put(e, this.allElements.size());
        if (oldIndex != -1) {
            throw new IllegalStateException("MaskedList must not contain duplicates! Trying to add " + e + " but it is already present at index " + oldIndex + ". Current size: " + this.allElements.size());
        } else {
            this.visibleMask.set(this.allElements.size(), this.defaultVisibility);
            return this.allElements.add(e);
        }
    }

    public boolean remove(Object o) {
        int index = this.element2Index.removeInt(o);
        if (index == -1) {
            return false;
        } else {
            this.visibleMask.clear(index);
            this.allElements.set(index, null);
            this.numCleared++;
            if (this.numCleared * 2 > this.allElements.size()) {
                ObjectArrayList<E> clonedElements = this.allElements.clone();
                BitSet clonedVisibleMask = (BitSet) this.visibleMask.clone();
                this.allElements.clear();
                this.visibleMask.clear();
                this.element2Index.clear();
                for (int i = 0; i < clonedElements.size(); i++) {
                    E element = (E) clonedElements.get(i);
                    int newIndex = this.allElements.size();
                    this.allElements.add(element);
                    this.visibleMask.set(newIndex, clonedVisibleMask.get(i));
                    this.element2Index.put(element, newIndex);
                }
                this.numCleared = 0;
            }
            return true;
        }
    }

    public E get(int index) {
        if (index >= 0 && index < this.size()) {
            int i;
            for (i = 0; index >= 0; i = this.visibleMask.nextSetBit(i + 1)) {
                index--;
            }
            return (E) this.allElements.get(i);
        } else {
            throw new IndexOutOfBoundsException(index);
        }
    }

    public int size() {
        return this.visibleMask.cardinality();
    }
}