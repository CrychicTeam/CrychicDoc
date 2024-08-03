package dev.latvian.mods.rhino;

import java.util.WeakHashMap;

public class NativeWeakSet extends IdScriptableObject {

    private static final Object MAP_TAG = "WeakSet";

    private static final int Id_constructor = 1;

    private static final int Id_add = 2;

    private static final int Id_delete = 3;

    private static final int Id_has = 4;

    private static final int SymbolId_toStringTag = 5;

    private static final int MAX_PROTOTYPE_ID = 5;

    private final transient WeakHashMap<Scriptable, Boolean> map = new WeakHashMap();

    private boolean instanceOfWeakSet = false;

    static void init(Scriptable scope, boolean sealed, Context cx) {
        NativeWeakSet m = new NativeWeakSet();
        m.exportAsJSClass(5, scope, sealed, cx);
    }

    private static NativeWeakSet realThis(Scriptable thisObj, IdFunctionObject f, Context cx) {
        if (thisObj == null) {
            throw incompatibleCallError(f, cx);
        } else {
            try {
                NativeWeakSet ns = (NativeWeakSet) thisObj;
                if (!ns.instanceOfWeakSet) {
                    throw incompatibleCallError(f, cx);
                } else {
                    return ns;
                }
            } catch (ClassCastException var4) {
                throw incompatibleCallError(f, cx);
            }
        }
    }

    @Override
    public String getClassName() {
        return "WeakSet";
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
                        NativeWeakSet ns = new NativeWeakSet();
                        ns.instanceOfWeakSet = true;
                        if (args.length > 0) {
                            NativeSet.loadFromIterable(cx, scope, ns, args[0]);
                        }
                        return ns;
                    }
                    throw ScriptRuntime.typeError1(cx, "msg.no.new", "WeakSet");
                case 2:
                    return realThis(thisObj, f, cx).js_add(args.length > 0 ? args[0] : Undefined.instance, cx);
                case 3:
                    return realThis(thisObj, f, cx).js_delete(args.length > 0 ? args[0] : Undefined.instance);
                case 4:
                    return realThis(thisObj, f, cx).js_has(args.length > 0 ? args[0] : Undefined.instance);
                default:
                    throw new IllegalArgumentException("WeakMap.prototype has no method: " + f.getFunctionName());
            }
        }
    }

    private Object js_add(Object key, Context cx) {
        if (!ScriptRuntime.isObject(key)) {
            throw ScriptRuntime.typeError1(cx, "msg.arg.not.object", ScriptRuntime.typeof(cx, key));
        } else {
            this.map.put((Scriptable) key, Boolean.TRUE);
            return this;
        }
    }

    private Object js_delete(Object key) {
        return !ScriptRuntime.isObject(key) ? Boolean.FALSE : this.map.remove(key) != null;
    }

    private Object js_has(Object key) {
        return !ScriptRuntime.isObject(key) ? Boolean.FALSE : this.map.containsKey(key);
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        if (id == 5) {
            this.initPrototypeValue(5, SymbolKey.TO_STRING_TAG, this.getClassName(), 3);
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
                    s = "add";
                    break;
                case 3:
                    arity = 1;
                    s = "delete";
                    break;
                case 4:
                    arity = 1;
                    s = "has";
                    break;
                default:
                    throw new IllegalArgumentException(String.valueOf(id));
            }
            this.initPrototypeMethod(MAP_TAG, id, s, fnName, arity, cx);
        }
    }

    @Override
    protected int findPrototypeId(Symbol k) {
        return SymbolKey.TO_STRING_TAG.equals(k) ? 5 : 0;
    }

    @Override
    protected int findPrototypeId(String s) {
        return switch(s) {
            case "constructor" ->
                1;
            case "add" ->
                2;
            case "delete" ->
                3;
            case "has" ->
                4;
            default ->
                0;
        };
    }
}