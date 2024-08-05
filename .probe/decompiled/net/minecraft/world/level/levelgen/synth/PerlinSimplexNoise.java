package net.minecraft.world.level.levelgen.synth;

import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import java.util.List;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;

public class PerlinSimplexNoise {

    private final SimplexNoise[] noiseLevels;

    private final double highestFreqValueFactor;

    private final double highestFreqInputFactor;

    public PerlinSimplexNoise(RandomSource randomSource0, List<Integer> listInteger1) {
        this(randomSource0, new IntRBTreeSet(listInteger1));
    }

    private PerlinSimplexNoise(RandomSource randomSource0, IntSortedSet intSortedSet1) {
        if (intSortedSet1.isEmpty()) {
            throw new IllegalArgumentException("Need some octaves!");
        } else {
            int $$2 = -intSortedSet1.firstInt();
            int $$3 = intSortedSet1.lastInt();
            int $$4 = $$2 + $$3 + 1;
            if ($$4 < 1) {
                throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
            } else {
                SimplexNoise $$5 = new SimplexNoise(randomSource0);
                int $$6 = $$3;
                this.noiseLevels = new SimplexNoise[$$4];
                if ($$3 >= 0 && $$3 < $$4 && intSortedSet1.contains(0)) {
                    this.noiseLevels[$$3] = $$5;
                }
                for (int $$7 = $$3 + 1; $$7 < $$4; $$7++) {
                    if ($$7 >= 0 && intSortedSet1.contains($$6 - $$7)) {
                        this.noiseLevels[$$7] = new SimplexNoise(randomSource0);
                    } else {
                        randomSource0.consumeCount(262);
                    }
                }
                if ($$3 > 0) {
                    long $$8 = (long) ($$5.getValue($$5.xo, $$5.yo, $$5.zo) * 9.223372E18F);
                    RandomSource $$9 = new WorldgenRandom(new LegacyRandomSource($$8));
                    for (int $$10 = $$6 - 1; $$10 >= 0; $$10--) {
                        if ($$10 < $$4 && intSortedSet1.contains($$6 - $$10)) {
                            this.noiseLevels[$$10] = new SimplexNoise($$9);
                        } else {
                            $$9.consumeCount(262);
                        }
                    }
                }
                this.highestFreqInputFactor = Math.pow(2.0, (double) $$3);
                this.highestFreqValueFactor = 1.0 / (Math.pow(2.0, (double) $$4) - 1.0);
            }
        }
    }

    public double getValue(double double0, double double1, boolean boolean2) {
        double $$3 = 0.0;
        double $$4 = this.highestFreqInputFactor;
        double $$5 = this.highestFreqValueFactor;
        for (SimplexNoise $$6 : this.noiseLevels) {
            if ($$6 != null) {
                $$3 += $$6.getValue(double0 * $$4 + (boolean2 ? $$6.xo : 0.0), double1 * $$4 + (boolean2 ? $$6.yo : 0.0)) * $$5;
            }
            $$4 /= 2.0;
            $$5 *= 2.0;
        }
        return $$3;
    }
}