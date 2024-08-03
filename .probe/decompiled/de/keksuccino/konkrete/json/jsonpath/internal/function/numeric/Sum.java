package de.keksuccino.konkrete.json.jsonpath.internal.function.numeric;

public class Sum extends AbstractAggregation {

    private Double summation = 0.0;

    @Override
    protected void next(Number value) {
        this.summation = this.summation + value.doubleValue();
    }

    @Override
    protected Number getValue() {
        return this.summation;
    }
}