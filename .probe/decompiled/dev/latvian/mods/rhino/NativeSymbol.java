package dev.latvian.mods.rhino;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class NativeSymbol extends IdScriptableObject implements Symbol {

    public static final String CLASS_NAME = "Symbol";

    private static final Object GLOBAL_TABLE_KEY = new Object();

    private static final Object CONSTRUCTOR_SLOT = new Object();

    private static final int ConstructorId_keyFor = -2;

    private static final int ConstructorId_for = -1;

    private static final int Id_constructor = 1;

    private static final int Id_toString = 2;

    private static final int Id_valueOf = 4;

    private static final int SymbolId_toStringTag = 3;

    private static final int SymbolId_toPrimitive = 5;

    private static final int MAX_PROTOTYPE_ID = 5;

    private final SymbolKey key;

    private final NativeSymbol symbolData;

    public static void init(Context cx, Scriptable scope, boolean sealed) {
        NativeSymbol obj = new NativeSymbol("");
        ScriptableObject ctor = obj.exportAsJSClass(5, scope, false, cx);
        cx.putThreadLocal(CONSTRUCTOR_SLOT, Boolean.TRUE);
        try {
            createStandardSymbol(cx, scope, ctor, "iterator", SymbolKey.ITERATOR);
            createStandardSymbol(cx, scope, ctor, "species", SymbolKey.SPECIES);
            createStandardSymbol(cx, scope, ctor, "toStringTag", SymbolKey.TO_STRING_TAG);
            createStandardSymbol(cx, scope, ctor, "hasInstance", SymbolKey.HAS_INSTANCE);
            createStandardSymbol(cx, scope, ctor, "isConcatSpreadable", SymbolKey.IS_CONCAT_SPREADABLE);
            createStandardSymbol(cx, scope, ctor, "isRegExp", SymbolKey.IS_REGEXP);
            createStandardSymbol(cx, scope, ctor, "toPrimitive", SymbolKey.TO_PRIMITIVE);
            createStandardSymbol(cx, scope, ctor, "match", SymbolKey.MATCH);
            createStandardSymbol(cx, scope, ctor, "replace", SymbolKey.REPLACE);
            createStandardSymbol(cx, scope, ctor, "search", SymbolKey.SEARCH);
            createStandardSymbol(cx, scope, ctor, "split", SymbolKey.SPLIT);
            createStandardSymbol(cx, scope, ctor, "unscopables", SymbolKey.UNSCOPABLES);
        } finally {
            cx.removeThreadLocal(CONSTRUCTOR_SLOT);
        }
        if (sealed) {
            ctor.sealObject(cx);
        }
    }

    public static NativeSymbol construct(Context cx, Scriptable scope, Object[] args) {
        cx.putThreadLocal(CONSTRUCTOR_SLOT, Boolean.TRUE);
        NativeSymbol var3;
        try {
            var3 = (NativeSymbol) cx.newObject(scope, "Symbol", args);
        } finally {
            cx.removeThreadLocal(CONSTRUCTOR_SLOT);
        }
        return var3;
    }

    private static void createStandardSymbol(Context cx, Scriptable scope, ScriptableObject ctor, String name, SymbolKey key) {
        Scriptable sym = cx.newObject(scope, "Symbol", new Object[] { name, key });
        ctor.defineProperty(cx, name, sym, 7);
    }

    private static NativeSymbol getSelf(Context cx, Object thisObj) {
        try {
            return (NativeSymbol) thisObj;
        } catch (ClassCastException var3) {
            throw ScriptRuntime.typeError1(cx, "msg.invalid.type", thisObj.getClass().getName());
        }
    }

    private static NativeSymbol js_constructor(Context cx, Object[] args) {
        String desc;
        if (args.length > 0) {
            if (Undefined.instance.equals(args[0])) {
                desc = "";
            } else {
                desc = ScriptRuntime.toString(cx, args[0]);
            }
        } else {
            desc = "";
        }
        return args.length > 1 ? new NativeSymbol((SymbolKey) args[1]) : new NativeSymbol(new SymbolKey(desc));
    }

    private static boolean isStrictMode(Context cx) {
        return cx != null && cx.isStrictMode();
    }

    private NativeSymbol(String desc) {
        this.key = new SymbolKey(desc);
        this.symbolData = null;
    }

    private NativeSymbol(SymbolKey key) {
        this.key = key;
        this.symbolData = this;
    }

    public NativeSymbol(NativeSymbol s) {
        this.key = s.key;
        this.symbolData = s.symbolData;
    }

    @Override
    public String getClassName() {
        return "Symbol";
    }

    @Override
    protected void fillConstructorProperties(IdFunctionObject ctor, Context cx) {
        super.fillConstructorProperties(ctor, cx);
        this.addIdFunctionProperty(ctor, "Symbol", -1, "for", 1, cx);
        this.addIdFunctionProperty(ctor, "Symbol", -2, "keyFor", 1, cx);
    }

    @Override
    protected int findPrototypeId(String s) {
        int id = 0;
        int var5 = 0;
        String X = null;
        int s_length = s.length();
        if (s_length == 7) {
            X = "valueOf";
            var5 = 4;
        } else if (s_length == 8) {
            X = "toString";
            var5 = 2;
        } else if (s_length == 11) {
            X = "constructor";
            var5 = 1;
        }
        if (X != null && X != s && !X.equals(s)) {
            var5 = 0;
        }
        return var5;
    }

    @Override
    protected int findPrototypeId(Symbol key) {
        if (SymbolKey.TO_STRING_TAG.equals(key)) {
            return 3;
        } else {
            return SymbolKey.TO_PRIMITIVE.equals(key) ? 5 : 0;
        }
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        switch(id) {
            case 1:
                this.initPrototypeMethod("Symbol", id, "constructor", 0, cx);
                break;
            case 2:
                this.initPrototypeMethod("Symbol", id, "toString", 0, cx);
                break;
            case 3:
                this.initPrototypeValue(id, SymbolKey.TO_STRING_TAG, "Symbol", 3);
                break;
            case 4:
                this.initPrototypeMethod("Symbol", id, "valueOf", 0, cx);
                break;
            case 5:
                this.initPrototypeMethod("Symbol", id, SymbolKey.TO_PRIMITIVE, "Symbol.toPrimitive", 1, cx);
                break;
            default:
                super.initPrototypeId(id, cx);
        }
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag("Symbol")) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            switch(id) {
                case -2:
                    return this.js_keyFor(cx, scope, args);
                case -1:
                    return this.js_for(cx, scope, args);
                case 0:
                case 3:
                default:
                    return super.execIdCall(f, cx, scope, thisObj, args);
                case 1:
                    if (thisObj == null) {
                        if (cx.getThreadLocal(CONSTRUCTOR_SLOT) == null) {
                            throw ScriptRuntime.typeError0(cx, "msg.no.symbol.new");
                        }
                        return js_constructor(cx, args);
                    }
                    return construct(cx, scope, args);
                case 2:
                    return getSelf(cx, thisObj).toString();
                case 4:
                case 5:
                    return getSelf(cx, thisObj).js_valueOf();
            }
        }
    }

    private Object js_valueOf() {
        return this.symbolData;
    }

    private Object js_for(Context cx, Scriptable scope, Object[] args) {
        String name = args.length > 0 ? ScriptRuntime.toString(cx, args[0]) : ScriptRuntime.toString(cx, Undefined.instance);
        Map<String, NativeSymbol> table = this.getGlobalMap();
        NativeSymbol ret = (NativeSymbol) table.get(name);
        if (ret == null) {
            ret = construct(cx, scope, new Object[] { name });
            table.put(name, ret);
        }
        return ret;
    }

    private Object js_keyFor(Context cx, Scriptable scope, Object[] args) {
        if ((args.length > 0 ? args[0] : Undefined.instance) instanceof NativeSymbol sym) {
            Map<String, NativeSymbol> table = this.getGlobalMap();
            for (Entry<String, NativeSymbol> e : table.entrySet()) {
                if (((NativeSymbol) e.getValue()).key == sym.key) {
                    return e.getKey();
                }
            }
            return Undefined.instance;
        } else {
            throw ScriptRuntime.throwCustomError(cx, scope, "TypeError", "Not a Symbol");
        }
    }

    public String toString() {
        return this.key.toString();
    }

    @Override
    public void put(Context cx, String name, Scriptable start, Object value) {
        if (!this.isSymbol()) {
            super.put(cx, name, start, value);
        } else if (isStrictMode(cx)) {
            throw ScriptRuntime.typeError0(cx, "msg.no.assign.symbol.strict");
        }
    }

    @Override
    public void put(Context cx, int index, Scriptable start, Object value) {
        if (!this.isSymbol()) {
            super.put(cx, index, start, value);
        } else if (isStrictMode(cx)) {
            throw ScriptRuntime.typeError0(cx, "msg.no.assign.symbol.strict");
        }
    }

    @Override
    public void put(Context cx, Symbol key, Scriptable start, Object value) {
        if (!this.isSymbol()) {
            super.put(cx, key, start, value);
        } else if (isStrictMode(cx)) {
            throw ScriptRuntime.typeError0(cx, "msg.no.assign.symbol.strict");
        }
    }

    public boolean isSymbol() {
        return this.symbolData == this;
    }

    @Override
    public MemberType getTypeOf() {
        return this.isSymbol() ? MemberType.SYMBOL : super.getTypeOf();
    }

    public int hashCode() {
        return this.key.hashCode();
    }

    public boolean equals(Object x) {
        return this.key.equals(x);
    }

    SymbolKey getKey() {
        return this.key;
    }

    private Map<String, NativeSymbol> getGlobalMap() {
        ScriptableObject top = (ScriptableObject) getTopLevelScope(this);
        Map<String, NativeSymbol> map = (Map<String, NativeSymbol>) top.getAssociatedValue(GLOBAL_TABLE_KEY);
        if (map == null) {
            map = new HashMap();
            top.associateValue(GLOBAL_TABLE_KEY, map);
        }
        return map;
    }
}