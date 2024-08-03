package net.minecraft.world.level.levelgen;

import java.util.function.LongFunction;
import net.minecraft.util.RandomSource;

public class WorldgenRandom extends LegacyRandomSource {

    private final RandomSource randomSource;

    private int count;

    public WorldgenRandom(RandomSource randomSource0) {
        super(0L);
        this.randomSource = randomSource0;
    }

    public int getCount() {
        return this.count;
    }

    @Override
    public RandomSource fork() {
        return this.randomSource.fork();
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return this.randomSource.forkPositional();
    }

    @Override
    public int next(int int0) {
        this.count++;
        return this.randomSource instanceof LegacyRandomSource $$1 ? $$1.next(int0) : (int) (this.randomSource.nextLong() >>> 64 - int0);
    }

    @Override
    public synchronized void setSeed(long long0) {
        if (this.randomSource != null) {
            this.randomSource.setSeed(long0);
        }
    }

    public long setDecorationSeed(long long0, int int1, int int2) {
        this.setSeed(long0);
        long $$3 = this.m_188505_() | 1L;
        long $$4 = this.m_188505_() | 1L;
        long $$5 = (long) int1 * $$3 + (long) int2 * $$4 ^ long0;
        this.setSeed($$5);
        return $$5;
    }

    public void setFeatureSeed(long long0, int int1, int int2) {
        long $$3 = long0 + (long) int1 + (long) (10000 * int2);
        this.setSeed($$3);
    }

    public void setLargeFeatureSeed(long long0, int int1, int int2) {
        this.setSeed(long0);
        long $$3 = this.m_188505_();
        long $$4 = this.m_188505_();
        long $$5 = (long) int1 * $$3 ^ (long) int2 * $$4 ^ long0;
        this.setSeed($$5);
    }

    public void setLargeFeatureWithSalt(long long0, int int1, int int2, int int3) {
        long $$4 = (long) int1 * 341873128712L + (long) int2 * 132897987541L + long0 + (long) int3;
        this.setSeed($$4);
    }

    public static RandomSource seedSlimeChunk(int int0, int int1, long long2, long long3) {
        return RandomSource.create(long2 + (long) (int0 * int0 * 4987142) + (long) (int0 * 5947611) + (long) (int1 * int1) * 4392871L + (long) (int1 * 389711) ^ long3);
    }

    public static enum Algorithm {

        LEGACY(LegacyRandomSource::new), XOROSHIRO(XoroshiroRandomSource::new);

        private final LongFunction<RandomSource> constructor;

        private Algorithm(LongFunction<RandomSource> p_190082_) {
            this.constructor = p_190082_;
        }

        public RandomSource newInstance(long p_224688_) {
            return (RandomSource) this.constructor.apply(p_224688_);
        }
    }
}