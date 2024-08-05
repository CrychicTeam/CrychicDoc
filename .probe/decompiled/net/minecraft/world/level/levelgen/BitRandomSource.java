package net.minecraft.world.level.levelgen;

import net.minecraft.util.RandomSource;

public interface BitRandomSource extends RandomSource {

    float FLOAT_MULTIPLIER = 5.9604645E-8F;

    double DOUBLE_MULTIPLIER = 1.110223E-16F;

    int next(int var1);

    @Override
    default int nextInt() {
        return this.next(32);
    }

    @Override
    default int nextInt(int int0) {
        if (int0 <= 0) {
            throw new IllegalArgumentException("Bound must be positive");
        } else if ((int0 & int0 - 1) == 0) {
            return (int) ((long) int0 * (long) this.next(31) >> 31);
        } else {
            int $$1;
            int $$2;
            do {
                $$1 = this.next(31);
                $$2 = $$1 % int0;
            } while ($$1 - $$2 + (int0 - 1) < 0);
            return $$2;
        }
    }

    @Override
    default long nextLong() {
        int $$0 = this.next(32);
        int $$1 = this.next(32);
        long $$2 = (long) $$0 << 32;
        return $$2 + (long) $$1;
    }

    @Override
    default boolean nextBoolean() {
        return this.next(1) != 0;
    }

    @Override
    default float nextFloat() {
        return (float) this.next(24) * 5.9604645E-8F;
    }

    @Override
    default double nextDouble() {
        int $$0 = this.next(26);
        int $$1 = this.next(27);
        long $$2 = ((long) $$0 << 27) + (long) $$1;
        return (double) $$2 * 1.110223E-16F;
    }
}