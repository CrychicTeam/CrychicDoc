package dev.latvian.mods.rhino;

public final class NativeCall extends IdScriptableObject {

    private static final Object CALL_TAG = "Call";

    private static final int Id_constructor = 1;

    private static final int MAX_PROTOTYPE_ID = 1;

    NativeFunction function;

    Object[] originalArgs;

    boolean isStrict;

    transient NativeCall parentActivationCall;

    private Arguments arguments;

    static void init(Scriptable scope, boolean sealed, Context cx) {
        NativeCall obj = new NativeCall();
        obj.exportAsJSClass(1, scope, sealed, cx);
    }

    NativeCall() {
    }

    NativeCall(NativeFunction function, Scriptable scope, Object[] args, boolean isArrow, boolean isStrict, Context cx) {
        this.function = function;
        this.setParentScope(scope);
        this.originalArgs = args == null ? ScriptRuntime.EMPTY_OBJECTS : args;
        this.isStrict = isStrict;
        int paramAndVarCount = function.getParamAndVarCount();
        int paramCount = function.getParamCount();
        if (paramAndVarCount != 0) {
            for (int i = 0; i < paramCount; i++) {
                String name = function.getParamOrVarName(i);
                Object val = i < args.length ? args[i] : Undefined.instance;
                this.defineProperty(cx, name, val, 4);
            }
        }
        if (!super.has(cx, "arguments", this) && !isArrow) {
            this.arguments = new Arguments(this, cx);
            this.defineProperty(cx, "arguments", this.arguments, 4);
        }
        if (paramAndVarCount != 0) {
            for (int i = paramCount; i < paramAndVarCount; i++) {
                String name = function.getParamOrVarName(i);
                if (!super.has(cx, name, this)) {
                    if (function.getParamOrVarConst(i)) {
                        this.defineProperty(cx, name, Undefined.instance, 13);
                    } else if (!(function instanceof InterpretedFunction) || ((InterpretedFunction) function).hasFunctionNamed(name)) {
                        this.defineProperty(cx, name, Undefined.instance, 4);
                    }
                }
            }
        }
    }

    @Override
    public String getClassName() {
        return "Call";
    }

    @Override
    protected int findPrototypeId(String s) {
        return s.equals("constructor") ? 1 : 0;
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        if (id == 1) {
            int arity = 1;
            String s = "constructor";
            this.initPrototypeMethod(CALL_TAG, id, s, arity, cx);
        } else {
            throw new IllegalArgumentException(String.valueOf(id));
        }
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(CALL_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            if (id == 1) {
                if (thisObj != null) {
                    throw Context.reportRuntimeError1("msg.only.from.new", "Call", cx);
                } else {
                    ScriptRuntime.checkDeprecated(cx, "Call");
                    NativeCall result = new NativeCall();
                    result.setPrototype(getObjectPrototype(scope, cx));
                    return result;
                }
            } else {
                throw new IllegalArgumentException(String.valueOf(id));
            }
        }
    }

    public void defineAttributesForArguments(Context cx) {
        if (this.arguments != null) {
            this.arguments.defineAttributesForStrictMode(cx);
        }
    }
}