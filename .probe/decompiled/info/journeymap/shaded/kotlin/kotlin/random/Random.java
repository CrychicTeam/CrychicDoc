package info.journeymap.shaded.kotlin.kotlin.random;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.internal.PlatformImplementationsKt;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.io.Serializable;

@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\b'\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H&J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0016J$\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\u00042\b\b\u0002\u0010\f\u001a\u00020\u0004H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u0004H\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0004H\u0016J\u0010\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\u0018\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0016H\u0016J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0011\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0016H\u0016¨\u0006\u0018" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/random/Random;", "", "()V", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "fromIndex", "toIndex", "size", "nextDouble", "", "until", "from", "nextFloat", "", "nextInt", "nextLong", "", "Default", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@SinceKotlin(version = "1.3")
public abstract class Random {

    @NotNull
    public static final Random.Default Default = new Random.Default(null);

    @NotNull
    private static final Random defaultRandom = PlatformImplementationsKt.IMPLEMENTATIONS.defaultPlatformRandom();

    public abstract int nextBits(int var1);

    public int nextInt() {
        return this.nextBits(32);
    }

    public int nextInt(int until) {
        return this.nextInt(0, until);
    }

    public int nextInt(int from, int until) {
        RandomKt.checkRangeBounds(from, until);
        int n = until - from;
        if (n <= 0 && n != Integer.MIN_VALUE) {
            int rnd;
            do {
                rnd = this.nextInt();
            } while (from <= rnd ? rnd >= until : true);
            return rnd;
        } else {
            int var10000;
            if ((n & -n) == n) {
                int bitCount = RandomKt.fastLog2(n);
                var10000 = this.nextBits(bitCount);
            } else {
                int v = 0;
                int bits;
                do {
                    bits = this.nextInt() >>> 1;
                    v = bits % n;
                } while (bits - v + (n - 1) < 0);
                var10000 = v;
            }
            int rnd = var10000;
            return from + rnd;
        }
    }

    public long nextLong() {
        return ((long) this.nextInt() << 32) + (long) this.nextInt();
    }

    public long nextLong(long until) {
        return this.nextLong(0L, until);
    }

    public long nextLong(long from, long until) {
        RandomKt.checkRangeBounds(from, until);
        long n = until - from;
        if (n <= 0L) {
            long rnd;
            do {
                rnd = this.nextLong();
            } while (from <= rnd ? rnd >= until : true);
            return rnd;
        } else {
            long rnd = 0L;
            if ((n & -n) == n) {
                int nLow = (int) n;
                int nHigh = (int) (n >>> 32);
                long var10000;
                if (nLow != 0) {
                    int bitCount = RandomKt.fastLog2(nLow);
                    var10000 = (long) this.nextBits(bitCount) & 4294967295L;
                } else if (nHigh == 1) {
                    var10000 = (long) this.nextInt() & 4294967295L;
                } else {
                    int bitCount = RandomKt.fastLog2(nHigh);
                    var10000 = ((long) this.nextBits(bitCount) << 32) + ((long) this.nextInt() & 4294967295L);
                }
                rnd = var10000;
            } else {
                long v = 0L;
                long bits;
                do {
                    bits = this.nextLong() >>> 1;
                    v = bits % n;
                } while (bits - v + (n - 1L) < 0L);
                rnd = v;
            }
            return from + rnd;
        }
    }

    public boolean nextBoolean() {
        return this.nextBits(1) != 0;
    }

    public double nextDouble() {
        return PlatformRandomKt.doubleFromParts(this.nextBits(26), this.nextBits(27));
    }

    public double nextDouble(double until) {
        return this.nextDouble(0.0, until);
    }

    public double nextDouble(double from, double until) {
        RandomKt.checkRangeBounds(from, until);
        double size = until - from;
        double var10000;
        if (Double.isInfinite(size) && !Double.isInfinite(from) && !Double.isNaN(from) && !Double.isInfinite(until) && !Double.isNaN(until)) {
            double r1 = this.nextDouble() * (until / (double) 2 - from / (double) 2);
            var10000 = from + r1 + r1;
        } else {
            var10000 = from + this.nextDouble() * size;
        }
        double r = var10000;
        return r >= until ? Math.nextAfter(until, Double.NEGATIVE_INFINITY) : r;
    }

    public float nextFloat() {
        return (float) this.nextBits(24) / 1.6777216E7F;
    }

