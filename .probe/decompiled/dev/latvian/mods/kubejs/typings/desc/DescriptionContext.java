package dev.latvian.mods.kubejs.typings.desc;

import java.util.Collection;
import java.util.Map;

public interface DescriptionContext {

    DescriptionContext DEFAULT = new DescriptionContext() {
    };

    DescriptionContext DISPLAY = new DescriptionContext() {

        @Override
        public String typeName(Class<?> type) {
            String n = type.getName();
            int i = n.lastIndexOf(46);
            return i != -1 ? n.substring(i + 1) : n;
        }
    };

    default TypeDescJS javaType(Class<?> type) {
        if (type == null) {
            return TypeDescJS.NULL;
        } else if (type == Object.class) {
            return TypeDescJS.ANY;
        } else if (Number.class.isAssignableFrom(type) || type == byte.class || type == short.class || type == int.class || type == long.class || type == float.class || type == double.class) {
            return TypeDescJS.NUMBER;
        } else if (CharSequence.class.isAssignableFrom(type) || type == Character.class || type == char.class) {
            return TypeDescJS.STRING;
        } else if (type == Boolean.class || type == boolean.class) {
            return TypeDescJS.BOOLEAN;
        } else if (Map.class.isAssignableFrom(type)) {
            return TypeDescJS.ANY.asMap(TypeDescJS.ANY);
        } else {
            return (TypeDescJS) (!type.isArray() && !Collection.class.isAssignableFrom(type) ? new PrimitiveDescJS(this.typeName(type)) : TypeDescJS.ANY.asArray());
        }
    }

    default String typeName(Class<?> type) {
        return type.getName();
    }
}