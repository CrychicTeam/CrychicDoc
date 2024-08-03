package icyllis.arc3d.engine;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.Arrays;

public non-sealed class KeyBuilder extends IntArrayList implements Key {

    private transient int mCurValue = 0;

    private transient int mBitsUsed = 0;

    public KeyBuilder() {
    }

    public KeyBuilder(KeyBuilder other) {
        super(other);
        assert other.mCurValue == 0 && other.mBitsUsed == 0;
    }

    public final void clear() {
        assert this.mCurValue == 0 && this.mBitsUsed == 0;
        super.clear();
    }

    public final int size() {
        assert this.mCurValue == 0 && this.mBitsUsed == 0;
        return super.size();
    }

    public final boolean isEmpty() {
        assert this.mCurValue == 0 && this.mBitsUsed == 0;
        return super.isEmpty();
    }

    public void addBits(int numBits, int value, String label) {
        assert numBits > 0 && numBits <= 32;
        assert numBits == 32 || 32 - numBits <= Integer.numberOfLeadingZeros(value);
        this.mCurValue = this.mCurValue | value << this.mBitsUsed;
        this.mBitsUsed += numBits;
        if (this.mBitsUsed >= 32) {
            this.add(this.mCurValue);
            int excess = this.mBitsUsed - 32;
            this.mCurValue = excess != 0 ? value >>> numBits - excess : 0;
            this.mBitsUsed = excess;
        }
        assert 32 - this.mBitsUsed <= Integer.numberOfLeadingZeros(this.mCurValue);
    }

    public final void addBool(boolean b, String label) {
        this.addBits(1, b ? 1 : 0, label);
    }

    public final void addInt32(int v, String label) {
        this.addBits(32, v, label);
    }

    public void appendComment(String comment) {
    }

    public final void flush() {
        if (this.mBitsUsed != 0) {
            this.add(this.mCurValue);
            this.mCurValue = 0;
            this.mBitsUsed = 0;
        }
    }

    public final void trim() {
        assert this.mCurValue == 0 && this.mBitsUsed == 0;
        super.trim();
    }

    public final Key.StorageKey toStorageKey() {
        assert this.mCurValue == 0 && this.mBitsUsed == 0;
        return new Key.StorageKey(this);
    }

    public final int hashCode() {
        assert this.mCurValue == 0 && this.mBitsUsed == 0;
        int[] e = this.elements();
        int h = 1;
        int s = this.size();
        for (int i = 0; i < s; i++) {
            h = 31 * h + e[i];
        }
        return h;
    }

    public final boolean equals(Object o) {
        assert this.mCurValue == 0 && this.mBitsUsed == 0;
        if (o instanceof Key.StorageKey key && Arrays.equals(this.elements(), 0, this.size(), key.data, 0, key.data.length)) {
            return true;
        }
        return false;
    }

    public static class StringKeyBuilder extends KeyBuilder {

        public final StringBuilder mStringBuilder = new StringBuilder();

        @Override
        public void addBits(int numBits, int value, String label) {
            super.addBits(numBits, value, label);
            this.mStringBuilder.append(label).append(": ").append((long) value & 4294967295L).append('\n');
        }

        @Override
        public void appendComment(String comment) {
            this.mStringBuilder.append(comment).append('\n');
        }

        public String toString() {
            return this.mStringBuilder.toString();
        }
    }
}