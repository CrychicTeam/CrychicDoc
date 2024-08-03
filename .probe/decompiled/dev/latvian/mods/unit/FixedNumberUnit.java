package dev.latvian.mods.unit;

public final class FixedNumberUnit extends Unit {

    public static final FixedNumberUnit ZERO = new FixedNumberUnit(0.0);

    public static final FixedNumberUnit ONE = new FixedNumberUnit(1.0);

    public static final FixedNumberUnit MINUS_ONE = new FixedNumberUnit(-1.0);

    public static final FixedNumberUnit TEN = new FixedNumberUnit(10.0);

    public static final FixedNumberUnit SIXTEEN = new FixedNumberUnit(16.0);

    public static final FixedNumberUnit PI = new FixedNumberUnit(Math.PI);

    public static final FixedNumberUnit TWO_PI = new FixedNumberUnit(Math.PI * 2);

    public static final FixedNumberUnit HALF_PI = new FixedNumberUnit(Math.PI / 2);

    public static final FixedNumberUnit E = new FixedNumberUnit(Math.E);

    public static final FixedNumberUnit NaN = new FixedNumberUnit(Double.NaN);

    public final double value;

    public static FixedNumberUnit of(double value) {
        if (value == 0.0) {
            return ZERO;
        } else if (value == 1.0) {
            return ONE;
        } else if (value == -1.0) {
            return MINUS_ONE;
        } else if (value == 10.0) {
            return TEN;
        } else {
            return value == 16.0 ? SIXTEEN : new FixedNumberUnit(value);
        }
    }

    private FixedNumberUnit(double value) {
        this.value = value;
    }

    @Override
    public boolean isFixed() {
        return true;
    }

    @Override
    public double get(UnitVariables variables) {
        return this.value;
    }

    @Override
    public boolean getBoolean(UnitVariables variables) {
        return this != ZERO;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            if (obj instanceof FixedNumberUnit u && this.value == u.value) {
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    public void toString(StringBuilder builder) {
        long r = Math.round(this.value);
        if (Math.abs((double) r - this.value) < 1.0E-5) {
            builder.append(r);
        } else {
            builder.append(this.value);
        }
    }

    @Override
    public Unit negate() {
        return of(-this.value);
    }

    @Override
    public Unit add(Unit other) {
        return (Unit) (other instanceof FixedNumberUnit u ? of(this.value + u.value) : super.add(other));
    }

    @Override
    public Unit add(double value) {
        return of(this.value + value);
    }

    @Override
    public Unit sub(Unit other) {
        return (Unit) (other instanceof FixedNumberUnit u ? of(this.value - u.value) : super.sub(other));
    }

    @Override
    public Unit sub(double value) {
        return of(this.value - value);
    }

    @Override
    public Unit mul(Unit other) {
        return (Unit) (other instanceof FixedNumberUnit u ? of(this.value * u.value) : super.mul(other));
    }

    @Override
    public Unit mul(double value) {
        return of(this.value * value);
    }

    @Override
    public Unit div(Unit other) {
        return (Unit) (other instanceof FixedNumberUnit u ? of(this.value / u.value) : super.div(other));
    }

    @Override
    public Unit div(double value) {
        return of(this.value / value);
    }

    @Override
    public Unit mod(Unit other) {
        return (Unit) (other instanceof FixedNumberUnit u ? of(this.value % u.value) : super.mod(other));
    }

    @Override
    public Unit mod(double value) {
        return of(this.value % value);
    }

    @Override
    public Unit pow(Unit other) {
        return (Unit) (other instanceof FixedNumberUnit u ? of(Math.pow(this.value, u.value)) : super.add(other));
    }

    @Override
    public Unit abs() {
        return of(Math.abs(this.value));
    }

    @Override
    public Unit sin() {
        return of(Math.sin(this.value));
    }

    @Override
    public Unit cos() {
        return of(Math.cos(this.value));
    }

    @Override
    public Unit tan() {
        return of(Math.tan(this.value));
    }

    @Override
    public Unit deg() {
        return of(Math.toDegrees(this.value));
    }

    @Override
    public Unit rad() {
        return of(Math.toRadians(this.value));
    }

    @Override
    public Unit atan() {
        return of(Math.atan(this.value));
    }

    @Override
    public Unit log() {
        return of(Math.log(this.value));
    }

    @Override
    public Unit log10() {
        return of(Math.log10(this.value));
    }

    @Override
    public Unit log1p() {
        return of(Math.log1p(this.value));
    }

    @Override
    public Unit sqrt() {
        return of(Math.sqrt(this.value));
    }

    @Override
    public Unit sq() {
        return of(this.value * this.value);
    }

    @Override
    public Unit floor() {
        return of(Math.floor(this.value));
    }

    @Override
    public Unit ceil() {
        return of(Math.ceil(this.value));
    }

    @Override
    public Unit bool() {
        return this == ZERO ? FixedBooleanUnit.FALSE : FixedBooleanUnit.TRUE;
    }

    @Override
    public Unit smoothstep() {
        return of(this.value * this.value * this.value * (this.value * (this.value * 6.0 - 15.0) + 10.0));
    }
}