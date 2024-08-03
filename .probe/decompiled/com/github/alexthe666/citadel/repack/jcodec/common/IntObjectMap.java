package com.github.alexthe666.citadel.repack.jcodec.common;

import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.lang.reflect.Array;

public class IntObjectMap<T> {

    private static final int GROW_BY = 128;

    private Object[] storage = new Object[128];

    private int _size;

    public void put(int key, T val) {
        if (this.storage.length <= key) {
            Object[] ns = new Object[key + 128];
            System.arraycopy(this.storage, 0, ns, 0, this.storage.length);
            this.storage = ns;
        }
        if (this.storage[key] == null) {
            this._size++;
        }
        this.storage[key] = val;
    }

    public T get(int key) {
        return (T) (key >= this.storage.length ? null : this.storage[key]);
    }

    public int[] keys() {
        int[] result = new int[this._size];
        int i = 0;
        for (int r = 0; i < this.storage.length; i++) {
            if (this.storage[i] != null) {
                result[r++] = i;
            }
        }
        return result;
    }

    public void clear() {
        for (int i = 0; i < this.storage.length; i++) {
            this.storage[i] = null;
        }
        this._size = 0;
    }

    public int size() {
        return this._size;
    }

    public void remove(int key) {
        if (this.storage[key] != null) {
            this._size--;
        }
        this.storage[key] = null;
    }

    public T[] values(T[] runtime) {
        T[] result = (T[]) ((Object[]) Array.newInstance(Platform.arrayComponentType(runtime), this._size));
        int i = 0;
        for (int r = 0; i < this.storage.length; i++) {
            if (this.storage[i] != null) {
                result[r++] = (T) this.storage[i];
            }
        }
        return result;
    }
}