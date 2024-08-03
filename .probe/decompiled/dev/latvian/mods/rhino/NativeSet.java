package dev.latvian.mods.rhino;

import java.util.Iterator;

public class NativeSet extends IdScriptableObject {

    static final String ITERATOR_TAG = "Set Iterator";

    static final SymbolKey GETSIZE = new SymbolKey("[Symbol.getSize]");

    private static final Object SET_TAG = "Set";

    private static final int Id_constructor = 1;

    private static final int Id_add = 2;

    private static final int Id_delete = 3;

    private static final int Id_has = 4;

    private static final int Id_clear = 5;

    private static final int Id_keys = 6;

    private static final int Id_values = 6;

    private static final int Id_entries = 7;

    private static final int Id_forEach = 8;

    private static final int SymbolId_getSize = 9;

    private static final int SymbolId_toStringTag = 10;

    private static final int MAX_PROTOTYPE_ID = 10;

    private final Hashtable entries;

    private boolean instanceOfSet = false;

    static void init(Context cx, Scriptable scope, boolean sealed) {
        NativeSet obj = new NativeSet(cx);
        obj.exportAsJSClass(10, scope, false, cx);
        ScriptableObject desc = (ScriptableObject) cx.newObject(scope);
        desc.put(cx, "enumerable", desc, Boolean.FALSE);
        desc.put(cx, "configurable", desc, Boolean.TRUE);
        desc.put(cx, "get", desc, obj.get(cx, GETSIZE, obj));
        obj.defineOwnProperty(cx, "size", desc);
        if (sealed) {
            obj.sealObject(cx);
        }
    }

    static void loadFromIterable(Context cx, Scriptable scope, ScriptableObject set, Object arg1) {
        if (arg1 != null && !Undefined.instance.equals(arg1)) {
            Object ito = ScriptRuntime.callIterator(cx, scope, arg1);
            if (!Undefined.instance.equals(ito)) {
                ScriptableObject dummy = ensureScriptableObject(cx.newObject(scope, set.getClassName()), cx);
                Callable add = ScriptRuntime.getPropFunctionAndThis(cx, scope, dummy.getPrototype(cx), "add");
                cx.lastStoredScriptable();
                IteratorLikeIterable it = new IteratorLikeIterable(cx, scope, ito);
                try {
                    for (Object val : it) {
                        Object finalVal = val == NOT_FOUND ? Undefined.instance : val;
                        add.call(cx, scope, set, new Object[] { finalVal });
                    }
                } catch (Throwable var12) {
                    try {
                        it.close();
                    } catch (Throwable var11) {
                        var12.addSuppressed(var11);
                    }
                    throw var12;
                }
                it.close();
            }
        }
    }

    private static NativeSet realThis(Scriptable thisObj, IdFunctionObject f, Context cx) {
        if (thisObj == null) {
            throw incompatibleCallError(f, cx);
        } else {
            try {
                NativeSet ns = (NativeSet) thisObj;
                if (!ns.instanceOfSet) {
                    throw incompatibleCallError(f, cx);
                } else {
                    return ns;
                }
            } catch (ClassCastException var4) {
                throw incompatibleCallError(f, cx);
            }
        }
    }

    public NativeSet(Context cx) {
        this.entries = new Hashtable(cx);
    }

    @Override
    public String getClassName() {
        return "Set";
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(SET_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            switch(id) {
                case 1:
                    if (thisObj == null) {
                        NativeSet ns = new NativeSet(cx);
                        ns.instanceOfSet = true;
                        if (args.length > 0) {
                            loadFromIterable(cx, scope, ns, args[0]);
                        }
                        return ns;
                    }
                    throw ScriptRuntime.typeError1(cx, "msg.no.new", "Set");
                case 2:
                    return realThis(thisObj, f, cx).js_add(cx, args.length > 0 ? args[0] : Undefined.instance);
                case 3:
                    return realThis(thisObj, f, cx).js_delete(cx, args.length > 0 ? args[0] : Undefined.instance);
                case 4:
                    return realThis(thisObj, f, cx).js_has(cx, args.length > 0 ? args[0] : Undefined.instance);
                case 5:
                    return realThis(thisObj, f, cx).js_clear(cx);
                case 6:
                    return realThis(thisObj, f, cx).js_iterator(scope, NativeCollectionIterator.Type.VALUES, cx);
                case 7:
                    return realThis(thisObj, f, cx).js_iterator(scope, NativeCollectionIterator.Type.BOTH, cx);
                case 8:
                    return realThis(thisObj, f, cx).js_forEach(cx, scope, args.length > 0 ? args[0] : Undefined.instance, args.length > 1 ? args[1] : Undefined.instance);
                case 9:
                    return realThis(thisObj, f, cx).js_getSize();
                default:
                    throw new IllegalArgumentException("Set.prototype has no method: " + f.getFunctionName());
            }
        }
    }

    private Object js_add(Context cx, Object k) {
        Object key = k;
        if (k instanceof Number && ((Number) k).doubleValue() == ScriptRuntime.negativeZero) {
            key = ScriptRuntime.zeroObj;
        }
        this.entries.put(cx, key, key);
        return this;
    }

    private Object js_delete(Context cx, Object arg) {
        Object ov = this.entries.delete(cx, arg);
        return ov != null;
    }

    private Object js_has(Context cx, Object arg) {
        return this.entries.has(cx, arg);
    }

    private Object js_clear(Context cx) {
        this.entries.clear(cx);
        return Undefined.instance;
    }

    private Object js_getSize() {
        return this.entries.size();
    }

    private Object js_iterator(Scriptable scope, NativeCollectionIterator.Type type, Context cx) {
        return new NativeCollectionIterator(scope, "Set Iterator", type, this.entries.iterator(), cx);
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
                f.call(cx, scope, thisObj, new Object[] { e.value, e.value, this });
            }
            return Undefined.instance;
        } else {
            throw ScriptRuntime.notFunctionError(cx, arg1);
        }
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        switch(id) {
            case 9:
                this.initPrototypeMethod(SET_TAG, id, GETSIZE, "get size", 0, cx);
                return;
            case 10:
                this.initPrototypeValue(10, SymbolKey.TO_STRING_TAG, this.getClassName(), 3);
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
                    case 5:
                        arity = 0;
                        s = "clear";
                        break;
                    case 6:
                        arity = 0;
                        s = "values";
                        break;
                    case 7:
                        arity = 0;
                        s = "entries";
                        break;
                    case 8:
                        arity = 1;
                        s = "forEach";
                        break;
                    default:
                        throw new IllegalArgumentException(String.valueOf(id));
                }
                this.initPrototypeMethod(SET_TAG, id, s, fnName, arity, cx);
        }
    }

    @Override
    protected int findPrototypeId(Symbol k) {
        if (GETSIZE.equals(k)) {
            return 9;
        } else if (SymbolKey.ITERATOR.equals(k)) {
            return 6;
        } else {
            return SymbolKey.TO_STRING_TAG.equals(k) ? 10 : 0;
        }
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
            case "clear" ->
                5;
            case "keys" ->
                6;
            case "values" ->
                6;
            case "entries" ->
                7;
            case "forEach" ->
                8;
            default ->
                0;
        };
    }
}