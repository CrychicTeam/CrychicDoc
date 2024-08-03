package journeymap.common.properties.config;

import journeymap.common.properties.catagory.Category;

public class IntegerField extends ConfigField<Integer> {

    public static final String ATTR_MIN = "min";

    public static final String ATTR_MAX = "max";

    private boolean updatedMaxValue = false;

    protected IntegerField() {
    }

    public IntegerField(Category category, String key, int minValue, int maxValue, int defaultValue) {
        this(category, key, minValue, maxValue, defaultValue, 100);
    }

    public IntegerField(Category category, String key, int minValue, int maxValue, int defaultValue, int sortOrder) {
        super(category, key);
        this.range(minValue, maxValue);
        this.defaultValue(Integer.valueOf(defaultValue));
        this.setToDefault();
        this.sortOrder(sortOrder);
    }

    public Integer getDefaultValue() {
        return this.getIntegerAttr("default");
    }

    public Integer get() {
        return this.getIntegerAttr("value");
    }

    @Override
    public boolean validate(boolean fix) {
        boolean valid = super.validate(fix);
        valid = this.require(new String[] { "min", "max" }) && valid;
        Integer value = this.get();
        if (value == null || value < this.getMinValue() || value > this.getMaxValue()) {
            if (fix && this.updatedMaxValue) {
                this.set(this.getIntegerAttr("max"));
                return valid;
            }
            if (fix) {
                this.setToDefault();
            } else {
                valid = false;
            }
        }
        return valid;
    }

    public IntegerField range(int min, int max) {
        this.put("min", Integer.valueOf(min));
        this.put("max", Integer.valueOf(max));
        return this;
    }

    public IntegerField setParent(String fieldName, Object value) {
        return (IntegerField) super.setParent(fieldName, value);
    }

    public int getMinValue() {
        return this.getIntegerAttr("min");
    }

    public int getMaxValue() {
        return this.getIntegerAttr("max");
    }

    public Integer incrementAndGet() {
        Integer value = Math.min(this.getMaxValue(), this.get() + 1);
        this.set(value);
        return value;
    }

    public Integer decrementAndGet() {
        Integer value = Math.max(this.getMinValue(), this.get() - 1);
        this.set(value);
        return value;
    }
}