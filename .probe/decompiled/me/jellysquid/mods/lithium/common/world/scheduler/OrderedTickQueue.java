package me.jellysquid.mods.lithium.common.world.scheduler;

import it.unimi.dsi.fastutil.HashCommon;
import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import net.minecraft.world.ticks.ScheduledTick;

public class OrderedTickQueue<T> extends AbstractQueue<ScheduledTick<T>> {

    private static final int INITIAL_CAPACITY = 16;

    private static final Comparator<ScheduledTick<?>> COMPARATOR = Comparator.comparingLong(ScheduledTick::f_193380_);

    private ScheduledTick<T>[] arr;

    private int lastIndexExclusive;

    private int firstIndex;

    private long currentMaxSubTickOrder = Long.MIN_VALUE;

    private boolean isSorted;

    private ScheduledTick<T> unsortedPeekResult;

    public OrderedTickQueue(int capacity) {
        this.arr = new ScheduledTick[capacity];
        this.lastIndexExclusive = 0;
        this.isSorted = true;
        this.unsortedPeekResult = null;
        this.firstIndex = 0;
    }

    public OrderedTickQueue() {
        this(16);
    }

    public void clear() {
        Arrays.fill(this.arr, null);
        this.lastIndexExclusive = 0;
        this.firstIndex = 0;
        this.currentMaxSubTickOrder = Long.MIN_VALUE;
        this.isSorted = true;
        this.unsortedPeekResult = null;
    }

    public Iterator<ScheduledTick<T>> iterator() {
        if (this.isEmpty()) {
            return Collections.emptyIterator();
        } else {
            this.sort();
            return new Iterator<ScheduledTick<T>>() {

                int nextIndex = OrderedTickQueue.this.firstIndex;

                public boolean hasNext() {
                    return this.nextIndex < OrderedTickQueue.this.lastIndexExclusive;
                }

                public ScheduledTick<T> next() {
                    return OrderedTickQueue.this.arr[this.nextIndex++];
                }
            };
        }
    }

    public ScheduledTick<T> poll() {
        if (this.isEmpty()) {
            return null;
        } else {
            if (!this.isSorted) {
                this.sort();
            }
            int polledIndex = this.firstIndex++;
            ScheduledTick<T>[] ticks = this.arr;
            ScheduledTick<T> nextTick = ticks[polledIndex];
            ticks[polledIndex] = null;
            return nextTick;
        }
    }

    public ScheduledTick<T> peek() {
        if (!this.isSorted) {
            return this.unsortedPeekResult;
        } else {
            return this.lastIndexExclusive > this.firstIndex ? this.getTickAtIndex(this.firstIndex) : null;
        }
    }

    public boolean offer(ScheduledTick<T> tick) {
        if (this.lastIndexExclusive >= this.arr.length) {
            this.arr = copyArray(this.arr, HashCommon.nextPowerOfTwo(this.arr.length + 1));
        }
        if (tick.subTickOrder() <= this.currentMaxSubTickOrder) {
            ScheduledTick<T> firstTick = this.isSorted ? (this.size() > 0 ? this.arr[this.firstIndex] : null) : this.unsortedPeekResult;
            this.isSorted = false;
            this.unsortedPeekResult = firstTick != null && tick.subTickOrder() >= firstTick.subTickOrder() ? firstTick : tick;
        } else {
            this.currentMaxSubTickOrder = tick.subTickOrder();
        }
        this.arr[this.lastIndexExclusive++] = tick;
        return true;
    }

    public int size() {
        return this.lastIndexExclusive - this.firstIndex;
    }

    private void handleCompaction(int size) {
        if (this.arr.length > 16 && size < this.arr.length / 2) {
            this.arr = copyArray(this.arr, size);
        } else {
            Arrays.fill(this.arr, size, this.arr.length, null);
        }
        this.firstIndex = 0;
        this.lastIndexExclusive = size;
        if (size != 0 && this.isSorted) {
            ScheduledTick<T> tick = this.arr[size - 1];
            this.currentMaxSubTickOrder = tick == null ? Long.MIN_VALUE : tick.subTickOrder();
        } else {
            this.currentMaxSubTickOrder = Long.MIN_VALUE;
        }
    }

    public void sort() {
        if (!this.isSorted) {
            this.removeNullsAndConsumed();
            Arrays.sort(this.arr, this.firstIndex, this.lastIndexExclusive, COMPARATOR);
            this.isSorted = true;
            this.unsortedPeekResult = null;
        }
    }

    public void removeNullsAndConsumed() {
        int src = this.firstIndex;
        int dst;
        for (dst = 0; src < this.lastIndexExclusive; src++) {
            ScheduledTick<T> orderedTick = this.arr[src];
            if (orderedTick != null) {
                this.arr[dst] = orderedTick;
                dst++;
            }
        }
        this.handleCompaction(dst);
    }

    public ScheduledTick<T> getTickAtIndex(int index) {
        if (!this.isSorted) {
            throw new IllegalStateException("Unexpected access on unsorted queue!");
        } else {
            return this.arr[index];
        }
    }

    public void setTickAtIndex(int index, ScheduledTick<T> tick) {
        if (!this.isSorted) {
            throw new IllegalStateException("Unexpected access on unsorted queue!");
        } else {
            this.arr[index] = tick;
        }
    }

    private static <T> ScheduledTick<T>[] copyArray(ScheduledTick<T>[] src, int size) {
        ScheduledTick<T>[] copy = new ScheduledTick[Math.max(16, size)];
        if (size != 0) {
            System.arraycopy(src, 0, copy, 0, Math.min(src.length, size));
        }
        return copy;
    }

    public boolean isEmpty() {
        return this.lastIndexExclusive <= this.firstIndex;
    }
}