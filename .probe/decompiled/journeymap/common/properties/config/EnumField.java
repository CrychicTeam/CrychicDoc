package journeymap.common.properties.config;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import journeymap.common.Journeymap;
import journeymap.common.properties.catagory.Category;

public class EnumField<E extends Enum> extends ConfigField<E> {

    public static final String ATTR_ENUM_TYPE = "enumType";

    private static final Map<String, Class<? extends Enum>> classCache = new HashMap();

    protected EnumField() {
    }

    public EnumField(Category category, String key, E defaultValue) {
        super(category, key);
        this.put("enumType", defaultValue.getClass().getName());
        this.defaultValue(defaultValue);
        this.setToDefault();
    }

    public EnumField(Category category, String key, E defaultValue, int sortOrder) {
        super(category, key);
        this.sortOrder(sortOrder);
        this.put("enumType", defaultValue.getClass().getName());
        this.defaultValue(defaultValue);
        this.setToDefault();
    }

    public E getDefaultValue() {
        return this.getEnumAttr("default", this.getEnumClass());
    }

    public EnumField<E> set(E value) {
        this.put("value", value.name());
        return this;
    }

    public E get() {
        return this.getEnumAttr("value", this.getEnumClass());
    }

    public Class<E> getEnumClass() {
        Object value = this.get("enumType");
        if (value instanceof Class) {
            return (Class<E>) value;
        } else {
            if (value instanceof String) {
                try {
                    if (classCache.containsKey(value)) {
                        return (Class<E>) classCache.get(value);
                    }
                    Class clazz = Class.forName(value.toString());
                    this.attributes.put("enumType", value);
                    classCache.put(value.toString(), clazz);
                    return clazz;
                } catch (Exception var3) {
                    Journeymap.getLogger().warn(String.format("Couldn't get Enum Class %s : %s", "enumType", var3.getMessage()));
                }
            }
            return null;
        }
    }

    public Set<E> getValidValues() {
        Class<? extends Enum> enumClass = this.getEnumClass();
        return EnumSet.allOf(enumClass);
    }

    public EnumField<E> setParent(String fieldName, Object value) {
        return (EnumField<E>) super.setParent(fieldName, value);
    }

    @Override
    public boolean validate(boolean fix) {
        return this.require(new String[] { "enumType" }) && super.validate(fix);
    }
}