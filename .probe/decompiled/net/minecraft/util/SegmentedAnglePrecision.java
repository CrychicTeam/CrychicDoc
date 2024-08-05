package net.minecraft.util;

import net.minecraft.core.Direction;

public class SegmentedAnglePrecision {

    private final int mask;

    private final int precision;

    private final float degreeToAngle;

    private final float angleToDegree;

    public SegmentedAnglePrecision(int int0) {
        if (int0 < 2) {
            throw new IllegalArgumentException("Precision cannot be less than 2 bits");
        } else if (int0 > 30) {
            throw new IllegalArgumentException("Precision cannot be greater than 30 bits");
        } else {
            int $$1 = 1 << int0;
            this.mask = $$1 - 1;
            this.precision = int0;
            this.degreeToAngle = (float) $$1 / 360.0F;
            this.angleToDegree = 360.0F / (float) $$1;
        }
    }

    public boolean isSameAxis(int int0, int int1) {
        int $$2 = this.getMask() >> 1;
        return (int0 & $$2) == (int1 & $$2);
    }

    public int fromDirection(Direction direction0) {
        if (direction0.getAxis().isVertical()) {
            return 0;
        } else {
            int $$1 = direction0.get2DDataValue();
            return $$1 << this.precision - 2;
        }
    }

    public int fromDegreesWithTurns(float float0) {
        return Math.round(float0 * this.degreeToAngle);
    }

    public int fromDegrees(float float0) {
        return this.normalize(this.fromDegreesWithTurns(float0));
    }

    public float toDegreesWithTurns(int int0) {
        return (float) int0 * this.angleToDegree;
    }

    public float toDegrees(int int0) {
        float $$1 = this.toDegreesWithTurns(this.normalize(int0));
        return $$1 >= 180.0F ? $$1 - 360.0F : $$1;
    }

    public int normalize(int int0) {
        return int0 & this.mask;
    }

    public int getMask() {
        return this.mask;
    }
}