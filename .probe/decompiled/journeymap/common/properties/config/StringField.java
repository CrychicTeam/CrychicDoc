package journeymap.common.properties.config;

import com.google.common.base.Joiner;
import java.util.Arrays;
import java.util.List;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.properties.catagory.Category;
import org.apache.logging.log4j.util.Strings;

public class StringField extends ConfigField<String> {

    public static final String ATTR_VALUE_PROVIDER = "valueProvider";

    public static final String ATTR_VALUE_PATTERN = "pattern";

    public static final String ATTR_MULTILINE = "multiline";

    protected StringField() {
    }

    public StringField(Category category, String key) {
        this(category, key, (String[]) null, (String) null);
    }

    public StringField(Category category, String key, String[] validValues, String defaultValue) {
        super(category, key);
        if (validValues != null) {
            this.put("validValues", Joiner.on(",").join(validValues));
        }
        if (!Strings.isEmpty(defaultValue)) {
            this.defaultValue(defaultValue);
            this.setToDefault();
        }
    }

    public StringField(Category category, String key, Class<? extends StringField.ValuesProvider> valueProviderClass) {
        this(category, key, null, valueProviderClass);
    }

    public StringField(Category category, String key, String defaultValue, Class<? extends StringField.ValuesProvider> valueProviderClass) {
        super(category, key);
        if (valueProviderClass != null) {
            this.put("valueProvider", valueProviderClass);
            try {
                StringField.ValuesProvider valuesProvider = (StringField.ValuesProvider) valueProviderClass.newInstance();
                this.validValues(valuesProvider.getStrings());
                if (!Strings.isEmpty(defaultValue)) {
                    this.defaultValue(defaultValue);
                } else {
                    this.defaultValue(valuesProvider.getDefaultString());
                }
                this.setToDefault();
                if (!this.getValidValues().contains(this.getDefaultValue())) {
                    Journeymap.getLogger().error(String.format("Default value '%s' isn't in one of the valid values '%s' for %s", this.getDefaultValue(), this.getStringAttr("validValues"), this));
                }
            } catch (Throwable var6) {
                Journeymap.getLogger().error(String.format("Couldn't use ValuesProvider %s: %s", valueProviderClass, LogFormatter.toString(var6)));
            }
        }
    }

    public String getDefaultValue() {
        return this.getStringAttr("default");
    }

    public String get() {
        return this.getStringAttr("value");
    }

    public StringField set(String value) {
        super.set(value);
        return this;
    }

    public StringField pattern(String regexPattern) {
        this.put("pattern", regexPattern);
        return this;
    }

    public String getPattern() {
        return this.getStringAttr("pattern");
    }

    public Class<? extends StringField.ValuesProvider> getValuesProviderClass() {
        Object value = this.get("valueProvider");
        if (value == null) {
            return null;
        } else if (value instanceof Class) {
            return (Class<? extends StringField.ValuesProvider>) value;
        } else {
            if (value instanceof String) {
                try {
                    value = Class.forName(value.toString());
                    this.put("valueProvider", value);
                    return (Class<? extends StringField.ValuesProvider>) value;
                } catch (Exception var3) {
                    Journeymap.getLogger().warn(String.format("Couldn't get ValuesProvider Class %s : %s", value, var3.getMessage()));
                }
            }
            return null;
        }
    }

    @Override
    public boolean validate(boolean fix) {
        boolean hasRequired = this.require(new String[] { "type" });
        boolean hasCategory = this.getCategory() != null;
        boolean valid = hasRequired && hasCategory;
        String value = this.get();
        if (Strings.isNotEmpty(value)) {
            String pattern = this.getPattern();
            if (Strings.isNotEmpty(pattern)) {
                boolean patternValid = value.matches(pattern);
                if (!patternValid) {
                    Journeymap.getLogger().warn(String.format("Value '%s' doesn't match pattern '%s' for %s", value, pattern, this));
                    if (fix && Strings.isNotEmpty(this.getDefaultValue())) {
                        this.setToDefault();
                        Journeymap.getLogger().warn(String.format("Value set to default '%s' for %s", this.getDefaultValue(), this));
                    } else {
                        valid = false;
                    }
                }
            }
        }
        List<String> validValues = this.getValidValues();
        if (validValues != null && !validValues.contains(value)) {
            Journeymap.getLogger().warn(String.format("Value '%s' isn't in one of the valid values '%s' for %s", value, this.getStringAttr("validValues"), this));
            String defaultValue = this.getDefaultValue();
            if (fix && Strings.isNotEmpty(defaultValue)) {
                this.setToDefault();
                Journeymap.getLogger().warn(String.format("Value set to default '%s' for %s", defaultValue, this));
            } else {
                valid = false;
            }
        }
        return valid;
    }

    public List<String> getValidValues() {
        String validValuesString = this.getStringAttr("validValues");
        return !Strings.isEmpty(validValuesString) ? Arrays.asList(validValuesString.split(",")) : null;
    }

    public StringField validValues(Iterable<String> values) {
        this.put("validValues", Joiner.on(",").join(values));
        return this;
    }

    public boolean isMultiline() {
        Boolean val = this.getBooleanAttr("multiline");
        return val == null ? false : val;
    }

    public StringField multiline(boolean isMultiline) {
        this.put("multiline", Boolean.valueOf(isMultiline));
        return this;
    }

    public interface ValuesProvider {

        List<String> getStrings();

        String getDefaultString();
    }
}