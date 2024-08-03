package dev.shadowsoffire.placebo.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Property {

    private String name;

    private String value;

    private String defaultValue;

    private String comment;

    private String[] values;

    private String[] defaultValues;

    private String[] validValues;

    private String langKey;

    private String minValue;

    private String maxValue;

    private boolean requiresWorldRestart = false;

    private boolean showInGui = true;

    private boolean requiresMcRestart = false;

    private Pattern validationPattern;

    private final boolean wasRead;

    private final boolean isList;

    private boolean isListLengthFixed = false;

    private int maxListLength = -1;

    private final Property.Type type;

    private boolean changed = false;

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Property(String name, String value, Property.Type type) {
        this(name, value, type, false, new String[0], name);
    }

    public Property(String name, String value, Property.Type type, boolean read) {
        this(name, value, type, read, new String[0], name);
    }

    public Property(String name, String value, Property.Type type, String[] validValues) {
        this(name, value, type, false, validValues, name);
    }

    public Property(String name, String value, Property.Type type, String langKey) {
        this(name, value, type, false, new String[0], langKey);
    }

    public Property(String name, String value, Property.Type type, boolean read, String langKey) {
        this(name, value, type, read, new String[0], langKey);
    }

    public Property(String name, String value, Property.Type type, String[] validValues, String langKey) {
        this(name, value, type, false, validValues, langKey);
    }

    Property(String name, String value, Property.Type type, boolean read, String[] validValues, String langKey) {
        this.setName(name);
        this.value = value;
        this.values = new String[0];
        this.type = type;
        this.wasRead = read;
        this.isList = false;
        this.defaultValue = value;
        this.defaultValues = new String[0];
        this.validValues = validValues;
        this.isListLengthFixed = false;
        this.maxListLength = -1;
        this.minValue = String.valueOf(Integer.MIN_VALUE);
        this.maxValue = String.valueOf(Integer.MAX_VALUE);
        this.langKey = langKey;
        this.setComment("");
    }

    public Property(String name, String[] values, Property.Type type) {
        this(name, values, type, false);
    }

    Property(String name, String[] values, Property.Type type, boolean read) {
        this(name, values, type, read, new String[0], name);
    }

    public Property(String name, String[] values, Property.Type type, String langKey) {
        this(name, values, type, false, langKey);
    }

    Property(String name, String[] values, Property.Type type, boolean read, String langKey) {
        this(name, values, type, read, new String[0], langKey);
    }

    Property(String name, String[] values, Property.Type type, boolean read, String[] validValues, String langKey) {
        this.setName(name);
        this.type = type;
        this.values = (String[]) Arrays.copyOf(values, values.length);
        this.wasRead = read;
        this.isList = true;
        this.value = "";
        this.defaultValue = "";
        for (String s : values) {
            this.defaultValue = this.defaultValue + ", [" + s + "]";
        }
        this.defaultValue = this.defaultValue.replaceFirst(", ", "");
        this.defaultValues = (String[]) Arrays.copyOf(values, values.length);
        this.validValues = validValues;
        this.isListLengthFixed = false;
        this.maxListLength = -1;
        this.minValue = String.valueOf(Integer.MIN_VALUE);
        this.maxValue = String.valueOf(Integer.MAX_VALUE);
        this.langKey = langKey;
        this.setComment("");
    }

    public boolean isDefault() {
        if (this.isBooleanList()) {
            if (this.values.length == this.defaultValues.length) {
                for (int i = 0; i < this.values.length; i++) {
                    if (Boolean.parseBoolean(this.values[i]) != Boolean.parseBoolean(this.defaultValues[i])) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else if (this.isIntList()) {
            if (this.values.length == this.defaultValues.length) {
                for (int ix = 0; ix < this.values.length; ix++) {
                    if (Integer.parseInt(this.values[ix]) != Integer.parseInt(this.defaultValues[ix])) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else if (this.isDoubleList()) {
            if (this.values.length == this.defaultValues.length) {
                for (int ixx = 0; ixx < this.values.length; ixx++) {
                    if (Double.parseDouble(this.values[ixx]) != Double.parseDouble(this.defaultValues[ixx])) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else if (this.isList()) {
            if (this.values.length == this.defaultValues.length) {
                for (int ixxx = 0; ixxx < this.values.length; ixxx++) {
                    if (!this.values[ixxx].equals(this.defaultValues[ixxx])) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else if (this.type == Property.Type.BOOLEAN && this.isBooleanValue()) {
            return Boolean.parseBoolean(this.value) == Boolean.parseBoolean(this.defaultValue);
        } else if (this.type == Property.Type.INTEGER && this.isIntValue()) {
            return Integer.parseInt(this.value) == Integer.parseInt(this.defaultValue);
        } else {
            return this.type == Property.Type.DOUBLE && this.isDoubleValue() ? Double.parseDouble(this.value) == Double.parseDouble(this.defaultValue) : this.value.equals(this.defaultValue);
        }
    }

    public Property setToDefault() {
        this.value = this.defaultValue;
        this.values = (String[]) Arrays.copyOf(this.defaultValues, this.defaultValues.length);
        return this;
    }

    public String getDefault() {
        return this.defaultValue;
    }

    public String[] getDefaults() {
        return (String[]) Arrays.copyOf(this.defaultValues, this.defaultValues.length);
    }

    public Property setRequiresWorldRestart(boolean requiresWorldRestart) {
        this.requiresWorldRestart = requiresWorldRestart;
        return this;
    }

    public boolean requiresWorldRestart() {
        return this.requiresWorldRestart;
    }

    public Property setShowInGui(boolean showInGui) {
        this.showInGui = showInGui;
        return this;
    }

    public boolean showInGui() {
        return this.showInGui;
    }

    public Property setRequiresMcRestart(boolean requiresMcRestart) {
        this.requiresMcRestart = this.requiresWorldRestart = requiresMcRestart;
        return this;
    }

    public boolean requiresMcRestart() {
        return this.requiresMcRestart;
    }

    public Property setMaxListLength(int max) {
        this.maxListLength = max;
        if (this.maxListLength != -1) {
            if (this.values != null && this.values.length != this.maxListLength && (this.isListLengthFixed || this.values.length > this.maxListLength)) {
                this.values = (String[]) Arrays.copyOf(this.values, this.maxListLength);
            }
            if (this.defaultValues != null && this.defaultValues.length != this.maxListLength && (this.isListLengthFixed || this.defaultValues.length > this.maxListLength)) {
                this.defaultValues = (String[]) Arrays.copyOf(this.defaultValues, this.maxListLength);
            }
        }
        return this;
    }

    public int getMaxListLength() {
        return this.maxListLength;
    }

    public Property setIsListLengthFixed(boolean isListLengthFixed) {
        this.isListLengthFixed = isListLengthFixed;
        return this;
    }

    public boolean isListLengthFixed() {
        return this.isListLengthFixed;
    }

    public Property setValidationPattern(Pattern validationPattern) {
        this.validationPattern = validationPattern;
        return this;
    }

    public Pattern getValidationPattern() {
        return this.validationPattern;
    }

    public Property setLanguageKey(String langKey) {
        this.langKey = langKey;
        return this;
    }

    public String getLanguageKey() {
        return this.langKey;
    }

    public Property setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public Property setDefaultValues(String[] defaultValues) {
        this.defaultValue = "";
        for (String s : defaultValues) {
            this.defaultValue = this.defaultValue + ", [" + s + "]";
        }
        this.defaultValue = this.defaultValue.replaceFirst(", ", "");
        this.defaultValues = (String[]) Arrays.copyOf(defaultValues, defaultValues.length);
        return this;
    }

    public Property setDefaultValue(int defaultValue) {
        this.setDefaultValue(Integer.toString(defaultValue));
        return this;
    }

    public Property setDefaultValues(int[] defaultValues) {
        String[] temp = new String[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            temp[i] = Integer.toString(defaultValues[i]);
        }
        this.setDefaultValues(temp);
        return this;
    }

    public Property setDefaultValue(double defaultValue) {
        this.setDefaultValue(Double.toString(defaultValue));
        return this;
    }

    public Property setDefaultValues(double[] defaultValues) {
        String[] temp = new String[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            temp[i] = Double.toString(defaultValues[i]);
        }
        this.setDefaultValues(temp);
        return this;
    }

    public Property setDefaultValue(boolean defaultValue) {
        this.setDefaultValue(Boolean.toString(defaultValue));
        return this;
    }

    public Property setDefaultValues(boolean[] defaultValues) {
        String[] temp = new String[defaultValues.length];
        for (int i = 0; i < defaultValues.length; i++) {
            temp[i] = Boolean.toString(defaultValues[i]);
        }
        this.setDefaultValues(temp);
        return this;
    }

    public Property setMinValue(int minValue) {
        this.minValue = Integer.toString(minValue);
        return this;
    }

    public Property setMaxValue(int maxValue) {
        this.maxValue = Integer.toString(maxValue);
        return this;
    }

    public Property setMinValue(double minValue) {
        this.minValue = Double.toString(minValue);
        return this;
    }

    public Property setMaxValue(double maxValue) {
        this.maxValue = Double.toString(maxValue);
        return this;
    }

    public String getMinValue() {
        return this.minValue;
    }

    public String getMaxValue() {
        return this.maxValue;
    }

    public String getString() {
        return this.value;
    }

    public Property setValidValues(String[] validValues) {
        this.validValues = validValues;
        return this;
    }

    public String[] getValidValues() {
        return this.validValues;
    }

    public int getInt() {
        return this.getInt(Integer.parseInt(this.defaultValue));
    }

    public int getInt(int _default) {
        try {
            return Integer.parseInt(this.value);
        } catch (NumberFormatException var3) {
            return _default;
        }
    }

    public boolean isIntValue() {
        try {
            Integer.parseInt(this.value);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    public long getLong() {
        return this.getLong(Long.parseLong(this.defaultValue));
    }

    public long getLong(long _default) {
        try {
            return Long.parseLong(this.value);
        } catch (NumberFormatException var4) {
            return _default;
        }
    }

    public boolean isLongValue() {
        try {
            Long.parseLong(this.value);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    public boolean getBoolean(boolean _default) {
        return this.isBooleanValue() ? Boolean.parseBoolean(this.value) : _default;
    }

    public boolean getBoolean() {
        return this.isBooleanValue() ? Boolean.parseBoolean(this.value) : Boolean.parseBoolean(this.defaultValue);
    }

    public boolean isBooleanValue() {
        return "true".equals(this.value.toLowerCase()) || "false".equals(this.value.toLowerCase());
    }

    public boolean isDoubleValue() {
        try {
            Double.parseDouble(this.value);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    public double getDouble(double _default) {
        try {
            return Double.parseDouble(this.value);
        } catch (NumberFormatException var4) {
            return _default;
        }
    }

    public double getDouble() {
        try {
            return Double.parseDouble(this.value);
        } catch (NumberFormatException var2) {
            return Double.parseDouble(this.defaultValue);
        }
    }

    public String[] getStringList() {
        return this.values;
    }

    public int[] getIntList() {
        ArrayList<Integer> nums = new ArrayList();
        for (String value : this.values) {
            try {
                nums.add(Integer.parseInt(value));
            } catch (NumberFormatException var7) {
            }
        }
        int[] primitives = new int[nums.size()];
        for (int i = 0; i < nums.size(); i++) {
            primitives[i] = (Integer) nums.get(i);
        }
        return primitives;
    }

    public boolean isIntList() {
        if (this.isList && this.type == Property.Type.INTEGER) {
            for (String value : this.values) {
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException var6) {
                    return false;
                }
            }
        }
        return this.isList && this.type == Property.Type.INTEGER;
    }

    public boolean[] getBooleanList() {
        ArrayList<Boolean> tmp = new ArrayList();
        for (String value : this.values) {
            try {
                tmp.add(Boolean.parseBoolean(value));
            } catch (NumberFormatException var7) {
            }
        }
        boolean[] primitives = new boolean[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            primitives[i] = (Boolean) tmp.get(i);
        }
        return primitives;
    }

    public boolean isBooleanList() {
        if (this.isList && this.type == Property.Type.BOOLEAN) {
            for (String value : this.values) {
                if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
                    return false;
                }
            }
        }
        return this.isList && this.type == Property.Type.BOOLEAN;
    }

    public double[] getDoubleList() {
        ArrayList<Double> tmp = new ArrayList();
        for (String value : this.values) {
            try {
                tmp.add(Double.parseDouble(value));
            } catch (NumberFormatException var7) {
            }
        }
        double[] primitives = new double[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            primitives[i] = (Double) tmp.get(i);
        }
        return primitives;
    }

    public boolean isDoubleList() {
        if (this.isList && this.type == Property.Type.DOUBLE) {
            for (String value : this.values) {
                try {
                    Double.parseDouble(value);
                } catch (NumberFormatException var6) {
                    return false;
                }
            }
        }
        return this.isList && this.type == Property.Type.DOUBLE;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean wasRead() {
        return this.wasRead;
    }

    public Property.Type getType() {
        return this.type;
    }

    public boolean isList() {
        return this.isList;
    }

    public boolean hasChanged() {
        return this.changed;
    }

    void resetChangedState() {
        this.changed = false;
    }

    public Property setValue(String value) {
        this.value = value;
        this.changed = true;
        return this;
    }

    public void set(String value) {
        this.setValue(value);
    }

    public Property setValues(String[] values) {
        this.values = (String[]) Arrays.copyOf(values, values.length);
        this.changed = true;
        return this;
    }

    public void set(String[] values) {
        this.setValues(values);
    }

    public Property setValue(int value) {
        this.setValue(Integer.toString(value));
        return this;
    }

    public Property setValue(boolean value) {
        this.setValue(Boolean.toString(value));
        return this;
    }

    public Property setValue(double value) {
        this.setValue(Double.toString(value));
        return this;
    }

    public Property setValues(boolean[] values) {
        this.values = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            this.values[i] = String.valueOf(values[i]);
        }
        this.changed = true;
        return this;
    }

    public void set(boolean[] values) {
        this.setValues(values);
    }

    public Property setValues(int[] values) {
        this.values = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            this.values[i] = String.valueOf(values[i]);
        }
        this.changed = true;
        return this;
    }

    public void set(int[] values) {
        this.setValues(values);
    }

    public Property setValues(double[] values) {
        this.values = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            this.values[i] = String.valueOf(values[i]);
        }
        this.changed = true;
        return this;
    }

    public void set(double[] values) {
        this.setValues(values);
    }

    public void set(int value) {
        this.set(Integer.toString(value));
    }

    public void set(long value) {
        this.set(Long.toString(value));
    }

    public void set(boolean value) {
        this.set(Boolean.toString(value));
    }

    public void set(double value) {
        this.set(Double.toString(value));
    }

    public static enum Type {

        STRING,
        INTEGER,
        BOOLEAN,
        DOUBLE,
        COLOR,
        MOD_ID;

        public static Property.Type tryParse(char id) {
            for (int x = 0; x < values().length; x++) {
                if (values()[x].getID() == id) {
                    return values()[x];
                }
            }
            return STRING;
        }

        public char getID() {
            return this.name().charAt(0);
        }
    }
}