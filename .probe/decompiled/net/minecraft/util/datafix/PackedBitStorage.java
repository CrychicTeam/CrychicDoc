package net.minecraft.util.datafix;

import net.minecraft.util.Mth;
import org.apache.commons.lang3.Validate;

public class PackedBitStorage {

    private static final int BIT_TO_LONG_SHIFT = 6;

    private final long[] data;

    private final int bits;

    private final long mask;

    private final int size;

    public PackedBitStorage(int int0, int int1) {
        this(int0, int1, new long[Mth.roundToward(int1 * int0, 64) / 64]);
    }

    public PackedBitStorage(int int0, int int1, long[] long2) {
        Validate.inclusiveBetween(1L, 32L, (long) int0);
        this.size = int1;
        this.bits = int0;
        this.data = long2;
        this.mask = (1L << int0) - 1L;
        int $$3 = Mth.roundToward(int1 * int0, 64) / 64;
        if (long2.length != $$3) {
            throw new IllegalArgumentException("Invalid length given for storage, got: " + long2.length + " but expected: " + $$3);
        }
    }

    public void set(int int0, int int1) {
        Validate.inclusiveBetween(0L, (long) (this.size - 1), (long) int0);
        Validate.inclusiveBetween(0L, this.mask, (long) int1);
        int $$2 = int0 * this.bits;
        int $$3 = $$2 >> 6;
        int $$4 = (int0 + 1) * this.bits - 1 >> 6;
        int $$5 = $$2 ^ $$3 << 6;
        this.data[$$3] = this.data[$$3] & ~(this.mask << $$5) | ((long) int1 & this.mask) << $$5;
        if ($$3 != $$4) {
            int $$6 = 64 - $$5;
            int $$7 = this.bits - $$6;
            this.data[$$4] = this.data[$$4] >>> $$7 << $$7 | ((long) int1 & this.mask) >> $$6;
        }
    }

    public int get(int int0) {
        Validate.inclusiveBetween(0L, (long) (this.size - 1), (long) int0);
        int $$1 = int0 * this.bits;
        int $$2 = $$1 >> 6;
        int $$3 = (int0 + 1) * this.bits - 1 >> 6;
        int $$4 = $$1 ^ $$2 << 6;
        if ($$2 == $$3) {
            return (int) (this.data[$$2] >>> $$4 & this.mask);
        } else {
            int $$5 = 64 - $$4;
            return (int) ((this.data[$$2] >>> $$4 | this.data[$$3] << $$5) & this.mask);
        }
    }

    public long[] getRaw() {
        return this.data;
    }

    public int getBits() {
        return this.bits;
    }
}