package fuzs.puzzleslib.impl.event.data;

import fuzs.puzzleslib.api.event.v1.data.MutableDouble;

public class ValueMutableDouble implements MutableDouble {

    private double value;

    public ValueMutableDouble(double value) {
        this.value = value;
    }

    public void accept(double value) {
        this.value = value;
    }

    public double getAsDouble() {
        return this.value;
    }
}