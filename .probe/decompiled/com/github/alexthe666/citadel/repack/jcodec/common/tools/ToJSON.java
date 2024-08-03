package com.github.alexthe666.citadel.repack.jcodec.common.tools;

import com.github.alexthe666.citadel.repack.jcodec.common.IntArrayList;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ToJSON {

    private static final Set<Class> primitive = new HashSet();

    private static final Set<String> omitMethods = new HashSet();

    public static String toJSON(Object obj) {
        StringBuilder builder = new StringBuilder();
        IntArrayList stack = IntArrayList.createIntArrayList();
        toJSONSub(obj, stack, builder);
        return builder.toString();
    }

    private static void toJSONSub(Object obj, IntArrayList stack, StringBuilder builder) {
        if (obj == null) {
            builder.append("null");
        } else {
            String className = obj.getClass().getName();
            if (className.startsWith("java.lang") && !className.equals("java.lang.String")) {
                builder.append("null");
            } else {
                int id = System.identityHashCode(obj);
                if (stack.contains(id)) {
                    builder.append("null");
                } else {
                    stack.push(id);
                    if (obj instanceof ByteBuffer) {
                        obj = NIOUtils.toArray((ByteBuffer) obj);
                    }
                    if (obj == null) {
                        builder.append("null");
                    } else if (obj instanceof String) {
                        builder.append("\"");
                        escape((String) obj, builder);
                        builder.append("\"");
                    } else if (obj instanceof Map) {
                        Iterator it = ((Map) obj).entrySet().iterator();
                        builder.append("{");
                        while (it.hasNext()) {
                            Entry e = (Entry) it.next();
                            builder.append("\"");
                            builder.append(e.getKey());
                            builder.append("\":");
                            toJSONSub(e.getValue(), stack, builder);
                            if (it.hasNext()) {
                                builder.append(",");
                            }
                        }
                        builder.append("}");
                    } else if (obj instanceof Iterable) {
                        Iterator it = ((Iterable) obj).iterator();
                        builder.append("[");
                        while (it.hasNext()) {
                            toJSONSub(it.next(), stack, builder);
                            if (it.hasNext()) {
                                builder.append(",");
                            }
                        }
                        builder.append("]");
                    } else if (obj instanceof Object[]) {
                        builder.append("[");
                        int len = Array.getLength(obj);
                        for (int i = 0; i < len; i++) {
                            toJSONSub(Array.get(obj, i), stack, builder);
                            if (i < len - 1) {
                                builder.append(",");
                            }
                        }
                        builder.append("]");
                    } else if (obj instanceof long[]) {
                        long[] a = (long[]) obj;
                        builder.append("[");
                        for (int ix = 0; ix < a.length; ix++) {
                            builder.append(String.format("0x%016x", a[ix]));
                            if (ix < a.length - 1) {
                                builder.append(",");
                            }
                        }
                        builder.append("]");
                    } else if (obj instanceof int[]) {
                        int[] a = (int[]) obj;
                        builder.append("[");
                        for (int ixx = 0; ixx < a.length; ixx++) {
                            builder.append(String.format("0x%08x", a[ixx]));
                            if (ixx < a.length - 1) {
                                builder.append(",");
                            }
                        }
                        builder.append("]");
                    } else if (obj instanceof float[]) {
                        float[] a = (float[]) obj;
                        builder.append("[");
                        for (int ixxx = 0; ixxx < a.length; ixxx++) {
                            builder.append(String.format("%.3f", a[ixxx]));
                            if (ixxx < a.length - 1) {
                                builder.append(",");
                            }
                        }
                        builder.append("]");
                    } else if (obj instanceof double[]) {
                        double[] a = (double[]) obj;
                        builder.append("[");
                        for (int ixxxx = 0; ixxxx < a.length; ixxxx++) {
                            builder.append(String.format("%.6f", a[ixxxx]));
                            if (ixxxx < a.length - 1) {
                                builder.append(",");
                            }
                        }
                        builder.append("]");
                    } else if (obj instanceof short[]) {
                        short[] a = (short[]) obj;
                        builder.append("[");
                        for (int ixxxxx = 0; ixxxxx < a.length; ixxxxx++) {
                            builder.append(String.format("0x%04x", a[ixxxxx]));
                            if (ixxxxx < a.length - 1) {
                                builder.append(",");
                            }
                        }
                        builder.append("]");
                    } else if (obj instanceof byte[]) {
                        byte[] a = (byte[]) obj;
                        builder.append("[");
                        for (int ixxxxxx = 0; ixxxxxx < a.length; ixxxxxx++) {
                            builder.append(String.format("0x%02x", a[ixxxxxx]));
                            if (ixxxxxx < a.length - 1) {
                                builder.append(",");
                            }
                        }
                        builder.append("]");
                    } else if (obj instanceof boolean[]) {
                        boolean[] a = (boolean[]) obj;
                        builder.append("[");
                        for (int ixxxxxxx = 0; ixxxxxxx < a.length; ixxxxxxx++) {
                            builder.append(a[ixxxxxxx]);
                            if (ixxxxxxx < a.length - 1) {
                                builder.append(",");
                            }
                        }
                        builder.append("]");
                    } else if (obj.getClass().isEnum()) {
                        builder.append(String.valueOf(obj));
                    } else {
                        builder.append("{");
                        Method[] methods = obj.getClass().getMethods();
                        List<Method> filteredMethods = new ArrayList();
                        for (Method method : methods) {
                            if (!omitMethods.contains(method.getName()) && isGetter(method)) {
                                filteredMethods.add(method);
                            }
                        }
                        Iterator<Method> iterator = filteredMethods.iterator();
                        while (iterator.hasNext()) {
                            Method methodx = (Method) iterator.next();
                            String name = toName(methodx);
                            invoke(obj, stack, builder, methodx, name);
                            if (iterator.hasNext()) {
                                builder.append(",");
                            }
                        }
                        builder.append("}");
                    }
                    stack.pop();
                }
            }
        }
    }

    private static void invoke(Object obj, IntArrayList stack, StringBuilder builder, Method method, String name) {
        try {
            Object invoke = method.invoke(obj);
            builder.append('"');
            builder.append(name);
            builder.append("\":");
            if (invoke != null && primitive.contains(invoke.getClass())) {
                builder.append(invoke);
            } else {
                toJSONSub(invoke, stack, builder);
            }
        } catch (Exception var6) {
        }
    }

    private static void escape(String invoke, StringBuilder sb) {
        char[] ch = invoke.toCharArray();
        for (char c : ch) {
            if (c < ' ') {
                sb.append(String.format("\\%02x", Integer.valueOf(c)));
            } else {
                sb.append(c);
            }
        }
    }

    private static String toName(Method method) {
        if (!isGetter(method)) {
            throw new IllegalArgumentException("Not a getter");
        } else {
            char[] name = method.getName().toCharArray();
            int ind = name[0] == 'g' ? 3 : 2;
            name[ind] = Character.toLowerCase(name[ind]);
            return new String(name, ind, name.length - ind);
        }
    }

    private static boolean isGetter(Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        } else {
            return method.getName().startsWith("get") || method.getName().startsWith("is") && method.getReturnType() == boolean.class ? method.getParameterTypes().length == 0 : false;
        }
    }

    static {
        primitive.add(Boolean.class);
        primitive.add(Byte.class);
        primitive.add(Short.class);
        primitive.add(Integer.class);
        primitive.add(Long.class);
        primitive.add(Float.class);
        primitive.add(Double.class);
        primitive.add(Character.class);
        omitMethods.add("getClass");
        omitMethods.add("get");
    }
}