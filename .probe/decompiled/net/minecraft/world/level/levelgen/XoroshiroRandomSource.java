package net.minecraft.world.level.levelgen;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class XoroshiroRandomSource implements RandomSource {

    private static final float FLOAT_UNIT = 5.9604645E-8F;

    private static final double DOUBLE_UNIT = 1.110223E-16F;

    public static final Codec<XoroshiroRandomSource> CODEC = Xoroshiro128PlusPlus.CODEC.xmap(p_287645_ -> new XoroshiroRandomSource(p_287645_), p_287690_ -> p_287690_.randomNumberGenerator);

    private Xoroshiro128PlusPlus randomNumberGenerator;

    private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);

    public XoroshiroRandomSource(long long0) {
        this.randomNumberGenerator = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(long0));
    }

    public XoroshiroRandomSource(RandomSupport.Seed128bit randomSupportSeedBit0) {
        this.randomNumberGenerator = new Xoroshiro128PlusPlus(randomSupportSeedBit0);
    }

    public XoroshiroRandomSource(long long0, long long1) {
        this.randomNumberGenerator = new Xoroshiro128PlusPlus(long0, long1);
    }

    private XoroshiroRandomSource(Xoroshiro128PlusPlus xoroshiroPlusPlus0) {
        this.randomNumberGenerator = xoroshiroPlusPlus0;
    }

    @Override
    public RandomSource fork() {
        return new XoroshiroRandomSource(this.randomNumberGenerator.nextLong(), this.randomNumberGenerator.nextLong());
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return new XoroshiroRandomSource.XoroshiroPositionalRandomFactory(this.randomNumberGenerator.nextLong(), this.randomNumberGenerator.nextLong());
    }

    @Override
    public void setSeed(long long0) {
        this.randomNumberGenerator = new Xoroshiro128PlusPlus(RandomSupport.upgradeSeedTo128bit(long0));
        this.gaussianSource.reset();
    }

    @Override
    public int nextInt() {
        return (int) this.randomNumberGenerator.nextLong();
    }

    @Override
    public int nextInt(int int0) {
        if (int0 <= 0) {
            throw new IllegalArgumentException("Bound must be positive");
        } else {
            long $$1 = Integer.toUnsignedLong(this.nextInt());
            long $$2 = $$1 * (long) int0;
            long $$3 = $$2 & 4294967295L;
            if ($$3 < (long) int0) {
                for (int $$4 = Integer.remainderUnsigned(~int0 + 1, int0); $$3 < (long) $$4; $$3 = $$2 & 4294967295L) {
                    $$1 = Integer.toUnsignedLong(this.nextInt());
                    $$2 = $$1 * (long) int0;
                }
            }
            long $$5 = $$2 >> 32;
            return (int) $$5;
        }
    }

    @Override
    public long nextLong() {
        return this.randomNumberGenerator.nextLong();
    }

    @Override
    public boolean nextBoolean() {
        return (this.randomNumberGenerator.nextLong() & 1L) != 0L;
    }

    @Override
    public float nextFloat() {
        return (float) this.nextBits(24) * 5.9604645E-8F;
    }

    @Override
    public double nextDouble() {
        return (double) this.nextBits(53) * 1.110223E-16F;
    }

    @Override
    public double nextGaussian() {
        return this.gaussianSource.nextGaussian();
    }

    @Override
    public void consumeCount(int int0) {
        for (int $$1 = 0; $$1 < int0; $$1++) {
            this.randomNumberGenerator.nextLong();
        }
    }

    private long nextBits(int int0) {
        return this.randomNumberGenerator.nextLong() >>> 64 - int0;
    }

    public static class XoroshiroPositionalRandomFactory implements PositionalRandomFactory {

        private final long seedLo;

        private final long seedHi;

        public XoroshiroPositionalRandomFactory(long long0, long long1) {
            this.seedLo = long0;
            this.seedHi = long1;
        }

        @Override
        public RandomSource at(int int0, int int1, int int2) {
            long $$3 = Mth.getSeed(int0, int1, int2);
            long $$4 = $$3 ^ this.seedLo;
            return new XoroshiroRandomSource($$4, this.seedHi);
        }

        @Override
        public RandomSource fromHashOf(String string0) {
            RandomSupport.Seed128bit $$1 = RandomSupport.seedFromHashOf(string0);
            return new XoroshiroRandomSource($$1.xor(this.seedLo, this.seedHi));
        }

        @VisibleForTesting
        @Override
        public void parityConfigString(StringBuilder stringBuilder0) {
            stringBuilder0.append("seedLo: ").append(this.seedLo).append(", seedHi: ").append(this.seedHi);
        }
    }
}