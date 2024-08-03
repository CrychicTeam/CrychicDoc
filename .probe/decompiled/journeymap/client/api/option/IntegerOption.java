package journeymap.client.api.option;

public class IntegerOption extends Option<Integer> {

    private final int minValue;

    private final int maxValue;

    public IntegerOption(OptionCategory category, String fieldName, String label, Integer defaultValue, int minValue, int maxValue) {
        super(category, fieldName, label, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return this.minValue;
    }

    public int getMaxValue() {
        return this.maxValue;
    }
}