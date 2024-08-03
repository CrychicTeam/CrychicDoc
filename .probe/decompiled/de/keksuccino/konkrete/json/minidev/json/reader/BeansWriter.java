package de.keksuccino.konkrete.json.minidev.json.reader;

import de.keksuccino.konkrete.json.minidev.json.JSONStyle;
import de.keksuccino.konkrete.json.minidev.json.JSONUtil;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BeansWriter implements JsonWriterI<Object> {

    @Override
    public <E> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
        try {
            Class<?> nextClass = value.getClass();
            boolean needSep = false;
            compression.objectStart(out);
            while (nextClass != Object.class) {
                Field[] fields = nextClass.getDeclaredFields();
                for (Field field : fields) {
                    int m = field.getModifiers();
                    if ((m & 152) <= 0) {
                        Object v = null;
                        if ((m & 1) > 0) {
                            v = field.get(value);
                        } else {
                            String g = JSONUtil.getGetterName(field.getName());
                            Method mtd = null;
                            try {
                                mtd = nextClass.getDeclaredMethod(g);
                            } catch (Exception var16) {
                            }
                            if (mtd == null) {
                                Class<?> c2 = field.getType();
                                if (c2 == boolean.class || c2 == Boolean.class) {
                                    g = JSONUtil.getIsName(field.getName());
                                    mtd = nextClass.getDeclaredMethod(g);
                                }
                            }
                            if (mtd == null) {
                                continue;
                            }
                            v = mtd.invoke(value);
                        }
                        if (v != null || !compression.ignoreNull()) {
                            if (needSep) {
                                compression.objectNext(out);
                            } else {
                                needSep = true;
                            }
                            String key = field.getName();
                            JsonWriter.writeJSONKV(key, v, out, compression);
                        }
                    }
                }
                nextClass = nextClass.getSuperclass();
            }
            compression.objectStop(out);
        } catch (Exception var17) {
            throw new RuntimeException(var17);
        }
    }
}