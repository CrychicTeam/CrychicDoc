package dev.latvian.mods.rhino;

final class NativeMath extends IdScriptableObject {

    private static final Object MATH_TAG = "Math";

    private static final double LOG2E = 1.4426950408889634;

    private static final Double Double32 = 32.0;

    private static final int Id_toSource = 1;

    private static final int Id_abs = 2;

    private static final int Id_acos = 3;

    private static final int Id_asin = 4;

    private static final int Id_atan = 5;

    private static final int Id_atan2 = 6;

    private static final int Id_ceil = 7;

    private static final int Id_cos = 8;

    private static final int Id_exp = 9;

    private static final int Id_floor = 10;

    private static final int Id_log = 11;

    private static final int Id_max = 12;

    private static final int Id_min = 13;

    private static final int Id_pow = 14;

    private static final int Id_random = 15;

    private static final int Id_round = 16;

    private static final int Id_sin = 17;

    private static final int Id_sqrt = 18;

    private static final int Id_tan = 19;

    private static final int Id_cbrt = 20;

    private static final int Id_cosh = 21;

    private static final int Id_expm1 = 22;

    private static final int Id_hypot = 23;

    private static final int Id_log1p = 24;

    private static final int Id_log10 = 25;

    private static final int Id_sinh = 26;

    private static final int Id_tanh = 27;

    private static final int Id_imul = 28;

    private static final int Id_trunc = 29;

    private static final int Id_acosh = 30;

    private static final int Id_asinh = 31;

    private static final int Id_atanh = 32;

    private static final int Id_sign = 33;

    private static final int Id_log2 = 34;

    private static final int Id_fround = 35;

    private static final int Id_clz32 = 36;

    private static final int LAST_METHOD_ID = 36;

    private static final int Id_E = 37;

    private static final int Id_PI = 38;

    private static final int Id_LN10 = 39;

    private static final int Id_LN2 = 40;

    private static final int Id_LOG2E = 41;

    private static final int Id_LOG10E = 42;

    private static final int Id_SQRT1_2 = 43;

    private static final int Id_SQRT2 = 44;

    private static final int MAX_ID = 44;

    static void init(Scriptable scope, boolean sealed, Context cx) {
        NativeMath obj = new NativeMath();
        obj.activatePrototypeMap(44);
        obj.setPrototype(getObjectPrototype(scope, cx));
        obj.setParentScope(scope);
        if (sealed) {
            obj.sealObject(cx);
        }
        defineProperty(scope, "Math", obj, 2, cx);
    }

    private static double js_pow(double x, double y) {
        double result;
        if (Double.isNaN(y)) {
            result = y;
        } else if (y == 0.0) {
            result = 1.0;
        } else if (x == 0.0) {
            if (1.0 / x > 0.0) {
                result = y > 0.0 ? 0.0 : Double.POSITIVE_INFINITY;
            } else {
                long y_long = (long) y;
                if ((double) y_long == y && (y_long & 1L) != 0L) {
                    result = y > 0.0 ? -0.0 : Double.NEGATIVE_INFINITY;
                } else {
                    result = y > 0.0 ? 0.0 : Double.POSITIVE_INFINITY;
                }
            }
        } else {
            result = Math.pow(x, y);
            if (Double.isNaN(result)) {
                if (y == Double.POSITIVE_INFINITY) {
                    if (x < -1.0 || 1.0 < x) {
                        result = Double.POSITIVE_INFINITY;
                    } else if (-1.0 < x && x < 1.0) {
                        result = 0.0;
                    }
                } else if (y == Double.NEGATIVE_INFINITY) {
                    if (x < -1.0 || 1.0 < x) {
                        result = 0.0;
                    } else if (-1.0 < x && x < 1.0) {
                        result = Double.POSITIVE_INFINITY;
                    }
                } else if (x == Double.POSITIVE_INFINITY) {
                    result = y > 0.0 ? Double.POSITIVE_INFINITY : 0.0;
                } else if (x == Double.NEGATIVE_INFINITY) {
                    long y_long = (long) y;
                    if ((double) y_long == y && (y_long & 1L) != 0L) {
                        result = y > 0.0 ? Double.NEGATIVE_INFINITY : -0.0;
                    } else {
                        result = y > 0.0 ? Double.POSITIVE_INFINITY : 0.0;
                    }
                }
            }
        }
        return result;
    }

