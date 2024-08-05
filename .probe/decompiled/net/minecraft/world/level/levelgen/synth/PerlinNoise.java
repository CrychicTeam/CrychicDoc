package net.minecraft.world.level.levelgen.synth;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

public class PerlinNoise {

    private static final int ROUND_OFF = 33554432;

    private final ImprovedNoise[] noiseLevels;

    private final int firstOctave;

    private final DoubleList amplitudes;

    private final double lowestFreqValueFactor;

    private final double lowestFreqInputFactor;

    private final double maxValue;

    @Deprecated
    public static PerlinNoise createLegacyForBlendedNoise(RandomSource randomSource0, IntStream intStream1) {
        return new PerlinNoise(randomSource0, makeAmplitudes(new IntRBTreeSet((Collection) intStream1.boxed().collect(ImmutableList.toImmutableList()))), false);
    }

    @Deprecated
    public static PerlinNoise createLegacyForLegacyNetherBiome(RandomSource randomSource0, int int1, DoubleList doubleList2) {
        return new PerlinNoise(randomSource0, Pair.of(int1, doubleList2), false);
    }

    public static PerlinNoise create(RandomSource randomSource0, IntStream intStream1) {
        return create(randomSource0, (List<Integer>) intStream1.boxed().collect(ImmutableList.toImmutableList()));
    }

    public static PerlinNoise create(RandomSource randomSource0, List<Integer> listInteger1) {
        return new PerlinNoise(randomSource0, makeAmplitudes(new IntRBTreeSet(listInteger1)), true);
    }

    public static PerlinNoise create(RandomSource randomSource0, int int1, double double2, double... double3) {
        DoubleArrayList $$4 = new DoubleArrayList(double3);
        $$4.add(0, double2);
        return new PerlinNoise(randomSource0, Pair.of(int1, $$4), true);
    }

    public static PerlinNoise create(RandomSource randomSource0, int int1, DoubleList doubleList2) {
        return new PerlinNoise(randomSource0, Pair.of(int1, doubleList2), true);
    }

    private static Pair<Integer, DoubleList> makeAmplitudes(IntSortedSet intSortedSet0) {
        if (intSortedSet0.isEmpty()) {
            throw new IllegalArgumentException("Need some octaves!");
        } else {
            int $$1 = -intSortedSet0.firstInt();
            int $$2 = intSortedSet0.lastInt();
            int $$3 = $$1 + $$2 + 1;
            if ($$3 < 1) {
                throw new IllegalArgumentException("Total number of octaves needs to be >= 1");
            } else {
                DoubleList $$4 = new DoubleArrayList(new double[$$3]);
                IntBidirectionalIterator $$5 = intSortedSet0.iterator();
                while ($$5.hasNext()) {
                    int $$6 = $$5.nextInt();
                    $$4.set($$6 + $$1, 1.0);
                }
                return Pair.of(-$$1, $$4);
            }
        }
    }

    protected PerlinNoise(RandomSource randomSource0, Pair<Integer, DoubleList> pairIntegerDoubleList1, boolean boolean2) {
        this.firstOctave = (Integer) pairIntegerDoubleList1.getFirst();
        this.amplitudes = (DoubleList) pairIntegerDoubleList1.getSecond();
        int $$3 = this.amplitudes.size();
        int $$4 = -this.firstOctave;
        this.noiseLevels = new ImprovedNoise[$$3];
        if (boolean2) {
            PositionalRandomFactory $$5 = randomSource0.forkPositional();
            for (int $$6 = 0; $$6 < $$3; $$6++) {
                if (this.amplitudes.getDouble($$6) != 0.0) {
                    int $$7 = this.firstOctave + $$6;
                    this.noiseLevels[$$6] = new ImprovedNoise($$5.fromHashOf("octave_" + $$7));
                }
            }
        } else {
            ImprovedNoise $$8 = new ImprovedNoise(randomSource0);
            if ($$4 >= 0 && $$4 < $$3) {
                double $$9 = this.amplitudes.getDouble($$4);
                if ($$9 != 0.0) {
                    this.noiseLevels[$$4] = $$8;
                }
            }
            for (int $$10 = $$4 - 1; $$10 >= 0; $$10--) {
                if ($$10 < $$3) {
                    double $$11 = this.amplitudes.getDouble($$10);
                    if ($$11 != 0.0) {
                        this.noiseLevels[$$10] = new ImprovedNoise(randomSource0);
                    } else {
                        skipOctave(randomSource0);
                    }
                } else {
                    skipOctave(randomSource0);
                }
            }
            if (Arrays.stream(this.noiseLevels).filter(Objects::nonNull).count() != this.amplitudes.stream().filter(p_192897_ -> p_192897_ != 0.0).count()) {
                throw new IllegalStateException("Failed to create correct number of noise levels for given non-zero amplitudes");
            }
            if ($$4 < $$3 - 1) {
                throw new IllegalArgumentException("Positive octaves are temporarily disabled");
            }
        }
        this.lowestFreqInputFactor = Math.pow(2.0, (double) (-$$4));
        this.lowestFreqValueFactor = Math.pow(2.0, (double) ($$3 - 1)) / (Math.pow(2.0, (double) $$3) - 1.0);
        this.maxValue = this.edgeValue(2.0);
    }

