package dev.latvian.mods.rhino;

public class NativeCallSite extends IdScriptableObject {

    private static final String CALLSITE_TAG = "CallSite";

    private static final int Id_constructor = 1;

    private static final int Id_getThis = 2;

    private static final int Id_getTypeName = 3;

    private static final int Id_getFunction = 4;

    private static final int Id_getFunctionName = 5;

    private static final int Id_getMethodName = 6;

    private static final int Id_getFileName = 7;

    private static final int Id_getLineNumber = 8;

    private static final int Id_getColumnNumber = 9;

    private static final int Id_getEvalOrigin = 10;

    private static final int Id_isToplevel = 11;

    private static final int Id_isEval = 12;

    private static final int Id_isNative = 13;

    private static final int Id_isConstructor = 14;

    private static final int Id_toString = 15;

    private static final int MAX_PROTOTYPE_ID = 15;

    private ScriptStackElement element;

    static void init(Scriptable scope, boolean sealed, Context cx) {
        NativeCallSite cs = new NativeCallSite();
        cs.exportAsJSClass(15, scope, sealed, cx);
    }

    static NativeCallSite make(Scriptable scope, Scriptable ctorObj, Context cx) {
        NativeCallSite cs = new NativeCallSite();
        Scriptable proto = (Scriptable) ctorObj.get(cx, "prototype", ctorObj);
        cs.setParentScope(scope);
        cs.setPrototype(proto);
        return cs;
    }

    private static Object js_toString(Scriptable obj, Context cx) {
        while (obj != null && !(obj instanceof NativeCallSite)) {
            obj = obj.getPrototype(cx);
        }
        if (obj == null) {
            return NOT_FOUND;
        } else {
            NativeCallSite cs = (NativeCallSite) obj;
            StringBuilder sb = new StringBuilder();
            cs.element.renderJavaStyle(sb);
            return sb.toString();
        }
    }

    private static Object getFunctionName(Scriptable obj, Context cx) {
        while (obj != null && !(obj instanceof NativeCallSite)) {
            obj = obj.getPrototype(cx);
        }
        if (obj == null) {
            return NOT_FOUND;
        } else {
            NativeCallSite cs = (NativeCallSite) obj;
            return cs.element == null ? null : cs.element.functionName;
        }
    }

    private static Object getFileName(Scriptable obj, Context cx) {
        while (obj != null && !(obj instanceof NativeCallSite)) {
            obj = obj.getPrototype(cx);
        }
        if (obj == null) {
            return NOT_FOUND;
        } else {
            NativeCallSite cs = (NativeCallSite) obj;
            return cs.element == null ? null : cs.element.fileName;
        }
    }

    private static Object getLineNumber(Scriptable obj, Context cx) {
        while (obj != null && !(obj instanceof NativeCallSite)) {
            obj = obj.getPrototype(cx);
        }
        if (obj == null) {
            return NOT_FOUND;
        } else {
            NativeCallSite cs = (NativeCallSite) obj;
            return cs.element != null && cs.element.lineNumber >= 0 ? cs.element.lineNumber : Undefined.instance;
        }
    }

    private NativeCallSite() {
    }

    void setElement(ScriptStackElement elt) {
        this.element = elt;
    }

    @Override
    public String getClassName() {
        return "CallSite";
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        String s;
        int arity;
        switch(id) {
            case 1:
                arity = 0;
                s = "constructor";
                break;
            case 2:
                arity = 0;
                s = "getThis";
                break;
            case 3:
                arity = 0;
                s = "getTypeName";
                break;
            case 4:
                arity = 0;
                s = "getFunction";
                break;
            case 5:
                arity = 0;
                s = "getFunctionName";
                break;
            case 6:
                arity = 0;
                s = "getMethodName";
                break;
            case 7:
                arity = 0;
                s = "getFileName";
                break;
            case 8:
                arity = 0;
                s = "getLineNumber";
                break;
            case 9:
                arity = 0;
                s = "getColumnNumber";
                break;
            case 10:
                arity = 0;
                s = "getEvalOrigin";
                break;
            case 11:
                arity = 0;
                s = "isToplevel";
                break;
            case 12:
                arity = 0;
                s = "isEval";
                break;
            case 13:
                arity = 0;
                s = "isNative";
                break;
            case 14:
                arity = 0;
                s = "isConstructor";
                break;
            case 15:
                arity = 0;
                s = "toString";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        this.initPrototypeMethod("CallSite", id, s, arity, cx);
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag("CallSite")) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            return switch(id) {
                case 1 ->
                    make(scope, f, cx);
                case 2, 3, 4, 9 ->
                    Undefined.instance;
                case 5 ->
                    getFunctionName(thisObj, cx);
                case 6 ->
                    null;
                case 7 ->
                    getFileName(thisObj, cx);
                case 8 ->
                    getLineNumber(thisObj, cx);
                case 10, 11, 12, 13, 14 ->
                    Boolean.FALSE;
                case 15 ->
                    js_toString(thisObj, cx);
                default ->
                    throw new IllegalArgumentException(String.valueOf(id));
            };
        }
    }

    public String toString() {
        return this.element == null ? "" : this.element.toString();
    }

    @Override
    protected int findPrototypeId(String s) {
        return switch(s) {
            case "isEval" ->
                12;
            case "getThis" ->
                2;
            case "isNative" ->
                13;
            case "toString" ->
                15;
            case "isToplevel" ->
                11;
            case "getFileName" ->
                7;
            case "constructor" ->
                1;
            case "getFunction" ->
                4;
            case "getTypeName" ->
                3;
            case "getEvalOrigin" ->
                10;
            case "getLineNumber" ->
                8;
            case "getMethodName" ->
                6;
            case "isConstructor" ->
                14;
            case "getColumnNumber" ->
                9;
            case "getFunctionName" ->
                5;
            default ->
                0;
        };
    }
}