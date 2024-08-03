package dev.latvian.mods.rhino;

public final class ES6Generator extends IdScriptableObject {

    private static final Object GENERATOR_TAG = "Generator";

    private static final int Id_next = 1;

    private static final int Id_return = 2;

    private static final int Id_throw = 3;

    private static final int SymbolId_iterator = 4;

    private static final int MAX_PROTOTYPE_ID = 4;

    private NativeFunction function;

    private Object savedState;

    private String lineSource;

    private int lineNumber;

    private ES6Generator.State state = ES6Generator.State.SUSPENDED_START;

    private Object delegee;

    static ES6Generator init(ScriptableObject scope, boolean sealed, Context cx) {
        ES6Generator prototype = new ES6Generator();
        if (scope != null) {
            prototype.setParentScope(scope);
            prototype.setPrototype(getObjectPrototype(scope, cx));
        }
        prototype.activatePrototypeMap(4);
        if (sealed) {
            prototype.sealObject(cx);
        }
        if (scope != null) {
            scope.associateValue(GENERATOR_TAG, prototype);
        }
        return prototype;
    }

    private ES6Generator() {
    }

    public ES6Generator(Scriptable scope, NativeFunction function, Object savedState, Context cx) {
        this.function = function;
        this.savedState = savedState;
        Scriptable top = ScriptableObject.getTopLevelScope(scope);
        this.setParentScope(top);
        ES6Generator prototype = (ES6Generator) ScriptableObject.getTopScopeValue(top, GENERATOR_TAG, cx);
        this.setPrototype(prototype);
    }

    @Override
    public String getClassName() {
        return "Generator";
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        if (id == 4) {
            this.initPrototypeMethod(GENERATOR_TAG, id, SymbolKey.ITERATOR, "[Symbol.iterator]", 0, cx);
        } else {
            String s;
            int arity;
            switch(id) {
                case 1:
                    arity = 1;
                    s = "next";
                    break;
                case 2:
                    arity = 1;
                    s = "return";
                    break;
                case 3:
                    arity = 1;
                    s = "throw";
                    break;
                default:
                    throw new IllegalArgumentException(String.valueOf(id));
            }
            this.initPrototypeMethod(GENERATOR_TAG, id, s, arity, cx);
        }
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(GENERATOR_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            if (thisObj instanceof ES6Generator generator) {
                Object value = args.length >= 1 ? args[0] : Undefined.instance;
                switch(id) {
                    case 1:
                        if (generator.delegee == null) {
                            return generator.resumeLocal(cx, scope, value);
                        }
                        return generator.resumeDelegee(cx, scope, value);
                    case 2:
                        if (generator.delegee == null) {
                            return generator.resumeAbruptLocal(cx, scope, 2, value);
                        }
                        return generator.resumeDelegeeReturn(cx, scope, value);
                    case 3:
                        if (generator.delegee == null) {
                            return generator.resumeAbruptLocal(cx, scope, 1, value);
                        }
                        return generator.resumeDelegeeThrow(cx, scope, value);
                    case 4:
                        return thisObj;
                    default:
                        throw new IllegalArgumentException(String.valueOf(id));
                }
            } else {
                throw incompatibleCallError(f, cx);
            }
        }
    }

    private Scriptable resumeDelegee(Context cx, Scriptable scope, Object value) {
        try {
            Object[] nextArgs = Undefined.instance.equals(value) ? ScriptRuntime.EMPTY_OBJECTS : new Object[] { value };
            Callable nextFn = ScriptRuntime.getPropFunctionAndThis(cx, scope, this.delegee, "next");
            Scriptable nextThis = cx.lastStoredScriptable();
            Object nr = cx.callSync(nextFn, scope, nextThis, nextArgs);
            Scriptable nextResult = ScriptableObject.ensureScriptable(nr, cx);
            if (ScriptRuntime.isIteratorDone(cx, nextResult)) {
                this.delegee = null;
                return this.resumeLocal(cx, scope, ScriptableObject.getProperty(nextResult, "value", cx));
            } else {
                return nextResult;
            }
        } catch (RhinoException var9) {
            this.delegee = null;
            return this.resumeAbruptLocal(cx, scope, 1, var9);
        }
    }

