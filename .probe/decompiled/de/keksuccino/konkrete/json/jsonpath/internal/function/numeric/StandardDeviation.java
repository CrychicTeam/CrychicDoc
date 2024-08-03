package de.keksuccino.konkrete.json.jsonpath.internal.function.numeric;

public class StandardDeviation extends AbstractAggregation {

    private Double sumSq = 0.0;

    private Double sum = 0.0;

    private Double count = 0.0;

    @Override
    protected void next(Number value) {
        this.sum = this.sum + value.doubleValue();
        this.sumSq = this.sumSq + value.doubleValue() * value.doubleValue();
        Double var2 = this.count;
        this.count = this.count + 1.0;
    }

    @Override
    protected Number getValue() {
        return Math.sqrt(this.sumSq / this.count - this.sum * this.sum / this.count / this.count);
    }
}