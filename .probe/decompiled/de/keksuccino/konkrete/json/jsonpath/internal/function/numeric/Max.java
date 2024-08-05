package de.keksuccino.konkrete.json.jsonpath.internal.function.numeric;

public class Max extends AbstractAggregation {

    private Double max = Double.MIN_VALUE;

    @Override
    protected void next(Number value) {
        if (this.max < value.doubleValue()) {
            this.max = value.doubleValue();
        }
    }

    @Override
    protected Number getValue() {
        return this.max;
    }
}