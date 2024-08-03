package dev.latvian.mods.rhino;

public class ArrowFunction extends BaseFunction {

    private final Callable targetFunction;

    private final Scriptable boundThis;

    static boolean equalObjectGraphs(Context cx, ArrowFunction f1, ArrowFunction f2, EqualObjectGraphs eq) {
        return eq.equalGraphs(cx, f1.boundThis, f2.boundThis) && eq.equalGraphs(cx, f1.targetFunction, f2.targetFunction);
    }

    public ArrowFunction(Context cx, Scriptable scope, Callable targetFunction, Scriptable boundThis) {
        this.targetFunction = targetFunction;
        this.boundThis = boundThis;
        ScriptRuntime.setFunctionProtoAndParent(cx, scope, this);
        Function thrower = ScriptRuntime.typeErrorThrower(cx);
        NativeObject throwing = new NativeObject(cx);
        throwing.put(cx, "get", throwing, thrower);
        throwing.put(cx, "set", throwing, thrower);
        throwing.put(cx, "enumerable", throwing, Boolean.FALSE);
        throwing.put(cx, "configurable", throwing, Boolean.FALSE);
        throwing.preventExtensions();
        this.defineOwnProperty(cx, "caller", throwing, false);
        this.defineOwnProperty(cx, "arguments", throwing, false);
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        Scriptable callThis = this.boundThis != null ? this.boundThis : cx.getTopCallOrThrow();
        return cx.callSync(this.targetFunction, scope, callThis, args);
    }

    @Override
    public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
        throw ScriptRuntime.typeError1(cx, "msg.not.ctor", this.toString());
    }

    @Override
    public boolean hasInstance(Context cx, Scriptable instance) {
        if (this.targetFunction instanceof Function) {
            return ((Function) this.targetFunction).hasInstance(cx, instance);
        } else {
            throw ScriptRuntime.typeError0(cx, "msg.not.ctor");
        }
    }

    @Override
    public int getLength() {
        return this.targetFunction instanceof BaseFunction ? ((BaseFunction) this.targetFunction).getLength() : 0;
    }

    @Override
    public int getArity() {
        return this.getLength();
    }

    @Override
    public String toString() {
        return this.targetFunction instanceof BaseFunction ? "ArrowFunction (" + ((BaseFunction) this.targetFunction).getLength() + ") => {...}" : "ArrowFunction";
    }
}