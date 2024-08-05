package net.minecraft.world.level.levelgen;

import com.google.common.annotations.VisibleForTesting;
import java.util.concurrent.atomic.AtomicLong;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.ThreadingDetector;

public class LegacyRandomSource implements BitRandomSource {

    private static final int MODULUS_BITS = 48;

    private static final long MODULUS_MASK = 281474976710655L;

    private static final long MULTIPLIER = 25214903917L;

    private static final long INCREMENT = 11L;

    private final AtomicLong seed = new AtomicLong();

    private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);

    public LegacyRandomSource(long long0) {
        this.setSeed(long0);
    }

    @Override
    public RandomSource fork() {
        return new LegacyRandomSource(this.m_188505_());
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        return new LegacyRandomSource.LegacyPositionalRandomFactory(this.m_188505_());
    }

    @Override
    public void setSeed(long long0) {
        if (!this.seed.compareAndSet(this.seed.get(), (long0 ^ 25214903917L) & 281474976710655L)) {
            throw ThreadingDetector.makeThreadingException("LegacyRandomSource", null);
        } else {
            this.gaussianSource.reset();
        }
    }

    @Override
    public int next(int int0) {
        long $$1 = this.seed.get();
        long $$2 = $$1 * 25214903917L + 11L & 281474976710655L;
        if (!this.seed.compareAndSet($$1, $$2)) {
            throw ThreadingDetector.makeThreadingException("LegacyRandomSource", null);
        } else {
            return (int) ($$2 >> 48 - int0);
        }
    }

    @Override
    public double nextGaussian() {
        return this.gaussianSource.nextGaussian();
    }

    public static class LegacyPositionalRandomFactory implements PositionalRandomFactory {

        private final long seed;

        public LegacyPositionalRandomFactory(long long0) {
            this.seed = long0;
        }

        @Override
        public RandomSource at(int int0, int int1, int int2) {
            long $$3 = Mth.getSeed(int0, int1, int2);
            long $$4 = $$3 ^ this.seed;
            return new LegacyRandomSource($$4);
        }

        @Override
        public RandomSource fromHashOf(String string0) {
            int $$1 = string0.hashCode();
            return new LegacyRandomSource((long) $$1 ^ this.seed);
        }

        @VisibleForTesting
        @Override
        public void parityConfigString(StringBuilder stringBuilder0) {
            stringBuilder0.append("LegacyPositionalRandomFactory{").append(this.seed).append("}");
        }
    }
}