    private static double js_hypot(Object[] args, Context cx) {
        if (args == null) {
            return 0.0;
        } else {
            double y = 0.0;
            boolean hasNaN = false;
            boolean hasInfinity = false;
            for (Object o : args) {
                double d = ScriptRuntime.toNumber(cx, o);
                if (Double.isNaN(d)) {
                    hasNaN = true;
                } else if (Double.isInfinite(d)) {
                    hasInfinity = true;
                } else {
                    y += d * d;
                }
            }
            if (hasInfinity) {
                return Double.POSITIVE_INFINITY;
            } else {
                return hasNaN ? Double.NaN : Math.sqrt(y);
            }
        }
    }

    private static double js_trunc(double d) {
        return d < 0.0 ? Math.ceil(d) : Math.floor(d);
    }

    private static int js_imul(Object[] args, Context cx) {
        if (args == null) {
            return 0;
        } else {
            int x = ScriptRuntime.toInt32(cx, args, 0);
            int y = ScriptRuntime.toInt32(cx, args, 1);
            return x * y;
        }
    }

    private NativeMath() {
    }

    @Override
    public String getClassName() {
        return "Math";
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        if (id <= 36) {
            String name;
            int arity;
            switch(id) {
                case 1:
                    arity = 0;
                    name = "toSource";
                    break;
                case 2:
                    arity = 1;
                    name = "abs";
                    break;
                case 3:
                    arity = 1;
                    name = "acos";
                    break;
                case 4:
                    arity = 1;
                    name = "asin";
                    break;
                case 5:
                    arity = 1;
                    name = "atan";
                    break;
                case 6:
                    arity = 2;
                    name = "atan2";
                    break;
                case 7:
                    arity = 1;
                    name = "ceil";
                    break;
                case 8:
                    arity = 1;
                    name = "cos";
                    break;
                case 9:
                    arity = 1;
                    name = "exp";
                    break;
                case 10:
                    arity = 1;
                    name = "floor";
                    break;
                case 11:
                    arity = 1;
                    name = "log";
                    break;
                case 12:
                    arity = 2;
                    name = "max";
                    break;
                case 13:
                    arity = 2;
                    name = "min";
                    break;
                case 14:
                    arity = 2;
                    name = "pow";
                    break;
                case 15:
                    arity = 0;
                    name = "random";
                    break;
                case 16:
                    arity = 1;
                    name = "round";
                    break;
                case 17:
                    arity = 1;
                    name = "sin";
                    break;
                case 18:
                    arity = 1;
                    name = "sqrt";
                    break;
                case 19:
                    arity = 1;
                    name = "tan";
                    break;
                case 20:
                    arity = 1;
                    name = "cbrt";
                    break;
                case 21:
                    arity = 1;
                    name = "cosh";
                    break;
                case 22:
                    arity = 1;
                    name = "expm1";
                    break;
                case 23:
                    arity = 2;
                    name = "hypot";
                    break;
                case 24:
                    arity = 1;
                    name = "log1p";
                    break;
                case 25:
                    arity = 1;
                    name = "log10";
                    break;
                case 26:
                    arity = 1;
                    name = "sinh";
                    break;
                case 27:
                    arity = 1;
                    name = "tanh";
                    break;
                case 28:
                    arity = 2;
                    name = "imul";
                    break;
                case 29:
                    arity = 1;
                    name = "trunc";
                    break;
                case 30:
                    arity = 1;
                    name = "acosh";
                    break;
                case 31:
                    arity = 1;
                    name = "asinh";
                    break;
                case 32:
                    arity = 1;
                    name = "atanh";
                    break;
                case 33:
                    arity = 1;
                    name = "sign";
                    break;
                case 34:
                    arity = 1;
                    name = "log2";
                    break;
                case 35:
                    arity = 1;
                    name = "fround";
                    break;
                case 36:
                    arity = 1;
                    name = "clz32";
                    break;
                default:
                    throw new IllegalStateException(String.valueOf(id));
            }
            this.initPrototypeMethod(MATH_TAG, id, name, arity, cx);
        } else {
            String name;
            double x;
            switch(id) {
                case 37:
                    x = Math.E;
                    name = "E";
                    break;
                case 38:
                    x = Math.PI;
                    name = "PI";
                    break;
                case 39:
                    x = 2.302585092994046;
                    name = "LN10";
                    break;
                case 40:
                    x = 0.6931471805599453;
                    name = "LN2";
                    break;
                case 41:
                    x = 1.4426950408889634;
                    name = "LOG2E";
                    break;
                case 42:
                    x = 0.4342944819032518;
                    name = "LOG10E";
                    break;
                case 43:
                    x = 0.7071067811865476;
                    name = "SQRT1_2";
                    break;
                case 44:
                    x = 1.4142135623730951;
                    name = "SQRT2";
                    break;
                default:
                    throw new IllegalStateException(String.valueOf(id));
            }
            this.initPrototypeValue(id, name, ScriptRuntime.wrapNumber(x), 7);
        }
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(MATH_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int methodId = f.methodId();
            double x;
            switch(methodId) {
                case 1:
                    return "Math";
                case 2:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = x == 0.0 ? 0.0 : (x < 0.0 ? -x : x);
                    break;
                case 3:
                case 4:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    if (!Double.isNaN(x) && -1.0 <= x && x <= 1.0) {
                        x = methodId == 3 ? Math.acos(x) : Math.asin(x);
                    } else {
                        x = Double.NaN;
                    }
                    break;
                case 5:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Math.atan(x);
                    break;
                case 6:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Math.atan2(x, ScriptRuntime.toNumber(cx, args, 1));
                    break;
                case 7:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Math.ceil(x);
                    break;
                case 8:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Double.isInfinite(x) ? Double.NaN : Math.cos(x);
                    break;
                case 9:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = x == Double.POSITIVE_INFINITY ? x : (x == Double.NEGATIVE_INFINITY ? 0.0 : Math.exp(x));
                    break;
                case 10:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Math.floor(x);
                    break;
                case 11:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = x < 0.0 ? Double.NaN : Math.log(x);
                    break;
                case 12:
                case 13:
                    x = methodId == 12 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
                    for (int i = 0; i != args.length; i++) {
                        double d = ScriptRuntime.toNumber(cx, args[i]);
                        if (Double.isNaN(d)) {
                            return ScriptRuntime.wrapNumber(d);
                        }
                        if (methodId == 12) {
                            x = Math.max(x, d);
                        } else {
                            x = Math.min(x, d);
                        }
                    }
                    break;
                case 14:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = js_pow(x, ScriptRuntime.toNumber(cx, args, 1));
                    break;
                case 15:
                    x = Math.random();
                    break;
                case 16:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    if (!Double.isNaN(x) && !Double.isInfinite(x)) {
                        long l = Math.round(x);
                        if (l != 0L) {
                            x = (double) l;
                        } else if (x < 0.0) {
                            x = ScriptRuntime.negativeZero;
                        } else if (x != 0.0) {
                            x = 0.0;
                        }
                    }
                    break;
                case 17:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Double.isInfinite(x) ? Double.NaN : Math.sin(x);
                    break;
                case 18:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Math.sqrt(x);
                    break;
                case 19:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Math.tan(x);
                    break;
                case 20:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Math.cbrt(x);
                    break;
                case 21:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Math.cosh(x);
                    break;
                case 22:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Math.expm1(x);
                    break;
                case 23:
                    x = js_hypot(args, cx);
                    break;
                case 24:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Math.log1p(x);
                    break;
                case 25:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Math.log10(x);
                    break;
                case 26:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Math.sinh(x);
                    break;
                case 27:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = Math.tanh(x);
                    break;
                case 28:
                    x = (double) js_imul(args, cx);
                    break;
                case 29:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = js_trunc(x);
                    break;
                case 30:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    if (!Double.isNaN(x)) {
                        return Math.log(x + Math.sqrt(x * x - 1.0));
                    }
                    return ScriptRuntime.NaNobj;
                case 31:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    if (Double.isInfinite(x)) {
                        return x;
                    }
                    if (!Double.isNaN(x)) {
                        if (x == 0.0) {
                            if (1.0 / x > 0.0) {
                                return ScriptRuntime.zeroObj;
                            }
                            return ScriptRuntime.negativeZeroObj;
                        }
                        return Math.log(x + Math.sqrt(x * x + 1.0));
                    }
                    return ScriptRuntime.NaNobj;
                case 32:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    if (!Double.isNaN(x) && -1.0 <= x && x <= 1.0) {
                        if (x == 0.0) {
                            if (1.0 / x > 0.0) {
                                return ScriptRuntime.zeroObj;
                            }
                            return ScriptRuntime.negativeZeroObj;
                        }
                        return 0.5 * Math.log((x + 1.0) / (x - 1.0));
                    }
                    return ScriptRuntime.NaNobj;
                case 33:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    if (!Double.isNaN(x)) {
                        if (x == 0.0) {
                            if (1.0 / x > 0.0) {
                                return ScriptRuntime.zeroObj;
                            }
                            return ScriptRuntime.negativeZeroObj;
                        }
                        return Math.signum(x);
                    }
                    return ScriptRuntime.NaNobj;
                case 34:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = x < 0.0 ? Double.NaN : Math.log(x) * 1.4426950408889634;
                    break;
                case 35:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    x = (double) ((float) x);
                    break;
                case 36:
                    x = ScriptRuntime.toNumber(cx, args, 0);
                    if (x != 0.0 && !Double.isNaN(x) && !Double.isInfinite(x)) {
                        long n = ScriptRuntime.toUint32(x);
                        if (n == 0L) {
                            return Double32;
                        }
                        return 31.0 - Math.floor(Math.log((double) (n >>> 0)) * 1.4426950408889634);
                    }
                    return Double32;
                default:
                    throw new IllegalStateException(String.valueOf(methodId));
            }
            return ScriptRuntime.wrapNumber(x);
        }
    }

