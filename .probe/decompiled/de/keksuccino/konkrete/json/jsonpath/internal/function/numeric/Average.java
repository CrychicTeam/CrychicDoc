package de.keksuccino.konkrete.json.jsonpath.internal.function.numeric;

public class Average extends AbstractAggregation {

    private Double summation = 0.0;

    private Double count = 0.0;

    @Override
    protected void next(Number value) {
        Double var2 = this.count;
        this.count = this.count + 1.0;
        this.summation = this.summation + value.doubleValue();
    }

    @Override
    protected Number getValue() {
        return this.count != 0.0 ? this.summation / this.count : 0.0;
    }
}