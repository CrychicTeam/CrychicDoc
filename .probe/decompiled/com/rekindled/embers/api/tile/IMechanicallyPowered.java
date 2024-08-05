package com.rekindled.embers.api.tile;

public interface IMechanicallyPowered {

    double getMechanicalSpeed(double var1);

    double getNominalSpeed();

    default double getMinimumPower() {
        return 0.0;
    }

    default double getMaximumPower() {
        return Double.POSITIVE_INFINITY;
    }

    default double getStandardPowerRatio() {
        return 0.0;
    }
}