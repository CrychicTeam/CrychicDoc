package journeymap.common.properties.config;

import journeymap.common.properties.catagory.Category;

public class FloatField extends ConfigField<Float> {

    public static final String ATTR_MIN = "min";

    public static final String ATTR_MAX = "max";

    public static final String ATTR_INC_VAL = "inc";

    public static final String PRECISION = "precision";

    protected FloatField() {
    }

    public FloatField(Category category, String key, float minValue, float maxValue, float defaultValue) {
        this(category, key, minValue, maxValue, defaultValue, 0.1F, 2);
    }

    public FloatField(Category category, String key, float minValue, float maxValue, float defaultValue, float incrementValue, int precision) {
        this(category, key, minValue, maxValue, defaultValue, incrementValue, precision, 100);
    }

    public FloatField(Category category, String key, float minValue, float maxValue, float defaultValue, float incrementValue, int precision, int sortOrder) {
        super(category, key);
        this.range(minValue, maxValue);
        this.defaultValue(Float.valueOf(defaultValue));
        this.setToDefault();
        this.sortOrder(sortOrder);
        this.setIncrementValue(incrementValue);
        this.setPrecision(precision);
    }

    public FloatField(Category category, String key, float minValue, float maxValue, float defaultValue, int sortOrder) {
        super(category, key);
        this.range(minValue, maxValue);
        this.defaultValue(Float.valueOf(defaultValue));
        this.setToDefault();
        this.sortOrder(sortOrder);
        this.setIncrementValue(0.1F);
        this.setPrecision(2);
    }

    public Float getDefaultValue() {
        return this.getFloatAttr("default");
    }

    public Float get() {
        return this.getFloatAttr("value");
    }

    @Override
    public boolean validate(boolean fix) {
        boolean valid = super.validate(fix);
        valid = this.require(new String[] { "min", "max" }) && valid;
        Float value = this.get();
        if (value == null || !(value >= this.getMinValue()) || !(value <= this.getMaxValue())) {
            if (fix) {
                this.setToDefault();
            } else {
                valid = false;
            }
        }
        return valid;
    }

    private void setPrecision(int precision) {
        this.put("precision", Integer.valueOf(precision));
    }

    private void setIncrementValue(float value) {
        this.put("inc", Float.valueOf(value));
    }

    public FloatField range(float min, float max) {
        this.put("min", Float.valueOf(min));
        this.put("max", Float.valueOf(max));
        return this;
    }

    public float getMinValue() {
        return this.getFloatAttr("min");
    }

    public float getMaxValue() {
        return this.getFloatAttr("max");
    }

    public float getIncrementValue() {
        return this.getFloatAttr("inc");
    }

    public int getPrecision() {
        return this.getIntegerAttr("precision");
    }
}