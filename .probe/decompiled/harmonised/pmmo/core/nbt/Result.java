package harmonised.pmmo.core.nbt;

import java.util.Map;

public record Result(Operator operator, String comparator, String comparison, Map<String, Double> values) {

    public boolean compares() {
        switch(this.operator) {
            case EQUALS:
                return this.comparator.equals(this.comparison);
            case GREATER_THAN:
                return Double.valueOf(this.comparator) < Double.valueOf(this.comparison);
            case LESS_THAN:
                return Double.valueOf(this.comparator) > Double.valueOf(this.comparison);
            case GREATER_THAN_OR_EQUAL:
                return Double.valueOf(this.comparator) <= Double.valueOf(this.comparison);
            case LESS_THAN_OR_EQUAL:
                return Double.valueOf(this.comparator) >= Double.valueOf(this.comparison);
            case EXISTS:
                return !this.comparison.isEmpty();
            default:
                return false;
        }
    }
}