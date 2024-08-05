package info.journeymap.shaded.org.eclipse.jetty.util;

import info.journeymap.shaded.org.eclipse.jetty.util.annotation.Name;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.resource.Resource;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TypeUtil {

    private static final Logger LOG = Log.getLogger(TypeUtil.class);

    public static final Class<?>[] NO_ARGS = new Class[0];

    public static final int CR = 13;

    public static final int LF = 10;

    private static final HashMap<String, Class<?>> name2Class = new HashMap();

    private static final HashMap<Class<?>, String> class2Name = new HashMap();

    private static final HashMap<Class<?>, Method> class2Value = new HashMap();

    public static <T> List<T> asList(T[] a) {
        return a == null ? Collections.emptyList() : Arrays.asList(a);
    }

    public static Class<?> fromName(String name) {
        return (Class<?>) name2Class.get(name);
    }

    public static String toName(Class<?> type) {
        return (String) class2Name.get(type);
    }

    public static Object valueOf(Class<?> type, String value) {
        try {
            if (type.equals(String.class)) {
                return value;
            }
            Method m = (Method) class2Value.get(type);
            if (m != null) {
                return m.invoke(null, value);
            }
            if (!type.equals(char.class) && !type.equals(Character.class)) {
                Constructor<?> c = type.getConstructor(String.class);
                return c.newInstance(value);
            }
            return value.charAt(0);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException var4) {
            LOG.ignore(var4);
        } catch (InvocationTargetException var5) {
            if (var5.getTargetException() instanceof Error) {
                throw (Error) var5.getTargetException();
            }
            LOG.ignore(var5);
        }
        return null;
    }

    public static Object valueOf(String type, String value) {
        return valueOf(fromName(type), value);
    }

    public static int parseInt(String s, int offset, int length, int base) throws NumberFormatException {
        int value = 0;
        if (length < 0) {
            length = s.length() - offset;
        }
        for (int i = 0; i < length; i++) {
            char c = s.charAt(offset + i);
            int digit = convertHexDigit((int) c);
            if (digit < 0 || digit >= base) {
                throw new NumberFormatException(s.substring(offset, offset + length));
            }
            value = value * base + digit;
        }
        return value;
    }

    public static int parseInt(byte[] b, int offset, int length, int base) throws NumberFormatException {
        int value = 0;
        if (length < 0) {
            length = b.length - offset;
        }
        for (int i = 0; i < length; i++) {
            char c = (char) (255 & b[offset + i]);
            int digit = c - '0';
            if (digit < 0 || digit >= base || digit >= 10) {
                digit = '\n' + c - 65;
                if (digit < 10 || digit >= base) {
                    digit = '\n' + c - 97;
                }
            }
            if (digit < 0 || digit >= base) {
                throw new NumberFormatException(new String(b, offset, length));
            }
            value = value * base + digit;
        }
        return value;
    }

    public static byte[] parseBytes(String s, int base) {
        byte[] bytes = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2) {
            bytes[i / 2] = (byte) parseInt(s, i, 2, base);
        }
        return bytes;
    }

    public static String toString(byte[] bytes, int base) {
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            int bi = 255 & b;
            int c = 48 + bi / base % base;
            if (c > 57) {
                c = 97 + (c - 48 - 10);
            }
            buf.append((char) c);
            c = 48 + bi % base;
            if (c > 57) {
                c = 97 + (c - 48 - 10);
            }
            buf.append((char) c);
        }
        return buf.toString();
    }

    public static byte convertHexDigit(byte c) {
        byte b = (byte) ((c & 31) + (c >> 6) * 25 - 16);
        if (b >= 0 && b <= 15) {
            return b;
        } else {
            throw new NumberFormatException("!hex " + c);
        }
    }

    public static int convertHexDigit(char c) {
        int d = (c & 31) + (c >> 6) * 25 - 16;
        if (d >= 0 && d <= 15) {
            return d;
        } else {
            throw new NumberFormatException("!hex " + c);
        }
    }

    public static int convertHexDigit(int c) {
        int d = (c & 31) + (c >> 6) * 25 - 16;
        if (d >= 0 && d <= 15) {
            return d;
        } else {
            throw new NumberFormatException("!hex " + c);
        }
    }

    public static void toHex(byte b, Appendable buf) {
        try {
            int d = 15 & (240 & b) >> 4;
            buf.append((char) ((d > 9 ? 55 : 48) + d));
            d = 15 & b;
            buf.append((char) ((d > 9 ? 55 : 48) + d));
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static void toHex(int value, Appendable buf) throws IOException {
        int d = 15 & (-268435456 & value) >> 28;
        buf.append((char) ((d > 9 ? 55 : 48) + d));
        d = 15 & (251658240 & value) >> 24;
        buf.append((char) ((d > 9 ? 55 : 48) + d));
        d = 15 & (15728640 & value) >> 20;
        buf.append((char) ((d > 9 ? 55 : 48) + d));
        d = 15 & (983040 & value) >> 16;
        buf.append((char) ((d > 9 ? 55 : 48) + d));
        d = 15 & (61440 & value) >> 12;
        buf.append((char) ((d > 9 ? 55 : 48) + d));
        d = 15 & (3840 & value) >> 8;
        buf.append((char) ((d > 9 ? 55 : 48) + d));
        d = 15 & (240 & value) >> 4;
        buf.append((char) ((d > 9 ? 55 : 48) + d));
        d = 15 & value;
        buf.append((char) ((d > 9 ? 55 : 48) + d));
        Integer.toString(0, 36);
    }

    public static void toHex(long value, Appendable buf) throws IOException {
        toHex((int) (value >> 32), buf);
        toHex((int) value, buf);
    }

    public static String toHexString(byte b) {
        return toHexString(new byte[] { b }, 0, 1);
    }

    public static String toHexString(byte[] b) {
        return toHexString(b, 0, b.length);
    }

    public static String toHexString(byte[] b, int offset, int length) {
        StringBuilder buf = new StringBuilder();
        for (int i = offset; i < offset + length; i++) {
            int bi = 255 & b[i];
            int c = 48 + bi / 16 % 16;
            if (c > 57) {
                c = 65 + (c - 48 - 10);
            }
            buf.append((char) c);
            c = 48 + bi % 16;
            if (c > 57) {
                c = 97 + (c - 48 - 10);
            }
            buf.append((char) c);
        }
        return buf.toString();
    }

    public static byte[] fromHexString(String s) {
        if (s.length() % 2 != 0) {
            throw new IllegalArgumentException(s);
        } else {
            byte[] array = new byte[s.length() / 2];
            for (int i = 0; i < array.length; i++) {
                int b = Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
                array[i] = (byte) (0xFF & b);
            }
            return array;
        }
    }

    public static void dump(Class<?> c) {
        System.err.println("Dump: " + c);
        dump(c.getClassLoader());
    }

    public static void dump(ClassLoader cl) {
        System.err.println("Dump Loaders:");
        while (cl != null) {
            System.err.println("  loader " + cl);
            cl = cl.getParent();
        }
    }

    public static Object call(Class<?> oClass, String methodName, Object obj, Object[] arg) throws InvocationTargetException, NoSuchMethodException {
        Objects.requireNonNull(oClass, "Class cannot be null");
        Objects.requireNonNull(methodName, "Method name cannot be null");
        if (StringUtil.isBlank(methodName)) {
            throw new IllegalArgumentException("Method name cannot be blank");
        } else {
            for (Method method : oClass.getMethods()) {
                if (method.getName().equals(methodName) && method.getParameterCount() == arg.length && Modifier.isStatic(method.getModifiers()) == (obj == null) && (obj != null || method.getDeclaringClass() == oClass)) {
                    try {
                        return method.invoke(obj, arg);
                    } catch (IllegalArgumentException | IllegalAccessException var11) {
                        LOG.ignore(var11);
                    }
                }
            }
            Object[] args_with_opts = null;
            for (Method methodx : oClass.getMethods()) {
                if (methodx.getName().equals(methodName) && methodx.getParameterCount() == arg.length + 1 && methodx.getParameterTypes()[arg.length].isArray() && Modifier.isStatic(methodx.getModifiers()) == (obj == null) && (obj != null || methodx.getDeclaringClass() == oClass)) {
                    if (args_with_opts == null) {
                        args_with_opts = ArrayUtil.addToArray(arg, new Object[0], Object.class);
                    }
                    try {
                        return methodx.invoke(obj, args_with_opts);
                    } catch (IllegalArgumentException | IllegalAccessException var10) {
                        LOG.ignore(var10);
                    }
                }
            }
            throw new NoSuchMethodException(methodName);
        }
    }

    public static Object construct(Class<?> klass, Object[] arguments) throws InvocationTargetException, NoSuchMethodException {
        Objects.requireNonNull(klass, "Class cannot be null");
        for (Constructor<?> constructor : klass.getConstructors()) {
            if (arguments == null ? constructor.getParameterCount() == 0 : constructor.getParameterCount() == arguments.length) {
                try {
                    return constructor.newInstance(arguments);
                } catch (IllegalAccessException | IllegalArgumentException | InstantiationException var7) {
                    LOG.ignore(var7);
                }
            }
        }
        throw new NoSuchMethodException("<init>");
    }

    public static Object construct(Class<?> klass, Object[] arguments, Map<String, Object> namedArgMap) throws InvocationTargetException, NoSuchMethodException {
        Objects.requireNonNull(klass, "Class cannot be null");
        Objects.requireNonNull(namedArgMap, "Named Argument Map cannot be null");
        for (Constructor<?> constructor : klass.getConstructors()) {
            if (arguments == null ? constructor.getParameterCount() == 0 : constructor.getParameterCount() == arguments.length) {
                try {
                    Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
                    if (arguments != null && arguments.length != 0) {
                        if (parameterAnnotations != null && parameterAnnotations.length != 0) {
                            Object[] swizzled = new Object[arguments.length];
                            int count = 0;
                            for (Annotation[] annotations : parameterAnnotations) {
                                for (Annotation annotation : annotations) {
                                    if (annotation instanceof Name) {
                                        Name param = (Name) annotation;
                                        if (namedArgMap.containsKey(param.value())) {
                                            if (LOG.isDebugEnabled()) {
                                                LOG.debug("placing named {} in position {}", param.value(), count);
                                            }
                                            swizzled[count] = namedArgMap.get(param.value());
                                        } else {
                                            if (LOG.isDebugEnabled()) {
                                                LOG.debug("placing {} in position {}", arguments[count], count);
                                            }
                                            swizzled[count] = arguments[count];
                                        }
                                        count++;
                                    } else if (LOG.isDebugEnabled()) {
                                        LOG.debug("passing on annotation {}", annotation);
                                    }
                                }
                            }
                            return constructor.newInstance(swizzled);
                        }
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Constructor has no parameter annotations");
                        }
                        return constructor.newInstance(arguments);
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Constructor has no arguments");
                    }
                    return constructor.newInstance(arguments);
                } catch (IllegalAccessException | IllegalArgumentException | InstantiationException var19) {
                    LOG.ignore(var19);
                }
            }
        }
        throw new NoSuchMethodException("<init>");
    }

    public static boolean isTrue(Object o) {
        if (o == null) {
            return false;
        } else {
            return o instanceof Boolean ? (Boolean) o : Boolean.parseBoolean(o.toString());
        }
    }

    public static boolean isFalse(Object o) {
        if (o == null) {
            return false;
        } else {
            return o instanceof Boolean ? !(Boolean) o : "false".equalsIgnoreCase(o.toString());
        }
    }

    public static Resource getLoadedFrom(Class<?> clazz) {
        ProtectionDomain domain = clazz.getProtectionDomain();
        if (domain != null) {
            CodeSource source = domain.getCodeSource();
            if (source != null) {
                URL location = source.getLocation();
                if (location != null) {
                    return Resource.newResource(location);
                }
            }
        }
        String rname = clazz.getName().replace('.', '/') + ".class";
        ClassLoader loader = clazz.getClassLoader();
        URL url = (loader == null ? ClassLoader.getSystemClassLoader() : loader).getResource(rname);
        if (url != null) {
            try {
                return Resource.newResource(URIUtil.getJarSource(url.toURI()));
            } catch (Exception var6) {
                LOG.debug(var6);
            }
        }
        return null;
    }

    static {
        name2Class.put("boolean", boolean.class);
        name2Class.put("byte", byte.class);
        name2Class.put("char", char.class);
        name2Class.put("double", double.class);
        name2Class.put("float", float.class);
        name2Class.put("int", int.class);
        name2Class.put("long", long.class);
        name2Class.put("short", short.class);
        name2Class.put("void", void.class);
        name2Class.put("java.lang.Boolean.TYPE", boolean.class);
        name2Class.put("java.lang.Byte.TYPE", byte.class);
        name2Class.put("java.lang.Character.TYPE", char.class);
        name2Class.put("java.lang.Double.TYPE", double.class);
        name2Class.put("java.lang.Float.TYPE", float.class);
        name2Class.put("java.lang.Integer.TYPE", int.class);
        name2Class.put("java.lang.Long.TYPE", long.class);
        name2Class.put("java.lang.Short.TYPE", short.class);
        name2Class.put("java.lang.Void.TYPE", void.class);
        name2Class.put("java.lang.Boolean", Boolean.class);
        name2Class.put("java.lang.Byte", Byte.class);
        name2Class.put("java.lang.Character", Character.class);
        name2Class.put("java.lang.Double", Double.class);
        name2Class.put("java.lang.Float", Float.class);
        name2Class.put("java.lang.Integer", Integer.class);
        name2Class.put("java.lang.Long", Long.class);
        name2Class.put("java.lang.Short", Short.class);
        name2Class.put("Boolean", Boolean.class);
        name2Class.put("Byte", Byte.class);
        name2Class.put("Character", Character.class);
        name2Class.put("Double", Double.class);
        name2Class.put("Float", Float.class);
        name2Class.put("Integer", Integer.class);
        name2Class.put("Long", Long.class);
        name2Class.put("Short", Short.class);
        name2Class.put(null, void.class);
        name2Class.put("string", String.class);
        name2Class.put("String", String.class);
        name2Class.put("java.lang.String", String.class);
        class2Name.put(boolean.class, "boolean");
        class2Name.put(byte.class, "byte");
        class2Name.put(char.class, "char");
        class2Name.put(double.class, "double");
        class2Name.put(float.class, "float");
        class2Name.put(int.class, "int");
        class2Name.put(long.class, "long");
        class2Name.put(short.class, "short");
        class2Name.put(void.class, "void");
        class2Name.put(Boolean.class, "java.lang.Boolean");
        class2Name.put(Byte.class, "java.lang.Byte");
        class2Name.put(Character.class, "java.lang.Character");
        class2Name.put(Double.class, "java.lang.Double");
        class2Name.put(Float.class, "java.lang.Float");
        class2Name.put(Integer.class, "java.lang.Integer");
        class2Name.put(Long.class, "java.lang.Long");
        class2Name.put(Short.class, "java.lang.Short");
        class2Name.put(null, "void");
        class2Name.put(String.class, "java.lang.String");
        try {
            Class<?>[] s = new Class[] { String.class };
            class2Value.put(boolean.class, Boolean.class.getMethod("valueOf", s));
            class2Value.put(byte.class, Byte.class.getMethod("valueOf", s));
            class2Value.put(double.class, Double.class.getMethod("valueOf", s));
            class2Value.put(float.class, Float.class.getMethod("valueOf", s));
            class2Value.put(int.class, Integer.class.getMethod("valueOf", s));
            class2Value.put(long.class, Long.class.getMethod("valueOf", s));
            class2Value.put(short.class, Short.class.getMethod("valueOf", s));
            class2Value.put(Boolean.class, Boolean.class.getMethod("valueOf", s));
            class2Value.put(Byte.class, Byte.class.getMethod("valueOf", s));
            class2Value.put(Double.class, Double.class.getMethod("valueOf", s));
            class2Value.put(Float.class, Float.class.getMethod("valueOf", s));
            class2Value.put(Integer.class, Integer.class.getMethod("valueOf", s));
            class2Value.put(Long.class, Long.class.getMethod("valueOf", s));
            class2Value.put(Short.class, Short.class.getMethod("valueOf", s));
        } catch (Exception var1) {
            throw new Error(var1);
        }
    }
}