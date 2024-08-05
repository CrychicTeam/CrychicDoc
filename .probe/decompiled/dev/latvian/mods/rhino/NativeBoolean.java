package dev.latvian.mods.rhino;

final class NativeBoolean extends IdScriptableObject {

    private static final Object BOOLEAN_TAG = "Boolean";

    private static final int Id_constructor = 1;

    private static final int Id_toString = 2;

    private static final int Id_toSource = 3;

    private static final int Id_valueOf = 4;

    private static final int MAX_PROTOTYPE_ID = 4;

    private final boolean booleanValue;

    static void init(Scriptable scope, boolean sealed, Context cx) {
        NativeBoolean obj = new NativeBoolean(false);
        obj.exportAsJSClass(4, scope, sealed, cx);
    }

    NativeBoolean(boolean b) {
        this.booleanValue = b;
    }

    @Override
    public String getClassName() {
        return "Boolean";
    }

    @Override
    public Object getDefaultValue(Context cx, Class<?> typeHint) {
        return typeHint == ScriptRuntime.BooleanClass ? this.booleanValue : super.getDefaultValue(cx, typeHint);
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        switch(id) {
            case 1:
                this.initPrototypeMethod(BOOLEAN_TAG, id, "constructor", 1, cx);
                break;
            case 2:
                this.initPrototypeMethod(BOOLEAN_TAG, id, "toString", 0, cx);
                break;
            case 3:
                this.initPrototypeMethod(BOOLEAN_TAG, id, "toSource", 0, cx);
                break;
            case 4:
                this.initPrototypeMethod(BOOLEAN_TAG, id, "valueOf", 0, cx);
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(BOOLEAN_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            if (id != 1) {
                if (!(thisObj instanceof NativeBoolean)) {
                    throw incompatibleCallError(f, cx);
                } else {
                    boolean value = ((NativeBoolean) thisObj).booleanValue;
                    switch(id) {
                        case 2:
                            return value ? "true" : "false";
                        case 3:
                            return "not_supported";
                        case 4:
                            return value;
                        default:
                            throw new IllegalArgumentException(String.valueOf(id));
                    }
                }
            } else {
                boolean b;
                if (args.length == 0) {
                    b = false;
                } else {
                    b = (!(args[0] instanceof ScriptableObject) || !((ScriptableObject) args[0]).avoidObjectDetection()) && ScriptRuntime.toBoolean(cx, args[0]);
                }
                return thisObj == null ? new NativeBoolean(b) : b;
            }
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
            case "valueOf" ->
                4;
            default ->
                0;
        };
    }
}