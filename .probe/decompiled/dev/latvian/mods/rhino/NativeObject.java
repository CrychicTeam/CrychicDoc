package dev.latvian.mods.rhino;

import dev.latvian.mods.rhino.util.DataObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;

public class NativeObject extends IdScriptableObject implements Map, DataObject {

    private static final Object OBJECT_TAG = "Object";

    private static final int ConstructorId_getPrototypeOf = -1;

    private static final int ConstructorId_keys = -2;

    private static final int ConstructorId_getOwnPropertyNames = -3;

    private static final int ConstructorId_getOwnPropertyDescriptor = -4;

    private static final int ConstructorId_defineProperty = -5;

    private static final int ConstructorId_isExtensible = -6;

    private static final int ConstructorId_preventExtensions = -7;

    private static final int ConstructorId_defineProperties = -8;

    private static final int ConstructorId_create = -9;

    private static final int ConstructorId_isSealed = -10;

    private static final int ConstructorId_isFrozen = -11;

    private static final int ConstructorId_seal = -12;

    private static final int ConstructorId_freeze = -13;

    private static final int ConstructorId_getOwnPropertySymbols = -14;

    private static final int ConstructorId_assign = -15;

    private static final int ConstructorId_is = -16;

    private static final int ConstructorId_setPrototypeOf = -17;

    private static final int ConstructorId_entries = -18;

    private static final int ConstructorId_values = -19;

    private static final int Id_constructor = 1;

    private static final int Id_toString = 2;

    private static final int Id_toLocaleString = 3;

    private static final int Id_valueOf = 4;

    private static final int Id_hasOwnProperty = 5;

    private static final int Id_propertyIsEnumerable = 6;

    private static final int Id_isPrototypeOf = 7;

    private static final int Id_toSource = 8;

    private static final int Id___defineGetter__ = 9;

    private static final int Id___defineSetter__ = 10;

    private static final int Id___lookupGetter__ = 11;

    private static final int Id___lookupSetter__ = 12;

    private static final int MAX_PROTOTYPE_ID = 12;

    public final Context localContext;

    static void init(Context cx, Scriptable scope, boolean sealed) {
        NativeObject obj = new NativeObject(cx);
        obj.exportAsJSClass(12, scope, sealed, cx);
    }

    private static Scriptable getCompatibleObject(Context cx, Scriptable scope, Object arg) {
        Scriptable s = ScriptRuntime.toObject(cx, scope, arg);
        return ensureScriptable(s, cx);
    }

    public NativeObject(Context cx) {
        this.localContext = cx;
    }

