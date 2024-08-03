package dev.latvian.mods.rhino;

public abstract class ES6Iterator extends IdScriptableObject {

    public static final String NEXT_METHOD = "next";

    public static final String DONE_PROPERTY = "done";

    public static final String RETURN_PROPERTY = "return";

    public static final String VALUE_PROPERTY = "value";

    public static final String RETURN_METHOD = "return";

    private static final int Id_next = 1;

    private static final int SymbolId_iterator = 2;

    private static final int SymbolId_toStringTag = 3;

    private static final int MAX_PROTOTYPE_ID = 3;

    protected boolean exhausted = false;

    private String tag;

    protected static void init(ScriptableObject scope, boolean sealed, IdScriptableObject prototype, String tag, Context cx) {
        if (scope != null) {
            prototype.setParentScope(scope);
            prototype.setPrototype(getObjectPrototype(scope, cx));
        }
        prototype.activatePrototypeMap(3);
        if (sealed) {
            prototype.sealObject(cx);
        }
        if (scope != null) {
            scope.associateValue(tag, prototype);
        }
    }

    static Scriptable makeIteratorResult(Context cx, Scriptable scope, Boolean done) {
        return makeIteratorResult(cx, scope, done, Undefined.instance);
    }

    static Scriptable makeIteratorResult(Context cx, Scriptable scope, Boolean done, Object value) {
        Scriptable iteratorResult = cx.newObject(scope);
        ScriptableObject.putProperty(iteratorResult, "value", value, cx);
        ScriptableObject.putProperty(iteratorResult, "done", done, cx);
        return iteratorResult;
    }

    protected ES6Iterator() {
    }

    protected ES6Iterator(Scriptable scope, String tag, Context cx) {
        this.tag = tag;
        Scriptable top = ScriptableObject.getTopLevelScope(scope);
        this.setParentScope(top);
        IdScriptableObject prototype = (IdScriptableObject) ScriptableObject.getTopScopeValue(top, tag, cx);
        this.setPrototype(prototype);
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        switch(id) {
            case 1:
                this.initPrototypeMethod(this.getTag(), id, "next", 0, cx);
                break;
            case 2:
                this.initPrototypeMethod(this.getTag(), id, SymbolKey.ITERATOR, "[Symbol.iterator]", 3, cx);
                break;
            case 3:
                this.initPrototypeValue(3, SymbolKey.TO_STRING_TAG, this.getClassName(), 3);
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(this.getTag())) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            if (thisObj instanceof ES6Iterator iterator) {
                return switch(id) {
                    case 1 ->
                        iterator.next(cx, scope);
                    case 2 ->
                        iterator;
                    default ->
                        throw new IllegalArgumentException(String.valueOf(id));
                };
            } else {
                throw incompatibleCallError(f, cx);
            }
        }
    }

    @Override
    protected int findPrototypeId(Symbol k) {
        if (SymbolKey.ITERATOR.equals(k)) {
            return 2;
        } else {
            return SymbolKey.TO_STRING_TAG.equals(k) ? 3 : 0;
        }
    }

    @Override
    protected int findPrototypeId(String s) {
        return "next".equals(s) ? 1 : 0;
    }

    protected abstract boolean isDone(Context var1, Scriptable var2);

    protected abstract Object nextValue(Context var1, Scriptable var2);

    protected Object next(Context cx, Scriptable scope) {
        Object value = Undefined.instance;
        boolean done = this.isDone(cx, scope) || this.exhausted;
        if (!done) {
            value = this.nextValue(cx, scope);
        } else {
            this.exhausted = true;
        }
        return makeIteratorResult(cx, scope, done, value);
    }

    protected String getTag() {
        return this.tag;
    }
}