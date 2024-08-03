package journeymap.client.api.option;

public class CustomIntegerOption extends CustomOption<Integer> {

    private final Integer minValue;

    private final Integer maxValue;

    private final Boolean allowNeg;

    public CustomIntegerOption(OptionCategory category, String fieldName, String label, Integer defaultValue, Integer minValue, Integer maxValue, Boolean allowNeg) {
        super(category, fieldName, label, defaultValue);
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.allowNeg = allowNeg;
    }

    public Integer getMinValue() {
        return this.minValue;
    }

    public Integer getMaxValue() {
        return this.maxValue;
    }

    public Boolean getAllowNeg() {
        return this.allowNeg;
    }
}