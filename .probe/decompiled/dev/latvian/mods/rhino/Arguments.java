package dev.latvian.mods.rhino;

final class Arguments extends IdScriptableObject {

    private static final String FTAG = "Arguments";

    private static final int Id_callee = 1;

    private static final int Id_length = 2;

    private static final int Id_caller = 3;

    private static final int MAX_INSTANCE_ID = 3;

    private static final BaseFunction iteratorMethod = new BaseFunction() {

        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            return new NativeArrayIterator(cx, scope, thisObj, NativeArrayIterator.ArrayIteratorType.VALUES);
        }
    };

    private final NativeCall activation;

    private Object callerObj;

    private Object calleeObj;

    private Object lengthObj;

    private int callerAttr = 2;

    private int calleeAttr = 2;

    private int lengthAttr = 2;

    private Object[] args;

    public Arguments(NativeCall activation, Context cx) {
        this.activation = activation;
        Scriptable parent = activation.getParentScope();
        this.setParentScope(parent);
        this.setPrototype(getObjectPrototype(parent, cx));
        this.args = activation.originalArgs;
        this.lengthObj = this.args.length;
        this.calleeObj = activation.function;
        this.callerObj = NOT_FOUND;
        this.defineProperty(cx, SymbolKey.ITERATOR, iteratorMethod, 2);
    }

    @Override
    public String getClassName() {
        return "Arguments";
    }

    private Object arg(int index) {
        return index >= 0 && this.args.length > index ? this.args[index] : NOT_FOUND;
    }

    private void putIntoActivation(int index, Object value, Context cx) {
        String argName = this.activation.function.getParamOrVarName(index);
        this.activation.put(cx, argName, this.activation, value);
    }

    private Object getFromActivation(int index, Context cx) {
        String argName = this.activation.function.getParamOrVarName(index);
        return this.activation.get(cx, argName, this.activation);
    }

    private void replaceArg(int index, Object value, Context cx) {
        if (this.sharedWithActivation(index, cx)) {
            this.putIntoActivation(index, value, cx);
        }
        synchronized (this) {
            if (this.args == this.activation.originalArgs) {
                this.args = (Object[]) this.args.clone();
            }
            this.args[index] = value;
        }
    }

    private void removeArg(int index) {
        synchronized (this) {
            if (this.args[index] != NOT_FOUND) {
                if (this.args == this.activation.originalArgs) {
                    this.args = (Object[]) this.args.clone();
                }
                this.args[index] = NOT_FOUND;
            }
        }
    }

    @Override
    public boolean has(Context cx, int index, Scriptable start) {
        return this.arg(index) != NOT_FOUND ? true : super.has(cx, index, start);
    }

    @Override
    public Object get(Context cx, int index, Scriptable start) {
        Object value = this.arg(index);
        if (value == NOT_FOUND) {
            return super.get(cx, index, start);
        } else {
            return this.sharedWithActivation(index, cx) ? this.getFromActivation(index, cx) : value;
        }
    }

    private boolean sharedWithActivation(int index, Context cx) {
        if (cx.isStrictMode()) {
            return false;
        } else {
            NativeFunction f = this.activation.function;
            int definedCount = f.getParamCount();
            if (index >= definedCount) {
                return false;
            } else {
                if (index < definedCount - 1) {
                    String argName = f.getParamOrVarName(index);
                    for (int i = index + 1; i < definedCount; i++) {
                        if (argName.equals(f.getParamOrVarName(i))) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
    }

    @Override
    public void put(Context cx, int index, Scriptable start, Object value) {
        if (this.arg(index) == NOT_FOUND) {
            super.put(cx, index, start, value);
        } else {
            this.replaceArg(index, value, cx);
        }
    }

    @Override
    public void put(Context cx, String name, Scriptable start, Object value) {
        super.put(cx, name, start, value);
    }

    @Override
    public void delete(Context cx, int index) {
        if (0 <= index && index < this.args.length) {
            this.removeArg(index);
        }
        super.delete(cx, index);
    }

    @Override
    protected int getMaxInstanceId() {
        return 3;
    }

    @Override
    protected int findInstanceIdInfo(String s, Context cx) {
        int id = switch(s) {
            case "callee" ->
                1;
            case "length" ->
                2;
            case "caller" ->
                3;
            default ->
                0;
        };
        if (!cx.isStrictMode() || id != 1 && id != 3) {
            if (id == 0) {
                return super.findInstanceIdInfo(s, cx);
            } else {
                int attr = switch(id) {
                    case 1 ->
                        this.calleeAttr;
                    case 2 ->
                        this.lengthAttr;
                    case 3 ->
                        this.callerAttr;
                    default ->
                        throw new IllegalStateException();
                };
                return instanceIdInfo(attr, id);
            }
        } else {
            return super.findInstanceIdInfo(s, cx);
        }
    }

    @Override
    protected String getInstanceIdName(int id) {
        return switch(id) {
            case 1 ->
                "callee";
            case 2 ->
                "length";
            case 3 ->
                "caller";
            default ->
                null;
        };
    }

    @Override
    protected Object getInstanceIdValue(int id, Context cx) {
        switch(id) {
            case 1:
                return this.calleeObj;
            case 2:
                return this.lengthObj;
            case 3:
                Object value = this.callerObj;
                if (value == UniqueTag.NULL_VALUE) {
                    value = null;
                } else if (value == null) {
                    NativeCall caller = this.activation.parentActivationCall;
                    if (caller != null) {
                        value = caller.get(cx, "arguments", caller);
                    }
                }
                return value;
            default:
                return super.getInstanceIdValue(id, cx);
        }
    }

    @Override
    protected void setInstanceIdValue(int id, Object value, Context cx) {
        switch(id) {
            case 1:
                this.calleeObj = value;
                break;
            case 2:
                this.lengthObj = value;
                break;
            case 3:
                this.callerObj = value != null ? value : UniqueTag.NULL_VALUE;
                break;
            default:
                super.setInstanceIdValue(id, value, cx);
        }
    }

    @Override
    protected void setInstanceIdAttributes(int id, int attr, Context cx) {
        switch(id) {
            case 1:
                this.calleeAttr = attr;
                break;
            case 2:
                this.lengthAttr = attr;
                break;
            case 3:
                this.callerAttr = attr;
                break;
            default:
                super.setInstanceIdAttributes(id, attr, cx);
        }
    }

    @Override
    Object[] getIds(Context cx, boolean getNonEnumerable, boolean getSymbols) {
        Object[] ids = super.getIds(cx, getNonEnumerable, getSymbols);
        if (this.args.length != 0) {
            boolean[] present = new boolean[this.args.length];
            int extraCount;
            for (Object id : this.args) {
                if (id instanceof Integer) {
                    int index = (Integer) id;
                    if (0 <= index && index < this.args.length && !present[index]) {
                        present[index] = true;
                        extraCount--;
                    }
                }
            }
            if (!getNonEnumerable) {
                for (int i = 0; i < present.length; i++) {
                    if (!present[i] && super.has(cx, i, this)) {
                        present[i] = true;
                        extraCount--;
                    }
                }
            }
            if (extraCount != false) {
                Object[] tmp = new Object[extraCount + ids.length];
                System.arraycopy(ids, 0, tmp, extraCount, ids.length);
                ids = tmp;
                int offset = 0;
                for (int ix = 0; ix != this.args.length; ix++) {
                    if (!present[ix]) {
                        ids[offset] = ix;
                        offset++;
                    }
                }
                if (offset != extraCount) {
                    Kit.codeBug();
                }
            }
        }
        return ids;
    }

    @Override
    protected ScriptableObject getOwnPropertyDescriptor(Context cx, Object id) {
        if (!ScriptRuntime.isSymbol(id) && !(id instanceof Scriptable)) {
            double d = ScriptRuntime.toNumber(cx, id);
            int index = (int) d;
            if (d != (double) index) {
                return super.getOwnPropertyDescriptor(cx, id);
            } else {
                Object value = this.arg(index);
                if (value == NOT_FOUND) {
                    return super.getOwnPropertyDescriptor(cx, id);
                } else {
                    if (this.sharedWithActivation(index, cx)) {
                        value = this.getFromActivation(index, cx);
                    }
                    if (super.has(cx, index, this)) {
                        ScriptableObject desc = super.getOwnPropertyDescriptor(cx, id);
                        desc.put(cx, "value", desc, value);
                        return desc;
                    } else {
                        Scriptable scope = this.getParentScope();
                        if (scope == null) {
                            scope = this;
                        }
                        return buildDataDescriptor(scope, value, 0, cx);
                    }
                }
            }
        } else {
            return super.getOwnPropertyDescriptor(cx, id);
        }
    }

    @Override
    protected void defineOwnProperty(Context cx, Object id, ScriptableObject desc, boolean checkValid) {
        super.defineOwnProperty(cx, id, desc, checkValid);
        if (!ScriptRuntime.isSymbol(id)) {
            double d = ScriptRuntime.toNumber(cx, id);
            int index = (int) d;
            if (d == (double) index) {
                Object value = this.arg(index);
                if (value != NOT_FOUND) {
                    if (this.isAccessorDescriptor(cx, desc)) {
                        this.removeArg(index);
                    } else {
                        Object newValue = getProperty(desc, "value", cx);
                        if (newValue != NOT_FOUND) {
                            this.replaceArg(index, newValue, cx);
                            if (isFalse(getProperty(desc, "writable", cx), cx)) {
                                this.removeArg(index);
                            }
                        }
                    }
                }
            }
        }
    }

    void defineAttributesForStrictMode(Context cx) {
        if (cx.isStrictMode()) {
            this.setGetterOrSetter(cx, "caller", 0, new Arguments.ThrowTypeError("caller"), true);
            this.setGetterOrSetter(cx, "caller", 0, new Arguments.ThrowTypeError("caller"), false);
            this.setGetterOrSetter(cx, "callee", 0, new Arguments.ThrowTypeError("callee"), true);
            this.setGetterOrSetter(cx, "callee", 0, new Arguments.ThrowTypeError("callee"), false);
            this.setAttributes(cx, "caller", 6);
            this.setAttributes(cx, "callee", 6);
            this.callerObj = null;
            this.calleeObj = null;
        }
    }

    private static class ThrowTypeError extends BaseFunction {

        private final String propertyName;

        ThrowTypeError(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            throw ScriptRuntime.typeError1(cx, "msg.arguments.not.access.strict", this.propertyName);
        }
    }
}