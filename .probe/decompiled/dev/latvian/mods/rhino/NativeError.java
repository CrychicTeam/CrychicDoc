package dev.latvian.mods.rhino;

final class NativeError extends IdScriptableObject {

    public static final int DEFAULT_STACK_LIMIT = -1;

    private static final Object ERROR_TAG = "Error";

    private static final WrappedExecutable ERROR_DELEGATE_GET_STACK = (cx, scope, self, args) -> ((NativeError) self).getStackDelegated(cx, scope);

    private static final WrappedExecutable ERROR_DELEGATE_SET_STACK = (cx, scope, self, args) -> ((NativeError) self).setStackDelegated(cx, scope, args[0]);

    private static final String STACK_HIDE_KEY = "_stackHide";

    private static final int Id_constructor = 1;

    private static final int Id_toString = 2;

    private static final int Id_toSource = 3;

    private static final int ConstructorId_captureStackTrace = -1;

    private static final int MAX_PROTOTYPE_ID = 3;

    private RhinoException stackProvider;

    private final Context localContext;

    static void init(Scriptable scope, boolean sealed, Context cx) {
        NativeError obj = new NativeError(cx);
        putProperty(obj, "name", "Error", cx);
        putProperty(obj, "message", "", cx);
        putProperty(obj, "fileName", "", cx);
        putProperty(obj, "lineNumber", Integer.valueOf(0), cx);
        obj.setAttributes(cx, "name", 2);
        obj.setAttributes(cx, "message", 2);
        obj.exportAsJSClass(3, scope, sealed, cx);
        NativeCallSite.init(obj, sealed, cx);
    }

    static NativeError make(Context cx, Scriptable scope, IdFunctionObject ctorObj, Object[] args) {
        Scriptable proto = (Scriptable) ctorObj.get(cx, "prototype", ctorObj);
        NativeError obj = new NativeError(cx);
        obj.setPrototype(proto);
        obj.setParentScope(scope);
        int arglen = args.length;
        if (arglen >= 1) {
            if (args[0] != Undefined.instance) {
                putProperty(obj, "message", ScriptRuntime.toString(cx, args[0]), cx);
            }
            if (arglen >= 2) {
                putProperty(obj, "fileName", args[1], cx);
                if (arglen >= 3) {
                    int line = ScriptRuntime.toInt32(cx, args[2]);
                    putProperty(obj, "lineNumber", Integer.valueOf(line), cx);
                }
            }
        }
        return obj;
    }

    private static Object js_toString(Context cx, Scriptable thisObj) {
        Object name = getProperty(thisObj, "name", cx);
        if (name != NOT_FOUND && name != Undefined.instance) {
            name = ScriptRuntime.toString(cx, name);
        } else {
            name = "Error";
        }
        Object msg = getProperty(thisObj, "message", cx);
        if (msg != NOT_FOUND && msg != Undefined.instance) {
            msg = ScriptRuntime.toString(cx, msg);
        } else {
            msg = "";
        }
        if (name.toString().length() == 0) {
            return msg;
        } else {
            return msg.toString().length() == 0 ? name : name + ": " + msg;
        }
    }

    private static void js_captureStackTrace(Context cx, Scriptable thisObj, Object[] args) {
        ScriptableObject obj = (ScriptableObject) ScriptRuntime.toObjectOrNull(cx, args[0], thisObj);
        Function func = null;
        if (args.length > 1) {
            func = (Function) ScriptRuntime.toObjectOrNull(cx, args[1], thisObj);
        }
        NativeError err = (NativeError) cx.newObject(thisObj, "Error");
        err.setStackProvider(new EvaluatorException(cx, "[object Object]"), cx);
        if (func != null) {
            Object funcName = func.get(cx, "name", func);
            if (funcName != null && !Undefined.instance.equals(funcName)) {
                err.associateValue("_stackHide", ScriptRuntime.toString(cx, funcName));
            }
        }
        obj.defineProperty(cx, "stack", err, ERROR_DELEGATE_GET_STACK, ERROR_DELEGATE_SET_STACK, 0);
    }

    public NativeError(Context cx) {
        this.localContext = cx;
    }

    @Override
    protected void fillConstructorProperties(IdFunctionObject ctor, Context cx) {
        this.addIdFunctionProperty(ctor, ERROR_TAG, -1, "captureStackTrace", 2, cx);
        NativeError.ProtoProps protoProps = new NativeError.ProtoProps();
        this.associateValue("_ErrorPrototypeProps", protoProps);
        ctor.defineProperty(cx, "stackTraceLimit", protoProps, NativeError.ProtoProps.GET_STACK_LIMIT, NativeError.ProtoProps.SET_STACK_LIMIT, 0);
        ctor.defineProperty(cx, "prepareStackTrace", protoProps, NativeError.ProtoProps.GET_PREPARE_STACK, NativeError.ProtoProps.SET_PREPARE_STACK, 0);
        super.fillConstructorProperties(ctor, cx);
    }

