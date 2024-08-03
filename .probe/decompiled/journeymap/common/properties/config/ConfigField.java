package journeymap.common.properties.config;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import journeymap.client.api.option.Config;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.properties.PropertiesBase;
import journeymap.common.properties.catagory.Category;

public abstract class ConfigField<T> implements Config<T> {

    public static final String ATTR_TYPE = "type";

    public static final String ATTR_CATEGORY = "category";

    public static final String ATTR_KEY = "key";

    public static final String ATTR_LABEL = "label";

    public static final String ATTR_TOOLTIP = "tooltip";

    public static final String ATTR_ORDER = "order";

    public static final String ATTR_VALUE = "value";

    public static final String ATTR_DEFAULT = "default";

    public static final String ATTR_VALID_VALUES = "validValues";

    public static final String ATTR_PARENT = "parent";

    public static final String ATTR_PARENT_VALUE = "parentValue";

    protected final transient Map<String, Object> attributes = new TreeMap();

    protected transient PropertiesBase owner;

    protected transient String fieldName;

    public ConfigField() {
        this.put("type", this.getClass().getSimpleName());
    }

    protected ConfigField(Category category) {
        this.put("type", this.getClass().getSimpleName());
        this.put("category", category);
    }

    protected ConfigField(Category category, String key) {
        this.put("type", this.getClass().getSimpleName());
        this.put("category", category);
        this.put("key", key);
    }

    public String getStringAttr(String attrName) {
        Object value = this.attributes.get(attrName);
        if (value == null) {
            return null;
        } else if (value instanceof Enum) {
            return ((Enum) value).name();
        } else {
            return value instanceof Class ? ((Class) value).getCanonicalName() : value.toString();
        }
    }

    public ConfigField<T> put(String attrName, Object value) {
        this.attributes.put(attrName, value);
        return this;
    }

    public abstract T getDefaultValue();

    @Override
    public abstract T get();

    public ConfigField<T> set(T value) {
        this.put("value", value);
        return this;
    }

    public boolean validate(boolean fix) {
        boolean hasRequired = this.require("type", "value", "default");
        boolean hasCategory = this.getCategory() != null;
        return hasRequired && hasCategory;
    }

    public ConfigField<T> sortOrder(int order) {
        this.put("order", order);
        return this;
    }

    public String getKey() {
        return this.getStringAttr("key");
    }

    public ConfigField<T> category(Category category) {
        this.attributes.put("category", category);
        return this;
    }

    public Category getCategory() {
        Object val = this.get("category");
        if (val instanceof Category) {
            return (Category) val;
        } else if (val instanceof String && this.owner != null) {
            Category category = this.owner.getCategoryByName((String) val);
            this.category(category);
            return category;
        } else {
            return null;
        }
    }

    public String getLabel() {
        return this.getStringAttr("label");
    }

    public ConfigField<T> label(String label) {
        this.attributes.put("label", label);
        return this;
    }

    public String getTooltip() {
        return this.getStringAttr("tooltip");
    }

    public String getType() {
        return this.getStringAttr("type");
    }

    public int getSortOrder() {
        Integer order = this.getIntegerAttr("order");
        if (order == null) {
            order = 100;
        }
        return order;
    }

    public Object get(String attrName) {
        return this.attributes.get(attrName);
    }

    public Integer getIntegerAttr(String attrName) {
        Object value = this.attributes.get(attrName);
        if (value instanceof Integer) {
            return (Integer) value;
        } else {
            if (value instanceof String) {
                try {
                    value = Integer.parseInt((String) value);
                    this.attributes.put(attrName, value);
                    return (Integer) value;
                } catch (NumberFormatException var4) {
                    Journeymap.getLogger().warn(String.format("Couldn't get Integer %s from %s: %s", attrName, value, var4.getMessage()));
                }
            }
            return null;
        }
    }

    public Float getFloatAttr(String attrName) {
        Object value = this.attributes.get(attrName);
        if (value instanceof Float) {
            return (Float) value;
        } else {
            if (value instanceof String) {
                try {
                    value = Float.parseFloat((String) value);
                    this.attributes.put(attrName, value);
                    return (Float) value;
                } catch (NumberFormatException var4) {
                    Journeymap.getLogger().warn(String.format("Couldn't get Float %s from %s: %s", attrName, value, var4.getMessage()));
                }
            }
            return null;
        }
    }

    public Boolean getBooleanAttr(String attrName) {
        Object value = this.attributes.get(attrName);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            if (value instanceof String) {
                try {
                    value = Boolean.valueOf((String) value);
                    this.attributes.put(attrName, value);
                    return (Boolean) value;
                } catch (NumberFormatException var4) {
                    Journeymap.getLogger().warn(String.format("Couldn't get Boolean %s from %s: %s", attrName, value, var4.getMessage()));
                }
            }
            return null;
        }
    }

    public <E extends Enum> E getEnumAttr(String attrName, Class<E> enumType) {
        Object value = this.attributes.get(attrName);
        if (value instanceof Enum) {
            return (E) value;
        } else {
            if (value instanceof String) {
                try {
                    return (E) Enum.valueOf(enumType, value.toString());
                } catch (Exception var5) {
                    Journeymap.getLogger().warn(String.format("Couldn't get %s as Enum %s with value %s: %s", attrName, enumType, value, LogFormatter.toString(var5)));
                }
            }
            this.setToDefault();
            return (E) ((Enum) this.getDefaultValue());
        }
    }

    public ConfigField<T> setParent(String fieldName, Object value) {
        this.put("parent", fieldName);
        this.put("parentValue", value);
        return this;
    }

    public void setToDefault() {
        this.set(this.getDefaultValue());
    }

    public ConfigField<T> defaultValue(T defaultValue) {
        if (defaultValue == null) {
            Journeymap.getLogger().warn("defaultValue shouldn't be null");
        }
        this.put("default", defaultValue);
        return this;
    }

    protected boolean require(String... attrNames) {
        boolean pass = true;
        for (String attrName : attrNames) {
            Object attr = this.get(attrName);
            if (attr == null) {
                Journeymap.getLogger().warn(String.format("Missing required attribute '%s' in %s", attrName, this.getDeclaredField()));
                pass = false;
            }
        }
        return pass;
    }

    public Map<String, Object> getAttributeMap() {
        return this.attributes;
    }

    public Set<String> getAttributeNames() {
        return this.attributes.keySet();
    }

    public PropertiesBase getOwner() {
        return this.owner;
    }

    public void setOwner(String fieldName, PropertiesBase properties) {
        this.fieldName = fieldName;
        this.owner = properties;
    }

    public boolean save() {
        return this.owner != null ? this.owner.save() : false;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof ConfigField<?> that) ? false : Objects.equal(this.getKey(), that.getKey()) && this.getCategory() == that.getCategory() && Objects.equal(this.get(), that.get());
        }
    }

    public int hashCode() {
        return Objects.hashCode(new Object[] { this.getKey(), this.getCategory(), this.get() });
    }

    public String getDeclaredField() {
        return this.owner == null ? null : String.format("%s.%s", this.owner.getClass().getSimpleName(), this.fieldName);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("on", this.getDeclaredField()).add("attributes", this.attributes).toString();
    }
}