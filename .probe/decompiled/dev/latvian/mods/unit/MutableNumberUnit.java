package dev.latvian.mods.unit;

public final class MutableNumberUnit extends Unit {

    public double value;

    public MutableNumberUnit(double value) {
        this.value = value;
    }

    public void set(double value) {
        this.value = value;
    }

    @Override
    public double get(UnitVariables variables) {
        return this.value;
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
}