    @Override
    public String getClassName() {
        return "Error";
    }

    public String toString() {
        Object toString = js_toString(this.localContext, this);
        return toString instanceof String ? (String) toString : super.toString();
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
                s = "toSource";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        this.initPrototypeMethod(ERROR_TAG, id, s, arity, cx);
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(ERROR_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            switch(id) {
                case -1:
                    js_captureStackTrace(cx, thisObj, args);
                    return Undefined.instance;
                case 0:
                default:
                    throw new IllegalArgumentException(String.valueOf(id));
                case 1:
                    return make(cx, scope, f, args);
                case 2:
                    return js_toString(cx, thisObj);
                case 3:
                    return "not_supported";
            }
        }
    }

    public void setStackProvider(RhinoException re, Context cx) {
        if (this.stackProvider == null) {
            this.stackProvider = re;
            this.defineProperty(cx, "stack", this, ERROR_DELEGATE_GET_STACK, ERROR_DELEGATE_SET_STACK, 2);
        }
    }

    public Object getStackDelegated(Context cx, Scriptable target) {
        if (this.stackProvider == null) {
            return NOT_FOUND;
        } else {
            int limit = -1;
            Function prepare = null;
            NativeError cons = (NativeError) this.getPrototype(cx);
            NativeError.ProtoProps pp = (NativeError.ProtoProps) cons.getAssociatedValue("_ErrorPrototypeProps");
            if (pp != null) {
                limit = pp.getStackTraceLimit();
                prepare = pp.getPrepareStackTrace();
            }
            String hideFunc = (String) this.getAssociatedValue("_stackHide");
            ScriptStackElement[] stack = this.stackProvider.getScriptStack(limit, hideFunc);
            Object value;
            if (prepare == null) {
                value = RhinoException.formatStackTrace(stack, this.stackProvider.details());
            } else {
                value = this.callPrepareStack(prepare, stack, cx);
            }
            this.setStackDelegated(cx, target, value);
            return value;
        }
    }

    public Object setStackDelegated(Context cx, Scriptable target, Object value) {
        target.delete(cx, "stack");
        this.stackProvider = null;
        target.put(cx, "stack", target, value);
        return null;
    }

    private Object callPrepareStack(Function prepare, ScriptStackElement[] stack, Context cx) {
        Object[] elts = new Object[stack.length];
        for (int i = 0; i < stack.length; i++) {
            NativeCallSite site = (NativeCallSite) cx.newObject(this, "CallSite");
            site.setElement(stack[i]);
            elts[i] = site;
        }
        Scriptable eltArray = cx.newArray(this, elts);
        return prepare.call(cx, prepare, this, new Object[] { this, eltArray });
    }

    @Override
    protected int findPrototypeId(String s) {
        return switch(s) {
            case "constructor" ->
                1;
            case "toString" ->
                2;
            case "toSource" ->
                3;
            default ->
                0;
        };
    }

    private static final class ProtoProps {

        static final String KEY = "_ErrorPrototypeProps";

        static final WrappedExecutable GET_STACK_LIMIT = (cx, scope, self, args) -> ((NativeError.ProtoProps) self).getStackTraceLimit(cx);

        static final WrappedExecutable SET_STACK_LIMIT = (cx, scope, self, args) -> ((NativeError.ProtoProps) self).setStackTraceLimit(cx, args[0]);

        static final WrappedExecutable GET_PREPARE_STACK = (cx, scope, self, args) -> ((NativeError.ProtoProps) self).getPrepareStackTrace(cx);

        static final WrappedExecutable SET_PREPARE_STACK = (cx, scope, self, args) -> ((NativeError.ProtoProps) self).setPrepareStackTrace(cx, args[0]);

        private int stackTraceLimit = -1;

        private Function prepareStackTrace;

        public Object getStackTraceLimit(Context cx) {
            return this.stackTraceLimit >= 0 ? this.stackTraceLimit : Double.POSITIVE_INFINITY;
        }

        public int getStackTraceLimit() {
            return this.stackTraceLimit;
        }

        public Object setStackTraceLimit(Context cx, Object value) {
            double limit = ScriptRuntime.toNumber(cx, value);
            if (!Double.isNaN(limit) && !Double.isInfinite(limit)) {
                this.stackTraceLimit = (int) limit;
            } else {
                this.stackTraceLimit = -1;
            }
            return null;
        }

        public Object getPrepareStackTrace(Context cx) {
            Object ps = this.getPrepareStackTrace();
            return ps == null ? Undefined.instance : ps;
        }

        public Function getPrepareStackTrace() {
            return this.prepareStackTrace;
        }

        public Object setPrepareStackTrace(Context cx, Object value) {
            if (value == null || Undefined.instance.equals(value)) {
                this.prepareStackTrace = null;
            } else if (value instanceof Function) {
                this.prepareStackTrace = (Function) value;
            }
            return null;
        }
    }
}