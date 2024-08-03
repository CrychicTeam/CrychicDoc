package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.util.CustomJavaToJsWrapper;
import dev.latvian.mods.rhino.util.JavaSetWrapper;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WrapFactory {

    private boolean javaPrimitiveWrap = true;

    public Object wrap(Context cx, Scriptable scope, Object obj, Class<?> staticType) {
        if (obj == null || obj == Undefined.instance || obj instanceof Scriptable) {
            return obj;
        } else if (staticType == void.class) {
            return Undefined.instance;
        } else if (staticType == char.class) {
            return Integer.valueOf((Character) obj);
        } else if (staticType != null && staticType.isPrimitive()) {
            return obj;
        } else {
            if (!this.isJavaPrimitiveWrap()) {
                if (obj instanceof String || obj instanceof Boolean || obj instanceof Integer || obj instanceof Short || obj instanceof Long || obj instanceof Float || obj instanceof Double) {
                    return obj;
                }
                if (obj instanceof Character) {
                    return String.valueOf((Character) obj);
                }
            }
            Class<?> cls = obj.getClass();
            return cls.isArray() ? NativeJavaArray.wrap(scope, obj, cx) : this.wrapAsJavaObject(cx, scope, obj, staticType);
        }
    }

    public Scriptable wrapNewObject(Scriptable scope, Object obj, Context cx) {
        if (obj instanceof Scriptable) {
            return (Scriptable) obj;
        } else {
            Class<?> cls = obj.getClass();
            return (Scriptable) (cls.isArray() ? NativeJavaArray.wrap(scope, obj, cx) : this.wrapAsJavaObject(cx, scope, obj, null));
        }
    }

    public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class<?> staticType) {
        if (javaObject instanceof CustomJavaToJsWrapper w) {
            return w.convertJavaToJs(cx, scope, staticType);
        } else {
            CustomJavaToJsWrapper w = cx.wrapCustomJavaToJs(javaObject);
            if (w != null) {
                return w.convertJavaToJs(cx, scope, staticType);
            } else if (javaObject instanceof Map map) {
                return new NativeJavaMap(cx, scope, map, map);
            } else if (javaObject instanceof List list) {
                return new NativeJavaList(cx, scope, list, list);
            } else {
                return (Scriptable) (javaObject instanceof Set<?> set ? new NativeJavaList(cx, scope, set, new JavaSetWrapper(set)) : new NativeJavaObject(scope, javaObject, staticType, cx));
            }
        }
    }

    public Scriptable wrapJavaClass(Context cx, Scriptable scope, Class<?> javaClass) {
        return new NativeJavaClass(cx, scope, javaClass);
    }

    public final boolean isJavaPrimitiveWrap() {
        return this.javaPrimitiveWrap;
    }

    public final void setJavaPrimitiveWrap(boolean value) {
        this.javaPrimitiveWrap = value;
    }
}