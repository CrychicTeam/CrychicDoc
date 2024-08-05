package dev.latvian.mods.rhino;

public class BaseFunction extends IdScriptableObject implements Function {

    static final String GENERATOR_FUNCTION_CLASS = "__GeneratorFunction";

    private static final Object FUNCTION_TAG = "Function";

    private static final String FUNCTION_CLASS = "Function";

    private static final int Id_length = 1;

    private static final int Id_arity = 2;

    private static final int Id_name = 3;

    private static final int Id_prototype = 4;

    private static final int Id_arguments = 5;

    private static final int MAX_INSTANCE_ID = 5;

    private static final int Id_constructor = 1;

    private static final int Id_toString = 2;

    private static final int Id_toSource = 3;

    private static final int Id_apply = 4;

    private static final int Id_call = 5;

    private static final int Id_bind = 6;

    private static final int MAX_PROTOTYPE_ID = 6;

    private Object prototypeProperty;

    private Object argumentsObj = NOT_FOUND;

    private boolean isGeneratorFunction = false;

    private int prototypePropertyAttributes = 6;

    private int argumentsAttributes = 6;

    static void init(Scriptable scope, boolean sealed, Context cx) {
        BaseFunction obj = new BaseFunction();
        obj.prototypePropertyAttributes = 7;
        obj.exportAsJSClass(6, scope, sealed, cx);
    }

    static Object initAsGeneratorFunction(Scriptable scope, boolean sealed, Context cx) {
        BaseFunction obj = new BaseFunction(true);
        obj.prototypePropertyAttributes = 5;
        obj.exportAsJSClass(6, scope, sealed, cx);
        return getProperty(scope, "__GeneratorFunction", cx);
    }

    static boolean isApply(IdFunctionObject f) {
        return f.hasTag(FUNCTION_TAG) && f.methodId() == 4;
    }

    static boolean isApplyOrCall(IdFunctionObject f) {
        if (f.hasTag(FUNCTION_TAG)) {
            switch(f.methodId()) {
                case 4:
                case 5:
                    return true;
            }
        }
        return false;
    }

    public BaseFunction() {
    }

    public BaseFunction(boolean isGenerator) {
        this.isGeneratorFunction = isGenerator;
    }

    public BaseFunction(Scriptable scope, Scriptable prototype) {
        super(scope, prototype);
    }

    @Override
    public String getClassName() {
        return this.isGeneratorFunction() ? "__GeneratorFunction" : "Function";
    }

    protected boolean isGeneratorFunction() {
        return this.isGeneratorFunction;
    }

    @Override
    public MemberType getTypeOf() {
        return this.avoidObjectDetection() ? MemberType.UNDEFINED : MemberType.FUNCTION;
    }

    @Override
    public boolean hasInstance(Context cx, Scriptable instance) {
        Object protoProp = getProperty(this, "prototype", cx);
        if (protoProp instanceof Scriptable) {
            return ScriptRuntime.jsDelegatesTo(cx, instance, (Scriptable) protoProp);
        } else {
            throw ScriptRuntime.typeError1(cx, "msg.instanceof.bad.prototype", this.getFunctionName());
        }
    }

    @Override
    protected int getMaxInstanceId() {
        return 5;
    }

    @Override
    protected int findInstanceIdInfo(String s, Context cx) {
        int id = switch(s) {
            case "name" ->
                3;
            case "length" ->
                1;
            case "arity" ->
                2;
            case "prototype" ->
                4;
            case "arguments" ->
                5;
            default ->
                0;
        };
        if (id == 0) {
            return super.findInstanceIdInfo(s, cx);
        } else {
            int attr;
            switch(id) {
                case 1:
                case 2:
                case 3:
                    attr = 7;
                    break;
                case 4:
                    if (!this.hasPrototypeProperty()) {
                        return 0;
                    }
                    attr = this.prototypePropertyAttributes;
                    break;
                case 5:
                    attr = this.argumentsAttributes;
                    break;
                default:
                    throw new IllegalStateException();
            }
            return instanceIdInfo(attr, id);
        }
    }

    @Override
    protected String getInstanceIdName(int id) {
        return switch(id) {
            case 1 ->
                "length";
            case 2 ->
                "arity";
            case 3 ->
                "name";
            case 4 ->
                "prototype";
            case 5 ->
                "arguments";
            default ->
                super.getInstanceIdName(id);
        };
    }