    @Override
    protected int findPrototypeId(String s) {
        return switch(s) {
            case "toSource" ->
                1;
            case "abs" ->
                2;
            case "acos" ->
                3;
            case "asin" ->
                4;
            case "atan" ->
                5;
            case "atan2" ->
                6;
            case "ceil" ->
                7;
            case "cos" ->
                8;
            case "exp" ->
                9;
            case "floor" ->
                10;
            case "log" ->
                11;
            case "max" ->
                12;
            case "min" ->
                13;
            case "pow" ->
                14;
            case "random" ->
                15;
            case "round" ->
                16;
            case "sin" ->
                17;
            case "sqrt" ->
                18;
            case "tan" ->
                19;
            case "cbrt" ->
                20;
            case "cosh" ->
                21;
            case "expm1" ->
                22;
            case "hypot" ->
                23;
            case "log1p" ->
                24;
            case "log10" ->
                25;
            case "sinh" ->
                26;
            case "tanh" ->
                27;
            case "imul" ->
                28;
            case "trunc" ->
                29;
            case "acosh" ->
                30;
            case "asinh" ->
                31;
            case "atanh" ->
                32;
            case "sign" ->
                33;
            case "log2" ->
                34;
            case "fround" ->
                35;
            case "clz32" ->
                36;
            default ->
                0;
        };
    }
}