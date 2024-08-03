package de.keksuccino.konkrete.json.jsonpath.internal.function.numeric;

public class Min extends AbstractAggregation {

    private Double min = Double.MAX_VALUE;

    @Override
    protected void next(Number value) {
        if (this.min > value.doubleValue()) {
            this.min = value.doubleValue();
        }
    }

    @Override
    protected Number getValue() {
        return this.min;
    }
}