    @Override
    public String getClassName() {
        return "Object";
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Entry<?, ?> entry : this.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(entry.getValue());
        }
        return sb.append('}').toString();
    }

    @Override
    protected void fillConstructorProperties(IdFunctionObject ctor, Context cx) {
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -1, "getPrototypeOf", 1, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -17, "setPrototypeOf", 2, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -2, "keys", 1, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -18, "entries", 1, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -19, "values", 1, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -3, "getOwnPropertyNames", 1, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -14, "getOwnPropertySymbols", 1, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -4, "getOwnPropertyDescriptor", 2, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -5, "defineProperty", 3, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -6, "isExtensible", 1, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -7, "preventExtensions", 1, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -8, "defineProperties", 2, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -9, "create", 2, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -10, "isSealed", 1, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -11, "isFrozen", 1, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -12, "seal", 1, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -13, "freeze", 1, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -15, "assign", 2, cx);
        this.addIdFunctionProperty(ctor, OBJECT_TAG, -16, "is", 2, cx);
        super.fillConstructorProperties(ctor, cx);
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        String s;
        int arity;
        switch(id) {
            case 1:
                arity = 1;
                s = "constructor";
                break;
            case 2:
                arity = 0;
                s = "toString";
                break;
            case 3:
                arity = 0;
                s = "toLocaleString";
                break;
            case 4:
                arity = 0;
                s = "valueOf";
                break;
            case 5:
                arity = 1;
                s = "hasOwnProperty";
                break;
            case 6:
                arity = 1;
                s = "propertyIsEnumerable";
                break;
            case 7:
                arity = 1;
                s = "isPrototypeOf";
                break;
            case 8:
                arity = 0;
                s = "toSource";
                break;
            case 9:
                arity = 2;
                s = "__defineGetter__";
                break;
            case 10:
                arity = 2;
                s = "__defineSetter__";
                break;
            case 11:
                arity = 1;
                s = "__lookupGetter__";
                break;
            case 12:
                arity = 1;
                s = "__lookupSetter__";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        this.initPrototypeMethod(OBJECT_TAG, id, s, arity, cx);
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(OBJECT_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            switch(id) {
                case -19:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        Scriptable obj = getCompatibleObject(cx, scope, arg);
                        Object[] ids = obj.getIds(cx);
                        Object[] values = new Object[ids.length];
                        for (int ix = 0; ix < ids.length; ix++) {
                            values[ix] = obj.get(cx, ScriptRuntime.toString(cx, ids[ix]), scope);
                        }
                        return cx.newArray(scope, values);
                    }
                case -18:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        Scriptable obj = getCompatibleObject(cx, scope, arg);
                        Object[] ids = obj.getIds(cx);
                        Object[] entries = new Object[ids.length];
                        for (int ixx = 0; ixx < ids.length; ixx++) {
                            Object[] entry = new Object[] { ScriptRuntime.toString(cx, ids[ixx]), null };
                            entry[1] = obj.get(cx, entry[0].toString(), scope);
                            entries[ixx] = cx.newArray(scope, entry);
                        }
                        return cx.newArray(scope, entries);
                    }
                case -17:
                    if (args.length < 2) {
                        throw ScriptRuntime.typeError1(cx, "msg.incompat.call", "setPrototypeOf");
                    } else {
                        Scriptable proto = args[1] == null ? null : ensureScriptable(args[1], cx);
                        if (proto instanceof Symbol) {
                            throw ScriptRuntime.typeError1(cx, "msg.arg.not.object", ScriptRuntime.typeof(cx, proto));
                        } else {
                            Object arg0 = args[0];
                            ScriptRuntimeES6.requireObjectCoercible(cx, arg0, f);
                            if (arg0 instanceof ScriptableObject objxxx) {
                                if (!objxxx.isExtensible()) {
                                    throw ScriptRuntime.typeError0(cx, "msg.not.extensible");
                                }
                                for (Scriptable prototypeProto = proto; prototypeProto != null; prototypeProto = prototypeProto.getPrototype(cx)) {
                                    if (prototypeProto == objxxx) {
                                        throw ScriptRuntime.typeError1(cx, "msg.object.cyclic.prototype", objxxx.getClass().getSimpleName());
                                    }
                                }
                                objxxx.setPrototype(proto);
                                return objxxx;
                            }
                            return arg0;
                        }
                    }
                case -16:
                    Object a1 = args.length < 1 ? Undefined.instance : args[0];
                    Object a2 = args.length < 2 ? Undefined.instance : args[1];
                    return ScriptRuntime.same(cx, a1, a2);
                case -15:
                    if (args.length < 1) {
                        throw ScriptRuntime.typeError1(cx, "msg.incompat.call", "assign");
                    }
                    Scriptable targetObj = ScriptRuntime.toObject(cx, thisObj, args[0]);
                    for (int ix = 1; ix < args.length; ix++) {
                        if (args[ix] != null && !Undefined.isUndefined(args[ix])) {
                            Scriptable sourceObj = ScriptRuntime.toObject(cx, thisObj, args[ix]);
                            Object[] ids = sourceObj.getIds(cx);
                            for (Object key : ids) {
                                if (key instanceof String) {
                                    Object val = sourceObj.get(cx, (String) key, sourceObj);
                                    if (val != NOT_FOUND && !Undefined.isUndefined(val)) {
                                        targetObj.put(cx, (String) key, targetObj, val);
                                    }
                                } else if (key instanceof Number) {
                                    int ii = ScriptRuntime.toInt32(cx, key);
                                    Object val = sourceObj.get(cx, ii, sourceObj);
                                    if (val != NOT_FOUND && !Undefined.isUndefined(val)) {
                                        targetObj.put(cx, ii, targetObj, val);
                                    }
                                }
                            }
                        }
                    }
                    return targetObj;
                case -14:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        Scriptable s = getCompatibleObject(cx, scope, arg);
                        ScriptableObject obj = ensureScriptableObject(s, cx);
                        Object[] ids = obj.getIds(cx, true, true);
                        ArrayList<Object> syms = new ArrayList();
                        for (int i = 0; i < ids.length; i++) {
                            if (ids[i] instanceof Symbol) {
                                syms.add(ids[i]);
                            }
                        }
                        return cx.newArray(scope, syms.toArray());
                    }
                case -13:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        if (!(arg instanceof ScriptableObject)) {
                            return arg;
                        }
                        ScriptableObject obj = ensureScriptableObject(arg, cx);
                        for (Object name : obj.getIds(cx, true, true)) {
                            ScriptableObject descxx = obj.getOwnPropertyDescriptor(cx, name);
                            if (this.isDataDescriptor(descxx, cx) && Boolean.TRUE.equals(descxx.get(cx, "writable"))) {
                                descxx.put(cx, "writable", descxx, Boolean.FALSE);
                            }
                            if (Boolean.TRUE.equals(descxx.get(cx, "configurable"))) {
                                descxx.put(cx, "configurable", descxx, Boolean.FALSE);
                            }
                            obj.defineOwnProperty(cx, name, descxx, false);
                        }
                        obj.preventExtensions();
                        return obj;
                    }
                case -12:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        if (!(arg instanceof ScriptableObject)) {
                            return arg;
                        }
                        ScriptableObject obj = ensureScriptableObject(arg, cx);
                        for (Object name : obj.getAllIds(cx)) {
                            ScriptableObject descxx = obj.getOwnPropertyDescriptor(cx, name);
                            if (Boolean.TRUE.equals(descxx.get(cx, "configurable"))) {
                                descxx.put(cx, "configurable", descxx, Boolean.FALSE);
                                obj.defineOwnProperty(cx, name, descxx, false);
                            }
                        }
                        obj.preventExtensions();
                        return obj;
                    }
                case -11:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        if (!(arg instanceof ScriptableObject)) {
                            return Boolean.TRUE;
                        } else {
                            ScriptableObject objxx = ensureScriptableObject(arg, cx);
                            if (objxx.isExtensible()) {
                                return Boolean.FALSE;
                            } else {
                                for (Object namexx : objxx.getAllIds(cx)) {
                                    ScriptableObject descx = objxx.getOwnPropertyDescriptor(cx, namexx);
                                    if (Boolean.TRUE.equals(descx.get(cx, "configurable"))) {
                                        return Boolean.FALSE;
                                    }
                                    if (this.isDataDescriptor(descx, cx) && Boolean.TRUE.equals(descx.get(cx, "writable"))) {
                                        return Boolean.FALSE;
                                    }
                                }
                                return Boolean.TRUE;
                            }
                        }
                    }
                case -10:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        if (!(arg instanceof ScriptableObject)) {
                            return Boolean.TRUE;
                        } else {
                            ScriptableObject objx = ensureScriptableObject(arg, cx);
                            if (objx.isExtensible()) {
                                return Boolean.FALSE;
                            } else {
                                for (Object namex : objx.getAllIds(cx)) {
                                    Object configurable = objx.getOwnPropertyDescriptor(cx, namex).get(cx, "configurable");
                                    if (Boolean.TRUE.equals(configurable)) {
                                        return Boolean.FALSE;
                                    }
                                }
                                return Boolean.TRUE;
                            }
                        }
                    }
                case -9:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        Scriptable obj = arg == null ? null : ensureScriptable(arg, cx);
                        ScriptableObject newObject = new NativeObject(cx);
                        newObject.setParentScope(scope);
                        newObject.setPrototype(obj);
                        if (args.length > 1 && !Undefined.isUndefined(args[1])) {
                            Scriptable propsx = ScriptRuntime.toObject(cx, scope, args[1]);
                            newObject.defineOwnProperties(cx, ensureScriptableObject(propsx, cx));
                        }
                        return newObject;
                    }
                case -8:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        ScriptableObject obj = ensureScriptableObject(arg, cx);
                        Object propsObj = args.length < 2 ? Undefined.instance : args[1];
                        Scriptable props = ScriptRuntime.toObject(cx, scope, propsObj);
                        obj.defineOwnProperties(cx, ensureScriptableObject(props, cx));
                        return obj;
                    }
                case -7:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        if (!(arg instanceof ScriptableObject)) {
                            return arg;
                        }
                        ScriptableObject obj = ensureScriptableObject(arg, cx);
                        obj.preventExtensions();
                        return obj;
                    }
                case -6:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        if (!(arg instanceof ScriptableObject)) {
                            return Boolean.FALSE;
                        }
                        ScriptableObject obj = ensureScriptableObject(arg, cx);
                        return obj.isExtensible();
                    }
                case -5:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        ScriptableObject obj = ensureScriptableObject(arg, cx);
                        Object name = args.length < 2 ? Undefined.instance : args[1];
                        Object descArg = args.length < 3 ? Undefined.instance : args[2];
                        ScriptableObject desc = ensureScriptableObject(descArg, cx);
                        obj.defineOwnProperty(cx, name, desc);
                        return obj;
                    }
                case -4:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        Scriptable s = getCompatibleObject(cx, scope, arg);
                        ScriptableObject obj = ensureScriptableObject(s, cx);
                        Object nameArg = args.length < 2 ? Undefined.instance : args[1];
                        Scriptable desc = obj.getOwnPropertyDescriptor(cx, nameArg);
                        return desc == null ? Undefined.instance : desc;
                    }
                case -3:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        Scriptable s = getCompatibleObject(cx, scope, arg);
                        ScriptableObject obj = ensureScriptableObject(s, cx);
                        Object[] ids = obj.getIds(cx, true, false);
                        for (int ix = 0; ix < ids.length; ix++) {
                            ids[ix] = ScriptRuntime.toString(cx, ids[ix]);
                        }
                        return cx.newArray(scope, ids);
                    }
                case -2:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        Scriptable obj = getCompatibleObject(cx, scope, arg);
                        Object[] ids = obj.getIds(cx);
                        for (int ix = 0; ix < ids.length; ix++) {
                            ids[ix] = ScriptRuntime.toString(cx, ids[ix]);
                        }
                        return cx.newArray(scope, ids);
                    }
                case -1:
                    {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        Scriptable obj = getCompatibleObject(cx, scope, arg);
                        return obj.getPrototype(cx);
                    }
                case 0:
                default:
                    throw new IllegalArgumentException(String.valueOf(id));
                case 1:
                    if (thisObj != null) {
                        return f.construct(cx, scope, args);
                    } else {
                        if (args.length != 0 && args[0] != null && !Undefined.isUndefined(args[0])) {
                            return ScriptRuntime.toObject(cx, scope, args[0]);
                        }
                        return new NativeObject(cx);
                    }
                case 2:
                    return ScriptRuntime.defaultObjectToString(thisObj);
                case 3:
                    Object toString = getProperty(thisObj, "toString", cx);
                    if (toString instanceof Callable fun) {
                        return fun.call(cx, scope, thisObj, ScriptRuntime.EMPTY_OBJECTS);
                    }
                    throw ScriptRuntime.notFunctionError(cx, toString);
                case 4:
                    if (thisObj != null && !Undefined.isUndefined(thisObj)) {
                        return thisObj;
                    }
                    throw ScriptRuntime.typeError0(cx, "msg." + (thisObj == null ? "null" : "undef") + ".to.object");
                case 5:
                    if (thisObj != null && !Undefined.isUndefined(thisObj)) {
                        Object arg = args.length < 1 ? Undefined.instance : args[0];
                        boolean result;
                        if (arg instanceof Symbol) {
                            result = ensureSymbolScriptable(thisObj, cx).has(cx, (Symbol) arg, thisObj);
                        } else {
                            ScriptRuntime.StringIdOrIndex s = ScriptRuntime.toStringIdOrIndex(cx, arg);
                            if (s.stringId == null) {
                                result = thisObj.has(cx, s.index, thisObj);
                            } else {
                                result = thisObj.has(cx, s.stringId, thisObj);
                            }
                        }
                        return result;
                    }
                    throw ScriptRuntime.typeError0(cx, "msg." + (thisObj == null ? "null" : "undef") + ".to.object");
                case 6:
                    if (thisObj != null && !Undefined.isUndefined(thisObj)) {
                        Object argx = args.length < 1 ? Undefined.instance : args[0];
                        boolean result;
                        if (argx instanceof Symbol) {
                            result = ((SymbolScriptable) thisObj).has(cx, (Symbol) argx, thisObj);
                            if (result && thisObj instanceof ScriptableObject so) {
                                int attrs = so.getAttributes(cx, (Symbol) argx);
                                result = (attrs & 2) == 0;
                            }
                        } else {
                            ScriptRuntime.StringIdOrIndex sx = ScriptRuntime.toStringIdOrIndex(cx, argx);
                            try {
                                if (sx.stringId == null) {
                                    result = thisObj.has(cx, sx.index, thisObj);
                                    if (result && thisObj instanceof ScriptableObject so) {
                                        int attrs = so.getAttributes(cx, sx.index);
                                        result = (attrs & 2) == 0;
                                    }
                                } else {
                                    result = thisObj.has(cx, sx.stringId, thisObj);
                                    if (result && thisObj instanceof ScriptableObject so) {
                                        int attrs = so.getAttributes(cx, sx.stringId);
                                        result = (attrs & 2) == 0;
                                    }
                                }
                            } catch (EvaluatorException var17) {
                                if (!var17.getMessage().startsWith(ScriptRuntime.getMessage1("msg.prop.not.found", sx.stringId == null ? Integer.toString(sx.index) : sx.stringId))) {
                                    throw var17;
                                }
                                result = false;
                            }
                        }
                        return result;
                    }
                    throw ScriptRuntime.typeError0(cx, "msg." + (thisObj == null ? "null" : "undef") + ".to.object");
                case 7:
                    if (thisObj != null && !Undefined.isUndefined(thisObj)) {
                        boolean result = false;
                        if (args.length != 0 && args[0] instanceof Scriptable vx) {
                            do {
                                vx = vx.getPrototype(cx);
                                if (vx == thisObj) {
                                    result = true;
                                    break;
                                }
                            } while (vx != null);
                        }
                        return result;
                    }
                    throw ScriptRuntime.typeError0(cx, "msg." + (thisObj == null ? "null" : "undef") + ".to.object");
                case 8:
                    return ScriptRuntime.defaultObjectToSource(cx, scope, thisObj, args);
                case 9:
                case 10:
                    if (args.length >= 2 && args[1] instanceof Callable getterOrSetter) {
                        if (thisObj instanceof ScriptableObject so) {
                            ScriptRuntime.StringIdOrIndex s = ScriptRuntime.toStringIdOrIndex(cx, args[0]);
                            int index = s.stringId != null ? 0 : s.index;
                            boolean isSetter = id == 10;
                            so.setGetterOrSetter(cx, s.stringId, index, getterOrSetter, isSetter);
                            if (so instanceof NativeArray) {
                                ((NativeArray) so).setDenseOnly(false);
                            }
                            return Undefined.instance;
                        }
                        throw Context.reportRuntimeError2("msg.extend.scriptable", thisObj == null ? "null" : thisObj.getClass().getName(), String.valueOf(args[0]), cx);
                    }
                    Object badArg = args.length >= 2 ? args[1] : Undefined.instance;
                    throw ScriptRuntime.notFunctionError(cx, badArg);
                case 11:
                case 12:
                    if (args.length >= 1 && thisObj instanceof ScriptableObject a1x) {
                        ScriptRuntime.StringIdOrIndex sx = ScriptRuntime.toStringIdOrIndex(cx, args[0]);
                        int index = sx.stringId != null ? 0 : sx.index;
                        boolean isSetter = id == 12;
                        Object gs;
                        Scriptable v;
                        do {
                            gs = a1x.getGetterOrSetter(sx.stringId, index, isSetter);
                            if (gs != null) {
                                break;
                            }
                            v = a1x.getPrototype(cx);
                        } while (v != null && v instanceof ScriptableObject a1x);
                        if (gs != null) {
                            return gs;
                        }
                        return Undefined.instance;
                    }
                    return Undefined.instance;
            }
        }
    }

    public boolean containsKey(Object key) {
        if (key instanceof String) {
            return this.has(this.localContext, (String) key, this);
        } else {
            return key instanceof Number ? this.has(this.localContext, ((Number) key).intValue(), this) : false;
        }
    }

    public boolean containsValue(Object value) {
        for (Object obj : this.values()) {
            if (Objects.equals(value, obj)) {
                return true;
            }
        }
        return false;
    }

    public Object remove(Object key) {
        Object value = this.get(key);
        if (key instanceof String) {
            this.delete(this.localContext, (String) key);
        } else if (key instanceof Number) {
            this.delete(this.localContext, ((Number) key).intValue());
        }
        return value;
    }

    public Set<Object> keySet() {
        return new NativeObject.KeySet();
    }

    public Collection<Object> values() {
        return new NativeObject.ValueCollection();
    }

    public Set<Entry<Object, Object>> entrySet() {
        return new NativeObject.EntrySet();
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        return this.get(this.localContext, key);
    }

    public void putAll(Map m) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected int findPrototypeId(String s) {
        return switch(s) {
            case "constructor" ->
                1;
            case "toString" ->
                2;
            case "toLocaleString" ->
                3;
            case "valueOf" ->
                4;
            case "hasOwnProperty" ->
                5;
            case "propertyIsEnumerable" ->
                6;
            case "isPrototypeOf" ->
                7;
            case "toSource" ->
                8;
            case "__defineGetter__" ->
                9;
            case "__defineSetter__" ->
                10;
            case "__lookupGetter__" ->
                11;
            case "__lookupSetter__" ->
                12;
            default ->
                0;
        };
    }

    @Override
    public <T> T createDataObject(Supplier<T> instanceFactory, Context cx) {
        T inst = (T) instanceFactory.get();
        try {
            for (Field field : inst.getClass().getFields()) {
                if (Modifier.isPublic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers()) && this.has(cx, field.getName(), this)) {
                    field.setAccessible(true);
                    field.set(inst, this.get(cx, field.getName(), this));
                }
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }
        return inst;
    }

    @Override
    public <T> List<T> createDataObjectList(Supplier<T> instanceFactory, Context cx) {
        return Collections.singletonList(this.createDataObject(instanceFactory, cx));
    }

    @Override
    public boolean isDataObjectList() {
        return false;
    }

    class EntrySet extends AbstractSet<Entry<Object, Object>> {

        public Iterator<Entry<Object, Object>> iterator() {
            return new Iterator<Entry<Object, Object>>() {

                final Object[] ids = NativeObject.this.getIds(NativeObject.this.localContext);

                Object key = null;

                int index = 0;

                public boolean hasNext() {
                    return this.index < this.ids.length;
                }

                public Entry<Object, Object> next() {
                    final Object ekey = this.key = this.ids[this.index++];
                    final Object value = NativeObject.this.get(this.key);
                    return new Entry<Object, Object>() {

                        public Object getKey() {
                            return ekey;
                        }

                        public Object getValue() {
                            return value;
                        }

                        public Object setValue(Object value) {
                            throw new UnsupportedOperationException();
                        }

                        public boolean equals(Object other) {
                            return !(other instanceof Entry<?, ?> e) ? false : (ekey == null ? e.getKey() == null : ekey.equals(e.getKey())) && (value == null ? e.getValue() == null : value.equals(e.getValue()));
                        }

                        public int hashCode() {
                            return (ekey == null ? 0 : ekey.hashCode()) ^ (value == null ? 0 : value.hashCode());
                        }

                        public String toString() {
                            return ekey + "=" + value;
                        }
                    };
                }

                public void remove() {
                    if (this.key == null) {
                        throw new IllegalStateException();
                    } else {
                        NativeObject.this.remove(this.key);
                        this.key = null;
                    }
                }
            };
        }

        public int size() {
            return NativeObject.this.size();
        }
    }

    class KeySet extends AbstractSet<Object> {

        public boolean contains(Object key) {
            return NativeObject.this.containsKey(key);
        }

        public Iterator<Object> iterator() {
            return new Iterator<Object>() {

                final Object[] ids = NativeObject.this.getIds(NativeObject.this.localContext);

                Object key;

                int index = 0;

                public boolean hasNext() {
                    return this.index < this.ids.length;
                }

                public Object next() {
                    try {
                        return this.key = this.ids[this.index++];
                    } catch (ArrayIndexOutOfBoundsException var2) {
                        this.key = null;
                        throw new NoSuchElementException();
                    }
                }

                public void remove() {
                    if (this.key == null) {
                        throw new IllegalStateException();
                    } else {
                        NativeObject.this.remove(this.key);
                        this.key = null;
                    }
                }
            };
        }

        public int size() {
            return NativeObject.this.size();
        }
    }

    class ValueCollection extends AbstractCollection<Object> {

        public Iterator<Object> iterator() {
            return new Iterator<Object>() {

                final Object[] ids = NativeObject.this.getIds(NativeObject.this.localContext);

                Object key;

                int index = 0;

                public boolean hasNext() {
                    return this.index < this.ids.length;
                }

                public Object next() {
                    return NativeObject.this.get(this.key = this.ids[this.index++]);
                }

                public void remove() {
                    if (this.key == null) {
                        throw new IllegalStateException();
                    } else {
                        NativeObject.this.remove(this.key);
                        this.key = null;
                    }
                }
            };
        }

        public int size() {
            return NativeObject.this.size();
        }
    }
}