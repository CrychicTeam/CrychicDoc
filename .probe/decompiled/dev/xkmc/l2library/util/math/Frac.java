package dev.xkmc.l2library.util.math;

public class Frac implements Comparable<Frac> {

    public long num;

    public long den;

    public double val;

    public boolean isFrac = true;

    public Frac(long num, long den) {
        this.num = num;
        this.den = den;
        this.validate();
    }

    private Frac(double val) {
        this.val = val;
        this.isFrac = false;
    }

    public static Frac mult(Frac f0, Frac f1) {
        if (!f0.isFrac || !f1.isFrac) {
            return new Frac(f0.getVal() * f1.getVal());
        } else if (f0.den != 0L && f1.den != 0L) {
            try {
                long gcd0 = gcd(f0.num, f1.den);
                long gcd1 = gcd(f1.num, f0.den);
                long num = Math.multiplyExact(f0.num / gcd0, f1.num / gcd1);
                long den = Math.multiplyExact(f0.den / gcd1, f1.den / gcd0);
                return new Frac(num, den);
            } catch (Exception var10) {
                return new Frac(f0.getVal() * f1.getVal());
            }
        } else {
            return new Frac(1L, 0L);
        }
    }

    private static long gcd(long a, long b) {
        long max = Math.max(a, b);
        long min = Math.min(a, b);
        return min == 0L ? max : gcd(min, max % min);
    }

    public void add(Frac o) {
        if (!this.isFrac || !o.isFrac) {
            this.isFrac = false;
            this.val = this.getVal() + o.getVal();
            this.num = this.den = 0L;
        } else if (this.den != 0L) {
            if (o.den == 0L) {
                this.den = 0L;
                this.num = 1L;
            } else {
                double val = this.getVal();
                try {
                    long gcd = gcd(this.den, o.den);
                    long v0 = Math.multiplyExact(this.num, o.den / gcd);
                    long v1 = Math.multiplyExact(o.num, this.den / gcd);
                    this.num = Math.addExact(v0, v1);
                    this.den = Math.multiplyExact(this.den, o.den / gcd);
                } catch (Exception var10) {
                    this.isFrac = false;
                    this.num = this.den = 0L;
                    this.val = val + o.getVal();
                }
                this.validate();
            }
        }
    }

    public int compareTo(Frac o) {
        return this.equals(o) ? 0 : Double.compare(this.getVal(), o.getVal());
    }

    public boolean equals(Object o) {
        if (!(o instanceof Frac f)) {
            return false;
        } else {
            return this.isFrac && ((Frac) o).isFrac ? f.num == this.num && f.den == this.den : false;
        }
    }

    public double getVal() {
        if (!this.isFrac) {
            return this.val;
        } else {
            return this.den == 0L ? Double.POSITIVE_INFINITY : 1.0 * (double) this.num / (double) this.den;
        }
    }

    public void times(Frac base) {
        if (!this.isFrac || !base.isFrac) {
            this.isFrac = false;
            this.val = this.getVal() * base.getVal();
            this.num = this.den = 0L;
        } else if (this.den != 0L) {
            double val = this.getVal();
            try {
                long gcd0 = gcd(this.num, base.den);
                long gcd1 = gcd(base.num, this.den);
                this.num = Math.multiplyExact(this.num / gcd0, base.num / gcd1);
                this.den = Math.multiplyExact(this.den / gcd1, base.den / gcd0);
                this.validate();
            } catch (Exception var8) {
                this.isFrac = false;
                this.num = this.den = 0L;
                this.val = val * base.getVal();
            }
        }
    }

    public String toString() {
        return this.isFrac && this.num <= 100L && this.den <= 100L ? this.num + "/" + this.den : (double) Math.round(this.getVal() * 100.0) / 100.0 + "";
    }

    private void validate() {
        if (this.isFrac) {
            if (this.den == 0L) {
                this.num = 1L;
            } else {
                long gcd = gcd(this.num, this.den);
                this.num /= gcd;
                this.den /= gcd;
            }
        }
    }

    public Frac revert() {
        return this.isFrac ? new Frac(this.den, this.den - this.num) : new Frac(1.0 / (1.0 - this.val));
    }
}