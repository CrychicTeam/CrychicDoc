package net.minecraft.world.level.levelgen;

import net.minecraft.util.RandomSource;

public class SingleThreadedRandomSource implements BitRandomSource {

    private static final int MODULUS_BITS = 48;

    private static final long MODULUS_MASK = 281474976710655L;

    private static final long MULTIPLIER = 25214903917L;

    private static final long INCREMENT = 11L;

    private long seed;

    private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);

    public SingleThreadedRandomSource(long long0) {
        this.setSeed(long0);
    }

    @Override
    public RandomSource fork() {
        return new SingleThreadedRandomSource(this.m_188505_());
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return new LegacyRandomSource.LegacyPositionalRandomFactory(this.m_188505_());
    }

    @Override
    public void setSeed(long long0) {
        this.seed = (long0 ^ 25214903917L) & 281474976710655L;
        this.gaussianSource.reset();
    }

    @Override
    public int next(int int0) {
        long $$1 = this.seed * 25214903917L + 11L & 281474976710655L;
        this.seed = $$1;
        return (int) ($$1 >> 48 - int0);
    }

    @Override
    public double nextGaussian() {
        return this.gaussianSource.nextGaussian();
    }
}