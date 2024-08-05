package dev.latvian.mods.rhino;

final class NativeNumber extends IdScriptableObject {

    public static final double MAX_SAFE_INTEGER = 9.007199254740991E15;

    private static final Object NUMBER_TAG = "Number";

    private static final int MAX_PRECISION = 100;

    private static final double MIN_SAFE_INTEGER = -9.007199254740991E15;

    private static final int ConstructorId_isFinite = -1;

    private static final int ConstructorId_isNaN = -2;

    private static final int ConstructorId_isInteger = -3;

    private static final int ConstructorId_isSafeInteger = -4;

    private static final int ConstructorId_parseFloat = -5;

    private static final int ConstructorId_parseInt = -6;

    private static final int Id_constructor = 1;

    private static final int Id_toString = 2;

    private static final int Id_toLocaleString = 3;

    private static final int Id_toSource = 4;

    private static final int Id_valueOf = 5;

    private static final int Id_toFixed = 6;

    private static final int Id_toExponential = 7;

    private static final int Id_toPrecision = 8;

    private static final int MAX_PROTOTYPE_ID = 8;

    private final Context localContext;

    private final double doubleValue;

    static void init(Scriptable scope, boolean sealed, Context cx) {
        NativeNumber obj = new NativeNumber(cx, 0.0);
        obj.exportAsJSClass(8, scope, sealed, cx);
    }

