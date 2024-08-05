package dev.latvian.mods.rhino;

import java.util.WeakHashMap;

public class NativeWeakMap extends IdScriptableObject {

    private static final Object MAP_TAG = "WeakMap";

    private static final Object NULL_VALUE = new Object();

    private static final int Id_constructor = 1;

    private static final int Id_delete = 2;

    private static final int Id_get = 3;

    private static final int Id_has = 4;

    private static final int Id_set = 5;

    private static final int SymbolId_toStringTag = 6;

    private static final int MAX_PROTOTYPE_ID = 6;

    private final transient WeakHashMap<Scriptable, Object> map = new WeakHashMap();

    private boolean instanceOfWeakMap = false;

    static void init(Scriptable scope, boolean sealed, Context cx) {
        NativeWeakMap m = new NativeWeakMap();
        m.exportAsJSClass(6, scope, sealed, cx);
    }

    private static NativeWeakMap realThis(Scriptable thisObj, IdFunctionObject f, Context cx) {
        if (thisObj == null) {
            throw incompatibleCallError(f, cx);
        } else {
            try {
                NativeWeakMap nm = (NativeWeakMap) thisObj;
                if (!nm.instanceOfWeakMap) {
                    throw incompatibleCallError(f, cx);
                } else {
                    return nm;
                }
            } catch (ClassCastException var4) {
                throw incompatibleCallError(f, cx);
            }
        }
    }

    @Override
    public String getClassName() {
        return "WeakMap";
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(MAP_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            switch(id) {
                case 1:
                    if (thisObj == null) {
                        NativeWeakMap nm = new NativeWeakMap();
                        nm.instanceOfWeakMap = true;
                        if (args.length > 0) {
                            NativeMap.loadFromIterable(cx, scope, nm, args[0]);
                        }
                        return nm;
                    }
                    throw ScriptRuntime.typeError1(cx, "msg.no.new", "WeakMap");
                case 2:
                    return realThis(thisObj, f, cx).js_delete(args.length > 0 ? args[0] : Undefined.instance);
                case 3:
                    return realThis(thisObj, f, cx).js_get(args.length > 0 ? args[0] : Undefined.instance);
                case 4:
                    return realThis(thisObj, f, cx).js_has(args.length > 0 ? args[0] : Undefined.instance);
                case 5:
                    return realThis(thisObj, f, cx).js_set(args.length > 0 ? args[0] : Undefined.instance, args.length > 1 ? args[1] : Undefined.instance, cx);
                default:
                    throw new IllegalArgumentException("WeakMap.prototype has no method: " + f.getFunctionName());
            }
        }
    }

    private Object js_delete(Object key) {
        return !ScriptRuntime.isObject(key) ? Boolean.FALSE : this.map.remove(key) != null;
    }

    private Object js_get(Object key) {
        if (!ScriptRuntime.isObject(key)) {
            return Undefined.instance;
        } else {
            Object result = this.map.get(key);
            if (result == null) {
                return Undefined.instance;
            } else {
                return result == NULL_VALUE ? null : result;
            }
        }
    }

    private Object js_has(Object key) {
        return !ScriptRuntime.isObject(key) ? Boolean.FALSE : this.map.containsKey(key);
    }

    private Object js_set(Object key, Object v, Context cx) {
        if (!ScriptRuntime.isObject(key)) {
            throw ScriptRuntime.typeError1(cx, "msg.arg.not.object", ScriptRuntime.typeof(cx, key));
        } else {
            Object value = v == null ? NULL_VALUE : v;
            this.map.put((Scriptable) key, value);
            return this;
        }
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        if (id == 6) {
            this.initPrototypeValue(6, SymbolKey.TO_STRING_TAG, this.getClassName(), 3);
        } else {
            String fnName = null;
            String s;
            int arity;
            switch(id) {
                case 1:
                    arity = 0;
                    s = "constructor";
                    break;
                case 2:
                    arity = 1;
                    s = "delete";
                    break;
                case 3:
                    arity = 1;
                    s = "get";
                    break;
                case 4:
                    arity = 1;
                    s = "has";
                    break;
                case 5:
                    arity = 2;
                    s = "set";
                    break;
                default:
                    throw new IllegalArgumentException(String.valueOf(id));
            }
            this.initPrototypeMethod(MAP_TAG, id, s, fnName, arity, cx);
        }
    }

    @Override
    protected int findPrototypeId(Symbol k) {
        return SymbolKey.TO_STRING_TAG.equals(k) ? 6 : 0;
    }

    @Override
    protected int findPrototypeId(String s) {
        return switch(s) {
            case "constructor" ->
                1;
            case "delete" ->
                2;
            case "get" ->
                3;
            case "has" ->
                4;
            case "set" ->
                5;
            default ->
                0;
        };
    }
}