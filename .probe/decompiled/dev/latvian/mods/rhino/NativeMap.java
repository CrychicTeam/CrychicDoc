package dev.latvian.mods.rhino;

import java.util.Iterator;

public class NativeMap extends IdScriptableObject {

    static final String ITERATOR_TAG = "Map Iterator";

    private static final Object MAP_TAG = "Map";

    private static final Object NULL_VALUE = new Object();

    private static final int Id_constructor = 1;

    private static final int Id_set = 2;

    private static final int Id_get = 3;

    private static final int Id_delete = 4;

    private static final int Id_has = 5;

    private static final int Id_clear = 6;

    private static final int Id_keys = 7;

    private static final int Id_values = 8;

    private static final int Id_entries = 9;

    private static final int Id_forEach = 10;

    private static final int SymbolId_getSize = 11;

    private static final int SymbolId_toStringTag = 12;

    private static final int MAX_PROTOTYPE_ID = 12;

    private final Hashtable entries;

    private boolean instanceOfMap = false;

    static void init(Context cx, Scriptable scope, boolean sealed) {
        NativeMap obj = new NativeMap(cx);
        obj.exportAsJSClass(12, scope, false, cx);
        ScriptableObject desc = (ScriptableObject) cx.newObject(scope);
        desc.put(cx, "enumerable", desc, Boolean.FALSE);
        desc.put(cx, "configurable", desc, Boolean.TRUE);
        desc.put(cx, "get", desc, obj.get(cx, NativeSet.GETSIZE, obj));
        obj.defineOwnProperty(cx, "size", desc);
        if (sealed) {
            obj.sealObject(cx);
        }
    }

    static void loadFromIterable(Context cx, Scriptable scope, ScriptableObject map, Object arg1) {
        if (arg1 != null && !Undefined.instance.equals(arg1)) {
            Object ito = ScriptRuntime.callIterator(cx, scope, arg1);
            if (!Undefined.instance.equals(ito)) {
                ScriptableObject dummy = ensureScriptableObject(cx.newObject(scope, map.getClassName()), cx);
                Callable set = ScriptRuntime.getPropFunctionAndThis(cx, scope, dummy.getPrototype(cx), "set");
                cx.lastStoredScriptable();
                IteratorLikeIterable it = new IteratorLikeIterable(cx, scope, ito);
                try {
                    for (Object val : it) {
                        Scriptable sVal = ensureScriptable(val, cx);
                        if (sVal instanceof Symbol) {
                            throw ScriptRuntime.typeError1(cx, "msg.arg.not.object", ScriptRuntime.typeof(cx, sVal));
                        }
                        Object finalKey = sVal.get(cx, 0, sVal);
                        if (finalKey == NOT_FOUND) {
                            finalKey = Undefined.instance;
                        }
                        Object finalVal = sVal.get(cx, 1, sVal);
                        if (finalVal == NOT_FOUND) {
                            finalVal = Undefined.instance;
                        }
                        set.call(cx, scope, map, new Object[] { finalKey, finalVal });
                    }
                } catch (Throwable var14) {
                    try {
                        it.close();
                    } catch (Throwable var13) {
                        var14.addSuppressed(var13);
                    }
                    throw var14;
                }
                it.close();
            }
        }
    }

    private static NativeMap realThis(Scriptable thisObj, IdFunctionObject f, Context cx) {
        if (thisObj == null) {
            throw incompatibleCallError(f, cx);
        } else {
            try {
                NativeMap nm = (NativeMap) thisObj;
                if (!nm.instanceOfMap) {
                    throw incompatibleCallError(f, cx);
                } else {
                    return nm;
                }
            } catch (ClassCastException var4) {
                throw incompatibleCallError(f, cx);
            }
        }
    }

    public NativeMap(Context cx) {
        this.entries = new Hashtable(cx);
    }

