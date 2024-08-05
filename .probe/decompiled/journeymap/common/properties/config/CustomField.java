package journeymap.common.properties.config;

import journeymap.common.properties.catagory.Category;
import org.apache.logging.log4j.util.Strings;

public class CustomField extends ConfigField<Object> {

    private static final String ATTR_NEG = "ATTR_NEG";

    private static final String ATTR_NUM = "ATTR_NUM";

    protected CustomField() {
    }

    public CustomField(Category category, String key) {
        this(category, key, null);
    }

    public CustomField(Category category, String key, String defaultValue, int sortOrder) {
        this(category, key, defaultValue);
        this.sortOrder(sortOrder);
    }

    public CustomField(Category category, String key, String defaultValue) {
        super(category, key);
        if (!Strings.isEmpty(defaultValue)) {
            this.defaultValue(defaultValue);
            this.setToDefault();
        }
        this.setIsNum(false);
    }

    public CustomField(Category category, String key, Integer minValue, Integer maxValue, Integer defaultValue, Boolean allowNeg) {
        this(category, key, minValue, maxValue, defaultValue, 100, allowNeg);
    }

    public CustomField(Category category, String key, Integer minValue, Integer maxValue, Integer defaultValue, Integer sortOrder, Boolean allowNeg) {
        super(category, key);
        this.range(minValue, maxValue);
        this.defaultValue(defaultValue);
        this.setToDefault();
        this.sortOrder(sortOrder);
        this.setAllowNeg(allowNeg);
        this.setIsNum(true);
    }

    private void setAllowNeg(Boolean allowNeg) {
        this.put("ATTR_NEG", allowNeg);
    }

    private void setIsNum(Boolean number) {
        this.put("ATTR_NUM", number);
    }

    @Override
    public boolean validate(boolean fix) {
        Object value = this.get();
        if (value instanceof Integer) {
            return this.validateInt(fix);
        } else {
            return value instanceof String ? super.validate(fix) : false;
        }
    }

    private boolean validateInt(boolean fix) {
        boolean valid = super.validate(fix);
        valid = this.require(new String[] { "min", "max" }) && valid;
        Integer value = this.getAsInteger();
        if (value == null || value < this.getMinValue() || value > this.getMaxValue()) {
            if (fix) {
                this.setToDefault();
            } else {
                valid = false;
            }
        }
        return valid;
    }

    public CustomField range(int min, int max) {
        this.put("min", Integer.valueOf(min));
        this.put("max", Integer.valueOf(max));
        return this;
    }

    public int getMinValue() {
        return this.getIntegerAttr("min");
    }

    public int getMaxValue() {
        return this.getIntegerAttr("max");
    }

    @Override
    public Object getDefaultValue() {
        return this.get("default");
    }

    public String getAsString() {
        return this.getStringAttr("value");
    }

    public Integer getAsInteger() {
        try {
            Integer val = Integer.valueOf(this.get().toString());
            if (val < this.getMinValue() || val > this.getMaxValue()) {
                this.setToDefault();
            }
        } catch (NumberFormatException var2) {
            this.setToDefault();
        }
        return this.getIntegerAttr("value");
    }

    @Override
    public Object get() {
        return this.get("value");
    }

    public CustomField set(Object value) {
        try {
            Integer val = Integer.valueOf(value.toString());
            if (val < this.getMinValue() || val > this.getMaxValue()) {
                this.setToDefault();
            }
        } catch (NumberFormatException var3) {
        }
        super.set(value);
        return this;
    }

    public boolean allowNeg() {
        return this.getBooleanAttr("ATTR_NEG");
    }

    public boolean isNumber() {
        return this.getBooleanAttr("ATTR_NUM");
    }
}