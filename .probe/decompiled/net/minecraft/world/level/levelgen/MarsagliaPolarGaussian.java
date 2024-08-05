package net.minecraft.world.level.levelgen;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class MarsagliaPolarGaussian {

    public final RandomSource randomSource;

    private double nextNextGaussian;

    private boolean haveNextNextGaussian;

    public MarsagliaPolarGaussian(RandomSource randomSource0) {
        this.randomSource = randomSource0;
    }

    public void reset() {
        this.haveNextNextGaussian = false;
    }

    public double nextGaussian() {
        if (this.haveNextNextGaussian) {
            this.haveNextNextGaussian = false;
            return this.nextNextGaussian;
        } else {
            double $$0;
            double $$1;
            double $$2;
            do {
                $$0 = 2.0 * this.randomSource.nextDouble() - 1.0;
                $$1 = 2.0 * this.randomSource.nextDouble() - 1.0;
                $$2 = Mth.square($$0) + Mth.square($$1);
            } while ($$2 >= 1.0 || $$2 == 0.0);
            double $$3 = Math.sqrt(-2.0 * Math.log($$2) / $$2);
            this.nextNextGaussian = $$1 * $$3;
            this.haveNextNextGaussian = true;
            return $$0 * $$3;
        }
    }
}