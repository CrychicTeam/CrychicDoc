package me.lucko.spark.common.sampler.async.jfr;

public class Dictionary<T> {

    private static final int INITIAL_CAPACITY = 16;

    private long[] keys = new long[16];

    private Object[] values = new Object[16];

    private int size;

    public void clear() {
        this.keys = new long[16];
        this.values = new Object[16];
        this.size = 0;
    }

    public int size() {
        return this.size;
    }

    public void put(long key, T value) {
        if (key == 0L) {
            throw new IllegalArgumentException("Zero key not allowed");
        } else {
            int mask = this.keys.length - 1;
            int i;
            for (i = hashCode(key) & mask; this.keys[i] != 0L; i = i + 1 & mask) {
                if (this.keys[i] == key) {
                    this.values[i] = value;
                    return;
                }
            }
            this.keys[i] = key;
            this.values[i] = value;
            if (++this.size * 2 > this.keys.length) {
                this.resize(this.keys.length * 2);
            }
        }
    }

    public T get(long key) {
        int mask = this.keys.length - 1;
        int i = hashCode(key) & mask;
        while (this.keys[i] != key && this.keys[i] != 0L) {
            i = i + 1 & mask;
        }
        return (T) this.values[i];
    }

    public void forEach(Dictionary.Visitor<T> visitor) {
        for (int i = 0; i < this.keys.length; i++) {
            if (this.keys[i] != 0L) {
                visitor.visit(this.keys[i], (T) this.values[i]);
            }
        }
    }

    public int preallocate(int count) {
        if (count * 2 > this.keys.length) {
            this.resize(Integer.highestOneBit(count * 4 - 1));
        }
        return count;
    }

    private void resize(int newCapacity) {
        long[] newKeys = new long[newCapacity];
        Object[] newValues = new Object[newCapacity];
        int mask = newKeys.length - 1;
        for (int i = 0; i < this.keys.length; i++) {
            if (this.keys[i] != 0L) {
                int j = hashCode(this.keys[i]) & mask;
                while (newKeys[j] != 0L) {
                    j = j + 1 & mask;
                }
                newKeys[j] = this.keys[i];
                newValues[j] = this.values[i];
            }
        }
        this.keys = newKeys;
        this.values = newValues;
    }

    private static int hashCode(long key) {
        key *= -4132994306676758123L;
        return (int) (key ^ key >>> 32);
    }

    public interface Visitor<T> {

        void visit(long var1, T var3);
    }
}