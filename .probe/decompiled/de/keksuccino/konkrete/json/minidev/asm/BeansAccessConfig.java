package de.keksuccino.konkrete.json.minidev.asm;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class BeansAccessConfig {

    protected static HashMap<Class<?>, LinkedHashSet<Class<?>>> classMapper = new HashMap();

    protected static HashMap<Class<?>, HashMap<String, String>> classFiledNameMapper = new HashMap();

    public static void addTypeMapper(Class<?> clz, Class<?> mapper) {
        synchronized (classMapper) {
            LinkedHashSet<Class<?>> h = (LinkedHashSet<Class<?>>) classMapper.get(clz);
            if (h == null) {
                h = new LinkedHashSet();
                classMapper.put(clz, h);
            }
            h.add(mapper);
        }
    }

    static {
        addTypeMapper(Object.class, DefaultConverter.class);
        addTypeMapper(Object.class, ConvertDate.class);
    }
}