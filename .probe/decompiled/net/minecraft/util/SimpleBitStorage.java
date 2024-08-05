package net.minecraft.util;

import java.util.function.IntConsumer;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;

public class SimpleBitStorage implements BitStorage {

    private static final int[] MAGIC = new int[] { -1, -1, 0, Integer.MIN_VALUE, 0, 0, 1431655765, 1431655765, 0, Integer.MIN_VALUE, 0, 1, 858993459, 858993459, 0, 715827882, 715827882, 0, 613566756, 613566756, 0, Integer.MIN_VALUE, 0, 2, 477218588, 477218588, 0, 429496729, 429496729, 0, 390451572, 390451572, 0, 357913941, 357913941, 0, 330382099, 330382099, 0, 306783378, 306783378, 0, 286331153, 286331153, 0, Integer.MIN_VALUE, 0, 3, 252645135, 252645135, 0, 238609294, 238609294, 0, 226050910, 226050910, 0, 214748364, 214748364, 0, 204522252, 204522252, 0, 195225786, 195225786, 0, 186737708, 186737708, 0, 178956970, 178956970, 0, 171798691, 171798691, 0, 165191049, 165191049, 0, 159072862, 159072862, 0, 153391689, 153391689, 0, 148102320, 148102320, 0, 143165576, 143165576, 0, 138547332, 138547332, 0, Integer.MIN_VALUE, 0, 4, 130150524, 130150524, 0, 126322567, 126322567, 0, 122713351, 122713351, 0, 119304647, 119304647, 0, 116080197, 116080197, 0, 113025455, 113025455, 0, 110127366, 110127366, 0, 107374182, 107374182, 0, 104755299, 104755299, 0, 102261126, 102261126, 0, 99882960, 99882960, 0, 97612893, 97612893, 0, 95443717, 95443717, 0, 93368854, 93368854, 0, 91382282, 91382282, 0, 89478485, 89478485, 0, 87652393, 87652393, 0, 85899345, 85899345, 0, 84215045, 84215045, 0, 82595524, 82595524, 0, 81037118, 81037118, 0, 79536431, 79536431, 0, 78090314, 78090314, 0, 76695844, 76695844, 0, 75350303, 75350303, 0, 74051160, 74051160, 0, 72796055, 72796055, 0, 71582788, 71582788, 0, 70409299, 70409299, 0, 69273666, 69273666, 0, 68174084, 68174084, 0, Integer.MIN_VALUE, 0, 5 };

    private final long[] data;

    private final int bits;

    private final long mask;

    private final int size;

    private final int valuesPerLong;

    private final int divideMul;

    private final int divideAdd;

    private final int divideShift;

    public SimpleBitStorage(int int0, int int1, int[] int2) {
        this(int0, int1);
        int $$3 = 0;
        int $$4;
        for ($$4 = 0; $$4 <= int1 - this.valuesPerLong; $$4 += this.valuesPerLong) {
            long $$5 = 0L;
            for (int $$6 = this.valuesPerLong - 1; $$6 >= 0; $$6--) {
                $$5 <<= int0;
                $$5 |= (long) int2[$$4 + $$6] & this.mask;
            }
            this.data[$$3++] = $$5;
        }
        int $$7 = int1 - $$4;
        if ($$7 > 0) {
            long $$8 = 0L;
            for (int $$9 = $$7 - 1; $$9 >= 0; $$9--) {
                $$8 <<= int0;
                $$8 |= (long) int2[$$4 + $$9] & this.mask;
            }
            this.data[$$3] = $$8;
        }
    }

    public SimpleBitStorage(int int0, int int1) {
        this(int0, int1, (long[]) null);
    }

