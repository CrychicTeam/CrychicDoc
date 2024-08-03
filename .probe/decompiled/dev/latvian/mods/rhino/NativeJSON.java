package dev.latvian.mods.rhino;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import dev.latvian.mods.rhino.json.JsonParser;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.Remapper;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

public final class NativeJSON extends IdScriptableObject {

    private static final Object JSON_TAG = "JSON";

    private static final int MAX_STRINGIFY_GAP_LENGTH = 10;

    private static final HashSet<String> IGNORED_METHODS = new HashSet();

    private static final int Id_toSource = 1;

    private static final int Id_parse = 2;

    private static final int Id_stringify = 3;

    private static final int LAST_METHOD_ID = 3;

    private static final int MAX_ID = 3;

    static void init(Scriptable scope, boolean sealed, Context cx) {
        NativeJSON obj = new NativeJSON();
        obj.activatePrototypeMap(3);
        obj.setPrototype(getObjectPrototype(scope, cx));
        obj.setParentScope(scope);
        if (sealed) {
            obj.sealObject(cx);
        }
        defineProperty(scope, "JSON", obj, 2, cx);
    }

    private static Object parse(Context cx, Scriptable scope, String jtext) {
        try {
            return new JsonParser(scope).parseValue(cx, jtext);
        } catch (JsonParser.ParseException var4) {
            throw ScriptRuntime.constructError(cx, "SyntaxError", var4.getMessage());
        }
    }

    public static Object parse(Context cx, Scriptable scope, String jtext, Callable reviver) {
        Object unfiltered = parse(cx, scope, jtext);
        Scriptable root = cx.newObject(scope);
        root.put(cx, "", root, unfiltered);
        return walk(cx, scope, reviver, root, "");
    }

    private static Object walk(Context cx, Scriptable scope, Callable reviver, Scriptable holder, Object name) {
        Object property;
        if (name instanceof Number) {
            property = holder.get(cx, ((Number) name).intValue(), holder);
        } else {
            property = holder.get(cx, (String) name, holder);
        }
        if (property instanceof Scriptable val) {
            if (val instanceof NativeArray) {
                long len = ((NativeArray) val).getLength();
                for (long i = 0L; i < len; i++) {
                    if (i > 2147483647L) {
                        String id = Long.toString(i);
                        Object newElement = walk(cx, scope, reviver, val, id);
                        if (newElement == Undefined.instance) {
                            val.delete(cx, id);
                        } else {
                            val.put(cx, id, val, newElement);
                        }
                    } else {
                        int idx = (int) i;
                        Object newElement = walk(cx, scope, reviver, val, idx);
                        if (newElement == Undefined.instance) {
                            val.delete(cx, idx);
                        } else {
                            val.put(cx, idx, val, newElement);
                        }
                    }
                }
            } else {
                Object[] keys = val.getIds(cx);
                for (Object p : keys) {
                    Object newElement = walk(cx, scope, reviver, val, p);
                    if (newElement == Undefined.instance) {
                        if (p instanceof Number) {
                            val.delete(cx, ((Number) p).intValue());
                        } else {
                            val.delete(cx, (String) p);
                        }
                    } else if (p instanceof Number) {
                        val.put(cx, ((Number) p).intValue(), val, newElement);
                    } else {
                        val.put(cx, (String) p, val, newElement);
                    }
                }
            }
        }
        return reviver.call(cx, scope, holder, new Object[] { name, property });
    }

    private static String repeat(char c, int count) {
        char[] chars = new char[count];
        Arrays.fill(chars, c);
        return new String(chars);
    }

    public static String stringify(Object value, Object replacer, Object space, Context cx) {
        JsonElement e = stringify0(cx, cx.getRemapper(), value);
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        String indent = null;
        if (space instanceof NativeNumber) {
            space = ScriptRuntime.toNumber(cx, space);
        } else if (space instanceof NativeString) {
            space = ScriptRuntime.toString(cx, space);
        }
        if (space instanceof Number) {
            int gapLength = (int) ScriptRuntime.toInteger(cx, space);
            gapLength = Math.min(10, gapLength);
            indent = gapLength > 0 ? repeat(' ', gapLength) : "";
        } else if (space instanceof String) {
            indent = (String) space;
            if (indent.length() > 10) {
                indent = indent.substring(0, 10);
            }
        }
        if (indent != null) {
            writer.setIndent(indent);
        }
        writer.setSerializeNulls(true);
        writer.setHtmlSafe(false);
        writer.setLenient(true);
        try {
            Streams.write(e, writer);
            return stringWriter.toString();
        } catch (Exception var9) {
            var9.printStackTrace();
            return "error";
        }
    }

    private static void type(Remapper remapper, StringBuilder builder, Class<?> type) {
        String s = remapper.getMappedClass(type);
        if (!s.startsWith("java.lang.") && !s.startsWith("java.util.")) {
            builder.append(s.isEmpty() ? type.getName() : s);
        } else {
            builder.append(s.substring(10));
        }
    }

    private static void params(Remapper remapper, StringBuilder builder, Class<?>[] params) {
        builder.append('(');
        for (int i = 0; i < params.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            type(remapper, builder, params[i]);
        }
        builder.append(')');
    }

