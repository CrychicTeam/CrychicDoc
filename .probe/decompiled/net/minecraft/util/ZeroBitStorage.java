package net.minecraft.util;

import java.util.Arrays;
import java.util.function.IntConsumer;
import org.apache.commons.lang3.Validate;

public class ZeroBitStorage implements BitStorage {

    public static final long[] RAW = new long[0];

    private final int size;

    public ZeroBitStorage(int int0) {
        this.size = int0;
    }

    @Override
    public int getAndSet(int int0, int int1) {
        Validate.inclusiveBetween(0L, (long) (this.size - 1), (long) int0);
        Validate.inclusiveBetween(0L, 0L, (long) int1);
        return 0;
    }

    @Override
    public void set(int int0, int int1) {
        Validate.inclusiveBetween(0L, (long) (this.size - 1), (long) int0);
        Validate.inclusiveBetween(0L, 0L, (long) int1);
    }

    @Override
    public int get(int int0) {
        Validate.inclusiveBetween(0L, (long) (this.size - 1), (long) int0);
        return 0;
    }

    @Override
    public long[] getRaw() {
        return RAW;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public int getBits() {
        return 0;
    }

    @Override
    public void getAll(IntConsumer intConsumer0) {
        for (int $$1 = 0; $$1 < this.size; $$1++) {
            intConsumer0.accept(0);
        }
    }

    @Override
    public void unpack(int[] int0) {
        Arrays.fill(int0, 0, this.size, 0);
    }

    @Override
    public BitStorage copy() {
        return this;
    }
}