    private Scriptable resumeDelegeeThrow(Context cx, Scriptable scope, Object value) {
        boolean returnCalled = false;
        try {
            Callable throwFn = ScriptRuntime.getPropFunctionAndThis(cx, scope, this.delegee, "throw");
            Scriptable nextThis = cx.lastStoredScriptable();
            Object throwResult = cx.callSync(throwFn, scope, nextThis, new Object[] { value });
            if (ScriptRuntime.isIteratorDone(cx, throwResult)) {
                try {
                    returnCalled = true;
                    this.callReturnOptionally(cx, scope, Undefined.instance);
                } finally {
                    this.delegee = null;
                }
                return this.resumeLocal(cx, scope, ScriptRuntime.getObjectProp(cx, scope, throwResult, "value"));
            } else {
                return ensureScriptable(throwResult, cx);
            }
        } catch (RhinoException var21) {
            Scriptable throwResultx;
            try {
                if (returnCalled) {
                    return this.resumeAbruptLocal(cx, scope, 1, var21);
                }
                try {
                    this.callReturnOptionally(cx, scope, Undefined.instance);
                    return this.resumeAbruptLocal(cx, scope, 1, var21);
                } catch (RhinoException var19) {
                    throwResultx = this.resumeAbruptLocal(cx, scope, 1, var19);
                }
            } finally {
                this.delegee = null;
            }
            return throwResultx;
        }
    }

    private Scriptable resumeDelegeeReturn(Context cx, Scriptable scope, Object value) {
        try {
            Object retResult = this.callReturnOptionally(cx, scope, value);
            if (retResult != null) {
                if (ScriptRuntime.isIteratorDone(cx, retResult)) {
                    this.delegee = null;
                    return this.resumeAbruptLocal(cx, scope, 2, ScriptRuntime.getObjectPropNoWarn(cx, scope, retResult, "value"));
                } else {
                    return ensureScriptable(retResult, cx);
                }
            } else {
                this.delegee = null;
                return this.resumeAbruptLocal(cx, scope, 2, value);
            }
        } catch (RhinoException var5) {
            this.delegee = null;
            return this.resumeAbruptLocal(cx, scope, 1, var5);
        }
    }

    private Scriptable resumeLocal(Context cx, Scriptable scope, Object value) {
        if (this.state == ES6Generator.State.COMPLETED) {
            return ES6Iterator.makeIteratorResult(cx, scope, Boolean.TRUE);
        } else if (this.state == ES6Generator.State.EXECUTING) {
            throw ScriptRuntime.typeError0(cx, "msg.generator.executing");
        } else {
            Scriptable result = ES6Iterator.makeIteratorResult(cx, scope, Boolean.FALSE);
            this.state = ES6Generator.State.EXECUTING;
            try {
                Object r = this.function.resumeGenerator(cx, scope, 0, this.savedState, value);
                if (!(r instanceof ES6Generator.YieldStarResult ysResult)) {
                    ScriptableObject.putProperty(result, "value", r, cx);
                    return result;
                }
                this.state = ES6Generator.State.SUSPENDED_YIELD;
                try {
                    this.delegee = ScriptRuntime.callIterator(cx, scope, ysResult.getResult());
                } catch (RhinoException var24) {
                    return this.resumeAbruptLocal(cx, scope, 1, var24);
                }
                Scriptable delResult;
                try {
                    delResult = this.resumeDelegee(cx, scope, Undefined.instance);
                } finally {
                    this.state = ES6Generator.State.EXECUTING;
                }
                if (ScriptRuntime.isIteratorDone(cx, delResult)) {
                    this.state = ES6Generator.State.COMPLETED;
                }
                return delResult;
            } catch (GeneratorState.GeneratorClosedException var25) {
                this.state = ES6Generator.State.COMPLETED;
                return result;
            } catch (JavaScriptException var26) {
                this.state = ES6Generator.State.COMPLETED;
                if (var26.getValue() instanceof NativeIterator.StopIteration) {
                    ScriptableObject.putProperty(result, "value", ((NativeIterator.StopIteration) var26.getValue()).getValue(), cx);
                    return result;
                } else {
                    this.lineNumber = var26.lineNumber();
                    this.lineSource = var26.lineSource();
                    if (var26.getValue() instanceof RhinoException) {
                        throw (RhinoException) var26.getValue();
                    } else {
                        throw var26;
                    }
                }
            } catch (RhinoException var27) {
                this.lineNumber = var27.lineNumber();
                this.lineSource = var27.lineSource();
                throw var27;
            } finally {
                if (this.state == ES6Generator.State.COMPLETED) {
                    ScriptableObject.putProperty(result, "done", Boolean.TRUE, cx);
                } else {
                    this.state = ES6Generator.State.SUSPENDED_YIELD;
                }
            }
        }
    }

