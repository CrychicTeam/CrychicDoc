package me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

class ObjectValueWriter implements ValueWriter {

    static final ValueWriter OBJECT_VALUE_WRITER = new ObjectValueWriter();

    @Override
    public boolean canWrite(Object value) {
        return true;
    }

    @Override
    public void write(Object value, WriterContext context) {
        Map<String, Object> to = new LinkedHashMap();
        for (Field field : getFields(value.getClass())) {
            to.put(field.getName(), getFieldValue(field, value));
        }
        MapValueWriter.MAP_VALUE_WRITER.write(to, context);
    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }

    private static Set<Field> getFields(Class<?> cls) {
        Set<Field> fields;
        for (fields = new LinkedHashSet(Arrays.asList(cls.getDeclaredFields())); cls != Object.class; cls = cls.getSuperclass()) {
            fields.addAll(Arrays.asList(cls.getDeclaredFields()));
        }
        removeConstantsAndSyntheticFields(fields);
        return fields;
    }

    private static void removeConstantsAndSyntheticFields(Set<Field> fields) {
        Iterator<Field> iterator = fields.iterator();
        while (iterator.hasNext()) {
            Field field = (Field) iterator.next();
            if (Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) || field.isSynthetic() || Modifier.isTransient(field.getModifiers())) {
                iterator.remove();
            }
        }
    }

    private static Object getFieldValue(Field field, Object o) {
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(o);
        } catch (IllegalAccessException var5) {
        }
        field.setAccessible(isAccessible);
        return value;
    }

    private ObjectValueWriter() {
    }
}