    protected double maxValue() {
        return this.maxValue;
    }

    private static void skipOctave(RandomSource randomSource0) {
        randomSource0.consumeCount(262);
    }

    public double getValue(double double0, double double1, double double2) {
        return this.getValue(double0, double1, double2, 0.0, 0.0, false);
    }

    @Deprecated
    public double getValue(double double0, double double1, double double2, double double3, double double4, boolean boolean5) {
        double $$6 = 0.0;
        double $$7 = this.lowestFreqInputFactor;
        double $$8 = this.lowestFreqValueFactor;
        for (int $$9 = 0; $$9 < this.noiseLevels.length; $$9++) {
            ImprovedNoise $$10 = this.noiseLevels[$$9];
            if ($$10 != null) {
                double $$11 = $$10.noise(wrap(double0 * $$7), boolean5 ? -$$10.yo : wrap(double1 * $$7), wrap(double2 * $$7), double3 * $$7, double4 * $$7);
                $$6 += this.amplitudes.getDouble($$9) * $$11 * $$8;
            }
            $$7 *= 2.0;
            $$8 /= 2.0;
        }
        return $$6;
    }

    public double maxBrokenValue(double double0) {
        return this.edgeValue(double0 + 2.0);
    }

    private double edgeValue(double double0) {
        double $$1 = 0.0;
        double $$2 = this.lowestFreqValueFactor;
        for (int $$3 = 0; $$3 < this.noiseLevels.length; $$3++) {
            ImprovedNoise $$4 = this.noiseLevels[$$3];
            if ($$4 != null) {
                $$1 += this.amplitudes.getDouble($$3) * double0 * $$2;
            }
            $$2 /= 2.0;
        }
        return $$1;
    }

    @Nullable
    public ImprovedNoise getOctaveNoise(int int0) {
        return this.noiseLevels[this.noiseLevels.length - 1 - int0];
    }

    public static double wrap(double double0) {
        return double0 - (double) Mth.lfloor(double0 / 3.3554432E7 + 0.5) * 3.3554432E7;
    }

    protected int firstOctave() {
        return this.firstOctave;
    }

    protected DoubleList amplitudes() {
        return this.amplitudes;
    }

    @VisibleForTesting
    public void parityConfigString(StringBuilder stringBuilder0) {
        stringBuilder0.append("PerlinNoise{");
        List<String> $$1 = this.amplitudes.stream().map(p_192889_ -> String.format(Locale.ROOT, "%.2f", p_192889_)).toList();
        stringBuilder0.append("first octave: ").append(this.firstOctave).append(", amplitudes: ").append($$1).append(", noise levels: [");
        for (int $$2 = 0; $$2 < this.noiseLevels.length; $$2++) {
            stringBuilder0.append($$2).append(": ");
            ImprovedNoise $$3 = this.noiseLevels[$$2];
            if ($$3 == null) {
                stringBuilder0.append("null");
            } else {
                $$3.parityConfigString(stringBuilder0);
            }
            stringBuilder0.append(", ");
        }
        stringBuilder0.append("]");
        stringBuilder0.append("}");
    }
}