    private Scriptable resumeAbruptLocal(Context cx, Scriptable scope, int op, Object value) {
        if (this.state == ES6Generator.State.EXECUTING) {
            throw ScriptRuntime.typeError0(cx, "msg.generator.executing");
        } else {
            if (this.state == ES6Generator.State.SUSPENDED_START) {
                this.state = ES6Generator.State.COMPLETED;
            }
            Scriptable result = ES6Iterator.makeIteratorResult(cx, scope, Boolean.FALSE);
            if (this.state == ES6Generator.State.COMPLETED) {
                if (op == 1) {
                    throw new JavaScriptException(cx, value, this.lineSource, this.lineNumber);
                } else {
                    ScriptableObject.putProperty(result, "done", Boolean.TRUE, cx);
                    return result;
                }
            } else {
                this.state = ES6Generator.State.EXECUTING;
                Object throwValue = value;
                if (op == 2) {
                    if (!(value instanceof GeneratorState.GeneratorClosedException)) {
                        throwValue = new GeneratorState.GeneratorClosedException();
                    }
                } else if (value instanceof JavaScriptException) {
                    throwValue = ((JavaScriptException) value).getValue();
                } else if (value instanceof RhinoException) {
                    throwValue = ScriptRuntime.wrapException(cx, scope, (Throwable) value);
                }
                try {
                    Object r = this.function.resumeGenerator(cx, scope, op, this.savedState, throwValue);
                    ScriptableObject.putProperty(result, "value", r, cx);
                    this.state = ES6Generator.State.SUSPENDED_YIELD;
                } catch (GeneratorState.GeneratorClosedException var13) {
                    this.state = ES6Generator.State.COMPLETED;
                } catch (JavaScriptException var14) {
                    this.state = ES6Generator.State.COMPLETED;
                    if (!(var14.getValue() instanceof NativeIterator.StopIteration)) {
                        this.lineNumber = var14.lineNumber();
                        this.lineSource = var14.lineSource();
                        if (var14.getValue() instanceof RhinoException) {
                            throw (RhinoException) var14.getValue();
                        }
                        throw var14;
                    }
                    ScriptableObject.putProperty(result, "value", ((NativeIterator.StopIteration) var14.getValue()).getValue(), cx);
                } catch (RhinoException var15) {
                    this.state = ES6Generator.State.COMPLETED;
                    this.lineNumber = var15.lineNumber();
                    this.lineSource = var15.lineSource();
                    throw var15;
                } finally {
                    if (this.state == ES6Generator.State.COMPLETED) {
                        this.delegee = null;
                        ScriptableObject.putProperty(result, "done", Boolean.TRUE, cx);
                    }
                }
                return result;
            }
        }
    }

    private Object callReturnOptionally(Context cx, Scriptable scope, Object value) {
        Object[] retArgs = Undefined.instance.equals(value) ? ScriptRuntime.EMPTY_OBJECTS : new Object[] { value };
        Object retFnObj = ScriptRuntime.getObjectPropNoWarn(cx, scope, this.delegee, "return");
        if (!Undefined.instance.equals(retFnObj)) {
            if (!(retFnObj instanceof Callable)) {
                throw ScriptRuntime.typeError2(cx, "msg.isnt.function", "return", ScriptRuntime.typeof(cx, retFnObj));
            } else {
                return cx.callSync((Callable) retFnObj, scope, ensureScriptable(this.delegee, cx), retArgs);
            }
        } else {
            return null;
        }
    }

    @Override
    protected int findPrototypeId(Symbol k) {
        return SymbolKey.ITERATOR.equals(k) ? 4 : 0;
    }

    @Override
    protected int findPrototypeId(String s) {
        return switch(s) {
            case "next" ->
                1;
            case "return" ->
                2;
            case "throw" ->
                3;
            default ->
                0;
        };
    }

    static enum State {

        SUSPENDED_START, SUSPENDED_YIELD, EXECUTING, COMPLETED
    }

    public static final class YieldStarResult {

        private final Object result;

        public YieldStarResult(Object result) {
            this.result = result;
        }

        Object getResult() {
            return this.result;
        }
    }
}