    @Override
    protected Object getInstanceIdValue(int id, Context cx) {
        return switch(id) {
            case 1 ->
                this.getLength();
            case 2 ->
                this.getArity();
            case 3 ->
                this.getFunctionName();
            case 4 ->
                this.getPrototypeProperty(cx);
            case 5 ->
                this.getArguments(cx);
            default ->
                super.getInstanceIdValue(id, cx);
        };
    }

    @Override
    protected void setInstanceIdValue(int id, Object value, Context cx) {
        switch(id) {
            case 1:
            case 2:
            case 3:
                return;
            case 4:
                if ((this.prototypePropertyAttributes & 1) == 0) {
                    this.prototypeProperty = value != null ? value : UniqueTag.NULL_VALUE;
                }
                return;
            case 5:
                if (value == NOT_FOUND) {
                    Kit.codeBug();
                }
                if (this.defaultHas(cx, "arguments")) {
                    this.defaultPut(cx, "arguments", value);
                } else if ((this.argumentsAttributes & 1) == 0) {
                    this.argumentsObj = value;
                }
                return;
            default:
                super.setInstanceIdValue(id, value, cx);
        }
    }

    @Override
    protected void setInstanceIdAttributes(int id, int attr, Context cx) {
        switch(id) {
            case 4:
                this.prototypePropertyAttributes = attr;
                return;
            case 5:
                this.argumentsAttributes = attr;
                return;
            default:
                super.setInstanceIdAttributes(id, attr, cx);
        }
    }

    @Override
    protected void fillConstructorProperties(IdFunctionObject ctor, Context cx) {
        ctor.setPrototype(this);
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
                arity = 1;
                s = "toSource";
                break;
            case 4:
                arity = 2;
                s = "apply";
                break;
            case 5:
                arity = 1;
                s = "call";
                break;
            case 6:
                arity = 1;
                s = "bind";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        this.initPrototypeMethod(FUNCTION_TAG, id, s, arity, cx);
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(FUNCTION_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            switch(id) {
                case 1:
                    return this.jsConstructor(cx, scope, args);
                case 2:
                case 3:
                    return this.toFunctionString(thisObj);
                case 4:
                case 5:
                    return ScriptRuntime.applyOrCall(cx, scope, id == 4, thisObj, args);
                case 6:
                    if (thisObj instanceof Callable targetFunction) {
                        int argc = args.length;
                        Scriptable boundThis;
                        Object[] boundArgs;
                        if (argc > 0) {
                            boundThis = ScriptRuntime.toObjectOrNull(cx, args[0], scope);
                            boundArgs = new Object[argc - 1];
                            System.arraycopy(args, 1, boundArgs, 0, argc - 1);
                        } else {
                            boundThis = null;
                            boundArgs = ScriptRuntime.EMPTY_OBJECTS;
                        }
                        return new BoundFunction(cx, scope, targetFunction, boundThis, boundArgs);
                    }
                    throw ScriptRuntime.notFunctionError(cx, thisObj);
                default:
                    throw new IllegalArgumentException(String.valueOf(id));
            }
        }
    }

    public void setImmunePrototypeProperty(Object value) {
        if ((this.prototypePropertyAttributes & 1) != 0) {
            throw new IllegalStateException();
        } else {
            this.prototypeProperty = value != null ? value : UniqueTag.NULL_VALUE;
            this.prototypePropertyAttributes = 7;
        }
    }

    protected Scriptable getClassPrototype(Context cx) {
        Object protoVal = this.getPrototypeProperty(cx);
        return protoVal instanceof Scriptable ? (Scriptable) protoVal : getObjectPrototype(this, cx);
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return Undefined.instance;
    }

