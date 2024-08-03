package net.minecraft.util;

public class SmoothDouble {

    private double targetValue;

    private double remainingValue;

    private double lastAmount;

    public double getNewDeltaValue(double double0, double double1) {
        this.targetValue += double0;
        double $$2 = this.targetValue - this.remainingValue;
        double $$3 = Mth.lerp(0.5, this.lastAmount, $$2);
        double $$4 = Math.signum($$2);
        if ($$4 * $$2 > $$4 * this.lastAmount) {
            $$2 = $$3;
        }
        this.lastAmount = $$3;
        this.remainingValue += $$2 * double1;
        return $$2 * double1;
    }

    public void reset() {
        this.targetValue = 0.0;
        this.remainingValue = 0.0;
        this.lastAmount = 0.0;
    }
}