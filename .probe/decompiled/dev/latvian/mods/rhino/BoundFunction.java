package dev.latvian.mods.rhino;

public class BoundFunction extends BaseFunction {

    private final Callable targetFunction;

    private final Scriptable boundThis;

    private final Object[] boundArgs;

    private final int length;

    private static Object[] concat(Object[] first, Object[] second) {
        Object[] args = new Object[first.length + second.length];
        System.arraycopy(first, 0, args, 0, first.length);
        System.arraycopy(second, 0, args, first.length, second.length);
        return args;
    }

    static boolean equalObjectGraphs(Context cx, BoundFunction f1, BoundFunction f2, EqualObjectGraphs eq) {
        return eq.equalGraphs(cx, f1.boundThis, f2.boundThis) && eq.equalGraphs(cx, f1.targetFunction, f2.targetFunction) && eq.equalGraphs(cx, f1.boundArgs, f2.boundArgs);
    }

    public BoundFunction(Context cx, Scriptable scope, Callable targetFunction, Scriptable boundThis, Object[] boundArgs) {
        this.targetFunction = targetFunction;
        this.boundThis = boundThis;
        this.boundArgs = boundArgs;
        if (targetFunction instanceof BaseFunction) {
            this.length = Math.max(0, ((BaseFunction) targetFunction).getLength() - boundArgs.length);
        } else {
            this.length = 0;
        }
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
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] extraArgs) {
        Scriptable callThis = this.boundThis != null ? this.boundThis : cx.getTopCallOrThrow();
        return this.targetFunction.call(cx, scope, callThis, concat(this.boundArgs, extraArgs));
    }

    @Override
    public Scriptable construct(Context cx, Scriptable scope, Object[] extraArgs) {
        if (this.targetFunction instanceof Function) {
            return ((Function) this.targetFunction).construct(cx, scope, concat(this.boundArgs, extraArgs));
        } else {
            throw ScriptRuntime.typeError0(cx, "msg.not.ctor");
        }
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
        return this.length;
    }
}