    @Override
    public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
        Scriptable result = this.createObject(cx, scope);
        if (result != null) {
            Object val = this.call(cx, scope, result, args);
            if (val instanceof Scriptable) {
                result = (Scriptable) val;
            }
        } else {
            Object val = this.call(cx, scope, null, args);
            if (!(val instanceof Scriptable)) {
                throw new IllegalStateException("Bad implementaion of call as constructor, name=" + this.getFunctionName() + " in " + this.getClass().getName());
            }
            result = (Scriptable) val;
            if (result.getPrototype(cx) == null) {
                Scriptable proto = this.getClassPrototype(cx);
                if (result != proto) {
                    result.setPrototype(proto);
                }
            }
            if (result.getParentScope() == null) {
                Scriptable parent = this.getParentScope();
                if (result != parent) {
                    result.setParentScope(parent);
                }
            }
        }
        return result;
    }

    public Scriptable createObject(Context cx, Scriptable scope) {
        Scriptable newInstance = new NativeObject(cx);
        newInstance.setPrototype(this.getClassPrototype(cx));
        newInstance.setParentScope(this.getParentScope());
        return newInstance;
    }

    public int getArity() {
        return 0;
    }

    public int getLength() {
        return 0;
    }

    public String getFunctionName() {
        return "";
    }

    protected String toFunctionString(Scriptable parent) {
        String s = this.getFunctionName();
        if (s.isEmpty()) {
            return parent != null ? parent.getClassName() : "Unknown";
        } else {
            return s;
        }
    }

    public String toString() {
        String s = this.getFunctionName();
        return s.isEmpty() ? "Unknown" : s;
    }

    protected boolean hasPrototypeProperty() {
        return this.prototypeProperty != null || this instanceof NativeFunction;
    }

    protected Object getPrototypeProperty(Context cx) {
        Object result = this.prototypeProperty;
        if (result == null) {
            if (this instanceof NativeFunction) {
                result = this.setupDefaultPrototype(cx);
            } else {
                result = Undefined.instance;
            }
        } else if (result == UniqueTag.NULL_VALUE) {
            result = null;
        }
        return result;
    }

    private synchronized Object setupDefaultPrototype(Context cx) {
        if (this.prototypeProperty != null) {
            return this.prototypeProperty;
        } else {
            NativeObject obj = new NativeObject(cx);
            int attr = 2;
            obj.defineProperty(cx, "constructor", this, 2);
            this.prototypeProperty = obj;
            Scriptable proto = getObjectPrototype(this, cx);
            if (proto != obj) {
                obj.setPrototype(proto);
            }
            return obj;
        }
    }

    private Object getArguments(Context cx) {
        Object value = this.defaultHas(cx, "arguments") ? this.defaultGet(cx, "arguments") : this.argumentsObj;
        if (value != NOT_FOUND) {
            return value;
        } else {
            NativeCall activation = ScriptRuntime.findFunctionActivation(cx, this);
            return activation == null ? null : activation.get(cx, "arguments", activation);
        }
    }

    private Object jsConstructor(Context cx, Scriptable scope, Object[] args) {
        int arglen = args.length;
        StringBuilder sourceBuf = new StringBuilder();
        sourceBuf.append("function ");
        if (this.isGeneratorFunction()) {
            sourceBuf.append("* ");
        }
        sourceBuf.append("anonymous");
        sourceBuf.append('(');
        for (int i = 0; i < arglen - 1; i++) {
            if (i > 0) {
                sourceBuf.append(',');
            }
            sourceBuf.append(ScriptRuntime.toString(cx, args[i]));
        }
        sourceBuf.append(") {");
        if (arglen != 0) {
            String funBody = ScriptRuntime.toString(cx, args[arglen - 1]);
            sourceBuf.append(funBody);
        }
        sourceBuf.append("\n}");
        String source = sourceBuf.toString();
        int[] linep = new int[1];
        String filename = Context.getSourcePositionFromStack(cx, linep);
        if (filename == null) {
            filename = "<eval'ed string>";
            linep[0] = 1;
        }
        String sourceURI = ScriptRuntime.makeUrlForGeneratedScript(false, filename, linep[0]);
        Scriptable global = getTopLevelScope(scope);
        ErrorReporter reporter = DefaultErrorReporter.forEval(cx.getErrorReporter());
        Evaluator evaluator = Context.createInterpreter();
        if (evaluator == null) {
            throw new JavaScriptException(cx, "Interpreter not present", filename, linep[0]);
        } else {
            return cx.compileFunction(global, source, evaluator, reporter, sourceURI, 1, null);
        }
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
            case "apply" ->
                4;
            case "call" ->
                5;
            case "bind" ->
                6;
            default ->
                0;
        };
    }
}