    @Override
    public String getClassName() {
        return "Map";
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
                        NativeMap nm = new NativeMap(cx);
                        nm.instanceOfMap = true;
                        if (args.length > 0) {
                            loadFromIterable(cx, scope, nm, args[0]);
                        }
                        return nm;
                    }
                    throw ScriptRuntime.typeError1(cx, "msg.no.new", "Map");
                case 2:
                    return realThis(thisObj, f, cx).js_set(cx, args.length > 0 ? args[0] : Undefined.instance, args.length > 1 ? args[1] : Undefined.instance);
                case 3:
                    return realThis(thisObj, f, cx).js_get(cx, args.length > 0 ? args[0] : Undefined.instance);
                case 4:
                    return realThis(thisObj, f, cx).js_delete(cx, args.length > 0 ? args[0] : Undefined.instance);
                case 5:
                    return realThis(thisObj, f, cx).js_has(cx, args.length > 0 ? args[0] : Undefined.instance);
                case 6:
                    return realThis(thisObj, f, cx).js_clear(cx);
                case 7:
                    return realThis(thisObj, f, cx).js_iterator(scope, NativeCollectionIterator.Type.KEYS, cx);
                case 8:
                    return realThis(thisObj, f, cx).js_iterator(scope, NativeCollectionIterator.Type.VALUES, cx);
                case 9:
                    return realThis(thisObj, f, cx).js_iterator(scope, NativeCollectionIterator.Type.BOTH, cx);
                case 10:
                    return realThis(thisObj, f, cx).js_forEach(cx, scope, args.length > 0 ? args[0] : Undefined.instance, args.length > 1 ? args[1] : Undefined.instance);
                case 11:
                    return realThis(thisObj, f, cx).js_getSize();
                default:
                    throw new IllegalArgumentException("Map.prototype has no method: " + f.getFunctionName());
            }
        }
    }

    private Object js_set(Context cx, Object k, Object v) {
        Object value = v == null ? NULL_VALUE : v;
        Object key = k;
        if (k instanceof Number && ((Number) k).doubleValue() == ScriptRuntime.negativeZero) {
            key = ScriptRuntime.zeroObj;
        }
        this.entries.put(cx, key, value);
        return this;
    }

    private Object js_delete(Context cx, Object arg) {
        Object e = this.entries.delete(cx, arg);
        return e != null;
    }

    private Object js_get(Context cx, Object arg) {
        Object val = this.entries.get(cx, arg);
        if (val == null) {
            return Undefined.instance;
        } else {
            return val == NULL_VALUE ? null : val;
        }
    }

    private Object js_has(Context cx, Object arg) {
        return this.entries.has(cx, arg);
    }

    private Object js_getSize() {
        return this.entries.size();
    }

    private Object js_iterator(Scriptable scope, NativeCollectionIterator.Type type, Context cx) {
        return new NativeCollectionIterator(scope, "Map Iterator", type, this.entries.iterator(), cx);
    }

    private Object js_clear(Context cx) {
        this.entries.clear(cx);
        return Undefined.instance;
    }

    private Object js_forEach(Context cx, Scriptable scope, Object arg1, Object arg2) {
        if (arg1 instanceof Callable f) {
            boolean isStrict = cx.isStrictMode();
            Iterator i = this.entries.iterator();
            while (i.hasNext()) {
                Scriptable thisObj = ScriptRuntime.toObjectOrNull(cx, arg2, scope);
                if (thisObj == null && !isStrict) {
                    thisObj = scope;
                }
                if (thisObj == null) {
                    thisObj = Undefined.SCRIPTABLE_UNDEFINED;
                }
                Hashtable.Entry e = (Hashtable.Entry) i.next();
                Object val = e.value;
                if (val == NULL_VALUE) {
                    val = null;
                }
                f.call(cx, scope, thisObj, new Object[] { val, e.key, this });
            }
            return Undefined.instance;
        } else {
            throw ScriptRuntime.typeError2(cx, "msg.isnt.function", arg1, ScriptRuntime.typeof(cx, arg1));
        }
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        switch(id) {
            case 11:
                this.initPrototypeMethod(MAP_TAG, id, NativeSet.GETSIZE, "get size", 0, cx);
                return;
            case 12:
                this.initPrototypeValue(12, SymbolKey.TO_STRING_TAG, this.getClassName(), 3);
                return;
            default:
                String fnName = null;
                String s;
                int arity;
                switch(id) {
                    case 1:
                        arity = 0;
                        s = "constructor";
                        break;
                    case 2:
                        arity = 2;
                        s = "set";
                        break;
                    case 3:
                        arity = 1;
                        s = "get";
                        break;
                    case 4:
                        arity = 1;
                        s = "delete";
                        break;
                    case 5:
                        arity = 1;
                        s = "has";
                        break;
                    case 6:
                        arity = 0;
                        s = "clear";
                        break;
                    case 7:
                        arity = 0;
                        s = "keys";
                        break;
                    case 8:
                        arity = 0;
                        s = "values";
                        break;
                    case 9:
                        arity = 0;
                        s = "entries";
                        break;
                    case 10:
                        arity = 1;
                        s = "forEach";
                        break;
                    default:
                        throw new IllegalArgumentException(String.valueOf(id));
                }
                this.initPrototypeMethod(MAP_TAG, id, s, fnName, arity, cx);
        }
    }

    @Override
    protected int findPrototypeId(Symbol k) {
        if (NativeSet.GETSIZE.equals(k)) {
            return 11;
        } else if (SymbolKey.ITERATOR.equals(k)) {
            return 9;
        } else {
            return SymbolKey.TO_STRING_TAG.equals(k) ? 12 : 0;
        }
    }

    @Override
    protected int findPrototypeId(String s) {
        return switch(s) {
            case "constructor" ->
                1;
            case "set" ->
                2;
            case "get" ->
                3;
            case "delete" ->
                4;
            case "has" ->
                5;
            case "clear" ->
                6;
            case "keys" ->
                7;
            case "values" ->
                8;
            case "entries" ->
                9;
            case "forEach" ->
                10;
            default ->
                0;
        };
    }
}