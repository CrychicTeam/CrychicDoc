package dev.latvian.mods.rhino;

public class NativeWith implements Scriptable, SymbolScriptable, IdFunctionCall {

    private static final Object FTAG = "With";

    private static final int Id_constructor = 1;

    protected Scriptable prototype;

    protected Scriptable parent;

    static void init(Scriptable scope, boolean sealed, Context cx) {
        NativeWith obj = new NativeWith();
        obj.setParentScope(scope);
        obj.setPrototype(ScriptableObject.getObjectPrototype(scope, cx));
        IdFunctionObject ctor = new IdFunctionObject(obj, FTAG, 1, "With", 0, scope);
        ctor.markAsConstructor(obj);
        if (sealed) {
            ctor.sealObject(cx);
        }
        ctor.exportAsScopeProperty(cx);
    }

    static boolean isWithFunction(Object functionObj) {
        return !(functionObj instanceof IdFunctionObject f) ? false : f.hasTag(FTAG) && f.methodId() == 1;
    }

    static Object newWithSpecial(Context cx, Scriptable scope, Object[] args) {
        ScriptRuntime.checkDeprecated(cx, "With");
        scope = ScriptableObject.getTopLevelScope(scope);
        NativeWith thisObj = new NativeWith();
        thisObj.setPrototype(args.length == 0 ? ScriptableObject.getObjectPrototype(scope, cx) : ScriptRuntime.toObject(cx, scope, args[0]));
        thisObj.setParentScope(scope);
        return thisObj;
    }

    private NativeWith() {
    }

    protected NativeWith(Scriptable parent, Scriptable prototype) {
        this.parent = parent;
        this.prototype = prototype;
    }

    @Override
    public String getClassName() {
        return "With";
    }

    @Override
    public boolean has(Context cx, String id, Scriptable start) {
        return this.prototype.has(cx, id, this.prototype);
    }

    @Override
    public boolean has(Context cx, Symbol key, Scriptable start) {
        return this.prototype instanceof SymbolScriptable ? ((SymbolScriptable) this.prototype).has(cx, key, this.prototype) : false;
    }

    @Override
    public boolean has(Context cx, int index, Scriptable start) {
        return this.prototype.has(cx, index, this.prototype);
    }

    @Override
    public Object get(Context cx, String id, Scriptable start) {
        if (start == this) {
            start = this.prototype;
        }
        return this.prototype.get(cx, id, start);
    }

    @Override
    public Object get(Context cx, Symbol key, Scriptable start) {
        if (start == this) {
            start = this.prototype;
        }
        return this.prototype instanceof SymbolScriptable ? ((SymbolScriptable) this.prototype).get(cx, key, start) : Scriptable.NOT_FOUND;
    }

    @Override
    public Object get(Context cx, int index, Scriptable start) {
        if (start == this) {
            start = this.prototype;
        }
        return this.prototype.get(cx, index, start);
    }

    @Override
    public void put(Context cx, String id, Scriptable start, Object value) {
        if (start == this) {
            start = this.prototype;
        }
        this.prototype.put(cx, id, start, value);
    }

    @Override
    public void put(Context cx, Symbol symbol, Scriptable start, Object value) {
        if (start == this) {
            start = this.prototype;
        }
        if (this.prototype instanceof SymbolScriptable) {
            ((SymbolScriptable) this.prototype).put(cx, symbol, start, value);
        }
    }

    @Override
    public void put(Context cx, int index, Scriptable start, Object value) {
        if (start == this) {
            start = this.prototype;
        }
        this.prototype.put(cx, index, start, value);
    }

    @Override
    public void delete(Context cx, String id) {
        this.prototype.delete(cx, id);
    }

    @Override
    public void delete(Context cx, Symbol key) {
        if (this.prototype instanceof SymbolScriptable) {
            ((SymbolScriptable) this.prototype).delete(cx, key);
        }
    }

    @Override
    public void delete(Context cx, int index) {
        this.prototype.delete(cx, index);
    }

    @Override
    public Scriptable getPrototype(Context cx) {
        return this.prototype;
    }

    @Override
    public void setPrototype(Scriptable prototype) {
        this.prototype = prototype;
    }

    @Override
    public Scriptable getParentScope() {
        return this.parent;
    }

    @Override
    public void setParentScope(Scriptable parent) {
        this.parent = parent;
    }

    @Override
    public Object[] getIds(Context cx) {
        return this.prototype.getIds(cx);
    }

    @Override
    public Object getDefaultValue(Context cx, Class<?> typeHint) {
        return this.prototype.getDefaultValue(cx, typeHint);
    }

    @Override
    public boolean hasInstance(Context cx, Scriptable value) {
        return this.prototype.hasInstance(cx, value);
    }

    protected Object updateDotQuery(boolean value) {
        throw new IllegalStateException();
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (f.hasTag(FTAG) && f.methodId() == 1) {
            throw Context.reportRuntimeError1("msg.cant.call.indirect", "With", cx);
        } else {
            throw f.unknown();
        }
    }
}