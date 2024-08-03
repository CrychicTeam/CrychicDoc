package net.minecraft.util;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.levelgen.ThreadSafeLegacyRandomSource;

public interface RandomSource {

    @Deprecated
    double GAUSSIAN_SPREAD_FACTOR = 2.297;

    static RandomSource create() {
        return create(RandomSupport.generateUniqueSeed());
    }

    @Deprecated
    static RandomSource createThreadSafe() {
        return new ThreadSafeLegacyRandomSource(RandomSupport.generateUniqueSeed());
    }

    static RandomSource create(long long0) {
        return new LegacyRandomSource(long0);
    }

    static RandomSource createNewThreadLocalInstance() {
        return new SingleThreadedRandomSource(ThreadLocalRandom.current().nextLong());
    }

    RandomSource fork();

    PositionalRandomFactory forkPositional();

    void setSeed(long var1);

    int nextInt();

    int nextInt(int var1);

    default int nextIntBetweenInclusive(int int0, int int1) {
        return this.nextInt(int1 - int0 + 1) + int0;
    }

    long nextLong();

    boolean nextBoolean();

    float nextFloat();

    double nextDouble();

    double nextGaussian();

    default double triangle(double double0, double double1) {
        return double0 + double1 * (this.nextDouble() - this.nextDouble());
    }

    default void consumeCount(int int0) {
        for (int $$1 = 0; $$1 < int0; $$1++) {
            this.nextInt();
        }
    }

    default int nextInt(int int0, int int1) {
        if (int0 >= int1) {
            throw new IllegalArgumentException("bound - origin is non positive");
        } else {
            return int0 + this.nextInt(int1 - int0);
        }
    }
}