    public SimpleBitStorage(int int0, int int1, @Nullable long[] long2) {
        Validate.inclusiveBetween(1L, 32L, (long) int0);
        this.size = int1;
        this.bits = int0;
        this.mask = (1L << int0) - 1L;
        this.valuesPerLong = (char) (64 / int0);
        int $$3 = 3 * (this.valuesPerLong - 1);
        this.divideMul = MAGIC[$$3 + 0];
        this.divideAdd = MAGIC[$$3 + 1];
        this.divideShift = MAGIC[$$3 + 2];
        int $$4 = (int1 + this.valuesPerLong - 1) / this.valuesPerLong;
        if (long2 != null) {
            if (long2.length != $$4) {
                throw new SimpleBitStorage.InitializationException("Invalid length given for storage, got: " + long2.length + " but expected: " + $$4);
            }
            this.data = long2;
        } else {
            this.data = new long[$$4];
        }
    }

    private int cellIndex(int int0) {
        long $$1 = Integer.toUnsignedLong(this.divideMul);
        long $$2 = Integer.toUnsignedLong(this.divideAdd);
        return (int) ((long) int0 * $$1 + $$2 >> 32 >> this.divideShift);
    }

    @Override
    public int getAndSet(int int0, int int1) {
        Validate.inclusiveBetween(0L, (long) (this.size - 1), (long) int0);
        Validate.inclusiveBetween(0L, this.mask, (long) int1);
        int $$2 = this.cellIndex(int0);
        long $$3 = this.data[$$2];
        int $$4 = (int0 - $$2 * this.valuesPerLong) * this.bits;
        int $$5 = (int) ($$3 >> $$4 & this.mask);
        this.data[$$2] = $$3 & ~(this.mask << $$4) | ((long) int1 & this.mask) << $$4;
        return $$5;
    }

    @Override
    public void set(int int0, int int1) {
        Validate.inclusiveBetween(0L, (long) (this.size - 1), (long) int0);
        Validate.inclusiveBetween(0L, this.mask, (long) int1);
        int $$2 = this.cellIndex(int0);
        long $$3 = this.data[$$2];
        int $$4 = (int0 - $$2 * this.valuesPerLong) * this.bits;
        this.data[$$2] = $$3 & ~(this.mask << $$4) | ((long) int1 & this.mask) << $$4;
    }

    @Override
    public int get(int int0) {
        Validate.inclusiveBetween(0L, (long) (this.size - 1), (long) int0);
        int $$1 = this.cellIndex(int0);
        long $$2 = this.data[$$1];
        int $$3 = (int0 - $$1 * this.valuesPerLong) * this.bits;
        return (int) ($$2 >> $$3 & this.mask);
    }

    @Override
    public long[] getRaw() {
        return this.data;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public int getBits() {
        return this.bits;
    }

    @Override
    public void getAll(IntConsumer intConsumer0) {
        int $$1 = 0;
        for (long $$2 : this.data) {
            for (int $$3 = 0; $$3 < this.valuesPerLong; $$3++) {
                intConsumer0.accept((int) ($$2 & this.mask));
                $$2 >>= this.bits;
                if (++$$1 >= this.size) {
                    return;
                }
            }
        }
    }

    @Override
    public void unpack(int[] int0) {
        int $$1 = this.data.length;
        int $$2 = 0;
        for (int $$3 = 0; $$3 < $$1 - 1; $$3++) {
            long $$4 = this.data[$$3];
            for (int $$5 = 0; $$5 < this.valuesPerLong; $$5++) {
                int0[$$2 + $$5] = (int) ($$4 & this.mask);
                $$4 >>= this.bits;
            }
            $$2 += this.valuesPerLong;
        }
        int $$6 = this.size - $$2;
        if ($$6 > 0) {
            long $$7 = this.data[$$1 - 1];
            for (int $$8 = 0; $$8 < $$6; $$8++) {
                int0[$$2 + $$8] = (int) ($$7 & this.mask);
                $$7 >>= this.bits;
            }
        }
    }

    @Override
    public BitStorage copy() {
        return new SimpleBitStorage(this.bits, this.size, (long[]) this.data.clone());
    }

    public static class InitializationException extends RuntimeException {

        InitializationException(String string0) {
            super(string0);
        }
    }
}