package net.minecraft.world.level.levelgen;

import java.util.concurrent.atomic.AtomicLong;
import net.minecraft.util.RandomSource;

@Deprecated
public class ThreadSafeLegacyRandomSource implements BitRandomSource {

    private static final int MODULUS_BITS = 48;

    private static final long MODULUS_MASK = 281474976710655L;

    private static final long MULTIPLIER = 25214903917L;

    private static final long INCREMENT = 11L;

    private final AtomicLong seed = new AtomicLong();

    private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);

    public ThreadSafeLegacyRandomSource(long long0) {
        this.setSeed(long0);
    }

    @Override
    public RandomSource fork() {
        return new ThreadSafeLegacyRandomSource(this.m_188505_());
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return new LegacyRandomSource.LegacyPositionalRandomFactory(this.m_188505_());
    }

    @Override
    public void setSeed(long long0) {
        this.seed.set((long0 ^ 25214903917L) & 281474976710655L);
    }

    @Override
    public int next(int int0) {
        long $$1;
        long $$2;
        do {
            $$1 = this.seed.get();
            $$2 = $$1 * 25214903917L + 11L & 281474976710655L;
        } while (!this.seed.compareAndSet($$1, $$2));
        return (int) ($$2 >>> 48 - int0);
    }

    @Override
    public double nextGaussian() {
        return this.gaussianSource.nextGaussian();
    }
}