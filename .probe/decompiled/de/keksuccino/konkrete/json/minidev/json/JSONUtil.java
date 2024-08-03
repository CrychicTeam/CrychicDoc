package de.keksuccino.konkrete.json.minidev.json;

import de.keksuccino.konkrete.json.minidev.asm.FieldFilter;
import de.keksuccino.konkrete.json.minidev.json.annotate.JsonIgnore;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JSONUtil {

    public static final JSONUtil.JsonSmartFieldFilter JSON_SMART_FIELD_FILTER = new JSONUtil.JsonSmartFieldFilter();

    public static Object convertToStrict(Object obj, Class<?> dest) {
        if (obj == null) {
            return null;
        } else if (dest.isAssignableFrom(obj.getClass())) {
            return obj;
        } else if (dest.isPrimitive()) {
            if (dest == int.class) {
                return obj instanceof Number ? ((Number) obj).intValue() : Integer.valueOf(obj.toString());
            } else if (dest == short.class) {
                return obj instanceof Number ? ((Number) obj).shortValue() : Short.valueOf(obj.toString());
            } else if (dest == long.class) {
                return obj instanceof Number ? ((Number) obj).longValue() : Long.valueOf(obj.toString());
            } else if (dest == byte.class) {
                return obj instanceof Number ? ((Number) obj).byteValue() : Byte.valueOf(obj.toString());
            } else if (dest == float.class) {
                return obj instanceof Number ? ((Number) obj).floatValue() : Float.valueOf(obj.toString());
            } else if (dest == double.class) {
                return obj instanceof Number ? ((Number) obj).doubleValue() : Double.valueOf(obj.toString());
            } else {
                if (dest == char.class) {
                    String asString = dest.toString();
                    if (asString.length() > 0) {
                        return asString.charAt(0);
                    }
                } else if (dest == boolean.class) {
                    return (Boolean) obj;
                }
                throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to " + dest.getName());
            }
        } else if (dest.isEnum()) {
            return Enum.valueOf(dest, obj.toString());
        } else if (dest == Integer.class) {
            return obj instanceof Number ? ((Number) obj).intValue() : Integer.valueOf(obj.toString());
        } else if (dest == Long.class) {
            return obj instanceof Number ? ((Number) obj).longValue() : Long.valueOf(obj.toString());
        } else if (dest == Short.class) {
            return obj instanceof Number ? ((Number) obj).shortValue() : Short.valueOf(obj.toString());
        } else if (dest == Byte.class) {
            return obj instanceof Number ? ((Number) obj).byteValue() : Byte.valueOf(obj.toString());
        } else if (dest == Float.class) {
            return obj instanceof Number ? ((Number) obj).floatValue() : Float.valueOf(obj.toString());
        } else if (dest == Double.class) {
            return obj instanceof Number ? ((Number) obj).doubleValue() : Double.valueOf(obj.toString());
        } else {
            if (dest == Character.class) {
                String asString = dest.toString();
                if (asString.length() > 0) {
                    return asString.charAt(0);
                }
            }
            throw new RuntimeException("Object: Can not Convert " + obj.getClass().getName() + " to " + dest.getName());
        }
    }

    public static Object convertToX(Object obj, Class<?> dest) {
        if (obj == null) {
            return null;
        } else if (dest.isAssignableFrom(obj.getClass())) {
            return obj;
        } else if (dest.isPrimitive()) {
            if (obj instanceof Number) {
                return obj;
            } else if (dest == int.class) {
                return Integer.valueOf(obj.toString());
            } else if (dest == short.class) {
                return Short.valueOf(obj.toString());
            } else if (dest == long.class) {
                return Long.valueOf(obj.toString());
            } else if (dest == byte.class) {
                return Byte.valueOf(obj.toString());
            } else if (dest == float.class) {
                return Float.valueOf(obj.toString());
            } else if (dest == double.class) {
                return Double.valueOf(obj.toString());
            } else {
                if (dest == char.class) {
                    String asString = dest.toString();
                    if (asString.length() > 0) {
                        return asString.charAt(0);
                    }
                } else if (dest == boolean.class) {
                    return (Boolean) obj;
                }
                throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to " + dest.getName());
            }
        } else if (dest.isEnum()) {
            return Enum.valueOf(dest, obj.toString());
        } else if (dest == Integer.class) {
            return obj instanceof Number ? ((Number) obj).intValue() : Integer.valueOf(obj.toString());
        } else if (dest == Long.class) {
            return obj instanceof Number ? ((Number) obj).longValue() : Long.valueOf(obj.toString());
        } else if (dest == Short.class) {
            return obj instanceof Number ? ((Number) obj).shortValue() : Short.valueOf(obj.toString());
        } else if (dest == Byte.class) {
            return obj instanceof Number ? ((Number) obj).byteValue() : Byte.valueOf(obj.toString());
        } else if (dest == Float.class) {
            return obj instanceof Number ? ((Number) obj).floatValue() : Float.valueOf(obj.toString());
        } else if (dest == Double.class) {
            return obj instanceof Number ? ((Number) obj).doubleValue() : Double.valueOf(obj.toString());
        } else {
            if (dest == Character.class) {
                String asString = dest.toString();
                if (asString.length() > 0) {
                    return asString.charAt(0);
                }
            }
            throw new RuntimeException("Object: Can not Convert " + obj.getClass().getName() + " to " + dest.getName());
        }
    }

    public static String getSetterName(String key) {
        int len = key.length();
        char[] b = new char[len + 3];
        b[0] = 's';
        b[1] = 'e';
        b[2] = 't';
        char c = key.charAt(0);
        if (c >= 'a' && c <= 'z') {
            c = (char) (c - ' ');
        }
        b[3] = c;
        for (int i = 1; i < len; i++) {
            b[i + 3] = key.charAt(i);
        }
        return new String(b);
    }

    public static String getGetterName(String key) {
        int len = key.length();
        char[] b = new char[len + 3];
        b[0] = 'g';
        b[1] = 'e';
        b[2] = 't';
        char c = key.charAt(0);
        if (c >= 'a' && c <= 'z') {
            c = (char) (c - ' ');
        }
        b[3] = c;
        for (int i = 1; i < len; i++) {
            b[i + 3] = key.charAt(i);
        }
        return new String(b);
    }

    public static String getIsName(String key) {
        int len = key.length();
        char[] b = new char[len + 2];
        b[0] = 'i';
        b[1] = 's';
        char c = key.charAt(0);
        if (c >= 'a' && c <= 'z') {
            c = (char) (c - ' ');
        }
        b[2] = c;
        for (int i = 1; i < len; i++) {
            b[i + 2] = key.charAt(i);
        }
        return new String(b);
    }

    public static class JsonSmartFieldFilter implements FieldFilter {

        @Override
        public boolean canUse(Field field) {
            JsonIgnore ignore = (JsonIgnore) field.getAnnotation(JsonIgnore.class);
            return ignore == null || !ignore.value();
        }

        @Override
        public boolean canUse(Field field, Method method) {
            JsonIgnore ignore = (JsonIgnore) method.getAnnotation(JsonIgnore.class);
            return ignore == null || !ignore.value();
        }

        @Override
        public boolean canRead(Field field) {
            return true;
        }

        @Override
        public boolean canWrite(Field field) {
            return true;
        }
    }
}