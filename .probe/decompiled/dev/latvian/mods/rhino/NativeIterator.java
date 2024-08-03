package dev.latvian.mods.rhino;

import java.util.Iterator;

public final class NativeIterator extends IdScriptableObject {

    public static final String ITERATOR_PROPERTY_NAME = "__iterator__";

    private static final Object ITERATOR_TAG = "Iterator";

    private static final String STOP_ITERATION = "StopIteration";

    private static final int Id_constructor = 1;

    private static final int Id_next = 2;

    private static final int Id___iterator__ = 3;

    private static final int MAX_PROTOTYPE_ID = 3;

    private IdEnumeration objectIterator;

    static void init(Context cx, ScriptableObject scope, boolean sealed) {
        NativeIterator iterator = new NativeIterator();
        iterator.exportAsJSClass(3, scope, sealed, cx);
        ES6Generator.init(scope, sealed, cx);
        NativeObject obj = new NativeIterator.StopIteration(cx);
        obj.setPrototype(getObjectPrototype(scope, cx));
        obj.setParentScope(scope);
        if (sealed) {
            obj.sealObject(cx);
        }
        defineProperty(scope, "StopIteration", obj, 2, cx);
        scope.associateValue(ITERATOR_TAG, obj);
    }

    public static Object getStopIterationObject(Scriptable scope, Context cx) {
        Scriptable top = getTopLevelScope(scope);
        return getTopScopeValue(top, ITERATOR_TAG, cx);
    }

    private static Object jsConstructor(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args.length != 0 && args[0] != null && args[0] != Undefined.instance) {
            Scriptable obj = ScriptRuntime.toObject(cx, scope, args[0]);
            boolean keyOnly = args.length > 1 && ScriptRuntime.toBoolean(cx, args[1]);
            if (thisObj != null) {
                Iterator<?> iterator = getJavaIterator(obj);
                if (iterator != null) {
                    scope = getTopLevelScope(scope);
                    return cx.getWrapFactory().wrap(cx, scope, new NativeIterator.WrappedJavaIterator(cx, iterator, scope), NativeIterator.WrappedJavaIterator.class);
                }
                Scriptable jsIterator = ScriptRuntime.toIterator(cx, scope, obj, keyOnly);
                if (jsIterator != null) {
                    return jsIterator;
                }
            }
            IdEnumeration objectIterator = ScriptRuntime.enumInit(cx, scope, obj, keyOnly ? 3 : 5);
            objectIterator.enumNumbers = true;
            NativeIterator result = new NativeIterator(objectIterator);
            result.setPrototype(getClassPrototype(scope, result.getClassName(), cx));
            result.setParentScope(scope);
            return result;
        } else {
            Object argument = args.length == 0 ? Undefined.instance : args[0];
            throw ScriptRuntime.typeError1(cx, "msg.no.properties", ScriptRuntime.toString(cx, argument));
        }
    }

    private static Iterator<?> getJavaIterator(Object obj) {
        if (obj instanceof Wrapper) {
            Object unwrapped = ((Wrapper) obj).unwrap();
            Iterator<?> iterator = null;
            if (unwrapped instanceof Iterator) {
                iterator = (Iterator<?>) unwrapped;
            }
            if (unwrapped instanceof Iterable) {
                iterator = ((Iterable) unwrapped).iterator();
            }
            return iterator;
        } else {
            return null;
        }
    }

    private NativeIterator() {
    }

    private NativeIterator(IdEnumeration objectIterator) {
        this.objectIterator = objectIterator;
    }

    @Override
    public String getClassName() {
        return "Iterator";
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        String s;
        int arity;
        switch(id) {
            case 1:
                arity = 2;
                s = "constructor";
                break;
            case 2:
                arity = 0;
                s = "next";
                break;
            case 3:
                arity = 1;
                s = "__iterator__";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        this.initPrototypeMethod(ITERATOR_TAG, id, s, arity, cx);
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(ITERATOR_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            if (id == 1) {
                return jsConstructor(cx, scope, thisObj, args);
            } else if (thisObj instanceof NativeIterator iterator) {
                return switch(id) {
                    case 2 ->
                        iterator.objectIterator.nextExec(cx, scope);
                    case 3 ->
                        thisObj;
                    default ->
                        throw new IllegalArgumentException(String.valueOf(id));
                };
            } else {
                throw incompatibleCallError(f, cx);
            }
        }
    }

    @Override
    protected int findPrototypeId(String s) {
        return switch(s) {
            case "next" ->
                2;
            case "__iterator__" ->
                3;
            case "constructor" ->
                1;
            default ->
                0;
        };
    }

    public static class StopIteration extends NativeObject {

        private Object value = Undefined.instance;

        public StopIteration(Context cx) {
            super(cx);
        }

        public StopIteration(Context cx, Object val) {
            this(cx);
            this.value = val;
        }

        public Object getValue() {
            return this.value;
        }

        @Override
        public String getClassName() {
            return "StopIteration";
        }

        @Override
        public boolean hasInstance(Context cx, Scriptable instance) {
            return instance instanceof NativeIterator.StopIteration;
        }
    }

    public static class WrappedJavaIterator {

        private final Context localContext;

        private final Iterator<?> iterator;

        private final Scriptable scope;

        WrappedJavaIterator(Context cx, Iterator<?> iterator, Scriptable scope) {
            this.localContext = cx;
            this.iterator = iterator;
            this.scope = scope;
        }

        public Object next() {
            if (!this.iterator.hasNext()) {
                throw new JavaScriptException(this.localContext, NativeIterator.getStopIterationObject(this.scope, this.localContext), null, 0);
            } else {
                return this.iterator.next();
            }
        }

        public Object __iterator__(boolean b) {
            return this;
        }
    }
}