    public static JsonElement stringify0(Context cx, Remapper remapper, Object v) {
        if (v == null) {
            return JsonNull.INSTANCE;
        } else if (v instanceof Boolean) {
            return new JsonPrimitive((Boolean) v);
        } else if (v instanceof CharSequence) {
            return new JsonPrimitive(v.toString());
        } else if (v instanceof Number) {
            return new JsonPrimitive((Number) v);
        } else if (v instanceof NativeString) {
            return new JsonPrimitive(ScriptRuntime.toString(cx, v));
        } else if (v instanceof NativeNumber) {
            return new JsonPrimitive(ScriptRuntime.toNumber(cx, v));
        } else if (v instanceof Map) {
            JsonObject json = new JsonObject();
            for (Entry<?, ?> entry : ((Map) v).entrySet()) {
                json.add(entry.getKey().toString(), stringify0(cx, remapper, entry.getValue()));
            }
            return json;
        } else if (v instanceof Iterable) {
            JsonArray json = new JsonArray();
            for (Object o : (Iterable) v) {
                json.add(stringify0(cx, remapper, o));
            }
            return json;
        } else {
            if (v instanceof Wrapper) {
                v = ((Wrapper) v).unwrap();
            }
            Class<?> cl = v.getClass();
            int array;
            for (array = 0; cl.isArray(); array++) {
                cl = cl.getComponentType();
            }
            String mcl = remapper.getMappedClass(cl);
            StringBuilder clName = new StringBuilder(mcl.isEmpty() ? cl.getName() : mcl);
            if (array > 0) {
                clName.append("[]".repeat(array));
            }
            JsonArray list = new JsonArray();
            if (cl.isInterface()) {
                clName.insert(0, "interface ");
            } else if (cl.isAnnotation()) {
                clName.insert(0, "annotation ");
            } else if (cl.isEnum()) {
                clName.insert(0, "enum ");
            } else {
                clName.insert(0, "class ");
            }
            list.add(clName.toString());
            for (Constructor<?> constructor : cl.getConstructors()) {
                if (!constructor.isAnnotationPresent(HideFromJS.class)) {
                    StringBuilder builder = new StringBuilder("new ");
                    String s = remapper.getMappedClass(constructor.getDeclaringClass());
                    if (s.isEmpty()) {
                        s = constructor.getDeclaringClass().getName();
                    }
                    int si = s.lastIndexOf(46);
                    builder.append(si != -1 && si < s.length() ? s.substring(si + 1) : s);
                    params(remapper, builder, constructor.getParameterTypes());
                    list.add(builder.toString());
                }
            }
            for (Field field : cl.getFields()) {
                int mod = field.getModifiers();
                if (!Modifier.isTransient(mod) && !field.isAnnotationPresent(HideFromJS.class)) {
                    StringBuilder builder = new StringBuilder();
                    if (Modifier.isStatic(mod)) {
                        builder.append("static ");
                    }
                    if (Modifier.isFinal(mod)) {
                        builder.append("final ");
                    }
                    if (Modifier.isNative(mod)) {
                        builder.append("native ");
                    }
                    type(remapper, builder, field.getType());
                    builder.append(' ');
                    builder.append(remapper.getMappedField(cl, field));
                    list.add(builder.toString());
                }
            }
            for (Method method : cl.getMethods()) {
                if (!method.isAnnotationPresent(HideFromJS.class)) {
                    int mod = method.getModifiers();
                    StringBuilder builderx = new StringBuilder();
                    if (Modifier.isStatic(mod)) {
                        builderx.append("static ");
                    }
                    if (Modifier.isNative(mod)) {
                        builderx.append("native ");
                    }
                    type(remapper, builderx, method.getReturnType());
                    builderx.append(' ');
                    builderx.append(remapper.getMappedMethod(cl, method));
                    params(remapper, builderx, method.getParameterTypes());
                    String s = builderx.toString();
                    if (!IGNORED_METHODS.contains(s)) {
                        list.add(s);
                    }
                }
            }
            return list;
        }
    }

    private NativeJSON() {
    }

    @Override
    public String getClassName() {
        return "JSON";
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        if (id <= 3) {
            String name;
            int arity;
            switch(id) {
                case 1:
                    arity = 0;
                    name = "toSource";
                    break;
                case 2:
                    arity = 2;
                    name = "parse";
                    break;
                case 3:
                    arity = 3;
                    name = "stringify";
                    break;
                default:
                    throw new IllegalStateException(String.valueOf(id));
            }
            this.initPrototypeMethod(JSON_TAG, id, name, arity, cx);
        } else {
            throw new IllegalStateException(String.valueOf(id));
        }
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(JSON_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int methodId = f.methodId();
            switch(methodId) {
                case 1:
                    return "JSON";
                case 2:
                    String jtext = ScriptRuntime.toString(cx, args, 0);
                    Object reviver = null;
                    if (args.length > 1) {
                        reviver = args[1];
                    }
                    if (reviver instanceof Callable) {
                        return parse(cx, scope, jtext, (Callable) reviver);
                    }
                    return parse(cx, scope, jtext);
                case 3:
                    Object value = null;
                    Object replacer = null;
                    Object space = null;
                    switch(args.length) {
                        case 3:
                            space = args[2];
                        case 2:
                            replacer = args[1];
                        case 1:
                            value = args[0];
                        case 0:
                        default:
                            return stringify(value, replacer, space, cx);
                    }
                default:
                    throw new IllegalStateException(String.valueOf(methodId));
            }
        }
    }

    @Override
    protected int findPrototypeId(String s) {
        return switch(s) {
            case "toSource" ->
                1;
            case "parse" ->
                2;
            case "stringify" ->
                3;
            default ->
                0;
        };
    }

    static {
        IGNORED_METHODS.add("void wait()");
        IGNORED_METHODS.add("void wait(long, int)");
        IGNORED_METHODS.add("native void wait(long)");
        IGNORED_METHODS.add("boolean equals(Object)");
        IGNORED_METHODS.add("String toString()");
        IGNORED_METHODS.add("native int hashCode()");
        IGNORED_METHODS.add("native Class getClass()");
        IGNORED_METHODS.add("native void notify()");
        IGNORED_METHODS.add("native void notifyAll()");
    }
}