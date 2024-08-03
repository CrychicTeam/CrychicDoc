package dev.latvian.mods.rhino.v8dtoa;

public class DoubleHelper {

    static final long kSignMask = Long.MIN_VALUE;

    static final long kExponentMask = 9218868437227405312L;

    static final long kSignificandMask = 4503599627370495L;

    static final long kHiddenBit = 4503599627370496L;

    private static final int kSignificandSize = 52;

    private static final int kExponentBias = 1075;

    private static final int kDenormalExponent = -1074;

    static DiyFp asDiyFp(long d64) {
        assert !isSpecial(d64);
        return new DiyFp(significand(d64), exponent(d64));
    }

    static DiyFp asNormalizedDiyFp(long d64) {
        long f = significand(d64);
        int e = exponent(d64);
        assert f != 0L;
        while ((f & 4503599627370496L) == 0L) {
            f <<= 1;
            e--;
        }
        f <<= 11;
        e -= 11;
        return new DiyFp(f, e);
    }

    static int exponent(long d64) {
        if (isDenormal(d64)) {
            return -1074;
        } else {
            int biased_e = (int) ((d64 & 9218868437227405312L) >>> 52 & 4294967295L);
            return biased_e - 1075;
        }
    }

    static long significand(long d64) {
        long significand = d64 & 4503599627370495L;
        return !isDenormal(d64) ? significand + 4503599627370496L : significand;
    }

    static boolean isDenormal(long d64) {
        return (d64 & 9218868437227405312L) == 0L;
    }

    static boolean isSpecial(long d64) {
        return (d64 & 9218868437227405312L) == 9218868437227405312L;
    }

    static boolean isNan(long d64) {
        return (d64 & 9218868437227405312L) == 9218868437227405312L && (d64 & 4503599627370495L) != 0L;
    }

    static boolean isInfinite(long d64) {
        return (d64 & 9218868437227405312L) == 9218868437227405312L && (d64 & 4503599627370495L) == 0L;
    }

    static int sign(long d64) {
        return (d64 & Long.MIN_VALUE) == 0L ? 1 : -1;
    }

    static void normalizedBoundaries(long d64, DiyFp m_minus, DiyFp m_plus) {
        DiyFp v = asDiyFp(d64);
        boolean significand_is_zero = v.f() == 4503599627370496L;
        m_plus.setF((v.f() << 1) + 1L);
        m_plus.setE(v.e() - 1);
        m_plus.normalize();
        if (significand_is_zero && v.e() != -1074) {
            m_minus.setF((v.f() << 2) - 1L);
            m_minus.setE(v.e() - 2);
        } else {
            m_minus.setF((v.f() << 1) - 1L);
            m_minus.setE(v.e() - 1);
        }
        m_minus.setF(m_minus.f() << m_minus.e() - m_plus.e());
        m_minus.setE(m_plus.e());
    }
}