package dev.latvian.mods.rhino;

import java.util.Objects;

public class IdFunctionObject extends BaseFunction {

    private final IdFunctionCall idcall;

    private final Object tag;

    private final int methodId;

    private final int arity;

    private boolean useCallAsConstructor;

    private String functionName;

    static boolean equalObjectGraphs(Context cx, IdFunctionObject f1, IdFunctionObject f2, EqualObjectGraphs eq) {
        return f1.methodId == f2.methodId && f1.hasTag(f2.tag) && eq.equalGraphs(cx, f1.idcall, f2.idcall);
    }

    public IdFunctionObject(IdFunctionCall idcall, Object tag, int id, int arity) {
        if (arity < 0) {
            throw new IllegalArgumentException();
        } else {
            this.idcall = idcall;
            this.tag = tag;
            this.methodId = id;
            this.arity = arity;
        }
    }

    public IdFunctionObject(IdFunctionCall idcall, Object tag, int id, String name, int arity, Scriptable scope) {
        super(scope, null);
        if (arity < 0) {
            throw new IllegalArgumentException();
        } else if (name == null) {
            throw new IllegalArgumentException();
        } else {
            this.idcall = idcall;
            this.tag = tag;
            this.methodId = id;
            this.arity = arity;
            this.functionName = name;
        }
    }

    public void initFunction(String name, Scriptable scope) {
        if (name == null) {
            throw new IllegalArgumentException();
        } else if (scope == null) {
            throw new IllegalArgumentException();
        } else {
            this.functionName = name;
            this.setParentScope(scope);
        }
    }

    public final boolean hasTag(Object tag) {
        return Objects.equals(tag, this.tag);
    }

    public Object getTag() {
        return this.tag;
    }

    public final int methodId() {
        return this.methodId;
    }

    public final void markAsConstructor(Scriptable prototypeProperty) {
        this.useCallAsConstructor = true;
        this.setImmunePrototypeProperty(prototypeProperty);
    }

    public final void addAsProperty(Scriptable target, Context cx) {
        defineProperty(target, this.functionName, this, 2, cx);
    }

    public void exportAsScopeProperty(Context cx) {
        this.addAsProperty(this.getParentScope(), cx);
    }

    @Override
    public Scriptable getPrototype(Context cx) {
        Scriptable proto = super.getPrototype(cx);
        if (proto == null) {
            proto = getFunctionPrototype(this.getParentScope(), cx);
            this.setPrototype(proto);
        }
        return proto;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return this.idcall.execIdCall(this, cx, scope, thisObj, args);
    }

    @Override
    public Scriptable createObject(Context cx, Scriptable scope) {
        if (this.useCallAsConstructor) {
            return null;
        } else {
            throw ScriptRuntime.typeError1(cx, "msg.not.ctor", this.functionName);
        }
    }

    @Override
    public int getArity() {
        return this.arity;
    }

    @Override
    public int getLength() {
        return this.getArity();
    }

    @Override
    public String getFunctionName() {
        return this.functionName == null ? "" : this.functionName;
    }

    public final RuntimeException unknown() {
        return new IllegalArgumentException("BAD FUNCTION ID=" + this.methodId + " MASTER=" + this.idcall);
    }
}