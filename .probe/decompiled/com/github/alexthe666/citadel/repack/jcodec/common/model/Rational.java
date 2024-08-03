package com.github.alexthe666.citadel.repack.jcodec.common.model;

import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class Rational {

    public static final Rational ONE = new Rational(1, 1);

    public static final Rational HALF = new Rational(1, 2);

    public static final Rational ZERO = new Rational(0, 1);

    public final int num;

    public final int den;

    public static Rational R(int num, int den) {
        return new Rational(num, den);
    }

    public static Rational R1(int num) {
        return R(num, 1);
    }

    public Rational(int num, int den) {
        this.num = num;
        this.den = den;
    }

    public int getNum() {
        return this.num;
    }

    public int getDen() {
        return this.den;
    }

    public static Rational parseRational(String string) {
        return parse(string);
    }

    public static Rational parse(String string) {
        int idx = string.indexOf(":");
        if (idx < 0) {
            idx = string.indexOf("/");
        }
        if (idx > 0) {
            String num = string.substring(0, idx);
            String den = string.substring(idx + 1);
            return new Rational(Integer.parseInt(num), Integer.parseInt(den));
        } else {
            return R(Integer.parseInt(string), 1);
        }
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + this.den;
        return 31 * result + this.num;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Rational other = (Rational) obj;
            return this.den != other.den ? false : this.num == other.num;
        }
    }

    public int multiplyS(int val) {
        return (int) ((long) this.num * (long) val / (long) this.den);
    }

    public int divideS(int val) {
        return (int) ((long) this.den * (long) val / (long) this.num);
    }

    public int divideByS(int val) {
        return this.num / (this.den * val);
    }

    public long multiplyLong(long val) {
        return (long) this.num * val / (long) this.den;
    }

    public long divideLong(long val) {
        return (long) this.den * val / (long) this.num;
    }

    public Rational flip() {
        return new Rational(this.den, this.num);
    }

    public boolean smallerThen(Rational sec) {
        return this.num * sec.den < sec.num * this.den;
    }

    public boolean greaterThen(Rational sec) {
        return this.num * sec.den > sec.num * this.den;
    }

    public boolean smallerOrEqualTo(Rational sec) {
        return this.num * sec.den <= sec.num * this.den;
    }

    public boolean greaterOrEqualTo(Rational sec) {
        return this.num * sec.den >= sec.num * this.den;
    }

    public boolean equalsRational(Rational other) {
        return this.num * other.den == other.num * this.den;
    }

    public Rational plus(Rational other) {
        return reduce(this.num * other.den + other.num * this.den, this.den * other.den);
    }

    public RationalLarge plusLarge(RationalLarge other) {
        return RationalLarge.reduceLong((long) this.num * other.den + other.num * (long) this.den, (long) this.den * other.den);
    }

    public Rational minus(Rational other) {
        return reduce(this.num * other.den - other.num * this.den, this.den * other.den);
    }

    public RationalLarge minusLarge(RationalLarge other) {
        return RationalLarge.reduceLong((long) this.num * other.den - other.num * (long) this.den, (long) this.den * other.den);
    }

    public Rational plusInt(int scalar) {
        return new Rational(this.num + scalar * this.den, this.den);
    }

    public Rational minusInt(int scalar) {
        return new Rational(this.num - scalar * this.den, this.den);
    }

    public Rational multiplyInt(int scalar) {
        return new Rational(this.num * scalar, this.den);
    }

    public Rational divideInt(int scalar) {
        return new Rational(this.den * scalar, this.num);
    }

    public Rational divideByInt(int scalar) {
        return new Rational(this.num, this.den * scalar);
    }

    public Rational multiply(Rational other) {
        return reduce(this.num * other.num, this.den * other.den);
    }

    public RationalLarge multiplyLarge(RationalLarge other) {
        return RationalLarge.reduceLong((long) this.num * other.num, (long) this.den * other.den);
    }

    public Rational divide(Rational other) {
        return reduce(other.num * this.den, other.den * this.num);
    }

    public RationalLarge divideLarge(RationalLarge other) {
        return RationalLarge.reduceLong(other.num * (long) this.den, other.den * (long) this.num);
    }

    public Rational divideBy(Rational other) {
        return reduce(this.num * other.den, this.den * other.num);
    }

    public RationalLarge divideByLarge(RationalLarge other) {
        return RationalLarge.reduceLong((long) this.num * other.den, (long) this.den * other.num);
    }

    public float scalar() {
        return (float) this.num / (float) this.den;
    }

    public int scalarClip() {
        return this.num / this.den;
    }

    public static Rational reduce(int num, int den) {
        int gcd = MathUtil.gcd(num, den);
        return new Rational(num / gcd, den / gcd);
    }

    public static Rational reduceRational(Rational r) {
        return reduce(r.getNum(), r.getDen());
    }

    public String toString() {
        return this.num + "/" + this.den;
    }

    public double toDouble() {
        return (double) this.num / (double) this.den;
    }
}