    @NotNull
    public byte[] nextBytes(@NotNull byte[] array, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter(array, "array");
        int steps = (0 <= fromIndex ? fromIndex <= array.length : false) && (0 <= toIndex ? toIndex <= array.length : false);
        if (!steps) {
            ???;
            String var17 = "fromIndex (" + fromIndex + ") or toIndex (" + toIndex + ") are out of range: 0.." + array.length + '.';
            throw new IllegalArgumentException(var17.toString());
        } else {
            steps = fromIndex <= toIndex;
            if (!steps) {
                ???;
                String var15 = "fromIndex (" + fromIndex + ") must be not greater than toIndex (" + toIndex + ").";
                throw new IllegalArgumentException(var15.toString());
            } else {
                steps = (toIndex - fromIndex) / 4;
                int position = 0;
                position = fromIndex;
                for (int remainder = 0; remainder < steps; position += 4) {
                    remainder++;
                    ???;
                    int v = this.nextInt();
                    array[position] = (byte) v;
                    array[position + 1] = (byte) (v >>> 8);
                    array[position + 2] = (byte) (v >>> 16);
                    array[position + 3] = (byte) (v >>> 24);
                }
                int remainder = toIndex - position;
                int vr = this.nextBits(remainder * 8);
                int it = 0;
                while (it < remainder) {
                    int i = it++;
                    array[position + i] = (byte) (vr >>> i * 8);
                }
                return array;
            }
        }
    }

    @NotNull
    public byte[] nextBytes(@NotNull byte[] array) {
        Intrinsics.checkNotNullParameter(array, "array");
        return this.nextBytes(array, 0, array.length);
    }

    @NotNull
    public byte[] nextBytes(int size) {
        return this.nextBytes(new byte[size]);
    }

    @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003:\u0001\u001cB\u0007\b\u0002¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0016J\b\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0016J \u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0007H\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u0007H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0016J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0007H\u0016J\u0010\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0007H\u0016J\u0018\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0007H\u0016J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0013\u001a\u00020\u0019H\u0016J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0014\u001a\u00020\u00192\u0006\u0010\u0013\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0002R\u000e\u0010\u0005\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/random/Random$Default;", "Linfo/journeymap/shaded/kotlin/kotlin/random/Random;", "Ljava/io/Serializable;", "Linfo/journeymap/shaded/kotlin/kotlin/io/Serializable;", "()V", "defaultRandom", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "fromIndex", "toIndex", "size", "nextDouble", "", "until", "from", "nextFloat", "", "nextInt", "nextLong", "", "writeReplace", "", "Serialized", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
    public static final class Default extends Random implements Serializable {

        private Default() {
        }

        private final Object writeReplace() {
            return Random.Default.Serialized.INSTANCE;
        }

        @Override
        public int nextBits(int bitCount) {
            return Random.defaultRandom.nextBits(bitCount);
        }

        @Override
        public int nextInt() {
            return Random.defaultRandom.nextInt();
        }

        @Override
        public int nextInt(int until) {
            return Random.defaultRandom.nextInt(until);
        }

        @Override
        public int nextInt(int from, int until) {
            return Random.defaultRandom.nextInt(from, until);
        }

        @Override
        public long nextLong() {
            return Random.defaultRandom.nextLong();
        }

        @Override
        public long nextLong(long until) {
            return Random.defaultRandom.nextLong(until);
        }

        @Override
        public long nextLong(long from, long until) {
            return Random.defaultRandom.nextLong(from, until);
        }

        @Override
        public boolean nextBoolean() {
            return Random.defaultRandom.nextBoolean();
        }

        @Override
        public double nextDouble() {
            return Random.defaultRandom.nextDouble();
        }

        @Override
        public double nextDouble(double until) {
            return Random.defaultRandom.nextDouble(until);
        }

        @Override
        public double nextDouble(double from, double until) {
            return Random.defaultRandom.nextDouble(from, until);
        }

        @Override
        public float nextFloat() {
            return Random.defaultRandom.nextFloat();
        }

        @NotNull
        @Override
        public byte[] nextBytes(@NotNull byte[] array) {
            Intrinsics.checkNotNullParameter(array, "array");
            return Random.defaultRandom.nextBytes(array);
        }

        @NotNull
        @Override
        public byte[] nextBytes(int size) {
            return Random.defaultRandom.nextBytes(size);
        }

        @NotNull
        @Override
        public byte[] nextBytes(@NotNull byte[] array, int fromIndex, int toIndex) {
            Intrinsics.checkNotNullParameter(array, "array");
            return Random.defaultRandom.nextBytes(array, fromIndex, toIndex);
        }

        @Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0000\bÂ\u0002\u0018\u00002\u00060\u0001j\u0002`\u0002B\u0007\b\u0002¢\u0006\u0002\u0010\u0003J\b\u0010\u0006\u001a\u00020\u0007H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\b" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/random/Random$Default$Serialized;", "Ljava/io/Serializable;", "Linfo/journeymap/shaded/kotlin/kotlin/io/Serializable;", "()V", "serialVersionUID", "", "readResolve", "", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
        private static final class Serialized implements Serializable {

            @NotNull
            public static final Random.Default.Serialized INSTANCE = new Random.Default.Serialized();

            private static final long serialVersionUID = 0L;

            private final Object readResolve() {
                return Random.Default;
            }
        }
    }
}