    private static Object execConstructorCall(Context cx, int id, Object[] args) {
        switch(id) {
            case -6:
                return NativeGlobal.js_parseInt(args, cx);
            case -5:
                return NativeGlobal.js_parseFloat(cx, args);
            case -4:
                if (args.length != 0 && Undefined.instance != args[0]) {
                    if (args[0] instanceof Number) {
                        return isSafeInteger((Number) args[0]);
                    }
                    return Boolean.FALSE;
                }
                return Boolean.FALSE;
            case -3:
                if (args.length != 0 && Undefined.instance != args[0]) {
                    if (args[0] instanceof Number) {
                        return isInteger((Number) args[0]);
                    }
                    return Boolean.FALSE;
                }
                return Boolean.FALSE;
            case -2:
                if (args.length != 0 && Undefined.instance != args[0]) {
                    if (args[0] instanceof Number) {
                        return isNaN((Number) args[0]);
                    }
                    return Boolean.FALSE;
                }
                return Boolean.FALSE;
            case -1:
                if (args.length != 0 && Undefined.instance != args[0]) {
                    if (args[0] instanceof Number) {
                        return isFinite(args[0], cx);
                    }
                    return Boolean.FALSE;
                }
                return Boolean.FALSE;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
    }

    private static String num_to(Context cx, double val, Object[] args, int zeroArgMode, int oneArgMode, int precisionMin, int precisionOffset) {
        int precision;
        if (args.length == 0) {
            precision = 0;
            oneArgMode = zeroArgMode;
        } else {
            double p = ScriptRuntime.toInteger(cx, args[0]);
            if (p < (double) precisionMin || p > 100.0) {
                String msg = ScriptRuntime.getMessage1("msg.bad.precision", ScriptRuntime.toString(cx, args[0]));
                throw ScriptRuntime.rangeError(cx, msg);
            }
            precision = ScriptRuntime.toInt32(p);
        }
        StringBuilder sb = new StringBuilder();
        DToA.JS_dtostr(sb, oneArgMode, precision + precisionOffset, val);
        return sb.toString();
    }

    static Object isFinite(Object val, Context cx) {
        double d = ScriptRuntime.toNumber(cx, val);
        Double nd = d;
        return !nd.isInfinite() && !nd.isNaN();
    }

    private static Boolean isNaN(Number val) {
        if (val instanceof Double) {
            return ((Double) val).isNaN();
        } else {
            double d = val.doubleValue();
            return Double.isNaN(d);
        }
    }

    private static boolean isInteger(Number val) {
        return val instanceof Double ? isDoubleInteger((Double) val) : isDoubleInteger(val.doubleValue());
    }

    private static boolean isDoubleInteger(Double d) {
        return !d.isInfinite() && !d.isNaN() && Math.floor(d) == d;
    }

    private static boolean isDoubleInteger(double d) {
        return !Double.isInfinite(d) && !Double.isNaN(d) && Math.floor(d) == d;
    }

    private static boolean isSafeInteger(Number val) {
        return val instanceof Double ? isDoubleSafeInteger((Double) val) : isDoubleSafeInteger(val.doubleValue());
    }

    private static boolean isDoubleSafeInteger(Double d) {
        return isDoubleInteger(d) && d <= 9.007199254740991E15 && d >= -9.007199254740991E15;
    }

    private static boolean isDoubleSafeInteger(double d) {
        return isDoubleInteger(d) && d <= 9.007199254740991E15 && d >= -9.007199254740991E15;
    }

    NativeNumber(Context cx, double number) {
        this.localContext = cx;
        this.doubleValue = number;
    }

    @Override
    public String getClassName() {
        return "Number";
    }

    @Override
    protected void fillConstructorProperties(IdFunctionObject ctor, Context cx) {
        int attr = 7;
        ctor.defineProperty(cx, "NaN", ScriptRuntime.NaNobj, 7);
        ctor.defineProperty(cx, "POSITIVE_INFINITY", ScriptRuntime.wrapNumber(Double.POSITIVE_INFINITY), 7);
        ctor.defineProperty(cx, "NEGATIVE_INFINITY", ScriptRuntime.wrapNumber(Double.NEGATIVE_INFINITY), 7);
        ctor.defineProperty(cx, "MAX_VALUE", ScriptRuntime.wrapNumber(Double.MAX_VALUE), 7);
        ctor.defineProperty(cx, "MIN_VALUE", ScriptRuntime.wrapNumber(Double.MIN_VALUE), 7);
        ctor.defineProperty(cx, "MAX_SAFE_INTEGER", ScriptRuntime.wrapNumber(9.007199254740991E15), 7);
        ctor.defineProperty(cx, "MIN_SAFE_INTEGER", ScriptRuntime.wrapNumber(-9.007199254740991E15), 7);
        this.addIdFunctionProperty(ctor, NUMBER_TAG, -1, "isFinite", 1, cx);
        this.addIdFunctionProperty(ctor, NUMBER_TAG, -2, "isNaN", 1, cx);
        this.addIdFunctionProperty(ctor, NUMBER_TAG, -3, "isInteger", 1, cx);
        this.addIdFunctionProperty(ctor, NUMBER_TAG, -4, "isSafeInteger", 1, cx);
        this.addIdFunctionProperty(ctor, NUMBER_TAG, -5, "parseFloat", 1, cx);
        this.addIdFunctionProperty(ctor, NUMBER_TAG, -6, "parseInt", 1, cx);
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
                arity = 1;
                s = "toString";
                break;
            case 3:
                arity = 1;
                s = "toLocaleString";
                break;
            case 4:
                arity = 0;
                s = "toSource";
                break;
            case 5:
                arity = 0;
                s = "valueOf";
                break;
            case 6:
                arity = 1;
                s = "toFixed";
                break;
            case 7:
                arity = 1;
                s = "toExponential";
                break;
            case 8:
                arity = 1;
                s = "toPrecision";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        this.initPrototypeMethod(NUMBER_TAG, id, s, arity, cx);
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(NUMBER_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            if (id == 1) {
                double val = args.length >= 1 ? ScriptRuntime.toNumber(cx, args[0]) : 0.0;
                return thisObj == null ? new NativeNumber(cx, val) : ScriptRuntime.wrapNumber(val);
            } else if (id < 1) {
                return execConstructorCall(cx, id, args);
            } else if (!(thisObj instanceof NativeNumber)) {
                throw incompatibleCallError(f, cx);
            } else {
                double value = ((NativeNumber) thisObj).doubleValue;
                switch(id) {
                    case 2:
                    case 3:
                        int base = args.length != 0 && args[0] != Undefined.instance ? ScriptRuntime.toInt32(cx, args[0]) : 10;
                        return ScriptRuntime.numberToString(cx, value, base);
                    case 4:
                        return "not_supported";
                    case 5:
                        return ScriptRuntime.wrapNumber(value);
                    case 6:
                        return num_to(cx, value, args, 2, 2, 0, 0);
                    case 7:
                        if (Double.isNaN(value)) {
                            return "NaN";
                        } else {
                            if (Double.isInfinite(value)) {
                                if (value >= 0.0) {
                                    return "Infinity";
                                }
                                return "-Infinity";
                            }
                            return num_to(cx, value, args, 1, 3, 0, 1);
                        }
                    case 8:
                        if (args.length != 0 && args[0] != Undefined.instance) {
                            if (Double.isNaN(value)) {
                                return "NaN";
                            }
                            if (Double.isInfinite(value)) {
                                if (value >= 0.0) {
                                    return "Infinity";
                                }
                                return "-Infinity";
                            }
                            return num_to(cx, value, args, 0, 4, 1, 0);
                        }
                        return ScriptRuntime.numberToString(cx, value, 10);
                    default:
                        throw new IllegalArgumentException(String.valueOf(id));
                }
            }
        }
    }

    public String toString() {
        return ScriptRuntime.numberToString(this.localContext, this.doubleValue, 10);
    }

    @Override
    public MemberType getTypeOf() {
        return MemberType.NUMBER;
    }

    @Override
    protected int findPrototypeId(String s) {
        return switch(s) {
            case "constructor" ->
                1;
            case "toString" ->
                2;
            case "toLocaleString" ->
                3;
            case "toSource" ->
                4;
            case "valueOf" ->
                5;
            case "toFixed" ->
                6;
            case "toExponential" ->
                7;
            case "toPrecision" ->
                8;
            default ->
                0;
        };
    }
}