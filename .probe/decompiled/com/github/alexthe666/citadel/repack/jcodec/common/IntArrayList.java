package com.github.alexthe666.citadel.repack.jcodec.common;

import java.util.Arrays;

public class IntArrayList {

    private static final int DEFAULT_GROW_AMOUNT = 128;

    private int[] storage;

    private int _size;

    private int growAmount;

    public static IntArrayList createIntArrayList() {
        return new IntArrayList(128);
    }

    public IntArrayList(int growAmount) {
        this.growAmount = growAmount;
        this.storage = new int[growAmount];
    }

    public int[] toArray() {
        int[] result = new int[this._size];
        System.arraycopy(this.storage, 0, result, 0, this._size);
        return result;
    }

    public void add(int val) {
        if (this._size >= this.storage.length) {
            int[] ns = new int[this.storage.length + this.growAmount];
            System.arraycopy(this.storage, 0, ns, 0, this.storage.length);
            this.storage = ns;
        }
        this.storage[this._size++] = val;
    }

    public void push(int id) {
        this.add(id);
    }

    public void pop() {
        if (this._size != 0) {
            this._size--;
        }
    }

    public void set(int index, int value) {
        this.storage[index] = value;
    }

    public int get(int index) {
        return this.storage[index];
    }

    public void fill(int start, int end, int val) {
        if (end > this.storage.length) {
            int[] ns = new int[end + this.growAmount];
            System.arraycopy(this.storage, 0, ns, 0, this.storage.length);
            this.storage = ns;
        }
        Arrays.fill(this.storage, start, end, val);
        this._size = Math.max(this._size, end);
    }

    public int size() {
        return this._size;
    }

    public void addAll(int[] other) {
        if (this._size + other.length >= this.storage.length) {
            int[] ns = new int[this._size + this.growAmount + other.length];
            System.arraycopy(this.storage, 0, ns, 0, this._size);
            this.storage = ns;
        }
        System.arraycopy(other, 0, this.storage, this._size, other.length);
        this._size += other.length;
    }

    public void clear() {
        this._size = 0;
    }

    public boolean contains(int needle) {
        for (int i = 0; i < this._size; i++) {
            if (this.storage[i] == needle) {
                return true;
            }
        }
        return false;
    }
}