package dev.latvian.mods.rhino.v8dtoa;

class DiyFp {

    static final int kSignificandSize = 64;

    static final long kUint64MSB = Long.MIN_VALUE;

    private long f;

    private int e;

    private static boolean uint64_gte(long a, long b) {
        return a == b || a > b ^ a < 0L ^ b < 0L;
    }

    static DiyFp minus(DiyFp a, DiyFp b) {
        DiyFp result = new DiyFp(a.f, a.e);
        result.subtract(b);
        return result;
    }

    static DiyFp times(DiyFp a, DiyFp b) {
        DiyFp result = new DiyFp(a.f, a.e);
        result.multiply(b);
        return result;
    }

    static DiyFp normalize(DiyFp a) {
        DiyFp result = new DiyFp(a.f, a.e);
        result.normalize();
        return result;
    }

    DiyFp() {
        this.f = 0L;
        this.e = 0;
    }

    DiyFp(long f, int e) {
        this.f = f;
        this.e = e;
    }

    void subtract(DiyFp other) {
        assert this.e == other.e;
        assert uint64_gte(this.f, other.f);
        this.f = this.f - other.f;
    }

    void multiply(DiyFp other) {
        long kM32 = 4294967295L;
        long a = this.f >>> 32;
        long b = this.f & 4294967295L;
        long c = other.f >>> 32;
        long d = other.f & 4294967295L;
        long ac = a * c;
        long bc = b * c;
        long ad = a * d;
        long bd = b * d;
        long tmp = (bd >>> 32) + (ad & 4294967295L) + (bc & 4294967295L);
        tmp += 2147483648L;
        long result_f = ac + (ad >>> 32) + (bc >>> 32) + (tmp >>> 32);
        this.e = this.e + other.e + 64;
        this.f = result_f;
    }

    void normalize() {
        assert this.f != 0L;
        long f = this.f;
        int e = this.e;
        for (long k10MSBits = -18014398509481984L; (f & -18014398509481984L) == 0L; e -= 10) {
            f <<= 10;
        }
        while ((f & Long.MIN_VALUE) == 0L) {
            f <<= 1;
            e--;
        }
        this.f = f;
        this.e = e;
    }

    long f() {
        return this.f;
    }

    int e() {
        return this.e;
    }

    void setF(long new_value) {
        this.f = new_value;
    }

    void setE(int new_value) {
        this.e = new_value;
    }

    public String toString() {
        return "[DiyFp f:" + this.f + ", e:" + this.e + "]";
    }
}