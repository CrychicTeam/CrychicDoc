package com.github.alexthe666.citadel.repack.jcodec.common;

import java.util.Arrays;

public class ByteArrayList {

    private static final int DEFAULT_GROW_AMOUNT = 2048;

    private byte[] storage;

    private int _size;

    private int growAmount;

    public static ByteArrayList createByteArrayList() {
        return new ByteArrayList(2048);
    }

    public ByteArrayList(int growAmount) {
        this.growAmount = growAmount;
        this.storage = new byte[growAmount];
    }

    public byte[] toArray() {
        byte[] result = new byte[this._size];
        System.arraycopy(this.storage, 0, result, 0, this._size);
        return result;
    }

    public void add(byte val) {
        if (this._size >= this.storage.length) {
            byte[] ns = new byte[this.storage.length + this.growAmount];
            System.arraycopy(this.storage, 0, ns, 0, this.storage.length);
            this.storage = ns;
        }
        this.storage[this._size++] = val;
    }

    public void push(byte id) {
        this.add(id);
    }

    public void pop() {
        if (this._size != 0) {
            this._size--;
        }
    }

    public void set(int index, byte value) {
        this.storage[index] = value;
    }

    public byte get(int index) {
        return this.storage[index];
    }

    public void fill(int start, int end, byte val) {
        if (end > this.storage.length) {
            byte[] ns = new byte[end + this.growAmount];
            System.arraycopy(this.storage, 0, ns, 0, this.storage.length);
            this.storage = ns;
        }
        Arrays.fill(this.storage, start, end, val);
        this._size = Math.max(this._size, end);
    }

    public int size() {
        return this._size;
    }

    public void addAll(byte[] other) {
        if (this._size + other.length >= this.storage.length) {
            byte[] ns = new byte[this._size + this.growAmount + other.length];
            System.arraycopy(this.storage, 0, ns, 0, this._size);
            this.storage = ns;
        }
        System.arraycopy(other, 0, this.storage, this._size, other.length);
        this._size += other.length;
    }

    public boolean contains(byte needle) {
        for (int i = 0; i < this._size; i++) {
            if (this.storage[i] == needle) {
                return true;
            }
        }
        return false;
    }
}