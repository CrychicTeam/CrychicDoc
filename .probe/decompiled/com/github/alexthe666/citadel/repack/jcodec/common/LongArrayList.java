package com.github.alexthe666.citadel.repack.jcodec.common;

import java.util.Arrays;

public class LongArrayList {

    private static final int DEFAULT_GROW_AMOUNT = 128;

    private long[] storage;

    private int limit;

    private int start;

    private int growAmount;

    public static LongArrayList createLongArrayList() {
        return new LongArrayList(128);
    }

    public LongArrayList(int growAmount) {
        this.growAmount = growAmount;
        this.storage = new long[growAmount];
    }

    public long[] toArray() {
        long[] result = new long[this.limit - this.start];
        System.arraycopy(this.storage, this.start, result, 0, this.limit - this.start);
        return result;
    }

    public void add(long val) {
        if (this.limit > this.storage.length - 1) {
            long[] ns = new long[this.storage.length + this.growAmount - this.start];
            System.arraycopy(this.storage, this.start, ns, 0, this.storage.length - this.start);
            this.storage = ns;
            this.limit = this.limit - this.start;
            this.start = 0;
        }
        this.storage[this.limit++] = val;
    }

    public void push(long id) {
        this.add(id);
    }

    public long pop() {
        if (this.limit <= this.start) {
            throw new IllegalStateException();
        } else {
            return this.storage[this.limit--];
        }
    }

    public void set(int index, int value) {
        this.storage[index + this.start] = (long) value;
    }

    public long get(int index) {
        return this.storage[index + this.start];
    }

    public long shift() {
        if (this.start >= this.limit) {
            throw new IllegalStateException();
        } else {
            return this.storage[this.start++];
        }
    }

    public void fill(int from, int to, int val) {
        if (to > this.storage.length) {
            long[] ns = new long[to + this.growAmount - this.start];
            System.arraycopy(this.storage, this.start, ns, 0, this.storage.length - this.start);
            this.storage = ns;
        }
        Arrays.fill(this.storage, from, to, (long) val);
        this.limit = Math.max(this.limit, to);
    }

    public int size() {
        return this.limit - this.start;
    }

    public void addAll(long[] other) {
        if (this.limit + other.length >= this.storage.length) {
            long[] ns = new long[this.limit + this.growAmount + other.length - this.start];
            System.arraycopy(this.storage, this.start, ns, 0, this.limit);
            this.storage = ns;
        }
        System.arraycopy(other, 0, this.storage, this.limit, other.length);
        this.limit += other.length;
    }

    public void clear() {
        this.limit = 0;
        this.start = 0;
    }

    public boolean contains(long needle) {
        for (int i = this.start; i < this.limit; i++) {
            if (this.storage[i] == needle) {
                return true;
            }
        }
        return false;
    }
}