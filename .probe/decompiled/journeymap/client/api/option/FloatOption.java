package journeymap.client.api.option;

public class FloatOption extends Option<Float> {

    private final float minValue;

    private final float maxValue;

    private final float incrementValue;

    private final int precision;

    public FloatOption(OptionCategory category, String fieldName, String label, Float defaultValue, float minValue, float maxValue) {
        this(category, fieldName, label, defaultValue, minValue, maxValue, 0.1F, 2);
    }

    public FloatOption(OptionCategory category, String fieldName, String label, Float defaultValue, float minValue, float maxValue, float incrementValue, int precision) {
        super(category, fieldName, label, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.incrementValue = incrementValue;
        this.precision = precision;
    }

    public float getMinValue() {
        return this.minValue;
    }

    public float getMaxValue() {
        return this.maxValue;
    }

    public float getIncrementValue() {
        return this.incrementValue;
    }

    public int getPrecision() {
        return this.precision;
    }
}