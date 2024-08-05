package net.minecraft.world.entity.ai.attributes;

import net.minecraft.util.Mth;

public class RangedAttribute extends Attribute {

    private final double minValue;

    private final double maxValue;

    public RangedAttribute(String string0, double double1, double double2, double double3) {
        super(string0, double1);
        this.minValue = double2;
        this.maxValue = double3;
        if (double2 > double3) {
            throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
        } else if (double1 < double2) {
            throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
        } else if (double1 > double3) {
            throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
        }
    }

    public double getMinValue() {
        return this.minValue;
    }

    public double getMaxValue() {
        return this.maxValue;
    }

    @Override
    public double sanitizeValue(double double0) {
        return Double.isNaN(double0) ? this.minValue : Mth.clamp(double0, this.minValue, this.maxValue);
    }
}